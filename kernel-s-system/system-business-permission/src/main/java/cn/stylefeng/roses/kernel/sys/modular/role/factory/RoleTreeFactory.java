package cn.stylefeng.roses.kernel.sys.modular.role.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.tree.factory.SortedTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleTreeDTO;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.RoleCategory;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.RoleTreeNodeTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色树构建工厂
 *
 * @author fengshuonan
 * @since 2025/1/24 12:27
 */
public class RoleTreeFactory {

    /**
     * 创建角色和角色分类组成的树结构
     *
     * @author fengshuonan
     * @since 2025/1/24 13:33
     */
    public static List<SysRoleTreeDTO> createRoleTree(List<RoleCategory> roleCategoryList, List<SysRole> sysRoleList) {

        List<SysRoleTreeDTO> roleTreeList = new ArrayList<>();

        // 先将角色分类放入到树结构中
        for (RoleCategory roleCategory : roleCategoryList) {
            SysRoleTreeDTO roleTree = new SysRoleTreeDTO();
            roleTree.setRoleTreeNodeId(roleCategory.getId());
            roleTree.setRoleTreeParentNodeId(roleCategory.getCategoryParentId());
            roleTree.setRoleTreeNodeName(roleCategory.getRoleCategoryName());
            roleTree.setNodeType(RoleTreeNodeTypeEnum.ROLE_CATEGORY.getCode());
            roleTree.setRoleType(roleCategory.getCategoryType());
            roleTreeList.add(roleTree);
        }

        // 将角色放在树结构中
        for (SysRole sysRole : sysRoleList) {
            SysRoleTreeDTO roleTree = new SysRoleTreeDTO();
            roleTree.setRoleTreeNodeId(sysRole.getRoleId());

            // 角色如果有分类，则设置角色的父级id为角色分类id，如果没有，则直接个挂载根下边
            if (ObjectUtil.isNotEmpty(sysRole.getRoleCategoryId())) {
                roleTree.setRoleTreeParentNodeId(sysRole.getRoleCategoryId());
            } else {
                roleTree.setRoleTreeParentNodeId(TreeConstants.DEFAULT_PARENT_ID);
            }

            roleTree.setRoleTreeNodeName(sysRole.getRoleName());
            roleTree.setNodeType(RoleTreeNodeTypeEnum.ROLE.getCode());
            roleTree.setRoleType(sysRole.getRoleType());
            roleTreeList.add(roleTree);
        }

        return new SortedTreeBuildFactory<SysRoleTreeDTO>().doTreeBuild(roleTreeList);
    }

}
