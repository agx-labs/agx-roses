package cn.stylefeng.roses.kernel.sys.modular.role.action;

import cn.stylefeng.roses.kernel.sys.api.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.request.RoleBindPermissionRequest;

import java.util.Set;

/**
 * 角色绑定权限操作的接口
 *
 * @author fengshuonan
 * @since 2023/6/13 22:16
 */
public interface RoleAssignOperateAction {

    /**
     * 获取节点的类型，有4种节点类型
     *
     * @author fengshuonan
     * @since 2023/6/13 22:17
     */
    PermissionNodeTypeEnum getNodeType();

    /**
     * 执行角色绑定权限的过程，根据不同的点击类型，执行不同的操作过程
     *
     * @param roleBindPermissionRequest    角色绑定权限的参数
     * @param roleLimitMenuIdsAndOptionIds 角色所能承受的绑定范围
     * @author fengshuonan
     * @since 2023/6/13 22:17
     */
    void doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest, Set<Long> roleLimitMenuIdsAndOptionIds);

}
