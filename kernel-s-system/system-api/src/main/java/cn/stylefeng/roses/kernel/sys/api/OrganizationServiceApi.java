package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.enums.org.DetectModeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.HrOrganizationDTO;

import java.util.Collection;
import java.util.List;

/**
 * 组织机构信息的api
 *
 * @author fengshuonan
 * @since 2023/7/14 16:47
 */
public interface OrganizationServiceApi {

    /**
     * 获取组织机构的名称，通过组织机构id
     *
     * @author fengshuonan
     * @since 2023/7/14 16:47
     */
    String getOrgNameById(Long orgId);

    /**
     * 获取指定组织机构的上级组织机构是什么
     * <p>
     * 自下而上：逐级向上获取直到获取到最高级
     * 自上而下：逐级向下获取，直到获取到本层机构
     *
     * @param orgId          指定机构id
     * @param parentLevelNum 上级机构的层级数，从0开始，0代表直接返回本部门
     * @param detectModeEnum 自上而下还是自下而上
     * @return 上级机构的id
     * @author fengshuonan
     * @since 2022/9/18 15:02
     */
    Long getParentLevelOrgId(Long orgId, Integer parentLevelNum, DetectModeEnum detectModeEnum);

    /**
     * 根据组织机构id，获取对应的具体的公司和部门信息
     *
     * @param orgId 组织机构id
     * @return 公司和部门信息
     * @author fengshuonan
     * @since 2023/6/12 15:42
     */
    CompanyDeptDTO getCompanyDeptInfo(Long orgId);

    /**
     * 根据组织机构id，获取这个组织机构id对应的公司部门信息
     *
     * @param orgId 组织机构id
     * @return 单独返回公司信息
     * @author fengshuonan
     * @since 2023/7/2 8:38
     */
    CompanyDeptDTO getOrgCompanyInfo(Long orgId);

    /**
     * 通过组织机构id获取组织机构信息
     *
     * @author fengshuonan
     * @since 2024/1/6 11:21
     */
    HrOrganizationDTO getOrgInfo(Long orgId);

    /**
     * 获取所有的组织机构名称
     *
     * @author fengshuonan
     * @since 2024-01-09 18:26
     */
    List<HrOrganizationDTO> getOrgNameList(Collection<Long> orgIdList);

    /**
     * 获取组织机构的全路径名称，例如：北京公司/信息部门
     *
     * @author fengshuonan
     * @since 2025/1/10 23:55
     */
    String getOrgTotalPathName(Long orgId);

    /**
     * 获取指定机构的指定层级的机构id，从下往上直到找到指定的机构层级
     * <p>
     * 如果找不到指定层级，返回为空
     *
     * @param orgId        指定机构id
     * @param orgLevelCode 机构层级编码
     * @return 指定层级的机构的id
     * @author fengshuonan
     * @since 2025/1/26 14:45
     */
    Long getParentOrgLevel(Long orgId, String orgLevelCode);

    /**
     * 获取指定机构的，指定层级编码的机构信息
     * <p>
     * 例如：获取北京公司/信息中心/技术部，获取技术部的一级机构信息（北京公司），二级机构信息（信息中心）
     * <p>
     *
     * @param hrOrganizationDTO 指定的机构信息
     * @param levelCode         指定的层级编码，对应的sys_hr_organization_level表的levelCode
     * @author fengshuonan
     * @since 2025/2/6 16:39
     */
    HrOrganizationDTO getParentOrgByDataScopeType(HrOrganizationDTO hrOrganizationDTO, String levelCode);

}
