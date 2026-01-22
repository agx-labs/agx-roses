package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.dsctn.api.context.DataSourceContext;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveMenuCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMenuOptionsMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色和菜单下的功能关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleMenuOptionsServiceImpl extends ServiceImpl<SysRoleMenuOptionsMapper, SysRoleMenuOptions> implements
        SysRoleMenuOptionsService, RemoveRoleCallbackApi, RemoveMenuCallbackApi {

    @Resource(name = "roleMenuOptionsCache")
    private CacheOperatorApi<List<Long>> roleMenuOptionsCache;

    @Override
    public void removeRoleBindOptions(Long optionsId) {
        LambdaQueryWrapper<SysRoleMenuOptions> sysRoleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getMenuOptionId, optionsId);
        this.remove(sysRoleMenuOptionsLambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRoleMenuOptions(Long roleId, List<SysMenuOptions> sysMenuOptionsList) {

        if (ObjectUtil.isEmpty(roleId) || ObjectUtil.isEmpty(sysMenuOptionsList)) {
            return;
        }

        // 清空角色绑定的菜单功能
        LambdaQueryWrapper<SysRoleMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
        this.remove(queryWrapper);

        // 绑定角色的菜单功能
        ArrayList<SysRoleMenuOptions> sysRoleMenuOptionList = new ArrayList<>();
        for (SysMenuOptions sysMenuOptions : sysMenuOptionsList) {
            SysRoleMenuOptions roleMenuOptionItem = new SysRoleMenuOptions();
            roleMenuOptionItem.setRoleId(roleId);
            roleMenuOptionItem.setMenuOptionId(sysMenuOptions.getMenuOptionId());
            roleMenuOptionItem.setMenuId(sysMenuOptions.getMenuId());
            roleMenuOptionItem.setAppId(sysMenuOptions.getAppId());
            sysRoleMenuOptionList.add(roleMenuOptionItem);
        }

        if (DbTypeEnum.MYSQL.equals(DataSourceContext.me().getCurrentDbType())) {
            this.getBaseMapper().insertBatchSomeColumn(sysRoleMenuOptionList);
        } else {
            this.saveBatch(sysRoleMenuOptionList);
        }

        // 清空角色和菜单功能的绑定
        roleMenuOptionsCache.remove(String.valueOf(roleId));
    }

    @Override
    public List<Long> getRoleBindMenuOptionsIdList(List<Long> roleIdList) {

        List<Long> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(roleIdList)) {
            return result;
        }

        for (Long roleId : roleIdList) {

            String roleIdKey = roleId.toString();

            // 先从缓存找到角色绑定的功能
            List<Long> optionsCached = roleMenuOptionsCache.get(roleIdKey);

            if (ObjectUtil.isNotEmpty(optionsCached)) {
                result.addAll(optionsCached);
                continue;
            }

            // 查询数据库角色对应的菜单功能
            LambdaQueryWrapper<SysRoleMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
            queryWrapper.select(SysRoleMenuOptions::getMenuOptionId);
            List<SysRoleMenuOptions> roleMenuOptions = this.list(queryWrapper);
            if (ObjectUtil.isNotEmpty(roleMenuOptions)) {

                List<Long> menuOptionsIdQueryResult = roleMenuOptions.stream().map(SysRoleMenuOptions::getMenuOptionId)
                        .collect(Collectors.toList());
                result.addAll(menuOptionsIdQueryResult);

                // 添加到缓存中
                roleMenuOptionsCache.put(roleIdKey, menuOptionsIdQueryResult, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            }
        }

        return result;
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysRoleMenuOptions> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleMenuOptions::getRoleId, beRemovedRoleIdList);
        this.remove(wrapper);

        // 清空角色和菜单功能的绑定
        roleMenuOptionsCache.remove(beRemovedRoleIdList.stream().map(Object::toString).collect(Collectors.toList()));
    }

    @Override
    public void removeMenuAction(Set<Long> beRemovedMenuIdList) {
        LambdaQueryWrapper<SysRoleMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenuOptions::getMenuId, beRemovedMenuIdList);
        this.remove(queryWrapper);
    }

}