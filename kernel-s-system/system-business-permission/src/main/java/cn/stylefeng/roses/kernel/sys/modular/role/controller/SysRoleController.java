package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.annotation.BizLog;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统角色控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@RestController
@ApiResource(name = "系统角色")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 添加角色
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "添加角色", path = "/sysRole/add", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.ADD_ROLE)
    @BizLog(logTypeCode = PermissionCodeConstants.ADD_ROLE)
    public ResponseData<SysRole> add(@RequestBody @Validated(SysRoleRequest.add.class) SysRoleRequest sysRoleRequest) {
        sysRoleService.add(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除角色
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "删除角色", path = "/sysRole/delete", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.DELETE_ROLE)
    @BizLog(logTypeCode = PermissionCodeConstants.DELETE_ROLE)
    public ResponseData<?> delete(@RequestBody @Validated(SysRoleRequest.delete.class) SysRoleRequest sysRoleRequest) {
        sysRoleService.del(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量删除角色
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "批量删除角色", path = "/sysRole/batchDelete", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.DELETE_ROLE)
    @BizLog(logTypeCode = PermissionCodeConstants.DELETE_ROLE)
    public ResponseData<?> batchDelete(@RequestBody @Validated(SysRoleRequest.batchDelete.class) SysRoleRequest sysRoleRequest) {
        sysRoleService.batchDelete(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑角色
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "编辑角色", path = "/sysRole/edit", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.EDIT_ROLE)
    @BizLog(logTypeCode = PermissionCodeConstants.EDIT_ROLE)
    public ResponseData<?> edit(@RequestBody @Validated(SysRoleRequest.edit.class) SysRoleRequest sysRoleRequest) {
        sysRoleService.edit(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看角色详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "查看角色详情", path = "/sysRole/detail")
    public ResponseData<SysRole> detail(@Validated(SysRoleRequest.detail.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.detail(sysRoleRequest));
    }

    /**
     * 分页查询-角色列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "分页查询-角色列表", path = "/sysRole/page")
    public ResponseData<PageResult<SysRole>> page(SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.findPage(sysRoleRequest));
    }

    /**
     * 获取所有角色列表
     * <p>
     * 一般用在用户分配角色，响应所有的角色列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "获取所有角色列表，一般用在用户分配角色，响应所有的角色列表", path = "/sysRole/list")
    public ResponseData<List<SysRole>> list(SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.userAssignRoleList(sysRoleRequest));
    }

}
