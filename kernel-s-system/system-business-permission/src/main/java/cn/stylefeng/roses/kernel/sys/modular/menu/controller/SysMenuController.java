package cn.stylefeng.roses.kernel.sys.modular.menu.controller;

import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.ProjectBusinessGetApi;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.api.pojo.menu.BusinessViewDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.menu.ProjectBusinessDTO;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理界面的接口
 *
 * @author fengshuonan
 * @since 2023/6/14 21:29
 */
@RestController
@ApiResource(name = "菜单管理界面的接口", requiredPermission = true, requirePermissionCode = PermissionCodeConstants.AUTH_MENU)
@Slf4j
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 获取菜单管理界面的每个应用组下的菜单信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "获取菜单管理界面的每个应用组下的菜单信息", path = "/sysMenu/getAppMenuGroupDetail")
    public ResponseData<List<AppGroupDetail>> getAppMenuGroupDetail(SysMenuRequest sysMenuRequest) {
        List<AppGroupDetail> appGroupDetails = sysMenuService.getAppMenuGroupDetail(sysMenuRequest);
        return new SuccessResponseData<>(appGroupDetails);
    }

    /**
     * 添加菜单
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "添加菜单", path = "/sysMenu/add")
    public ResponseData<SysMenu> add(@RequestBody @Validated(SysMenuRequest.add.class) SysMenuRequest sysMenuRequest) {
        sysMenuService.add(sysMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除菜单
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "删除菜单", path = "/sysMenu/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysMenuRequest.delete.class) SysMenuRequest sysMenuRequest) {
        sysMenuService.del(sysMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑菜单
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "编辑菜单", path = "/sysMenu/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysMenuRequest.edit.class) SysMenuRequest sysMenuRequest) {
        sysMenuService.edit(sysMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看菜单详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "查看菜单详情", path = "/sysMenu/detail")
    public ResponseData<SysMenu> detail(@Validated(SysMenuRequest.detail.class) SysMenuRequest sysMenuRequest) {
        return new SuccessResponseData<>(sysMenuService.detail(sysMenuRequest));
    }

    /**
     * 调整菜单上下级机构和菜单的顺序
     *
     * @author fengshuonan
     * @since 2023/6/15 11:28
     */
    @PostResource(name = "调整菜单上下级机构和菜单的顺序", path = "/sysMenu/updateMenuTree")
    public ResponseData<?> updateMenuTree(@RequestBody @Validated(SysMenuRequest.updateMenuTree.class) SysMenuRequest sysMenuRequest) {
        sysMenuService.updateMenuTree(sysMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 获取所有项目的应用设计业务列表
     *
     * @author fengshuonan
     * @since 2024-01-10 17:16
     */
    @GetResource(name = "获取所有项目的应用设计业务列表", path = "/sysMenu/getProjectBusinessList")
    public ResponseData<List<ProjectBusinessDTO>> getProjectBusinessList() {

        ProjectBusinessGetApi bean = null;
        try {
            bean = SpringUtil.getBean(ProjectBusinessGetApi.class);
        } catch (Exception e) {
            log.warn("无法获取到ProjectBusinessGetApi Bean！");
            return new SuccessResponseData<>(new ArrayList<>());
        }

        return new SuccessResponseData<>(bean.getProjectBusinessList());
    }

    /**
     * 获取业务设计下的所有视图列表
     *
     * @author fengshuonan
     * @since 2024/5/13 16:54
     */
    @GetResource(name = "获取业务设计下的所有视图列表", path = "/sysMenu/getBusinessViewList")
    public ResponseData<List<BusinessViewDTO>> getBusinessViewList(@Param("businessId") Long businessId) {

        ProjectBusinessGetApi bean = null;
        try {
            bean = SpringUtil.getBean(ProjectBusinessGetApi.class);
        } catch (Exception e) {
            log.warn("无法获取到ProjectBusinessGetApi Bean！");
            return new SuccessResponseData<>(new ArrayList<>());
        }

        return new SuccessResponseData<>(bean.getBusinessViewList(businessId));
    }

}
