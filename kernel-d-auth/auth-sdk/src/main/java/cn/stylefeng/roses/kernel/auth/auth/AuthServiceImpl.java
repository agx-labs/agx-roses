/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.auth.auth;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.stylefeng.roses.kernel.auth.api.AuthServiceApi;
import cn.stylefeng.roses.kernel.auth.api.SessionManagerApi;
import cn.stylefeng.roses.kernel.auth.api.context.AuthJwtContext;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.exception.AuthException;
import cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum;
import cn.stylefeng.roses.kernel.auth.api.expander.AuthConfigExpander;
import cn.stylefeng.roses.kernel.auth.api.pojo.auth.LoginRequest;
import cn.stylefeng.roses.kernel.auth.api.pojo.auth.LoginResponse;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.auth.api.pojo.payload.DefaultJwtPayload;
import cn.stylefeng.roses.kernel.auth.api.pojo.sso.DecryptCaLoginUser;
import cn.stylefeng.roses.kernel.auth.api.pojo.sso.DecryptCaTokenInfo;
import cn.stylefeng.roses.kernel.auth.api.pojo.sso.LoginBySsoTokenRequest;
import cn.stylefeng.roses.kernel.auth.api.pojo.sso.LogoutBySsoTokenRequest;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.demo.expander.DemoConfigExpander;
import cn.stylefeng.roses.kernel.jwt.api.JwtApi;
import cn.stylefeng.roses.kernel.jwt.api.exception.JwtException;
import cn.stylefeng.roses.kernel.jwt.api.exception.enums.JwtExceptionEnum;
import cn.stylefeng.roses.kernel.log.api.LoginLogServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserValidateDTO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

import static cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum.AUTH_EXPIRED_ERROR;
import static cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum.TOKEN_PARSE_ERROR;

/**
 * 认证服务的实现
 *
 * @author fengshuonan
 * @since 2020/10/20 10:25
 */
@Service
public class AuthServiceImpl implements AuthServiceApi {

    @Resource
    private SysUserServiceApi sysUserServiceApi;

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private LoginLogServiceApi loginLogServiceApi;

    @Resource(name = "loginErrorCountCacheApi")
    private CacheOperatorApi<Integer> loginErrorCountCacheApi;

    @Resource(name = "caClientTokenCacheApi")
    private CacheOperatorApi<String> caClientTokenCacheApi;

    @Resource
    private JwtApi jwtApi;

    @Resource
    private LoginService loginService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return loginService.loginAction(loginRequest, true, null);
    }

    @Override
    public LoginResponse loginWithUserName(String username) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccount(username);
        return loginService.loginAction(loginRequest, false, null);
    }

    @Override
    public LoginResponse loginWithUserNameAndCaToken(String username, String caToken) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccount(username);
        return loginService.loginAction(loginRequest, false, caToken);
    }

    @Override
    public LoginResponse LoginByCaToken(LoginBySsoTokenRequest loginWithTokenRequest) {

        // sso token是一个加密的字符串，需要用AES秘钥对称解密
        String encryptUserInfo = loginWithTokenRequest.getToken();

        // aes解密出用户信息
        AES aesUtil = SecureUtil.aes(Base64.decode(AuthConfigExpander.getSsoDataDecryptSecret()));
        String userInfoJson = null;
        try {
            userInfoJson = aesUtil.decryptStr(encryptUserInfo, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            throw new AuthException(AuthExceptionEnum.SSO_TOKEN_PARSE_ERROR, "sso token无法解析");
        }

        // 转化为实体类
        DecryptCaTokenInfo decryptCaTokenInfo = JSON.parseObject(userInfoJson, DecryptCaTokenInfo.class);

        // 如果时间大于2分钟则认定token过期
        String tokenGenerateTimeStr = decryptCaTokenInfo.getGenerationDateTime();
        DateTime tokenGenerateTime = DateUtil.parse(tokenGenerateTimeStr);
        DateTime tokenExpTime = tokenGenerateTime.offset(DateField.MINUTE, 2);
        if (tokenExpTime.isBefore(new Date())) {
            throw new AuthException(AuthExceptionEnum.SSO_TOKEN_PARSE_ERROR, "sso token过期");
        }

        // 获取token中的用户信息
        DecryptCaLoginUser caLoginUser = decryptCaTokenInfo.getCaLoginUser();
        if (caLoginUser == null) {
            throw new AuthException(AuthExceptionEnum.SSO_TOKEN_GET_USER_ERROR);
        }

        // 解密出用户账号和caToken（caToken用于校验用户是否在单点中心）
        if (ObjectUtil.isEmpty(caLoginUser.getAccount()) || ObjectUtil.isEmpty(caLoginUser.getCaToken())) {
            throw new AuthException(AuthExceptionEnum.SSO_TOKEN_GET_USER_ERROR);
        }

        // 通过账号和caToken登录
        LoginResponse loginResponse = loginWithUserNameAndCaToken(caLoginUser.getAccount(), caLoginUser.getCaToken());

        // 存储单点token和生成的本地token的映射关系，用在远程退出的时，将本地缓存清空
        caClientTokenCacheApi.put(caLoginUser.getCaToken(), loginResponse.getToken());

        return loginResponse;
    }

    @Override
    public void logout() {
        String token = LoginContext.me().getToken();

        // 演示环境，不记录退出日志
        if (!DemoConfigExpander.getDemoEnvFlag()) {
            if (StrUtil.isNotEmpty(token)) {
                loginLogServiceApi.loginOutSuccess(LoginContext.me().getLoginUser().getUserId());
            }
        }

        logoutWithToken(token);
    }

    @Override
    public void logoutWithToken(String token) {
        // 清除token缓存的用户信息
        sessionManagerApi.removeSession(token);
    }

    @Override
    public DefaultJwtPayload validateToken(String token) throws AuthException {
        try {
            // 1. 先校验jwt token本身是否有问题
            jwtApi.validateTokenWithException(token);

            // 2. 获取jwt的payload
            DefaultJwtPayload defaultPayload = AuthJwtContext.me().getDefaultPayload(token);

            // 3. 如果是7天免登陆，则不校验session过期
            if (defaultPayload.getRememberMe()) {
                return defaultPayload;
            }

            // 4. 判断session里是否有这个token
            LoginUser session = sessionManagerApi.getSession(token);
            if (session == null) {
                throw new AuthException(AUTH_EXPIRED_ERROR);
            }

            return defaultPayload;
        } catch (JwtException jwtException) {
            // jwt token本身过期的话，返回 AUTH_EXPIRED_ERROR
            if (JwtExceptionEnum.JWT_EXPIRED_ERROR.getErrorCode().equals(jwtException.getErrorCode())) {
                throw new AuthException(AUTH_EXPIRED_ERROR);
            } else {
                // 其他情况为返回jwt解析错误
                throw new AuthException(TOKEN_PARSE_ERROR);
            }
        } catch (io.jsonwebtoken.JwtException jwtSelfException) {
            // 其他jwt解析错误
            throw new AuthException(TOKEN_PARSE_ERROR);
        }
    }

    @Override
    public void checkAuth(String token, String requestUrl) {

        // 1. 校验token是否传参
        if (StrUtil.isEmpty(token)) {
            throw new AuthException(AuthExceptionEnum.TOKEN_GET_ERROR);
        }

        // 2. 校验用户token是否正确，校验失败会抛出异常
        this.validateToken(token);
    }

    @Override
    public void cancelFreeze(LoginRequest loginRequest) {
        loginErrorCountCacheApi.remove(loginRequest.getAccount());
    }

    @Override
    public LoginUser createNewLoginInfo(String token, DefaultJwtPayload defaultJwtPayload) {

        // 获取用户的信息
        Long userId = defaultJwtPayload.getUserId();

        LoginUser loginUser;

        // 获取用户信息
        UserValidateDTO userValidateDTO = sysUserServiceApi.getUserLoginValidateDTO(userId);

        // 创建登录用户
        loginUser = new LoginUser(userValidateDTO.getUserId(), userValidateDTO.getAccount(), token, userValidateDTO.getTenantId());

        // 创建用户会话信息
        sessionManagerApi.updateSession(token, loginUser);

        return loginUser;
    }

    @Override
    public void logoutByCaToken(LogoutBySsoTokenRequest logoutBySsoTokenRequest) {

        // 通过CaToken查询到本地是否有对应的会话
        String localGunsToken = caClientTokenCacheApi.get(logoutBySsoTokenRequest.getCaToken());

        // 如果缓存不存在则直接返回
        if (ObjectUtil.isEmpty(localGunsToken)) {
            return;
        }

        // 如果缓存存在，则直接移除token
        this.sessionManagerApi.removeSession(localGunsToken);
    }

}
