package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.response.RoleBindPermissionResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleLimitService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色权限限制控制器
 * <p>
 * 除了超级管理员拥有权限绑定外，如果其他角色的成员也需要绑定权限，那么需要在权限绑定界面绑定权限限制
 *
 * @author fengshuonan
 * @date 2023/09/08 12:55
 */
@RestController
@ApiResource(name = "角色权限限制")
public class SysRoleLimitController {

    @Resource
    private SysRoleLimitService sysRoleLimitService;

    /**
     * 获取角色的权限限制列表，和角色绑定权限界面返回的数据结构一样
     *
     * @author fengshuonan
     * @since 2023/9/8 13:08
     */
    @GetResource(name = "获取角色的权限限制列表", path = "/roleLimit/getRoleLimit", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.CHANGE_ROLE_BIND_LIMIT)
    public ResponseData<RoleBindPermissionResponse> getRoleBindLimit(
            @Validated(BaseRequest.detail.class) RoleBindPermissionRequest roleBindPermissionRequest) {
        RoleBindPermissionResponse roleLimit = sysRoleLimitService.getRoleLimit(roleBindPermissionRequest);
        return new SuccessResponseData<>(roleLimit);
    }

    /**
     * 绑定角色的限制列表
     *
     * @author fengshuonan
     * @since 2023/9/8 13:09
     */
    @PostResource(name = "绑定角色权限的限制列表", path = "/roleLimit/bindRoleLimit", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.CHANGE_ROLE_BIND_LIMIT)
    public ResponseData<?> bindRoleLimit(@RequestBody @Validated(RoleBindPermissionRequest.roleBindPermission.class)
                                         RoleBindPermissionRequest roleBindPermissionRequest) {
        sysRoleLimitService.updateRoleBindLimit(roleBindPermissionRequest);
        return new SuccessResponseData<>();
    }

}
