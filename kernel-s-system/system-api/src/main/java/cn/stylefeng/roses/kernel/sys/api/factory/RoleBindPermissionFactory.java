package cn.stylefeng.roses.kernel.sys.api.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.response.RoleBindPermissionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 权限分配相关的实体创建
 *
 * @author fengshuonan
 * @since 2023/6/13 16:30
 */
public class RoleBindPermissionFactory {

    /**
     * 组合成角色绑定权限需要的详情信息，一颗树形结构，选中状态都是未选中
     *
     * @param apps    应用信息
     * @param menus   菜单信息
     * @param options 功能信息
     * @author fengshuonan
     * @since 2023/6/13 17:43
     */
    public static RoleBindPermissionResponse composeSelectStructure(List<RoleBindPermissionItem> apps, List<RoleBindPermissionItem> menus, List<RoleBindPermissionItem> options) {

        // 定义全选属性
        RoleBindPermissionResponse roleBindPermissionResponse = new RoleBindPermissionResponse();
        roleBindPermissionResponse.setChecked(false);

        // 【2024年10月30日】把菜单上的功能挂载到菜单上
        for (RoleBindPermissionItem menuItem : menus) {
            for (RoleBindPermissionItem optionItem : options) {
                if (Convert.toLong(optionItem.getNodeParentId()).equals(Convert.toLong(menuItem.getNodeId()))) {
                    List<RoleBindPermissionItem> functionList = menuItem.getFunctionList();
                    if (ObjectUtil.isEmpty(functionList)) {
                        functionList = new ArrayList<>();
                    }
                    functionList.add(optionItem);
                    menuItem.setFunctionList(functionList);
                }
            }
        }

        // 合并应用菜单和功能，并构建树形结构
        apps.addAll(menus);

        List<RoleBindPermissionItem> roleBindPermissionItems = new DefaultTreeBuildFactory<RoleBindPermissionItem>().doTreeBuild(apps);
        roleBindPermissionResponse.setAppPermissionList(roleBindPermissionItems);

        // 递归遍历roleBindPermissionItems，如果children为空，则设置为null而不是空数组
        circulateDeleteEmptyChildren(roleBindPermissionItems);

        return roleBindPermissionResponse;
    }

    /**
     * 将空状态的权限树，填充角色绑定的权限
     *
     * @param roleBindPermissionResponse 空状态的角色权限树（未设置选中状态）
     * @param rolePermissions            角色所拥有的菜单id和功能id的集合
     * @author fengshuonan
     * @since 2023/6/13 19:00
     */
    public static RoleBindPermissionResponse fillCheckedFlag(RoleBindPermissionResponse roleBindPermissionResponse, Set<Long> rolePermissions) {

        List<RoleBindPermissionItem> appList = roleBindPermissionResponse.getAppPermissionList();

        // 开始填充菜单和功能的选中状态
        fillSubItemCheckedFlag(appList, rolePermissions);

        // 填充应用的选中状态，所有的叶子节点和叶子节点的功能都选中，则选中应用
        for (RoleBindPermissionItem appItem : appList) {
            // 默认选中应用，如果有未选中的，则会设置为false
            appItem.setChecked(true);
            fillAppCheckedFlag(appItem, appItem.getChildren());
        }

        // 填充全选的选中状态
        roleBindPermissionResponse.setChecked(true);
        for (RoleBindPermissionItem appItem : appList) {
            if (!appItem.getChecked()) {
                roleBindPermissionResponse.setChecked(false);
            }
        }

        return roleBindPermissionResponse;
    }

    /**
     * 填充子节点的选中状态
     * <p>
     * 根据执行的角色权限参数匹配判断
     *
     * @author fengshuonan
     * @since 2023/6/13 19:21
     */
    private static void fillSubItemCheckedFlag(List<RoleBindPermissionItem> beFilled, Set<Long> rolePermissionList) {

        if (ObjectUtil.isEmpty(beFilled) || ObjectUtil.isEmpty(rolePermissionList)) {
            return;
        }

        for (RoleBindPermissionItem roleBindPermissionItem : beFilled) {

            // 针对菜单的选中填充
            if (rolePermissionList.contains(Convert.toLong(roleBindPermissionItem.getNodeId()))) {
                roleBindPermissionItem.setChecked(true);
            }

            // 针对功能的选中填充
            List<RoleBindPermissionItem> functionList = roleBindPermissionItem.getFunctionList();
            if (ObjectUtil.isNotEmpty(functionList)) {
                for (RoleBindPermissionItem bindPermissionItem : functionList) {
                    if (rolePermissionList.contains(Convert.toLong(bindPermissionItem.getNodeId()))) {
                        bindPermissionItem.setChecked(true);
                    }
                }
            }

            fillSubItemCheckedFlag(roleBindPermissionItem.getChildren(), rolePermissionList);
        }
    }

    /**
     * 填充父级的节点的选中状态
     * <p>
     * 如果所有子集都选中了，则选中所有的父级状态
     *
     * @author fengshuonan
     * @since 2023/6/13 19:25
     */
    private static void fillAppCheckedFlag(RoleBindPermissionItem appItem, List<RoleBindPermissionItem> children) {

        if (ObjectUtil.isEmpty(appItem) || ObjectUtil.isEmpty(children)) {
            return;
        }

        // 遍历自己菜单中，如果是叶子节点勾选了，并且所有功能也选中了，则设置应用也选中
        for (RoleBindPermissionItem menuItem : children) {

            // 非叶子节点，继续向下执行判断
            if (!menuItem.getLeafFlag()) {
                if (ObjectUtil.isNotEmpty(menuItem.getChildren())) {
                    fillAppCheckedFlag(appItem, menuItem.getChildren());
                } else {
                    continue;
                }
            }

            // 菜单是叶子节点，并且未选中，则设置应用也未选中
            if (menuItem.getLeafFlag() && !menuItem.getChecked()) {
                appItem.setChecked(false);
                return;
            }

            // 判断菜单上的功能，是否有未选中的
            if (ObjectUtil.isNotEmpty(menuItem.getFunctionList())) {
                for (RoleBindPermissionItem functionItem : menuItem.getFunctionList()) {
                    if (!functionItem.getChecked()) {
                        appItem.setChecked(false);
                        return;
                    }
                }
            }

            // 如果有子集继续递归遍历
            fillAppCheckedFlag(appItem, menuItem.getChildren());
        }
    }

    /**
     * 递归遍历，删掉为空数组的children属性，改为null
     *
     * @author fengshuonan
     * @since 2024/10/31 10:24
     */
    private static void circulateDeleteEmptyChildren(List<RoleBindPermissionItem> params) {
        for (RoleBindPermissionItem item : params) {
            if (ObjectUtil.isEmpty(item.getChildren())) {
                item.setChildren(null);
            } else {
                circulateDeleteEmptyChildren(item.getChildren());
            }
        }
    }

}
