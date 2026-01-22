package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.enums.role.RoleTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleTreeDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.UserRoleDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.factory.RoleAssignV2Factory;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysRoleAssignV2Service;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新的用户授权界面业务
 *
 * @author fengshuonan
 * @since 2024/1/17 23:08
 */
@Service
public class SysRoleAssignV2ServiceImpl implements SysRoleAssignV2Service {

    @Resource
    private SysRoleServiceApi sysRoleServiceApi;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private OrganizationServiceApi organizationServiceApi;

    @Override
    public List<SysRoleTreeDTO> getCompanyBusinessRoleTree(Long userId, Long orgId) {

        // 1. 获取系统中所有业务角色的树（业务角色分类 + 业务角色）
        List<SysRoleTreeDTO> totalBusinessRoleTree = sysRoleServiceApi.roleCategoryAndRoleTreeList(RoleTypeEnum.BUSINESS_ROLE.getCode(), null);

        // 2. 获取用户绑定的角色，并挂载到树中
        return getUserBindRoleAndCombine(totalBusinessRoleTree, userId, orgId);
    }

    @Override
    public List<SysRoleTreeDTO> getCompanyRoleTree(Long userId, Long orgId) {

        // 1. 获取机构的对应的公司
        CompanyDeptDTO orgCompanyInfo = organizationServiceApi.getOrgCompanyInfo(orgId);
        if (orgCompanyInfo == null) {
            return new ArrayList<>();
        }

        // 2. 获取系统中所有业务角色的树（业务角色分类 + 业务角色）
        List<SysRoleTreeDTO> totalCompanyRoleTree = sysRoleServiceApi.roleCategoryAndRoleTreeList(RoleTypeEnum.COMPANY_ROLE.getCode(), orgCompanyInfo.getCompanyId());

        // 3. 获取用户绑定的角色，并挂载到树中
        return getUserBindRoleAndCombine(totalCompanyRoleTree, userId, orgId);
    }

    /**
     * 获取用户绑定的角色，并挂载到树中
     *
     * @author fengshuonan
     * @since 2025/1/24 15:51
     */
    private List<SysRoleTreeDTO> getUserBindRoleAndCombine(List<SysRoleTreeDTO> totalRoleTree, Long userId, Long orgId) {

        // 1. 获取用户，指定的机构，已经绑定的了哪些业务角色
        List<UserRoleDTO> userLinkedOrgRoleList = sysUserRoleService.getUserLinkedOrgRoleList(userId);
        if (ObjectUtil.isEmpty(userLinkedOrgRoleList)) {
            return totalRoleTree;
        }
        userLinkedOrgRoleList = userLinkedOrgRoleList.stream().filter(item -> orgId.equals(item.getRoleOrgId())).collect(Collectors.toList());

        // 2. 将用户绑定的角色，挂载到角色树中，赋值checked选项
        RoleAssignV2Factory.mountBusinessRole(totalRoleTree, userLinkedOrgRoleList);

        return totalRoleTree;
    }

}