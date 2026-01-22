package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.dsctn.api.context.DataSourceContext;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.SysRoleLimitServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.enums.role.RoleTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindItem;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.UserRoleDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserRoleMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.stylefeng.roses.kernel.sys.modular.user.constants.UserConstants.UPDATE_USER_ROLE_EVENT;

/**
 * 用户角色关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService, RemoveUserCallbackApi, RemoveRoleCallbackApi {

    @Resource
    private SysRoleServiceApi sysRoleServiceApi;

    @Resource
    private SysUserService sysUserService;

    @Resource(name = "userRoleCache")
    private CacheOperatorApi<List<SysUserRole>> userRoleCache;

    @Resource
    private SysRoleLimitServiceApi sysRoleLimitServiceApi;

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRoles(SysUserRoleRequest sysUserRoleRequest) {

        // 不能修改超级管理员用户的角色，修改管理员角色可能登录会有问题
        boolean userSuperAdminFlag = sysUserService.getUserSuperAdminFlag(sysUserRoleRequest.getUserId());
        if (userSuperAdminFlag) {
            throw new ServiceException(SysUserExceptionEnum.CANT_CHANGE_ADMIN_ROLE);
        }

        // 非超级管理员不能改变系统级角色
        boolean superAdminFlag = LoginContext.me().getSuperAdminFlag();
        if (!superAdminFlag) {
            throw new ServiceException(SysUserExceptionEnum.CANT_CHANGE_BASE_SYSTEM_ROLE);
        }

        // 清空用户绑定的所有系统角色，因为这个界面只分配系统角色
        this.removeRoleAlreadyBind(sysUserRoleRequest);

        // 获取本次绑定的角色id集合
        Set<Long> roleIdList = sysUserRoleRequest.getRoleIdList();

        // 重新绑定用户角色信息
        List<SysUserRole> newUserRoles = this.createUserSystemRoleBinds(sysUserRoleRequest, roleIdList);
        this.saveBatch(newUserRoles);

        // 发布修改用户绑定角色的事件
        BusinessEventPublisher.publishEvent(UPDATE_USER_ROLE_EVENT, sysUserRoleRequest.getUserId());

        // 记录日志
        BusinessLogUtil.setLogTitle("为用户绑定角色，用户账号：", sysUserService.getUserRealName(sysUserRoleRequest.getUserId()));
        BusinessLogUtil.addContent("用户id：\n", sysUserRoleRequest.getUserId());
        BusinessLogUtil.addContent("用户角色信息如下：\n", roleIdList);
    }

    @Override
    public void bindUserDefaultRole(Long userId) {

        // 查询默认角色的角色id
        Long defaultRoleId = sysRoleServiceApi.getDefaultRoleId();

        // 给用户绑定默认角色
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        sysUserRole.setRoleId(defaultRoleId);
        this.save(sysUserRole);

        // 发布修改用户绑定角色的事件
        BusinessEventPublisher.publishEvent(UPDATE_USER_ROLE_EVENT, userId);

    }

    @Override
    public void validateHaveUserBind(Set<Long> beRemovedUserIdList) {
        // none
    }

    @Override
    public void removeUserAction(Set<Long> beRemovedUserIdList) {
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.in(SysUserRole::getUserId, beRemovedUserIdList);
        this.remove(sysUserRoleLambdaQueryWrapper);
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUserRole::getRoleId, beRemovedRoleIdList);
        this.remove(wrapper);
    }

    @Override
    public List<Long> getUserRoleIdList(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        // 先从缓存查找用户的角色
        List<SysUserRole> cachedRoleList = userRoleCache.get(userId.toString());
        if (ObjectUtil.isNotEmpty(cachedRoleList)) {
            return cachedRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        }

        List<SysUserRole> sysUserRoleList = this.dbGetUserTotalRoleList(userId);

        // 查询结果缓存起来
        if (ObjectUtil.isNotEmpty(sysUserRoleList)) {
            userRoleCache.put(userId.toString(), sysUserRoleList, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        }

        return sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public Set<Long> getUserSystemRoleIdList(Long userId) {
        if (userId == null) {
            return new LinkedHashSet<>();
        }

        // 先从缓存查找用户的角色
        List<SysUserRole> cachedRoleList = userRoleCache.get(userId.toString());
        if (ObjectUtil.isNotEmpty(cachedRoleList)) {
            return cachedRoleList.stream().filter(i -> RoleTypeEnum.SYSTEM_ROLE.getCode().equals(i.getRoleType())).map(SysUserRole::getRoleId).collect(Collectors.toSet());
        }

        List<SysUserRole> sysUserRoleList = this.dbGetUserTotalRoleList(userId);

        // 查询结果缓存起来
        if (ObjectUtil.isNotEmpty(sysUserRoleList)) {
            userRoleCache.put(userId.toString(), sysUserRoleList, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        }

        return sysUserRoleList.stream().filter(i -> RoleTypeEnum.SYSTEM_ROLE.getCode().equals(i.getRoleType())).map(SysUserRole::getRoleId).collect(Collectors.toSet());
    }

    @Override
    public List<UserRoleDTO> getUserLinkedOrgRoleList(Long userId) {

        // 先从缓存查找用户的角色
        List<SysUserRole> cachedRoleList = userRoleCache.get(userId.toString());
        if (ObjectUtil.isNotEmpty(cachedRoleList)) {
            List<SysUserRole> result = cachedRoleList.stream().filter(i -> ObjectUtil.isNotEmpty(i.getRoleOrgId())).collect(Collectors.toList());
            return BeanUtil.copyToList(result, UserRoleDTO.class);
        }

        List<SysUserRole> sysUserRoleList = this.dbGetUserTotalRoleList(userId);

        // 查询结果缓存起来
        if (ObjectUtil.isNotEmpty(sysUserRoleList)) {
            userRoleCache.put(userId.toString(), sysUserRoleList, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        }

        List<SysUserRole> finalResult = sysUserRoleList.stream().filter(i -> ObjectUtil.isNotEmpty(i.getRoleOrgId())).collect(Collectors.toList());
        return BeanUtil.copyToList(finalResult, UserRoleDTO.class);
    }

    @Override
    public List<Long> getUserRoleIdListCurrentCompany(Long userId, Long orgId) {

        // 先从缓存查找用户的角色
        List<SysUserRole> cachedRoleList = userRoleCache.get(userId.toString());
        if (ObjectUtil.isNotEmpty(cachedRoleList)) {
            return this.getUserCompanyPermissionRole(cachedRoleList, orgId);
        }

        List<SysUserRole> sysUserRoleList = this.dbGetUserTotalRoleList(userId);

        // 查询结果缓存起来
        if (ObjectUtil.isNotEmpty(sysUserRoleList)) {
            userRoleCache.put(userId.toString(), sysUserRoleList, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        }

        return this.getUserCompanyPermissionRole(sysUserRoleList, orgId);
    }

    @Override
    public List<Long> findUserIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getRoleId, roleId);
        queryWrapper.select(SysUserRole::getUserId);
        List<SysUserRole> list = this.list(queryWrapper);
        return list.stream().map(SysUserRole::getUserId).collect(Collectors.toList());
    }

    @Override
    public Set<Long> findUserRoleLimitScope(Long userId) {

        // 获取用户的所有角色id
        List<Long> userRoleIdList = this.getUserRoleIdList(userId);

        // 获取角色的限制范围列表（菜单和菜单功能id集合）
        return this.sysRoleLimitServiceApi.getRoleBindLimitList(userRoleIdList);
    }

    @Override
    public Set<Long> findCurrentUserRoleLimitScope() {

        Long userId = LoginContext.me().getLoginUser().getUserId();

        return this.findUserRoleLimitScope(userId);
    }

    @Override
    public void removeUserOrgRoleLink(Long userId, Long orgId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        queryWrapper.eq(SysUserRole::getRoleOrgId, orgId);
        this.remove(queryWrapper);
    }

    @Override
    public void deleteUserAllOrgBind(Long userId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        queryWrapper.isNotNull(SysUserRole::getRoleOrgId);
        this.remove(queryWrapper);
    }

    @Override
    public SysUserRole getPointUserRole(Long userId, Long roleId, Long orgId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        queryWrapper.eq(SysUserRole::getRoleId, roleId);
        queryWrapper.eq(SysUserRole::getRoleOrgId, orgId);
        queryWrapper.select(SysUserRole::getUserRoleId, SysUserRole::getUserId, SysUserRole::getRoleId, SysUserRole::getRoleType, SysUserRole::getRoleOrgId);
        return this.getOne(queryWrapper, false);
    }

    @Override
    public void addBusinessAndCompanyBindRole(SysUserRole sysUserRole) {
        if (sysUserRole == null) {
            return;
        }

        // 这个接口只能操作业务的和公司的绑定
        Integer roleType = sysUserRole.getRoleType();
        if (!RoleTypeEnum.BUSINESS_ROLE.getCode().equals(roleType) || !RoleTypeEnum.COMPANY_ROLE.getCode().equals(roleType)) {
            throw new ServiceException(SysUserExceptionEnum.ROLE_TYPE_PERMISSION_ERROR);
        }

        // 这个接口必须传递组织机构，给这个人的组织机构下绑定角色
        Long roleOrgId = sysUserRole.getRoleOrgId();
        if (ObjectUtil.isEmpty(roleOrgId)) {
            throw new ServiceException(SysUserExceptionEnum.ORG_ID_EMPTY_ERROR);
        }

        this.save(sysUserRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOtherOrgBusinessRole(Long userId, Long excludeOrgId, List<NewUserRoleBindItem> currentOrgBindRoleList) {

        // 1. 清空用户在其他公司的业务角色
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, userId);
        queryWrapper.eq(SysUserRole::getRoleType, RoleTypeEnum.BUSINESS_ROLE.getCode());
        queryWrapper.ne(SysUserRole::getRoleOrgId, excludeOrgId);
        this.remove(queryWrapper);

        // 清空角色缓存
        userRoleCache.remove(userId.toString());

        // 2. 获取用户有几个机构
        List<Long> userOrgIdList = sysUserOrgService.getUserOrgIdList(userId, true);
        if (ObjectUtil.isEmpty(userOrgIdList)) {
            return;
        }
        List<Long> excludeAfterOrgIds = userOrgIdList.stream().filter(orgId -> !orgId.equals(excludeOrgId)).collect(Collectors.toList());
        if (ObjectUtil.isEmpty(excludeAfterOrgIds)) {
            return;
        }

        // 3. 给这些机构，绑定上当前公司的业务角色
        if (ObjectUtil.isEmpty(currentOrgBindRoleList)) {
            return;
        }
        ArrayList<SysUserRole> sysUserRoles = new ArrayList<>();
        for (Long userOrgId : excludeAfterOrgIds) {
            for (NewUserRoleBindItem newUserRoleBindItem : currentOrgBindRoleList) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(newUserRoleBindItem.getRoleId());
                sysUserRole.setRoleType(RoleTypeEnum.BUSINESS_ROLE.getCode());
                sysUserRole.setRoleOrgId(userOrgId);
                sysUserRoles.add(sysUserRole);
            }
        }
        this.saveBatch(sysUserRoles);

        // 清空角色缓存
        userRoleCache.remove(userId.toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void quickBatchSaveUserRole(List<SysUserRole> batchUserRole) {
        if (DbTypeEnum.MYSQL.equals(DataSourceContext.me().getCurrentDbType())) {
            this.getBaseMapper().insertBatchSomeColumn(batchUserRole);
        } else {
            this.saveBatch(batchUserRole);
        }
    }

    /**
     * 清空用户绑定的所有系统角色，这个界面只管分配系统角色
     *
     * @author fengshuonan
     * @since 2024/1/17 22:47
     */
    private void removeRoleAlreadyBind(SysUserRoleRequest sysUserRoleRequest) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, sysUserRoleRequest.getUserId());
        queryWrapper.eq(SysUserRole::getRoleType, RoleTypeEnum.SYSTEM_ROLE.getCode());
        this.remove(queryWrapper);
    }

    /**
     * 从数据库获取所有的用户角色
     *
     * @author fengshuonan
     * @since 2024-01-17 16:40
     */
    private List<SysUserRole> dbGetUserTotalRoleList(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        wrapper.select(SysUserRole::getRoleId, SysUserRole::getRoleOrgId, SysUserRole::getRoleType);
        return this.list(wrapper);
    }

    /**
     * 获取当前用户登录机构的角色id集合
     * <p>
     * 从参数中找到当前用户登录身份机构的 + 用户基本的角色集合
     *
     * @author fengshuonan
     * @since 2024-01-17 16:16
     */
    private List<Long> getUserCompanyPermissionRole(List<SysUserRole> paramRoles, Long userCurrentOrgId) {

        // 1. 先获取最基本的用户角色，不分公司的，每个人都有的角色
        Set<Long> baseRoleIdList = paramRoles.stream().filter(i -> RoleTypeEnum.SYSTEM_ROLE.getCode().equals(i.getRoleType()) && i.getRoleOrgId() == null).map(SysUserRole::getRoleId)
                .collect(Collectors.toSet());

        // 没传当前公司id，则只返回最基本的角色
        if (ObjectUtil.isEmpty(userCurrentOrgId)) {
            return new ArrayList<>(baseRoleIdList);
        }

        // 2. 获取用户当前登录公司下的角色id集合
        Set<Long> currentCompanyRoleIdList = paramRoles.stream()
                .filter(i -> i.getRoleOrgId() != null && i.getRoleOrgId().equals(userCurrentOrgId)).map(SysUserRole::getRoleId)
                .collect(Collectors.toSet());

        // 3. 合并两个集合并返回
        List<Long> resultRoleIdList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(baseRoleIdList)) {
            resultRoleIdList.addAll(baseRoleIdList);
        }
        if (ObjectUtil.isNotEmpty(currentCompanyRoleIdList)) {
            resultRoleIdList.addAll(currentCompanyRoleIdList);
        }
        return resultRoleIdList;
    }

    /**
     * 创建用户角色的绑定
     *
     * @param sysUserRoleRequest 接口请求参数
     * @param roleIdList         用户绑定的角色列表
     * @author fengshuonan
     * @since 2024-01-17 17:27
     */
    private List<SysUserRole> createUserSystemRoleBinds(SysUserRoleRequest sysUserRoleRequest, Set<Long> roleIdList) {
        List<SysUserRole> newUserRoles = new ArrayList<>();
        for (Long newRoleId : roleIdList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUserRoleRequest.getUserId());
            sysUserRole.setRoleId(newRoleId);
            sysUserRole.setRoleType(RoleTypeEnum.SYSTEM_ROLE.getCode());
            newUserRoles.add(sysUserRole);
        }
        return newUserRoles;
    }

}