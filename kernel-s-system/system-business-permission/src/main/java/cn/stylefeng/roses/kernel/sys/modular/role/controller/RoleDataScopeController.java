package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleDataScope;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleDataScopeService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色数据范围配置
 *
 * @author fengshuonan
 * @since 2025/1/24 18:12
 */
@RestController
@ApiResource(name = "角色数据范围配置")
public class RoleDataScopeController {

    @Resource
    private SysRoleDataScopeService sysRoleDataScopeService;

    /**
     * 获取角色的数据范围列表
     *
     * @author fengshuonan
     * @since 2025/1/24 18:12
     */
    @GetResource(name = "获取角色的数据范围列表", path = "/roleDataScope/getRoleDataScopePageList", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.CHANGE_ROLE_DATA_SCOPE)
    public ResponseData<PageResult<SysRoleDataScope>> getRoleDataScopePageList(
            @Validated(BaseRequest.page.class) SysRoleDataScopeRequest roleDataScopeRequest) {
        PageResult<SysRoleDataScope> pageList = sysRoleDataScopeService.findPage(roleDataScopeRequest);
        return new SuccessResponseData<>(pageList);
    }

    /**
     * 新增一个角色的数据权限
     *
     * @author fengshuonan
     * @since 2025/1/24 22:34
     */
    @PostResource(name = "新增一个角色的数据权限", path = "/roleDataScope/addRoleDataScope", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.CHANGE_ROLE_DATA_SCOPE)
    public ResponseData<?> addRoleDataScope(@RequestBody @Validated(BaseRequest.add.class) SysRoleDataScopeRequest roleDataScopeRequest) {
        sysRoleDataScopeService.add(roleDataScopeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改角色的数据权限
     *
     * @author fengshuonan
     * @since 2025/1/24 22:49
     */
    @PostResource(name = "修改角色的数据权限", path = "/roleDataScope/editRoleDataScope", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.CHANGE_ROLE_DATA_SCOPE)
    public ResponseData<?> editRoleDataScope(@RequestBody @Validated(BaseRequest.edit.class) SysRoleDataScopeRequest roleDataScopeRequest) {
        sysRoleDataScopeService.edit(roleDataScopeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除角色的数据权限
     *
     * @author fengshuonan
     * @since 2025/1/24 22:49
     */
    @PostResource(name = "删除角色的数据权限", path = "/roleDataScope/delete", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.CHANGE_ROLE_DATA_SCOPE)
    public ResponseData<?> delete(@RequestBody @Validated(BaseRequest.delete.class) SysRoleDataScopeRequest roleDataScopeRequest) {
        sysRoleDataScopeService.del(roleDataScopeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查询角色数据权限详情
     *
     * @author fengshuonan
     * @since 2025/1/24 22:59
     */
    @GetResource(name = "查看详情", path = "/roleDataScope/getDetail", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.CHANGE_ROLE_DATA_SCOPE)
    public ResponseData<SysRoleDataScope> getDetail(@Validated(BaseRequest.detail.class) SysRoleDataScopeRequest roleDataScopeRequest) {
        SysRoleDataScope detail = sysRoleDataScopeService.detail(roleDataScopeRequest);
        return new SuccessResponseData<>(detail);
    }

}
