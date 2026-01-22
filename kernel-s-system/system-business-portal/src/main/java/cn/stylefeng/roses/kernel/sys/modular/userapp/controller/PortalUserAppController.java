package cn.stylefeng.roses.kernel.sys.modular.userapp.controller;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.pojo.menu.UserAppMenuInfo;
import cn.stylefeng.roses.kernel.sys.modular.userapp.entity.PortalUserApp;
import cn.stylefeng.roses.kernel.sys.modular.userapp.pojo.request.PortalUserAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.userapp.service.PortalUserAppService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户常用功能控制器
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
@RestController
@ApiResource(name = "用户常用功能")
public class PortalUserAppController {

    @Resource
    private PortalUserAppService portalUserAppService;

    /**
     * 获取用户常用功能
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    @GetResource(name = "获取用户常用功能", path = "/portalUserApp/getUserAppList")
    public ResponseData<List<UserAppMenuInfo>> getUserAppList() {
        return new SuccessResponseData<>(portalUserAppService.getUserAppList());
    }

    /**
     * 更新用户的常用功能
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    @PostResource(name = "更新用户的常用功能", path = "/portalUserApp/updateUserAppList")
    public ResponseData<PortalUserApp> updateUserAppList(
            @RequestBody @Validated(BaseRequest.edit.class) PortalUserAppRequest portalUserAppRequest) {
        portalUserAppService.updateUserAppList(portalUserAppRequest);
        return new SuccessResponseData<>();
    }

}
