package cn.stylefeng.roses.kernel.sys.modular.security.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.SecurityConfigService;
import cn.stylefeng.roses.kernel.sys.api.pojo.security.SecurityConfig;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants.PASSWORD_STRATEGY_CONFIG;

/**
 * 安全策略配置
 *
 * @author fengshuonan
 * @since 2023/10/4 15:59
 */
@RestController
@ApiResource(name = "安全策略配置", requiredPermission = true, requirePermissionCode = PASSWORD_STRATEGY_CONFIG)
public class SecurityStrategyController {

    @Resource
    private SecurityConfigService securityConfigService;

    /**
     * 获取安全策略配置
     *
     * @author fengshuonan
     * @since 2023/10/4 16:00
     */
    @GetResource(name = "获取安全策略配置", path = "/security/getSecurityStrategy")
    public ResponseData<SecurityConfig> getSecurityStrategy() {
        SecurityConfig securityConfig = this.securityConfigService.getSecurityConfig();
        return new SuccessResponseData<>(securityConfig);
    }

    /**
     * 更新安全策略配置
     *
     * @author fengshuonan
     * @since 2023/10/4 21:50
     */
    @PostResource(name = "更新安全策略配置", path = "/security/updateSecurityStrategy")
    public ResponseData<?> updateSecurityStrategy(@Validated @RequestBody SecurityConfig securityConfig) {
        this.securityConfigService.updateSecurityConfig(securityConfig);
        return new SuccessResponseData<>();
    }

}
