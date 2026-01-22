package cn.stylefeng.roses.kernel.sys.modular.login.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.UpdateUserOrgAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.UserIndexInfo;
import cn.stylefeng.roses.kernel.sys.modular.login.service.UserIndexInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息获取的接口
 *
 * @author fengshuonan
 * @since 2023/6/18 22:08
 */
@RestController
@Slf4j
@ApiResource(name = "用户信息获取的接口")
public class UserIndexInfoController {

    @Resource
    private UserIndexInfoService userIndexInfoService;

    /**
     * 获取用户登录后的信息
     *
     * @author fengshuonan
     * @since 2023/6/18 22:08
     */
    @GetResource(name = "获取用户登录后的信息", path = "/userIndexInfo")
    public ResponseData<UserIndexInfo> userIndexInfo() {
        return new SuccessResponseData<>(userIndexInfoService.getUserIndexInfo());
    }

    /**
     * 切换当前用户激活的组织机构id或当前激活的应用id
     *
     * @author fengshuonan
     * @since 2023/6/21 16:03
     */
    @PostResource(name = "切换当前用户激活的组织机构id或当前激活的应用id", path = "/updateUserOrgOrApp")
    public ResponseData<?> updateUserOrgOrApp(@RequestBody UpdateUserOrgAppRequest updateUserOrgAppRequest) {
        userIndexInfoService.updateUserOrgOrApp(updateUserOrgAppRequest);
        return new SuccessResponseData<>();
    }

}
