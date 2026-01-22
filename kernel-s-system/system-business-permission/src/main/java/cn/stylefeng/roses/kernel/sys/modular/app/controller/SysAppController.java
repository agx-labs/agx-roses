package cn.stylefeng.roses.kernel.sys.modular.app.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.app.pojo.request.SysAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统应用控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@RestController
@ApiResource(name = "系统应用")
public class SysAppController {

    @Resource
    private SysAppService sysAppService;

    /**
     * 添加应用
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "添加应用", path = "/sysApp/add", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.ADD_APP)
    public ResponseData<SysApp> add(@RequestBody @Validated(SysAppRequest.add.class) SysAppRequest sysAppRequest) {
        sysAppService.add(sysAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除应用
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "删除应用", path = "/sysApp/delete", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.DELETE_APP)
    public ResponseData<?> delete(@RequestBody @Validated(SysAppRequest.delete.class) SysAppRequest sysAppRequest) {
        sysAppService.del(sysAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量删除应用
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "批量删除应用", path = "/sysApp/batchDelete", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.DELETE_APP)
    public ResponseData<?> batchDelete(@RequestBody @Validated(BaseRequest.batchDelete.class) SysAppRequest sysAppRequest) {
        sysAppService.batchDelete(sysAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑应用
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "编辑应用", path = "/sysApp/edit", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.EDIT_APP)
    public ResponseData<?> edit(@RequestBody @Validated(SysAppRequest.edit.class) SysAppRequest sysAppRequest) {
        sysAppService.edit(sysAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看应用详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "查看应用详情", path = "/sysApp/detail")
    public ResponseData<SysApp> detail(@Validated(SysAppRequest.detail.class) SysAppRequest sysAppRequest) {
        return new SuccessResponseData<>(sysAppService.detail(sysAppRequest));
    }

    /**
     * 分页查询-应用列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "分页查询-应用列表", path = "/sysApp/page")
    public ResponseData<PageResult<SysApp>> page(SysAppRequest sysAppRequest) {
        return new SuccessResponseData<>(sysAppService.findPage(sysAppRequest));
    }

    /**
     * 应用列表，查询全部
     * <p>
     * 一般用在新增菜单界面的选择应用界面
     *
     * @author liyanjun
     * @date 2023/07/02 18:28
     */
    @GetResource(name = "应用列表查询", path = "/sysApp/list")
    public ResponseData<List<SysApp>> list(SysAppRequest sysAppRequest) {
        return new SuccessResponseData<>(sysAppService.findList(sysAppRequest));
    }

    /**
     * 修改应用状态
     *
     * @author liyanjun
     * @since 2023/6/30 10:58
     */
    @PostResource(name = "修改应用状态", path = "/sysApp/updateStatus", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.UPDATE_APP_STATUS)
    public ResponseData<?> updateStatus(@RequestBody @Validated(SysAppRequest.updateStatus.class) SysAppRequest sysAppRequest) {
        sysAppService.updateStatus(sysAppRequest);
        return new SuccessResponseData<>();
    }
}
