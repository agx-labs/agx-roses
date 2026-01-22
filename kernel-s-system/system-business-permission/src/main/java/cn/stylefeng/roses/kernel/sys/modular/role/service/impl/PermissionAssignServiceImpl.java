package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.db.mp.tenant.context.TenantSwitchContext;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.constants.RoleConstants;
import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.api.factory.RoleBindPermissionFactory;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.response.RoleBindPermissionResponse;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.factory.PermissionAssignFactory;
import cn.stylefeng.roses.kernel.sys.modular.role.service.PermissionAssignService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色权限绑定相关的接口
 *
 * @author fengshuonan
 * @since 2023/6/13 16:14
 */
@Service
public class PermissionAssignServiceImpl implements PermissionAssignService {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysAppService sysAppService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysUserRoleServiceApi userRoleServiceApi;

    @Override
    public RoleBindPermissionResponse getRoleBindPermission(RoleBindPermissionRequest roleBindPermissionRequest) {

        // 1. 获取角色的限制范围，如果限制范围为空，则为查询所有的范围
        Set<Long> userRoleLimitScope = userRoleServiceApi.findCurrentUserRoleLimitScope();

        // 2. 整理出一个总的响应的结构树，选择状态为空
        RoleBindPermissionResponse selectTreeStructure = TenantSwitchContext.me().doNoTenantChange(() -> {
            return this.createSelectTreeStructure(userRoleLimitScope);
        });

        // 3. 获取角色绑定的应用，菜单，功能列表
        Set<Long> roleBindMenusAndOptions = this.getRoleBindMenusAndOptions(roleBindPermissionRequest.getRoleId());

        // 4. 组合结构树和角色绑定的信息，填充选择状态，封装返回结果
        return RoleBindPermissionFactory.fillCheckedFlag(selectTreeStructure, roleBindMenusAndOptions);
    }

    @Override
    public void updateRoleBindPermission(RoleBindPermissionRequest roleBindPermissionRequest) {

        BusinessLogUtil.setLogTitle("更新角色绑定的权限");

        // 1. 获取角色的限制范围，如果限制范围为空，则为查询所有的范围
        Set<Long> userRoleLimitScope = userRoleServiceApi.findCurrentUserRoleLimitScope();

        // 2. 绑定角色的权限
        Map<String, RoleAssignOperateAction> operateActionMap = SpringUtil.getBeansOfType(RoleAssignOperateAction.class);
        for (RoleAssignOperateAction roleAssignOperateAction : operateActionMap.values()) {
            if (roleAssignOperateAction.getNodeType().getCode().equals(roleBindPermissionRequest.getPermissionNodeType())) {
                roleAssignOperateAction.doOperateAction(roleBindPermissionRequest, userRoleLimitScope);

                // 更新角色绑定权限的缓存
                BusinessEventPublisher.publishEvent(RoleConstants.ROLE_BIND_MENU_EVENT, roleBindPermissionRequest.getRoleId());
                BusinessEventPublisher.publishEvent(RoleConstants.ROLE_BIND_MENU_OPTIONS_EVENT, roleBindPermissionRequest.getRoleId());

                BusinessLogUtil.addContent("角色绑定权限的相关参数如下：\n", roleBindPermissionRequest);
                return;
            }
        }
    }

    @Override
    public RoleBindPermissionResponse createSelectTreeStructure() {
        return this.createSelectTreeStructure(null);
    }

    @Override
    public RoleBindPermissionResponse createSelectTreeStructure(Set<Long> limitMenuIdsAndOptionIds) {

        // 获取指定所有的菜单，如果不限制范围，则直接查询所有的菜单
        List<SysMenu> limitScopeTotalMenus = null;
        if (ObjectUtil.isEmpty(limitMenuIdsAndOptionIds)) {
            limitScopeTotalMenus = this.sysMenuService.getTotalMenus();
        } else {
            limitScopeTotalMenus = this.sysMenuService.getTotalMenus(limitMenuIdsAndOptionIds);
        }

        // 【2024年10月30日新增】将这些菜单，补全父级节点，因为角色权限分配这块，单纯渲染叶子节点不方便
        List<SysMenu> limitScopeTotalMenusWithTree = this.sysMenuService.completeParentMenu(limitScopeTotalMenus);

        // 组装所有的叶子节点菜单【初始化菜单】
        List<RoleBindPermissionItem> totalResultMenus = PermissionAssignFactory.createPermissionMenus(limitScopeTotalMenusWithTree);

        // 查询菜单对应的所有应用
        Set<Long> appIdList = limitScopeTotalMenusWithTree.stream().map(SysMenu::getAppId).collect(Collectors.toSet());
        LambdaQueryWrapper<SysApp> sysAppLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysAppLambdaQueryWrapper.in(SysApp::getAppId, appIdList);
        sysAppLambdaQueryWrapper.select(SysApp::getAppId, SysApp::getAppName);
        sysAppLambdaQueryWrapper.orderByAsc(SysApp::getAppSort);
        List<SysApp> totalAppList = sysAppService.list(sysAppLambdaQueryWrapper);

        // 组装所有的应用节点信息【初始化应用】
        List<RoleBindPermissionItem> totalResultApps = PermissionAssignFactory.createApps(totalAppList);

        // 获取所有的菜单上的功能
        LambdaQueryWrapper<SysMenuOptions> optionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        optionsLambdaQueryWrapper.select(SysMenuOptions::getMenuId, SysMenuOptions::getMenuOptionId, SysMenuOptions::getOptionName);
        Set<Long> menuIds = totalResultMenus.stream().map(i -> Long.valueOf(i.getNodeId())).collect(Collectors.toSet());
        optionsLambdaQueryWrapper.in(SysMenuOptions::getMenuId, menuIds);
        // 如果限制了范围，则只查询限制范围内的功能
        if (ObjectUtil.isNotEmpty(limitMenuIdsAndOptionIds)) {
            optionsLambdaQueryWrapper.in(SysMenuOptions::getMenuOptionId, limitMenuIdsAndOptionIds);
        }
        List<SysMenuOptions> sysMenuOptionsList = sysMenuOptionsService.list(optionsLambdaQueryWrapper);

        // 组装所有的应用节点信息【初始化菜单功能】
        List<RoleBindPermissionItem> totalResultOptions = PermissionAssignFactory.createMenuOptions(sysMenuOptionsList);

        // 将应用、菜单、功能组成返回结果
        return RoleBindPermissionFactory.composeSelectStructure(totalResultApps, totalResultMenus, totalResultOptions);
    }

    @Override
    public Set<Long> getRoleBindMenusAndOptions(Long roleId) {

        HashSet<Long> resultPermissions = new HashSet<>();

        // 获取角色绑定的菜单
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.select(SysRoleMenu::getMenuId);
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenuList = this.sysRoleMenuService.list(sysRoleMenuLambdaQueryWrapper);
        Set<Long> menuIdSet = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
        resultPermissions.addAll(menuIdSet);

        // 获取角色绑定的功能
        LambdaQueryWrapper<SysRoleMenuOptions> sysRoleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuOptionsLambdaQueryWrapper.select(SysRoleMenuOptions::getMenuOptionId);
        sysRoleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
        List<SysRoleMenuOptions> sysRoleMenuOptionsList = this.sysRoleMenuOptionsService.list(sysRoleMenuOptionsLambdaQueryWrapper);
        Set<Long> optionsIds = sysRoleMenuOptionsList.stream().map(SysRoleMenuOptions::getMenuOptionId).collect(Collectors.toSet());
        resultPermissions.addAll(optionsIds);

        return resultPermissions;
    }

}
