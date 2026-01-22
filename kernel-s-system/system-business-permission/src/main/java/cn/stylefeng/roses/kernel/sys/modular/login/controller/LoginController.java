package cn.stylefeng.roses.kernel.sys.modular.login.controller;

import cn.stylefeng.roses.kernel.auth.api.AuthServiceApi;
import cn.stylefeng.roses.kernel.auth.api.pojo.auth.LoginRequest;
import cn.stylefeng.roses.kernel.auth.api.pojo.auth.LoginResponse;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录相关接口
 *
 * @author fengshuonan
 * @since 2023/6/17 17:08
 */
@RestController
@Slf4j
@ApiResource(name = "登录相关接口")
public class LoginController {

    @Resource
    private AuthServiceApi authServiceApi;

    /**
     * 系统登录接口
     *
     * @author fengshuonan
     * @since 2023/6/17 17:09
     */
    @PostResource(name = "系统登录接口", path = "/loginApi", requiredLogin = false)
    public ResponseData<LoginResponse> loginApi(@RequestBody @Validated LoginRequest loginRequest) {
        LoginResponse loginResponse = authServiceApi.login(loginRequest);
        return new SuccessResponseData<>(loginResponse);
    }

    /**
     * 用户登出
     *
     * @author fengshuonan
     * @since 2023/6/25 16:43
     */
    @ApiResource(name = "登出", path = "/logoutAction", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseData<?> logoutAction() {
        authServiceApi.logout();
        return new SuccessResponseData<>();
    }

}
