package cn.stylefeng.roses.kernel.sys.modular.menu.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.MenuItemDetail;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单信息组装工厂
 *
 * @author fengshuonan
 * @since 2023/6/14 22:05
 */
public class MenuFactory {

    /**
     * 获取指定参数的菜单集合，缺失的所有的父级菜单id
     * <p>
     * 此方法用在菜单管理界面，带搜索条件查询时，组装菜单树使用
     *
     * @author fengshuonan
     * @since 2023/6/14 22:05
     */
    public static Set<Long> getMenuParentIds(List<SysMenu> sysMenus) {

        // 已经有的菜单id集合
        Set<Long> alreadyMenuIdList = sysMenus.stream().map(SysMenu::getMenuId).collect(Collectors.toSet());

        // 需要补全的父级id集合
        Set<Long> needToAddResult = new HashSet<>();

        // 遍历菜单，找到所有的菜单父级集合
        for (SysMenu sysMenu : sysMenus) {
            String menuPidsStr = sysMenu.getMenuPids();

            // 去掉中括号符号
            menuPidsStr = menuPidsStr.replaceAll("\\[", "");
            menuPidsStr = menuPidsStr.replaceAll("]", "");

            // 获取所有上级id列表
            String[] menuParentIdList = menuPidsStr.split(",");

            // 查找到缺失的父级id
            for (String parentMenuIdItem : menuParentIdList) {
                Long menuId = Long.valueOf(parentMenuIdItem);
                if (!alreadyMenuIdList.contains(menuId)) {
                    if (!parentMenuIdItem.equals(TreeConstants.DEFAULT_PARENT_ID.toString())) {
                        needToAddResult.add(menuId);
                    }
                }
            }
        }

        return needToAddResult;
    }

    /**
     * 将应用信息和菜单信息组装为界面需要的展示结果
     *
     * @author fengshuonan
     * @since 2023/6/14 22:16
     */
    public static List<AppGroupDetail> createAppGroupDetailResult(List<AppGroupDetail> appGroupDetails, List<SysMenu> sysMenus) {

        // 1. 按应用拆分菜单，进行分组，key是appId，value是应用下的菜单集合
        Map<Long, List<SysMenu>> appIdMenuList = sysMenus.stream().collect(Collectors.groupingBy(SysMenu::getAppId));

        // 2. 遍历应用详情信息，将菜单挂载到应用列表下
        for (AppGroupDetail appGroupDetail : appGroupDetails) {

            Long appId = appGroupDetail.getAppId();

            List<SysMenu> appMenus = appIdMenuList.get(appId);
            if (ObjectUtil.isEmpty(appMenus)) {
                continue;
            }

            // 将菜单信息转化为响应类型
            List<MenuItemDetail> appMenuItems = new ArrayList<>();
            for (SysMenu appMenu : appMenus) {
                MenuItemDetail menuItemDetail = new MenuItemDetail(appMenu.getMenuId(), appMenu.getMenuParentId(), appMenu.getMenuName(),
                        appMenu.getMenuType());
                appMenuItems.add(menuItemDetail);
            }

            // 将应用下的菜单组装成树
            List<MenuItemDetail> menuItemDetailList = new DefaultTreeBuildFactory<MenuItemDetail>().doTreeBuild(appMenuItems);
            appGroupDetail.setMenuList(menuItemDetailList);

            // 将一级菜单展开
            List<Long> openMenuIdList = new ArrayList<>();
            if (ObjectUtil.isEmpty(menuItemDetailList)) {
                appGroupDetail.setOpenMenuIdList(openMenuIdList);
            } else {
                for (MenuItemDetail menuItemDetail : menuItemDetailList) {
                    if (ObjectUtil.isNotEmpty(menuItemDetail.getChildren())) {
                        openMenuIdList.add(menuItemDetail.getMenuId());
                    }
                }
                appGroupDetail.setOpenMenuIdList(openMenuIdList);
            }
        }

        return appGroupDetails;
    }

}
