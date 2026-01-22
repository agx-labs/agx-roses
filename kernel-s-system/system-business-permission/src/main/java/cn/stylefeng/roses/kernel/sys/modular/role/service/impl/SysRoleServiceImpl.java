package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.api.enums.role.RoleTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleTreeDTO;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.RoleCategory;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.exception.SysRoleExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.factory.RoleTreeFactory;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统角色业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleLimitService sysRoleLimitService;

    @Resource(name = "roleNameCache")
    private CacheOperatorApi<String> roleNameCache;

    @Resource
    private RoleCategoryService roleCategoryService;

    @Override
    public void add(SysRoleRequest sysRoleRequest) {

        // 权限检查，针对非管理员
        this.rolePermissionValidate(sysRoleRequest);

        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(sysRoleRequest, sysRole);

        // 设置角色默认的数据范围，默认查看全部
        sysRole.setDataScopeType(DataScopeTypeEnum.DEPT_WITH_CHILD.getCode());

        this.save(sysRole);

        // 添加日志
        BusinessLogUtil.setLogTitle("添加角色，角色名称：", sysRoleRequest.getRoleName());
        BusinessLogUtil.addContent("角色信息详情如下：\n", sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // 系统角色不能被删除
        if (RoleTypeEnum.SYSTEM_ROLE.getCode().equals(sysRole.getRoleType())) {
            throw new ServiceException(SysRoleExceptionEnum.SYSTEM_ROLE_CANT_DELETE);
        }

        // 非管理员，只能删除自己公司的角色
        if (!LoginContext.me().getSuperAdminFlag()) {
            Long currentUserCompanyId = LoginContext.me().getCurrentUserCompanyId();
            if (currentUserCompanyId == null || !currentUserCompanyId.equals(sysRole.getRoleCompanyId())) {
                throw new ServiceException(SysRoleExceptionEnum.DEL_PERMISSION_ERROR);
            }
        }

        // 删除角色
        this.baseDelete(CollectionUtil.set(false, sysRole.getRoleId()));

        // 添加日志
        BusinessLogUtil.setLogTitle("删除角色，角色名称：", sysRole.getRoleName());
        BusinessLogUtil.addContent("角色信息详情如下：\n", sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(SysRoleRequest sysRoleRequest) {

        // 校验被删除的角色中是否有管理员角色
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRole::getRoleId, sysRoleRequest.getRoleIdList());
        queryWrapper.eq(SysRole::getRoleType, RoleTypeEnum.SYSTEM_ROLE.getCode());
        long haveSystemFlagCount = this.count(queryWrapper);
        if (haveSystemFlagCount > 0) {
            throw new ServiceException(SysRoleExceptionEnum.SYSTEM_ROLE_CANT_DELETE);
        }

        // 如果当前用户是非管理员，则只能删除自己公司的角色
        if (!LoginContext.me().getSuperAdminFlag()) {
            LambdaQueryWrapper<SysRole> tempWrapper = new LambdaQueryWrapper<>();
            tempWrapper.in(SysRole::getRoleId, sysRoleRequest.getRoleIdList());
            tempWrapper.ne(SysRole::getRoleCompanyId, LoginContext.me().getCurrentUserCompanyId());
            long notMeCreateCount = this.count(tempWrapper);
            if (notMeCreateCount > 0) {
                throw new ServiceException(SysRoleExceptionEnum.DEL_PERMISSION_ERROR);
            }
        }

        // 执行删除角色
        this.baseDelete(sysRoleRequest.getRoleIdList());

        // 添加日志
        BusinessLogUtil.setLogTitle("批量删除角色");
        BusinessLogUtil.addContent("角色id集合如下：\n", sysRoleRequest.getRoleIdList());
    }

    @Override
    public void edit(SysRoleRequest sysRoleRequest) {

        // 权限检查，针对非管理员
        this.rolePermissionValidate(sysRoleRequest);

        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // 添加日志
        BusinessLogUtil.setLogTitle("修改角色信息");
        BusinessLogUtil.addContent("原角色信息如下：\n", sysRole);

        // 不允许修改角色编码
        if (!sysRole.getRoleCode().equals(sysRoleRequest.getRoleCode())) {
            throw new ServiceException(SysRoleExceptionEnum.SUPER_ADMIN_ROLE_CODE_ERROR);
        }

        BeanUtil.copyProperties(sysRoleRequest, sysRole);

        // 如果是编辑角色，改为了基础类型，则需要将公司id清空
        if (RoleTypeEnum.SYSTEM_ROLE.getCode().equals(sysRoleRequest.getRoleType())) {
            sysRole.setRoleCompanyId(null);
            sysRole.setRoleCategoryId(null);
        }

        this.updateById(sysRole);

        // 清空缓存
        this.roleNameCache.remove(sysRole.getRoleId().toString());

        BusinessLogUtil.addContent("修改后角色信息如下：\n", sysRole);
    }

    @Override
    public SysRole detail(SysRoleRequest sysRoleRequest) {
        return this.querySysRole(sysRoleRequest);
    }

    @Override
    public PageResult<SysRole> findPage(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = createWrapper(sysRoleRequest);

        // 只查询需要的字段
        wrapper.select(SysRole::getRoleName, SysRole::getRoleCode, SysRole::getRoleSort, SysRole::getRoleId, BaseEntity::getCreateTime, SysRole::getRoleType, SysRole::getRoleCompanyId);

        // 非管理员用户只能查看自己创建的角色
        this.filterRolePermission(wrapper, sysRoleRequest);

        Page<SysRole> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public Integer getRoleDataScopeType(Long roleId) {

        // 角色id为空，返回仅本人数据
        if (ObjectUtil.isEmpty(roleId)) {
            return DataScopeTypeEnum.SELF.getCode();
        }

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleId, roleId);
        sysRoleLambdaQueryWrapper.select(SysRole::getDataScopeType);
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);

        if (sysRole != null) {
            Integer dataScopeType = sysRole.getDataScopeType();
            if (dataScopeType != null) {
                return dataScopeType;
            }
        }

        return DataScopeTypeEnum.SELF.getCode();
    }

    @Override
    public void updateRoleDataScopeType(Long roleId, Integer dataScopeType) {

        if (ObjectUtil.isEmpty(roleId) || ObjectUtil.isEmpty(dataScopeType)) {
            return;
        }

        LambdaUpdateWrapper<SysRole> sysRoleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysRoleLambdaUpdateWrapper.eq(SysRole::getRoleId, roleId);
        sysRoleLambdaUpdateWrapper.set(SysRole::getDataScopeType, dataScopeType);
        this.update(sysRoleLambdaUpdateWrapper);
    }

    @Override
    public DataScopeTypeEnum getRoleDataScope(List<Long> roleIds) {

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.in(SysRole::getRoleId, roleIds);
        sysRoleLambdaQueryWrapper.select(SysRole::getDataScopeType);

        // 按数据范围降序排列，50-全部数据最大
        sysRoleLambdaQueryWrapper.orderByDesc(SysRole::getDataScopeType);

        List<SysRole> sysRoleList = this.list(sysRoleLambdaQueryWrapper);
        if (ObjectUtil.isNotEmpty(sysRoleList)) {
            Integer dataScopeType = sysRoleList.get(0).getDataScopeType();
            if (dataScopeType != null) {
                return DataScopeTypeEnum.codeToEnum(dataScopeType);
            }
        }

        // 如果是查询不到，则直接返回仅本人数据
        return DataScopeTypeEnum.SELF;
    }

    @Override
    public List<SysRole> userAssignRoleList(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = this.createWrapper(sysRoleRequest);

        // 超级管理员，直接返回所有系统角色，非超级管理员看不到角色信息绑定
        boolean superAdminFlag = LoginContext.me().getSuperAdminFlag();
        if (superAdminFlag) {
            wrapper.eq(SysRole::getRoleType, RoleTypeEnum.SYSTEM_ROLE.getCode());
        } else {
            return new ArrayList<>();
        }

        // 只查询id和名称
        wrapper.select(SysRole::getRoleId, SysRole::getRoleName, SysRole::getRoleType);

        return this.list(wrapper);
    }

    @Override
    public void refreshRoleAuth(Set<Long> roleIdList, Set<Long> menuIdList, Set<Long> menuOptionIdList) {

        // 获取菜单的appId和menuId
        List<SysMenu> totalMenus = sysMenuService.getTotalMenus(menuIdList);

        // 获取菜单功能的填充信息
        List<SysMenuOptions> totalMenuOptionsList = sysMenuOptionsService.getTotalMenuOptionsList(menuOptionIdList);

        for (Long roleId : roleIdList) {
            // 绑定角色的菜单
            sysRoleMenuService.bindRoleMenus(roleId, totalMenus);

            // 绑定角色菜单功能
            sysRoleMenuOptionsService.bindRoleMenuOptions(roleId, totalMenuOptionsList);

            // 更新角色的权限范围
            this.sysRoleLimitService.updateRoleLimit(roleId, menuIdList, menuOptionIdList);
        }
    }

    @Override
    public List<SysRole> getRoleDataScopeType(List<Long> roleIdList) {
        if (ObjectUtil.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.in(SysRole::getRoleId, roleIdList);
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleId, SysRole::getDataScopeType);
        return this.list(sysRoleLambdaQueryWrapper);
    }

    @Override
    public List<SysRole> permissionGetRoleList(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = this.createWrapper(sysRoleRequest);

        // 只查询id和名称
        wrapper.select(SysRole::getRoleId, SysRole::getRoleName);

        // 过滤角色的权限信息
        this.filterRolePermission(wrapper, sysRoleRequest);

        return this.list(wrapper);
    }

    @Override
    public Long getDefaultRoleId() {

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleCode, SysConstants.DEFAULT_ROLE_CODE);
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleId);
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);

        if (sysRole != null) {
            return sysRole.getRoleId();
        }

        return null;
    }

    @Override
    public String getRoleNameByRoleId(Long roleId) {

        if (ObjectUtil.isEmpty(roleId)) {
            return "";
        }

        // 先从缓存中获取
        String roleName = this.roleNameCache.get(roleId.toString());
        if (roleName != null) {
            return roleName;
        }

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleId, roleId);
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleName);
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);
        if (sysRole != null) {
            this.roleNameCache.put(roleId.toString(), sysRole.getRoleName(), SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            return sysRole.getRoleName();
        }

        return "";
    }

    @Override
    public List<String> getRoleMenuOptionsByRoleId(String roleCode) {

        if (ObjectUtil.isEmpty(roleCode)) {
            return new ArrayList<>();
        }

        // 获取角色编码对应的角色id
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleCode, roleCode);
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleId);
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);
        if (sysRole == null) {
            return new ArrayList<>();
        }
        Long roleId = sysRole.getRoleId();

        // 获取角色的角色功能id集合
        List<Long> roleBindMenuOptionsIdList = sysRoleMenuOptionsService.getRoleBindMenuOptionsIdList(ListUtil.list(false, roleId));
        if (ObjectUtil.isEmpty(roleBindMenuOptionsIdList)) {
            return new ArrayList<>();
        }

        // 获取角色功能id的集合对应的功能编码集合
        return sysMenuOptionsService.getOptionsCodeList(roleBindMenuOptionsIdList);
    }

    @Override
    public List<SysRoleDTO> getRolesByIds(List<Long> roleIds) {

        if (ObjectUtil.isEmpty(roleIds)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.in(SysRole::getRoleId, roleIds);
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleName, SysRole::getRoleId, SysRole::getRoleCode, SysRole::getRoleType, SysRole::getRoleCompanyId);
        List<SysRole> sysRoleList = this.list(sysRoleLambdaQueryWrapper);

        if (ObjectUtil.isEmpty(sysRoleList)) {
            return new ArrayList<>();
        }

        return BeanUtil.copyToList(sysRoleList, SysRoleDTO.class, CopyOptions.create().ignoreError());
    }

    @Override
    public List<SysRoleDTO> getBusinessRoleAndCompanyRole(List<Long> companyIdList) {
        if (ObjectUtil.isEmpty(companyIdList)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleType, RoleTypeEnum.BUSINESS_ROLE.getCode());
        sysRoleLambdaQueryWrapper.or().nested(i -> i.eq(SysRole::getRoleType, RoleTypeEnum.COMPANY_ROLE.getCode()).and(j -> j.in(SysRole::getRoleCompanyId, companyIdList)));
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleId, SysRole::getRoleName, SysRole::getRoleType, SysRole::getRoleCompanyId);
        sysRoleLambdaQueryWrapper.orderByAsc(SysRole::getRoleType);
        List<SysRole> sysRoleList = this.list(sysRoleLambdaQueryWrapper);
        if (ObjectUtil.isEmpty(sysRoleList)) {
            return new ArrayList<>();
        } else {
            return BeanUtil.copyToList(sysRoleList, SysRoleDTO.class, CopyOptions.create().ignoreError());
        }
    }

    @Override
    public List<SysRoleTreeDTO> roleCategoryAndRoleTreeList(Integer roleType, Long companyId) {

        // 根据业务类型或者公司类型，获取所有的角色分类列表
        LambdaQueryWrapper<RoleCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleCategory::getCategoryType, roleType);
        if (ObjectUtil.isNotEmpty(companyId)) {
            queryWrapper.eq(RoleCategory::getCompanyId, companyId);
        }
        List<RoleCategory> roleCategoryList = this.roleCategoryService.list(queryWrapper);

        // 根据业务类型，获取所有的角色列表
        LambdaQueryWrapper<SysRole> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.eq(SysRole::getRoleType, roleType);
        if (ObjectUtil.isNotEmpty(companyId)) {
            roleQueryWrapper.eq(SysRole::getRoleCompanyId, companyId);
        }
        List<SysRole> sysRoleList = this.list(roleQueryWrapper);

        // 将角色类型和角色树组成一颗完整的树
        return RoleTreeFactory.createRoleTree(roleCategoryList, sysRoleList);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRole querySysRole(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.getById(sysRoleRequest.getRoleId());
        if (ObjectUtil.isEmpty(sysRole)) {
            throw new ServiceException(SysRoleExceptionEnum.SYS_ROLE_NOT_EXISTED);
        }
        return sysRole;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRole> createWrapper(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

        // 根据名称模糊搜索
        String searchText = sysRoleRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.nested(i -> i.like(SysRole::getRoleName, searchText).or().like(SysRole::getRoleCode, searchText));
        }

        // 根据角色分类id查询角色
        if (ObjectUtil.isNotEmpty(sysRoleRequest.getRoleCategoryId())) {
            queryWrapper.eq(SysRole::getRoleCategoryId, sysRoleRequest.getRoleCategoryId());
        }

        // 根据公司id查询角色
        if (ObjectUtil.isNotEmpty(sysRoleRequest.getRoleCompanyId())) {
            queryWrapper.eq(SysRole::getRoleCompanyId, sysRoleRequest.getRoleCompanyId());
        }

        // 排序字段
        queryWrapper.orderByAsc(SysRole::getRoleType);
        queryWrapper.orderByAsc(SysRole::getRoleSort);

        return queryWrapper;
    }

    /**
     * 删除角色的基础业务操作
     *
     * @author fengshuonan
     * @since 2023/6/12 21:14
     */
    private void baseDelete(Set<Long> roleIdList) {
        // 执行角色相关的校验
        Map<String, RemoveRoleCallbackApi> callbackApiMap = SpringUtil.getBeansOfType(RemoveRoleCallbackApi.class);
        for (RemoveRoleCallbackApi removeRoleCallbackApi : callbackApiMap.values()) {
            removeRoleCallbackApi.validateHaveRoleBind(roleIdList);
        }

        // 执行角色相关关联业务的删除操作
        for (RemoveRoleCallbackApi removeRoleCallbackApi : callbackApiMap.values()) {
            removeRoleCallbackApi.removeRoleAction(roleIdList);
        }

        // 删除角色
        this.removeBatchByIds(roleIdList);
    }

    /**
     * 过滤角色的权限展示
     * <p>
     * 非管理员只能看到自己的角色和自己创建的角色
     * <p>
     * 用在权限界面，获取左侧角色列表
     *
     * @author fengshuonan
     * @since 2023/10/9 10:44
     */
    private void filterRolePermission(LambdaQueryWrapper<SysRole> wrapper, SysRoleRequest sysRoleRequest) {

        // 超级管理员，直接略过
        boolean superAdminFlag = LoginContext.me().getSuperAdminFlag();
        if (superAdminFlag) {
            // 根据角色类型填充参数
            if (ObjectUtil.isNotEmpty(sysRoleRequest.getRoleType())) {
                wrapper.eq(SysRole::getRoleType, sysRoleRequest.getRoleType());
            }

            // 根据角色的所属公司id填充参数
            if (ObjectUtil.isNotEmpty(sysRoleRequest.getRoleCompanyId())) {
                wrapper.eq(SysRole::getRoleCompanyId, sysRoleRequest.getRoleCompanyId());
            }
            return;
        }

        // 非超级管理员，直接拼好，角色类型和角色的公司id，只能查本公司的
        wrapper.eq(SysRole::getRoleType, RoleTypeEnum.COMPANY_ROLE.getCode());
        wrapper.eq(SysRole::getRoleCompanyId, LoginContext.me().getCurrentUserCompanyId());
    }

    /**
     * 角色的类型校验，非系统管理员，只能添加公司级别的角色，并且只能添加当前登录本公司的角色
     *
     * @author fengshuonan
     * @since 2024-01-16 17:19
     */
    private void rolePermissionValidate(SysRoleRequest sysRoleRequest) {

        boolean superAdminFlag = LoginContext.me().getSuperAdminFlag();
        if (superAdminFlag) {
            return;
        }

        // 非管理员，只能添加公司级别的角色
        if (!RoleTypeEnum.COMPANY_ROLE.getCode().equals(sysRoleRequest.getRoleType())) {
            throw new ServiceException(SysRoleExceptionEnum.ROLE_TYPE_ERROR);
        }

        // 非管理员，只能添加本公司的角色
        if (!LoginContext.me().getCurrentUserCompanyId().equals(sysRoleRequest.getRoleCompanyId())) {
            throw new ServiceException(SysRoleExceptionEnum.ROLE_COMPANY_ERROR);
        }

    }

}