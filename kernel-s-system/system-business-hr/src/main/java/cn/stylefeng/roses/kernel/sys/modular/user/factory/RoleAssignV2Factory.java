package cn.stylefeng.roses.kernel.sys.modular.user.factory;

import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleTreeDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.UserRoleDTO;

import java.util.List;

/**
 * 用户绑定角色的工厂
 *
 * @author fengshuonan
 * @since 2025/1/24 15:23
 */
public class RoleAssignV2Factory {

    /**
     * 在角色树上，绑定业务角色
     *
     * @author fengshuonan
     * @since 2025/1/24 15:27
     */
    public static void mountBusinessRole(List<SysRoleTreeDTO> totalBusinessRoleTree, List<UserRoleDTO> userLinkedOrgRoleList) {

        // 判断list中的角色，是否有绑定的
        for (SysRoleTreeDTO treeNode : totalBusinessRoleTree) {
            for (UserRoleDTO userRoleInfo : userLinkedOrgRoleList) {
                if (treeNode.getRoleTreeNodeId().equals(userRoleInfo.getRoleId())) {
                    treeNode.setCheckedFlag(true);
                }
            }

            // 如果有还有子集，则判断子集的列表中是否有绑定的角色
            if (treeNode.getChildren() != null && !treeNode.getChildren().isEmpty()) {
                mountBusinessRole(treeNode.getChildren(), userLinkedOrgRoleList);
            }
        }
    }

}
