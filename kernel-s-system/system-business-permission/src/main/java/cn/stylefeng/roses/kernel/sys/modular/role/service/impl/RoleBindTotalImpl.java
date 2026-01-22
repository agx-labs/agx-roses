package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.api.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
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
import java.util.Set;

/**
 * 角色绑定权限，点击绑定所有时候的业务处理
 *
 * @author fengshuonan
 * @since 2023/6/14 14:13
 */
@Service
public class RoleBindTotalImpl implements RoleAssignOperateAction, RoleBindLimitAction {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysRoleLimitService sysRoleLimitService;

    @Override
    public PermissionNodeTypeEnum getNodeType() {
        return PermissionNodeTypeEnum.TOTAL;
    }

    @Override
    public void doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest, Set<Long> roleLimitMenuIdsAndOptionIds) {

        Long roleId = roleBindPermissionRequest.getRoleId();

        // 清空角色绑定的所有菜单和功能
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.sysRoleMenuService.remove(sysRoleMenuLambdaQueryWrapper);

        LambdaQueryWrapper<SysRoleMenuOptions> sysRoleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
        AssertAssignUtil.assertAssign(roleId, sysRoleMenuOptionsLambdaQueryWrapper);
        this.sysRoleMenuOptionsService.remove(sysRoleMenuOptionsLambdaQueryWrapper);

        // 如果是选中状态，则从新绑定所有的选项
        if (roleBindPermissionRequest.getChecked()) {

            // 获取所有的菜单
            List<SysMenu> totalMenus = this.sysMenuService.getTotalMenus(roleLimitMenuIdsAndOptionIds);

            // 绑定角色的所有菜单
            List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
            for (SysMenu menuItem : totalMenus) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setAppId(menuItem.getAppId());
                sysRoleMenu.setMenuId(menuItem.getMenuId());
                sysRoleMenuList.add(sysRoleMenu);
            }
            this.sysRoleMenuService.saveBatch(sysRoleMenuList);

            // 获取所有的功能
            List<SysMenuOptions> sysMenuOptionsList = sysMenuOptionsService.getTotalMenuOptionsList(roleLimitMenuIdsAndOptionIds);

            // 绑定角色的所有功能
            List<SysRoleMenuOptions> sysRoleMenuOptionsList = new ArrayList<>();
            for (SysMenuOptions menuOptionItem : sysMenuOptionsList) {
                SysRoleMenuOptions sysRoleMenuOptions = new SysRoleMenuOptions();
                sysRoleMenuOptions.setRoleId(roleId);
                sysRoleMenuOptions.setAppId(menuOptionItem.getAppId());
                sysRoleMenuOptions.setMenuId(menuOptionItem.getMenuId());
                sysRoleMenuOptions.setMenuOptionId(menuOptionItem.getMenuOptionId());
                sysRoleMenuOptionsList.add(sysRoleMenuOptions);
            }
            this.sysRoleMenuOptionsService.saveBatch(sysRoleMenuOptionsList);
        }
    }

    @Override
    public PermissionNodeTypeEnum getRoleBindLimitNodeType() {
        return this.getNodeType();
    }

    @Override
    public void doRoleBindLimitAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();

        // 先清空所有的角色限制
        LambdaQueryWrapper<SysRoleLimit> sysRoleLimitLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLimitLambdaQueryWrapper.eq(SysRoleLimit::getRoleId, roleId);
        this.sysRoleLimitService.remove(sysRoleLimitLambdaQueryWrapper);

        // 如果是选中状态，则绑定所有的选项
        if (roleBindPermissionRequest.getChecked()) {

            // 获取所有的菜单
            List<SysMenu> totalMenus = this.sysMenuService.getTotalMenus();

            // 绑定角色的所有菜单
            List<SysRoleLimit> sysRoleLimitList = new ArrayList<>();
            for (SysMenu menuItem : totalMenus) {
                SysRoleLimit sysRoleLimit = new SysRoleLimit();
                sysRoleLimit.setRoleId(roleId);
                sysRoleLimit.setLimitType(RoleLimitTypeEnum.MENU.getCode());
                sysRoleLimit.setBusinessId(menuItem.getMenuId());
                sysRoleLimitList.add(sysRoleLimit);
            }

            // 获取所有的功能
            List<SysMenuOptions> sysMenuOptionsList = sysMenuOptionsService.getTotalMenuOptionsList();

            // 绑定角色的所有菜单功能
            for (SysMenuOptions menuOptionItem : sysMenuOptionsList) {
                SysRoleLimit sysRoleLimit = new SysRoleLimit();
                sysRoleLimit.setRoleId(roleId);
                sysRoleLimit.setLimitType(RoleLimitTypeEnum.MENU_OPTIONS.getCode());
                sysRoleLimit.setBusinessId(menuOptionItem.getMenuOptionId());
                sysRoleLimitList.add(sysRoleLimit);
            }
            this.sysRoleLimitService.saveBatch(sysRoleLimitList);
        }

    }

}
