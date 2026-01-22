package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.OrgLevelServiceApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import cn.stylefeng.roses.kernel.sys.api.exception.SysException;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.OrganizationLevelRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.response.RoleBindDataScopeResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleDataScope;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.exception.SysRoleDataScopeExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleDataScopeMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleDataScopeService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色数据范围业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleDataScopeServiceImpl extends ServiceImpl<SysRoleDataScopeMapper, SysRoleDataScope> implements SysRoleDataScopeService, RemoveRoleCallbackApi {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private OrgLevelServiceApi orgLevelServiceApi;

    @Override
    public void add(SysRoleDataScopeRequest sysRoleDataScopeRequest) {

        // 先校验参数
        this.validateRoleDataScopeRequest(sysRoleDataScopeRequest);

        SysRoleDataScope sysRoleDataScope = new SysRoleDataScope();
        BeanUtil.copyProperties(sysRoleDataScopeRequest, sysRoleDataScope);
        this.save(sysRoleDataScope);
    }

    @Override
    public void del(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        SysRoleDataScope sysRoleDataScope = this.querySysRoleDataScope(sysRoleDataScopeRequest);
        this.removeById(sysRoleDataScope.getRoleDataScopeId());
    }

    @Override
    public void edit(SysRoleDataScopeRequest sysRoleDataScopeRequest) {

        // 先校验参数
        this.validateRoleDataScopeRequest(sysRoleDataScopeRequest);

        // 进行赋值
        SysRoleDataScope sysRoleDataScope = this.querySysRoleDataScope(sysRoleDataScopeRequest);
        sysRoleDataScope.setDataScopeType(sysRoleDataScopeRequest.getDataScopeType());
        sysRoleDataScope.setOrgLevelCode(sysRoleDataScopeRequest.getOrgLevelCode());
        sysRoleDataScope.setDefineOrgList(sysRoleDataScopeRequest.getDefineOrgList());
        sysRoleDataScope.setDefineOrgId(sysRoleDataScopeRequest.getDefineOrgId());

        this.updateById(sysRoleDataScope);
    }

    @Override
    public SysRoleDataScope detail(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        return this.querySysRoleDataScope(sysRoleDataScopeRequest);
    }

    @Override
    public PageResult<SysRoleDataScope> findPage(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        LambdaQueryWrapper<SysRoleDataScope> wrapper = createWrapper(sysRoleDataScopeRequest);
        Page<SysRoleDataScope> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);

        List<SysRoleDataScope> records = sysRolePage.getRecords();
        if (ObjectUtil.isEmpty(records)) {
            return PageResultFactory.createPageResult(sysRolePage);
        }

        // 组织机构层级编码，转化为层级的名称
        boolean haveOrgLevelType = false;
        for (SysRoleDataScope record : records) {
            if (DataScopeTypeEnum.DEFINE_ORG_LEVEL_WITH_CHILD.getCode().equals(record.getDataScopeType())) {
                haveOrgLevelType = true;
                break;
            }
        }
        if (haveOrgLevelType) {
            List<OrganizationLevel> list = orgLevelServiceApi.findList(new OrganizationLevelRequest());
            for (SysRoleDataScope record : records) {
                record.setOrganizationLevel(list.stream().filter(item -> item.getLevelCode().equals(record.getOrgLevelCode())).findFirst().orElse(null));
            }
        }

        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public RoleBindDataScopeResponse getRoleBindDataScope(RoleBindDataScopeRequest roleBindDataScopeRequest) {

        RoleBindDataScopeResponse roleBindDataScopeResponse = new RoleBindDataScopeResponse();
        roleBindDataScopeResponse.setOrgIdList(new ArrayList<>());

        // 获取角色的数据范围类型
        Integer dataScopeType = sysRoleService.getRoleDataScopeType(roleBindDataScopeRequest.getRoleId());
        roleBindDataScopeResponse.setDataScopeType(dataScopeType);

        if (!DataScopeTypeEnum.DEFINE.getCode().equals(dataScopeType)) {
            return roleBindDataScopeResponse;
        }

        // 如果是指定部门，则获取指定部门的orgId集合
        Set<Long> roleBindOrgIdList = this.getRoleBindOrgIdList(ListUtil.list(false, roleBindDataScopeRequest.getRoleId()));
        roleBindDataScopeResponse.setOrgIdList(CollectionUtil.list(false, roleBindOrgIdList));

        return roleBindDataScopeResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleBindDataScope(RoleBindDataScopeRequest roleBindDataScopeRequest) {

        BusinessLogUtil.setLogTitle("修改角色数据范围");
        BusinessLogUtil.addContent("角色绑定数据范围的参数如下：\n", roleBindDataScopeRequest);

        // 无论角色是否绑定的是指定部门，清空掉角色绑定的数据范围
        LambdaQueryWrapper<SysRoleDataScope> removeWrapper = new LambdaQueryWrapper<>();
        removeWrapper.eq(SysRoleDataScope::getRoleId, roleBindDataScopeRequest.getRoleId());
        this.remove(removeWrapper);

        // 更新角色的数据权限类型
        Integer dataScopeType = roleBindDataScopeRequest.getDataScopeType();
        this.sysRoleService.updateRoleDataScopeType(roleBindDataScopeRequest.getRoleId(), dataScopeType);

        // 非指定部门，直接返回
        if (!DataScopeTypeEnum.DEFINE.getCode().equals(dataScopeType)) {
            return;
        }

        // 如果是指定部门的话，则更新角色关联的指定部门的信息
        List<Long> orgIdList = roleBindDataScopeRequest.getOrgIdList();
        if (ObjectUtil.isEmpty(orgIdList)) {
            return;
        }

        ArrayList<SysRoleDataScope> bindRoleDataScopeList = new ArrayList<>();
        for (Long orgId : orgIdList) {
            SysRoleDataScope sysRoleDataScope = new SysRoleDataScope();
            sysRoleDataScope.setRoleId(roleBindDataScopeRequest.getRoleId());
            sysRoleDataScope.setOrganizationId(orgId);
            bindRoleDataScopeList.add(sysRoleDataScope);
        }
        this.saveBatch(bindRoleDataScopeList);
    }

    @Override
    public Set<Long> getRoleBindOrgIdList(List<Long> roleIdList) {

        if (ObjectUtil.isEmpty(roleIdList)) {
            return new HashSet<>();
        }

        LambdaQueryWrapper<SysRoleDataScope> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleDataScope::getRoleId, roleIdList);
        wrapper.select(SysRoleDataScope::getOrganizationId);
        List<SysRoleDataScope> sysRoleDataScopes = this.list(wrapper);

        if (ObjectUtil.isNotEmpty(sysRoleDataScopes)) {
            return sysRoleDataScopes.stream().map(SysRoleDataScope::getOrganizationId).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    @Override
    public List<SysRoleDataScope> getRoleDataScopeList(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleDataScope> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleDataScope::getRoleId, roleIdList);
        return this.list(wrapper);
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysRoleDataScope> sysRoleDataScopeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleDataScopeLambdaQueryWrapper.in(SysRoleDataScope::getRoleId, beRemovedRoleIdList);
        this.remove(sysRoleDataScopeLambdaQueryWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRoleDataScope querySysRoleDataScope(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        SysRoleDataScope sysRoleDataScope = this.getById(sysRoleDataScopeRequest.getRoleDataScopeId());
        if (ObjectUtil.isEmpty(sysRoleDataScope)) {
            throw new ServiceException(SysRoleDataScopeExceptionEnum.SYS_ROLE_DATA_SCOPE_NOT_EXISTED);
        }
        return sysRoleDataScope;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRoleDataScope> createWrapper(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        LambdaQueryWrapper<SysRoleDataScope> queryWrapper = new LambdaQueryWrapper<>();

        // 根据角色id进行查询
        queryWrapper.eq(SysRoleDataScope::getRoleId, sysRoleDataScopeRequest.getRoleId());

        // 按类型排序
        queryWrapper.orderByAsc(SysRoleDataScope::getDataScopeType);

        return queryWrapper;
    }

    /**
     * 校验数据范围请求的参数，一般用在新增和修改的时候
     *
     * @author fengshuonan
     * @since 2025/1/24 22:42
     */
    private void validateRoleDataScopeRequest(SysRoleDataScopeRequest sysRoleDataScopeRequest) {

        // 如果是新增的是指定层级，则判断层级参数
        if (DataScopeTypeEnum.DEFINE_ORG_LEVEL_WITH_CHILD.getCode().equals(sysRoleDataScopeRequest.getDataScopeType())) {
            if (ObjectUtil.isEmpty(sysRoleDataScopeRequest.getOrgLevelCode())) {
                throw new SysException(SysRoleDataScopeExceptionEnum.PARAM_IS_EMPTY, "组织机构层级编码orgLevelCode");
            }

            // 校验机构编码是否真实存在
            boolean levelCodeOk = false;
            List<OrganizationLevel> list = orgLevelServiceApi.findList(new OrganizationLevelRequest());
            for (OrganizationLevel organizationLevel : list) {
                if (organizationLevel.getLevelCode().equals(sysRoleDataScopeRequest.getOrgLevelCode())) {
                    levelCodeOk = true;
                    break;
                }
            }
            if (!levelCodeOk) {
                throw new SysException(SysRoleDataScopeExceptionEnum.LEVEL_CODE_IS_ERROR);
            }
        }

        // 如果是新增的是指定机构，则判断机构参数
        else if (DataScopeTypeEnum.DEFINE.getCode().equals(sysRoleDataScopeRequest.getDataScopeType())) {
            if (ObjectUtil.isEmpty(sysRoleDataScopeRequest.getDefineOrgList())) {
                throw new SysException(SysRoleDataScopeExceptionEnum.PARAM_IS_EMPTY, "机构集合列表defineOrgList");
            }
        }

        // 如果是新增的是指定机构及以下
        else if (DataScopeTypeEnum.DEFINE_ORG_WITH_CHILD.getCode().equals(sysRoleDataScopeRequest.getDataScopeType())) {
            if (ObjectUtil.isEmpty(sysRoleDataScopeRequest.getDefineOrgId())) {
                throw new SysException(SysRoleDataScopeExceptionEnum.PARAM_IS_EMPTY, "指定机构defineOrgId");
            }
        }
    }

}