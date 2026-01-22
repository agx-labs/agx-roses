package cn.stylefeng.roses.kernel.auth.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.stylefeng.roses.kernel.auth.api.AuthJwtTokenApi;
import cn.stylefeng.roses.kernel.auth.api.expander.AuthConfigExpander;
import cn.stylefeng.roses.kernel.auth.api.pojo.payload.DefaultJwtPayload;
import cn.stylefeng.roses.kernel.config.api.ConfigInitCallbackApi;
import cn.stylefeng.roses.kernel.jwt.api.JwtApi;
import cn.stylefeng.roses.kernel.jwt.api.pojo.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Auth模块对JWT token的使用
 *
 * @author fengshuonan
 * @since 2023/5/9 10:05
 */
@Service
public class AuthJwtTokenService implements AuthJwtTokenApi, ConfigInitCallbackApi {

    @Resource
    private JwtApi jwtApi;

    @Override
    public String generateTokenDefaultPayload(DefaultJwtPayload defaultJwtPayload) {

        // 计算过期时间
        DateTime expirationDate = DateUtil.offsetSecond(new Date(), Convert.toInt(AuthConfigExpander.getAuthJwtTimeoutSeconds()));

        // 设置过期时间
        defaultJwtPayload.setExpirationDate(expirationDate.getTime());

        // 构造jwt token
        return Jwts.builder()
                .setClaims(BeanUtil.beanToMap(defaultJwtPayload))
                .setSubject(defaultJwtPayload.getUserId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, AuthConfigExpander.getAuthJwtSecret())
                .compact();
    }

    @Override
    public DefaultJwtPayload getDefaultPayload(String token) {
        Map<String, Object> jwtPayload = jwtApi.getJwtPayloadClaims(token);
        return BeanUtil.toBeanIgnoreError(jwtPayload, DefaultJwtPayload.class);
    }

    @Override
    public void initBefore() {

    }

    @Override
    public void initAfter() {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setJwtSecret(AuthConfigExpander.getAuthJwtSecret());
        jwtConfig.setExpiredSeconds(AuthConfigExpander.getAuthJwtTimeoutSeconds());
        jwtApi.updateJwtConfig(jwtConfig);
    }

}
