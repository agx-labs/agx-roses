package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.sys.api.factory.RoleBindPermissionFactory;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.response.RoleBindPermissionResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleBindLimitAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleLimit;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.RoleLimitTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleLimitMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.service.PermissionAssignService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleLimitService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色权限限制业务实现层
 *
 * @author fengshuonan
 * @date 2023/09/08 12:55
 */
@Service
public class SysRoleLimitServiceImpl extends ServiceImpl<SysRoleLimitMapper, SysRoleLimit> implements SysRoleLimitService {

    @Resource
    private PermissionAssignService permissionAssignService;

    @Override
    public RoleBindPermissionResponse getRoleLimit(RoleBindPermissionRequest roleBindPermissionRequest) {

        // 1. 整理出来一个总的相应的结构树，选择状态为空
        RoleBindPermissionResponse selectTreeStructure = permissionAssignService.createSelectTreeStructure();

        // 2. 获取角色限制所对应的菜单和功能列表
        Set<Long> roleBindLimitList = this.getRoleBindLimitList(roleBindPermissionRequest.getRoleId());

        // 3. 组合结构和角色绑定的限制信息，填充选择状态，封装返回结果
        return RoleBindPermissionFactory.fillCheckedFlag(selectTreeStructure, roleBindLimitList);
    }

    @Override
    public void updateRoleBindLimit(RoleBindPermissionRequest roleBindPermissionRequest) {
        Map<String, RoleBindLimitAction> operateActionMap = SpringUtil.getBeansOfType(RoleBindLimitAction.class);
        for (RoleBindLimitAction roleBindLimitAction : operateActionMap.values()) {
            if (roleBindLimitAction.getRoleBindLimitNodeType().getCode().equals(roleBindPermissionRequest.getPermissionNodeType())) {
                roleBindLimitAction.doRoleBindLimitAction(roleBindPermissionRequest);
                return;
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleLimit(Long roleId, Set<Long> menuIdList, Set<Long> menuOptionIdList) {

        // 删除角色所绑定的权限范围
        LambdaQueryWrapper<SysRoleLimit> sysRoleLimitLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLimitLambdaQueryWrapper.eq(SysRoleLimit::getRoleId, roleId);
        this.remove(sysRoleLimitLambdaQueryWrapper);

        // 初始化菜单id列表数据
        List<SysRoleLimit> sysRoleLimits = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(menuIdList)) {
            for (Long menuId : menuIdList) {
                SysRoleLimit sysRoleLimit = new SysRoleLimit();
                sysRoleLimit.setRoleId(roleId);
                sysRoleLimit.setLimitType(RoleLimitTypeEnum.MENU.getCode());
                sysRoleLimit.setBusinessId(menuId);
                sysRoleLimits.add(sysRoleLimit);
            }
        }

        // 初始化菜单功能id列表的数据
        if (ObjectUtil.isNotEmpty(menuOptionIdList)) {
            for (Long menuOptionId : menuOptionIdList) {
                SysRoleLimit sysRoleLimit = new SysRoleLimit();
                sysRoleLimit.setRoleId(roleId);
                sysRoleLimit.setLimitType(RoleLimitTypeEnum.MENU_OPTIONS.getCode());
                sysRoleLimit.setBusinessId(menuOptionId);
                sysRoleLimits.add(sysRoleLimit);
            }
        }

        if (ObjectUtil.isNotEmpty(sysRoleLimits)) {
            this.saveBatch(sysRoleLimits);
        }
    }

    /**
     * 获取角色绑定的限制列表（包含限制的菜单id和限制的菜单功能id）
     *
     * @author fengshuonan
     * @since 2023/9/8 13:55
     */
    @Override
    public Set<Long> getRoleBindLimitList(Long roleId) {
        return this.getRoleBindLimitList(ListUtil.toList(roleId));
    }

    @Override
    public Set<Long> getRoleBindLimitList(List<Long> roleIdList) {
        if (ObjectUtil.isEmpty(roleIdList)) {
            return new HashSet<>();
        }

        LambdaQueryWrapper<SysRoleLimit> sysRoleLimitLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLimitLambdaQueryWrapper.select(SysRoleLimit::getBusinessId);
        sysRoleLimitLambdaQueryWrapper.in(SysRoleLimit::getRoleId, roleIdList);
        List<SysRoleLimit> sysRoleMenuList = this.list(sysRoleLimitLambdaQueryWrapper);
        if (ObjectUtil.isEmpty(sysRoleMenuList)) {
            return new HashSet<>();
        }
        return sysRoleMenuList.stream().map(SysRoleLimit::getBusinessId).collect(Collectors.toSet());
    }

}