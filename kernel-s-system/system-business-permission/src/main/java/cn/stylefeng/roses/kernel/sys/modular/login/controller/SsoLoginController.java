package cn.stylefeng.roses.kernel.sys.modular.login.controller;

import cn.stylefeng.roses.kernel.auth.api.AuthServiceApi;
import cn.stylefeng.roses.kernel.auth.api.pojo.auth.LoginResponse;
import cn.stylefeng.roses.kernel.auth.api.pojo.sso.LoginBySsoTokenRequest;
import cn.stylefeng.roses.kernel.auth.api.pojo.sso.LogoutBySsoTokenRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 单点登录接口
 *
 * @author fengshuonan
 * @since 2023/11/7 14:06
 */
@RestController
@Slf4j
@ApiResource(name = "单点登录接口")
public class SsoLoginController {

    @Resource
    private AuthServiceApi authServiceApi;

    /**
     * 通过单点服务的CaToken进行登录
     *
     * @author fengshuonan
     * @since 2023/11/7 14:12
     */
    @PostResource(name = "通过单点服务的CaToken进行登录", path = "/loginByCaToken", requiredLogin = false)
    public ResponseData<LoginResponse> loginByCaToken(@RequestBody @Validated LoginBySsoTokenRequest loginWithTokenRequest) {
        LoginResponse loginResponse = authServiceApi.LoginByCaToken(loginWithTokenRequest);
        return new SuccessResponseData<>(loginResponse);
    }

    /**
     * 通过单点服务的CaToken进行退出本平台的会话
     *
     * @author fengshuonan
     * @since 2023/11/7 15:57
     */
    @GetResource(name = "通过单点服务的CaToken进行退出本平台的会话", path = "/logoutByCaToken", requiredLogin = false)
    public ResponseData<?> logoutByCaToken(@Validated LogoutBySsoTokenRequest logoutBySsoTokenRequest) {
        authServiceApi.logoutByCaToken(logoutBySsoTokenRequest);
        return new SuccessResponseData<>();
    }

}
