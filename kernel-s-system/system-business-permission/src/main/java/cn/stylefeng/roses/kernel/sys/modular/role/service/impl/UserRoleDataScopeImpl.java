package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.db.mp.datascope.ProjectDataScopeHandler;
import cn.stylefeng.roses.kernel.db.mp.datascope.UserRoleDataScopeApi;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleDataScope;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleDataScopeService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户的数据范围的获取实现
 *
 * @author fengshuonan
 * @since 2024-03-01 14:06
 */
@Service
public class UserRoleDataScopeImpl implements UserRoleDataScopeApi {

    @Resource
    private SysUserRoleServiceApi sysUserRoleServiceApi;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleDataScopeService sysRoleDataScopeService;

    @Resource
    private DbOperatorApi dbOperatorApi;

    @Resource
    private OrganizationServiceApi organizationServiceApi;

    @Override
    public DataScopeConfig getUserRoleDataScopeConfig() {

        // 获取当前登录用户id
        LoginUser loginUser = LoginContext.me().getLoginUserNullable();
        if (loginUser == null) {
            return nullDataScopeConfig();
        }

        // 获取用户的角色id集合
        List<Long> userRoleIdList = sysUserRoleServiceApi.getUserRoleIdListCurrentCompany(loginUser.getUserId(), loginUser.getCurrentOrgId());

        // 如果这个角色列表大于1，也就是不单纯是普通角色，则将普通角色去掉，以配置的角色为主
        Long defaultRoleId = sysRoleService.getDefaultRoleId();
        if (userRoleIdList.size() > 1 && CollectionUtil.contains(userRoleIdList, defaultRoleId)) {
            userRoleIdList.remove(defaultRoleId);
        }

        // 获取这些角色的所有的数据范围信息的汇总
        List<SysRoleDataScope> roleDataScopeList = sysRoleDataScopeService.getRoleDataScopeList(userRoleIdList);
        if (ObjectUtil.isEmpty(roleDataScopeList)) {
            return nullDataScopeConfig();
        }

        // 通过这些角色的数据范围信息，构建出数据范围配置
        return parseSysRoleDataScope(roleDataScopeList, loginUser);
    }

    @Override
    public DataScopeConfig getUserPointDataScopeConfig(DataScopeTypeEnum dataScopeTypeEnum) {

        // 获取当前登录用户id
        LoginUser loginUser = LoginContext.me().getLoginUserNullable();
        if (loginUser == null) {
            return nullDataScopeConfig();
        }

        // 只能进行非指定类型的数据范围的生成
        if (dataScopeTypeEnum == null || dataScopeTypeEnum.equals(DataScopeTypeEnum.DEFINE_ORG_LEVEL_WITH_CHILD)
                || dataScopeTypeEnum.equals(DataScopeTypeEnum.DEFINE)
                || dataScopeTypeEnum.equals(DataScopeTypeEnum.DEFINE_ORG_WITH_CHILD)) {
            return nullDataScopeConfig();
        }

        SysRoleDataScope sysRoleDataScope = new SysRoleDataScope();
        sysRoleDataScope.setDataScopeType(dataScopeTypeEnum.getCode());
        return parseSysRoleDataScope(ListUtil.of(sysRoleDataScope), loginUser);
    }

    /**
     * 空的数据范围的配置
     *
     * @author fengshuonan
     * @since 2025/1/25 21:59
     */
    private DataScopeConfig nullDataScopeConfig() {
        DataScopeConfig dataScopeConfig = new DataScopeConfig();

        dataScopeConfig.setDoCreateUserValidate(true);
        dataScopeConfig.setUserId(ProjectDataScopeHandler.NONE_ID_VALUE);

        dataScopeConfig.setDoOrgScopeValidate(true);
        dataScopeConfig.setUserOrgIdList(CollectionUtil.set(false, ProjectDataScopeHandler.NONE_ID_VALUE));

        return dataScopeConfig;
    }

    /**
     * 将角色的数据范围配置，转化为最终的数据权限的结果
     *
     * @author fengshuonan
     * @since 2025/1/25 22:01
     */
    private DataScopeConfig parseSysRoleDataScope(List<SysRoleDataScope> sysRoleDataScopeList, LoginUser loginUser) {

        DataScopeConfig dataScopeConfig = new DataScopeConfig();

        // 如果包含了全部数据，则将结果设置为全部数据，并直接返回结果
        for (SysRoleDataScope sysRoleDataScope : sysRoleDataScopeList) {
            if (DataScopeTypeEnum.ALL.getCode().equals(sysRoleDataScope.getDataScopeType())) {
                dataScopeConfig.setTotalDataScope(true);
                return dataScopeConfig;
            }
        }

        // 如果有仅包含自己的数据，则将结果设置为仅包含自己的数据
        for (SysRoleDataScope sysRoleDataScope : sysRoleDataScopeList) {
            if (DataScopeTypeEnum.SELF.getCode().equals(sysRoleDataScope.getDataScopeType())) {
                dataScopeConfig.setDoCreateUserValidate(true);
                dataScopeConfig.setUserId(loginUser.getUserId());
            }
        }

        // 剔除掉本人和全部的类型，看是否还有校验部门的权限范围的，如果有则找到部门的权限范围列表
        List<SysRoleDataScope> otherRoleDataScopeList = sysRoleDataScopeList.stream()
                .filter(sysRoleDataScope -> !DataScopeTypeEnum.SELF.getCode().equals(sysRoleDataScope.getDataScopeType()) && !DataScopeTypeEnum.ALL.getCode()
                        .equals(sysRoleDataScope.getDataScopeType())).collect(Collectors.toList());
        if (ObjectUtil.isEmpty(otherRoleDataScopeList)) {
            return dataScopeConfig;
        } else {
            dataScopeConfig.setDoOrgScopeValidate(true);
        }

        // 开始处理除了仅本人、全部数据外的数据范围
        Set<Long> userOrgIdList = new HashSet<>();
        for (SysRoleDataScope sysRoleDataScope : otherRoleDataScopeList) {

            // 如果是20-本部门数据
            if (DataScopeTypeEnum.DEPT.getCode().equals(sysRoleDataScope.getDataScopeType())) {
                userOrgIdList.add(loginUser.getCurrentOrgId());
            }

            // 如果是30-本部门及以下数据
            else if (DataScopeTypeEnum.DEPT_WITH_CHILD.getCode().equals(sysRoleDataScope.getDataScopeType())) {
                Set<Long> pointOrgAndSub = this.getPointOrgAndSub(loginUser.getCurrentOrgId());
                userOrgIdList.addAll(pointOrgAndSub);
            }

            // 如果是31-本公司及以下数据
            else if (DataScopeTypeEnum.COMPANY_WITH_CHILD.getCode().equals(sysRoleDataScope.getDataScopeType())) {
                // 获取当前部门的公司id
                CompanyDeptDTO companyDeptInfo = organizationServiceApi.getCompanyDeptInfo(loginUser.getCurrentOrgId());
                if (companyDeptInfo == null) {
                    continue;
                }
                Long companyId = companyDeptInfo.getCompanyId();
                Set<Long> pointOrgAndSub = this.getPointOrgAndSub(companyId);
                userOrgIdList.addAll(pointOrgAndSub);
            }

            // 如果是32-指定机构层级及以下
            else if (DataScopeTypeEnum.DEFINE_ORG_LEVEL_WITH_CHILD.getCode().equals(sysRoleDataScope.getDataScopeType())) {
                Long parentOrgLevelOrgId = organizationServiceApi.getParentOrgLevel(loginUser.getCurrentOrgId(), sysRoleDataScope.getOrgLevelCode());
                if (parentOrgLevelOrgId == null) {
                    continue;
                }
                Set<Long> subOrgIdList = this.getPointOrgAndSub(parentOrgLevelOrgId);
                userOrgIdList.addAll(subOrgIdList);
            }

            // 如果是40-指定机构集合数据
            else if (DataScopeTypeEnum.DEFINE.getCode().equals(sysRoleDataScope.getDataScopeType())) {
                userOrgIdList.addAll(sysRoleDataScope.getDefineOrgList());
            }

            // 如果是41-指定机构及以下
            else if (DataScopeTypeEnum.DEFINE_ORG_WITH_CHILD.getCode().equals(sysRoleDataScope.getDataScopeType())) {
                Long defineOrgId = sysRoleDataScope.getDefineOrgId();
                Set<Long> pointOrgAndSub = this.getPointOrgAndSub(defineOrgId);
                userOrgIdList.addAll(pointOrgAndSub);
            }
        }

        // 如果部门的数据范围最终没获取到，则设定为空数据范围
        if (ObjectUtil.isEmpty(userOrgIdList)) {
            userOrgIdList.add(ProjectDataScopeHandler.NONE_ID_VALUE);
        }

        dataScopeConfig.setUserOrgIdList(userOrgIdList);
        return dataScopeConfig;
    }

    /**
     * 获取指定机构及以下的所有机构的id集合
     *
     * @author fengshuonan
     * @since 2025/1/25 22:24
     */
    public Set<Long> getPointOrgAndSub(Long orgId) {
        if (orgId == null) {
            return CollectionUtil.set(false);
        }
        Set<Long> subOrgIdList = this.dbOperatorApi.findSubListByParentId("sys_hr_organization", "org_pids", "org_id", orgId);
        subOrgIdList.add(orgId);
        return subOrgIdList;
    }

}
