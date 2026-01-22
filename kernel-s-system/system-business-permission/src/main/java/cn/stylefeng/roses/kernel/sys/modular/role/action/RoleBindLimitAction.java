package cn.stylefeng.roses.kernel.sys.modular.role.action;

import cn.stylefeng.roses.kernel.sys.api.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.request.RoleBindPermissionRequest;

/**
 * 角色绑定权限限制的接口
 *
 * @author fengshuonan
 * @since 2023/9/8 14:15
 */
public interface RoleBindLimitAction {

    /**
     * 获取操作的类型，有4种节点类型
     *
     * @author fengshuonan
     * @since 2023/9/8 14:15
     */
    PermissionNodeTypeEnum getRoleBindLimitNodeType();

    /**
     * 进行角色绑定权限限制的过程，执行绑定的操作
     *
     * @author fengshuonan
     * @since 2023/9/8 14:16
     */
    void doRoleBindLimitAction(RoleBindPermissionRequest roleBindPermissionRequest);

}
