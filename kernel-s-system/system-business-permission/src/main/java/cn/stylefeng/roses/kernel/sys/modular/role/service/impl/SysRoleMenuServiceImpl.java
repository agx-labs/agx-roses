package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.dsctn.api.context.DataSourceContext;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveMenuCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMenuMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色菜单关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService,
        RemoveRoleCallbackApi, RemoveMenuCallbackApi {

    @Resource(name = "roleMenuCache")
    private CacheOperatorApi<List<Long>> roleMenuCache;

    @Override
    public void bindRoleMenus(Long roleId, List<SysMenu> menuList) {

        if (ObjectUtil.isEmpty(roleId) || ObjectUtil.isEmpty(menuList)) {
            return;
        }

        // 清空角色的相关角色菜单关联
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.remove(queryWrapper);

        // 绑定角色菜单
        ArrayList<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        for (SysMenu sysMenu : menuList) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setAppId(sysMenu.getAppId());
            sysRoleMenu.setMenuId(sysMenu.getMenuId());
            sysRoleMenus.add(sysRoleMenu);
        }

        if (DbTypeEnum.MYSQL.equals(DataSourceContext.me().getCurrentDbType())) {
            this.getBaseMapper().insertBatchSomeColumn(sysRoleMenus);
        } else {
            this.saveBatch(sysRoleMenus);
        }

        // 清空角色和菜单的缓存
        roleMenuCache.remove(String.valueOf(roleId));
    }

    @Override
    public List<Long> getRoleBindMenuIdList(List<Long> roleIdList) {

        List<Long> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(roleIdList)) {
            return result;
        }

        for (Long roleId : roleIdList) {

            String roleIdKey = String.valueOf(roleId);

            // 先从缓存中获取，是否有绑定的菜单
            List<Long> cacheMenuIdList = roleMenuCache.get(roleIdKey);

            if (ObjectUtil.isNotEmpty(cacheMenuIdList)) {
                result.addAll(cacheMenuIdList);
                continue;
            }

            // 缓存中没有，则从数据库中查询
            LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
            sysRoleMenuLambdaQueryWrapper.select(SysRoleMenu::getMenuId);
            List<SysRoleMenu> sysRoleMenuList = this.list(sysRoleMenuLambdaQueryWrapper);
            if (ObjectUtil.isNotEmpty(sysRoleMenuList)) {

                List<Long> menuIdListQueryResult = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
                result.addAll(menuIdListQueryResult);

                // 将查询结果加入到缓存中
                roleMenuCache.put(roleIdKey, menuIdListQueryResult, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            }
        }

        return result;
    }

    @Override
    public boolean validateRoleHaveAppIdPermission(List<Long> roleIdList, Long appId) {
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.in(SysRoleMenu::getRoleId, roleIdList);
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getAppId, appId);
        return this.count(sysRoleMenuLambdaQueryWrapper) > 0;
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleMenu::getRoleId, beRemovedRoleIdList);
        this.remove(wrapper);
    }

    @Override
    public void removeMenuAction(Set<Long> beRemovedMenuIdList) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenu::getMenuId, beRemovedMenuIdList);
        this.remove(queryWrapper);
    }

}