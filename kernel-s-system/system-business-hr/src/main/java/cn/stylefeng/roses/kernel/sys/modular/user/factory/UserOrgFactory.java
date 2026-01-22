package cn.stylefeng.roses.kernel.sys.modular.user.factory;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;
import cn.stylefeng.roses.kernel.sys.modular.position.service.HrPositionService;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;

/**
 * 用户组织机构详情
 *
 * @author fengshuonan
 * @since 2023/6/12 15:31
 */
public class UserOrgFactory {

    /**
     * 获取用户组织机构的详细信息
     *
     * @author fengshuonan
     * @since 2023/6/12 15:36
     */
    public static UserOrgDTO createUserOrgDetailInfo(SysUserOrg sysUserOrg) {

        UserOrgDTO result = new UserOrgDTO();

        result.setUserId(sysUserOrg.getUserId());
        result.setMainFlag(sysUserOrg.getMainFlag());

        // 获取用户的组织机构id
        Long orgId = sysUserOrg.getOrgId();
        result.setOrgId(orgId);

        // 获取用户的职位id
        Long positionId = sysUserOrg.getPositionId();

        // 先获取组织机构对应的公司和部门的详情信息
        HrOrganizationService hrOrganizationService = SpringUtil.getBean(HrOrganizationService.class);
        CompanyDeptDTO companyDeptInfo = hrOrganizationService.getCompanyDeptInfo(orgId);
        BeanUtil.copyProperties(companyDeptInfo, result);

        // 再获取职位对应的名称
        HrPositionService hrPositionService = SpringUtil.getBean(HrPositionService.class);
        String positionName = hrPositionService.getPositionName(positionId);
        result.setPositionId(positionId);
        result.setPositionName(positionName);

        // 设置用户关联部门的状态
        result.setStatusFlag(sysUserOrg.getStatusFlag());

        return result;
    }

}
