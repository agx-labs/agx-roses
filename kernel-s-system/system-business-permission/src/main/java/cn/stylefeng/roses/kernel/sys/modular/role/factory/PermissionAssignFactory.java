package cn.stylefeng.roses.kernel.sys.modular.role.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.api.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限分配相关的实体创建
 *
 * @author fengshuonan
 * @since 2023/6/13 16:30
 */
public class PermissionAssignFactory {

    /**
     * 创建权限绑定的菜单列表
     * <p>
     * 注意： 菜单必须是最子节点，也就是叶子节点
     *
     * @author fengshuonan
     * @since 2023/6/13 16:32
     */
    public static List<RoleBindPermissionItem> createPermissionMenus(List<SysMenu> limitScopeTotalMenusWithTree) {

        if (ObjectUtil.isEmpty(limitScopeTotalMenusWithTree)) {
            return new ArrayList<>();
        }

        // 搜集所有的父级菜单id
        Set<Long> totalParentMenuId = limitScopeTotalMenusWithTree.stream().map(SysMenu::getMenuParentId).collect(Collectors.toSet());

        // 通过父级菜单，筛选出来所有的叶子节点（如果菜单不存在父级菜单里，则代表是叶子节点）
        Set<Long> leafMenus = limitScopeTotalMenusWithTree.stream().map(SysMenu::getMenuId).filter(menuId -> !totalParentMenuId.contains(menuId)).collect(Collectors.toSet());

        // 叶子节点转化为RoleBindPermissionItem结构
        ArrayList<RoleBindPermissionItem> roleBindPermissionItems = new ArrayList<>();
        for (SysMenu sysMenu : limitScopeTotalMenusWithTree) {

            // 设置菜单的父级id，如果父级id是-1，则设置appId为父级id
            if (TreeConstants.DEFAULT_PARENT_ID.equals(sysMenu.getMenuParentId())) {
                sysMenu.setMenuParentId(sysMenu.getAppId());
            }

            // 转化结构
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem(sysMenu.getMenuId(), sysMenu.getMenuParentId(), sysMenu.getMenuName(), PermissionNodeTypeEnum.MENU.getCode(),
                    false);

            // 判断是否是叶子节点
            if (leafMenus.contains(sysMenu.getMenuId())) {
                roleBindPermissionItem.setLeafFlag(true);
            } else {
                roleBindPermissionItem.setLeafFlag(false);
            }

            roleBindPermissionItems.add(roleBindPermissionItem);
        }

        return roleBindPermissionItems;
    }

    /**
     * 创建权限绑定的应用信息
     *
     * @author fengshuonan
     * @since 2023/6/13 17:00
     */
    public static List<RoleBindPermissionItem> createApps(List<SysApp> sysApps) {

        if (ObjectUtil.isEmpty(sysApps)) {
            return new ArrayList<>();
        }

        ArrayList<RoleBindPermissionItem> appResults = new ArrayList<>();

        // 封装响应结果
        for (SysApp sysApp : sysApps) {
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem(sysApp.getAppId(), TreeConstants.DEFAULT_PARENT_ID, sysApp.getAppName(), PermissionNodeTypeEnum.APP.getCode(),
                    false);
            appResults.add(roleBindPermissionItem);
        }

        return appResults;
    }

    /**
     * 创建菜单功能信息
     *
     * @author fengshuonan
     * @since 2023/6/13 17:25
     */
    public static List<RoleBindPermissionItem> createMenuOptions(List<SysMenuOptions> sysMenuOptionsList) {

        if (ObjectUtil.isEmpty(sysMenuOptionsList)) {
            return new ArrayList<>();
        }

        ArrayList<RoleBindPermissionItem> optionsResult = new ArrayList<>();

        // 封装响应结果
        for (SysMenuOptions sysMenuOptions : sysMenuOptionsList) {
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem(sysMenuOptions.getMenuOptionId(), sysMenuOptions.getMenuId(), sysMenuOptions.getOptionName(),
                    PermissionNodeTypeEnum.OPTIONS.getCode(), false);
            optionsResult.add(roleBindPermissionItem);
        }

        return optionsResult;
    }

}
