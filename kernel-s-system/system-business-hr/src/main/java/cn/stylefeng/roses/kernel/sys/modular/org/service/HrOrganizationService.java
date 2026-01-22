package cn.stylefeng.roses.kernel.sys.modular.org.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.api.remote.OrgInfoRemoteApi;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.CommonOrgTreeRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.CommonOrgTreeResponse;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.HomeCompanyInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 组织机构信息 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
public interface HrOrganizationService extends IService<HrOrganization>, OrganizationServiceApi, OrgInfoRemoteApi {

    /**
     * 新增
     *
     * @param hrOrganizationRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void add(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 删除
     *
     * @param hrOrganizationRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void del(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 批量删除组织机构
     *
     * @author fengshuonan
     * @since 2023/6/11 16:59
     */
    void batchDelete(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 编辑
     *
     * @param hrOrganizationRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void edit(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 查询详情
     *
     * @param hrOrganizationRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    HrOrganization detail(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 获取列表
     *
     * @param hrOrganizationRequest 请求参数
     * @return List<HrOrganization>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    List<HrOrganization> findList(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 获取列表（带分页）
     *
     * @param hrOrganizationRequest 请求参数
     * @return PageResult<HrOrganization>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    PageResult<HrOrganization> findPage(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 分页获取组织机构信息（用在通用选择机构组件中）
     *
     * @param hrOrganizationRequest 请求参数
     * @return PageResult<HrOrganization>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    PageResult<HrOrganization> commonOrgPage(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 通用获取组织机构树
     * <p>
     * ps：用在获取用户管理和组织机构管理界面左侧树
     *
     * @author fengshuonan
     * @since 2023/6/11 10:40
     */
    CommonOrgTreeResponse commonOrgTree(CommonOrgTreeRequest commonOrgTreeRequest);

    /**
     * 获取组织机构对应的公司信息
     *
     * @param hrOrganization 被查询的组织机构
     * @return 单独返回公司信息
     * @author fengshuonan
     * @since 2023/6/12 16:09
     */
    CompanyDeptDTO getOrgCompanyInfo(HrOrganization hrOrganization);

    /**
     * 获取组织机构统计信息，包含系统的统计，包含当前用户公司的统计
     * <p>
     * 一般用在首页展示组织机构的统计信息界面
     *
     * @author fengshuonan
     * @since 2023/6/26 22:53
     */
    HomeCompanyInfo orgStatInfo();

    /**
     * 查询组织机构的所有上级，包括上级的上级组织机构
     *
     * @author fengshuonan
     * @since 2023/7/13 20:50
     */
    Set<Long> queryOrgIdParentIdList(Set<Long> orgIdList);

    /**
     * 获取组织机构是否包含下级的标识
     *
     * @param orgId 被查询的组织机构id
     * @return true-包含下级，false-不含下级
     * @author fengshuonan
     * @since 2023/7/14 1:09
     */
    Boolean getOrgHaveSubFlag(Long orgId);

    /**
     * 批量获取组织机构名称
     *
     * @author fengshuonan
     * @since 2023/11/2 10:19
     */
    List<SimpleDict> getOrgListName(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 批量保存组织机构
     *
     * @author fengshuonan
     * @since 2024-02-19 8:42
     */
    void quickBatchSaveOrg(List<HrOrganization> batchOrgList);

}