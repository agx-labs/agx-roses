package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.api.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.factory.MenuPathCalcFactory;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleBindLimitAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleLimit;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.RoleLimitTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleLimitService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.util.AssertAssignUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色绑定菜单的相关操作
 *
 * @author fengshuonan
 * @since 2023/9/8 15:31
 */
@Service
public class RoleBindMenuImpl implements RoleAssignOperateAction, RoleBindLimitAction {

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleLimitService sysRoleLimitService;

    @Override
    public PermissionNodeTypeEnum getNodeType() {
        return PermissionNodeTypeEnum.MENU;
    }

    @Override
    public void doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest, Set<Long> roleLimitMenuIdsAndOptionIds) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long menuId = roleBindPermissionRequest.getNodeId();

        // 非法操作
        if (ObjectUtil.isNotEmpty(roleLimitMenuIdsAndOptionIds) && !roleLimitMenuIdsAndOptionIds.contains(menuId)) {
            return;
        }

        // 【2024年6月24日】获取菜单的所有id和parentId，后续判断删除的节点的父级没有其他自己节点时候使用
        Map<Long, Long> menuIdParentIdMap = sysMenuService.getMenuIdParentIdMap();
        Set<Long> needToDelete = MenuPathCalcFactory.getMenuParentIds(menuId, menuIdParentIdMap);

        // 1. 先取消绑定角色和菜单
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.in(SysRoleMenu::getMenuId, needToDelete);
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.sysRoleMenuService.remove(sysRoleMenuLambdaQueryWrapper);

        // 1.2. 如果是选中，则执行菜单和角色的绑定
        // 查询菜单对应的appId，冗余一下appId
        Map<Long, Long> menuAppIdMap = sysMenuService.getMenuAppId(ListUtil.list(false, menuId));
        Long appId = menuAppIdMap.get(menuId);
        if (roleBindPermissionRequest.getChecked()) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setAppId(appId);
            this.sysRoleMenuService.save(sysRoleMenu);
        }

        // 2.1. 查询菜单下的所有菜单功能
        List<Long> menuOptions = this.sysMenuService.getMenuOptions(menuId, roleLimitMenuIdsAndOptionIds);

        // 菜单下没有菜单功能，则直接返回
        if (ObjectUtil.isEmpty(menuOptions)) {
            return;
        }

        // 2.2. 如果有菜单功能，则执行先删除后添加的逻辑
        // 先删除角色和菜单功能的绑定
        LambdaQueryWrapper<SysRoleMenuOptions> roleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
        roleMenuOptionsLambdaQueryWrapper.in(SysRoleMenuOptions::getMenuOptionId, menuOptions);
        AssertAssignUtil.assertAssign(roleId, roleMenuOptionsLambdaQueryWrapper);
        sysRoleMenuOptionsService.remove(roleMenuOptionsLambdaQueryWrapper);

        // 2.3. 如果是选中状态，则从新进行这些角色和功能的绑定
        if (roleBindPermissionRequest.getChecked()) {
            ArrayList<SysRoleMenuOptions> sysRoleMenuOptions = new ArrayList<>();
            for (Long menuOptionId : menuOptions) {
                SysRoleMenuOptions roleMenuOptions = new SysRoleMenuOptions();
                roleMenuOptions.setRoleId(roleId);
                roleMenuOptions.setAppId(appId);
                roleMenuOptions.setMenuId(menuId);
                roleMenuOptions.setMenuOptionId(menuOptionId);
                sysRoleMenuOptions.add(roleMenuOptions);
            }
            this.sysRoleMenuOptionsService.saveBatch(sysRoleMenuOptions);
        }
    }

    @Override
    public PermissionNodeTypeEnum getRoleBindLimitNodeType() {
        return this.getNodeType();
    }

    @Override
    public void doRoleBindLimitAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long menuId = roleBindPermissionRequest.getNodeId();

        List<SysRoleLimit> sysRoleLimitTotal = new ArrayList<>();

        // 1. 先取消绑定，角色对菜单的限制
        LambdaQueryWrapper<SysRoleLimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleLimit::getRoleId, roleId);
        wrapper.eq(SysRoleLimit::getBusinessId, menuId);
        this.sysRoleLimitService.remove(wrapper);

        // 2. 如果是选中，则执行角色绑定菜单限制
        if (roleBindPermissionRequest.getChecked()) {
            SysRoleLimit sysRoleLimit = new SysRoleLimit();
            sysRoleLimit.setRoleId(roleId);
            sysRoleLimit.setLimitType(RoleLimitTypeEnum.MENU.getCode());
            sysRoleLimit.setBusinessId(menuId);
            sysRoleLimitTotal.add(sysRoleLimit);
        }

        // 2.1. 查询菜单下的所有菜单功能
        List<Long> menuOptionsIds = this.sysMenuService.getMenuOptions(menuId, null);

        // 菜单下没有菜单功能，则直接返回
        if (ObjectUtil.isEmpty(menuOptionsIds)) {
            this.sysRoleLimitService.saveBatch(sysRoleLimitTotal);
            return;
        }

        // 2.2. 如果有菜单功能，则执行先删除后添加的逻辑
        // 先删除角色和菜单功能的绑定
        LambdaQueryWrapper<SysRoleLimit> optionWrapper = new LambdaQueryWrapper<>();
        optionWrapper.eq(SysRoleLimit::getRoleId, roleId);
        optionWrapper.in(SysRoleLimit::getBusinessId, menuOptionsIds);
        this.sysRoleLimitService.remove(optionWrapper);

        // 2.3. 如果是选中，则创建角色对菜单功能的绑定限制
        if (roleBindPermissionRequest.getChecked()) {
            for (Long menuOptionId : menuOptionsIds) {
                SysRoleLimit sysRoleLimit = new SysRoleLimit();
                sysRoleLimit.setRoleId(roleId);
                sysRoleLimit.setLimitType(RoleLimitTypeEnum.MENU_OPTIONS.getCode());
                sysRoleLimit.setBusinessId(menuOptionId);
                sysRoleLimitTotal.add(sysRoleLimit);
            }
        }
        this.sysRoleLimitService.saveBatch(sysRoleLimitTotal);
    }

}
