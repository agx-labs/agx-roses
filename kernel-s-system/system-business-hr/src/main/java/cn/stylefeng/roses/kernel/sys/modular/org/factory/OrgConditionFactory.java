package cn.stylefeng.roses.kernel.sys.modular.org.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.db.mp.datascope.holder.DataScopeHolder;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.sys.api.enums.org.OrgTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.CommonOrgTreeRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.Set;

/**
 * 组织机构查询相关的条件组装
 *
 * @author fengshuonan
 * @since 2025/1/26 20:00
 */
public class OrgConditionFactory {

    /**
     * 针对通用组织机构树查询，组装一些通用的查询条件
     *
     * @author fengshuonan
     * @since 2025/1/26 20:00
     */
    public static void prepareRequest(CommonOrgTreeRequest commonOrgTreeRequest) {

        // 获取数据范围的信息，如果数据范围是包含全部数据，则将parentId设置为-1
        DataScopeConfig dataScopeConfig = DataScopeHolder.get();
        if (dataScopeConfig == null || dataScopeConfig.isTotalDataScope()) {
            if (commonOrgTreeRequest.getOrgParentId() == null) {
                commonOrgTreeRequest.setOrgParentId(TreeConstants.DEFAULT_PARENT_ID);
            }
        }

        // 如果查询带组织机构名称的搜索，则清空其他条件
        if (ObjectUtil.isNotEmpty(commonOrgTreeRequest.getSearchText())) {
            commonOrgTreeRequest.setOrgParentId(null);
            commonOrgTreeRequest.setIndexOrgIdList(null);
        }

        // 如果查询待组织机构的状态信息，则清空parentId
        if (ObjectUtil.isNotEmpty(commonOrgTreeRequest.getIndexOrgIdList())) {
            commonOrgTreeRequest.setOrgParentId(null);
        }

    }

    /**
     * 创建查询组织机构树的Wrapper
     * <p>
     * 根据父级id、定位id、公司筛选标识等查询条件进行查询
     *
     * @author fengshuonan
     * @since 2025/1/26 20:13
     */
    public static LambdaQueryWrapper<HrOrganization> createCommonTreeWrapper(CommonOrgTreeRequest commonOrgTreeRequest, HrOrganizationService hrOrganizationService) {

        // 创建基本的wrapper
        HrOrganizationRequest hrOrganizationRequest = new HrOrganizationRequest();
        hrOrganizationRequest.setSearchText(commonOrgTreeRequest.getSearchText());
        LambdaQueryWrapper<HrOrganization> queryWrapper = OrgConditionFactory.createWrapper(hrOrganizationRequest);

        // 如果查询条件有orgId，则查询指定机构下的子机构
        Long parentId = commonOrgTreeRequest.getOrgParentId();
        if (parentId != null) {
            queryWrapper.eq(HrOrganization::getOrgParentId, parentId);
        }

        // 如果有定位状态的组织机构，则需要查询到定位的组织机构的所有子一级
        Set<Long> indexOrgIdList = commonOrgTreeRequest.getIndexOrgIdList();
        if (ObjectUtil.isNotEmpty(indexOrgIdList)) {
            Set<Long> parentIdListTotal = hrOrganizationService.queryOrgIdParentIdList(indexOrgIdList);
            if (ObjectUtil.isNotEmpty(parentIdListTotal)) {
                queryWrapper.in(HrOrganization::getOrgParentId, parentIdListTotal);
            }
        }

        // 如果有筛选公司的标识，则只查询公司列表
        Boolean companySearchFlag = commonOrgTreeRequest.getCompanySearchFlag();
        if (ObjectUtil.isNotEmpty(companySearchFlag) && companySearchFlag) {
            queryWrapper.eq(HrOrganization::getOrgType, OrgTypeEnum.COMPANY.getCode());
        }

        // 只查询启用状态的机构
        queryWrapper.eq(HrOrganization::getStatusFlag, StatusEnum.ENABLE.getCode());

        return queryWrapper;
    }

    /**
     * 创建通用查询Wrapper
     * <p>
     * 根据名称、状态、指定机构id等参数
     *
     * @author fengshuonan
     * @since 2025/1/26 20:20
     */
    public static LambdaQueryWrapper<HrOrganization> createWrapper(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> queryWrapper = new LambdaQueryWrapper<>();

        // 如果按文本查询条件不为空，则判断组织机构名称、简称、税号、备注是否有匹配
        String searchText = hrOrganizationRequest.getSearchText();
        if (StrUtil.isNotEmpty(searchText)) {
            queryWrapper.nested(wrap -> {
                wrap.like(HrOrganization::getOrgName, searchText);
                wrap.or().like(HrOrganization::getOrgShortName, searchText);
                wrap.or().like(HrOrganization::getTaxNo, searchText);
                wrap.or().like(HrOrganization::getOrgCode, searchText);
                wrap.or().like(HrOrganization::getRemark, searchText);
            });
        }

        // 根据机构状态查询
        Integer statusFlag = hrOrganizationRequest.getStatusFlag();
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), HrOrganization::getStatusFlag, statusFlag);

        // 如果查询条件有orgId，则查询指定机构下的子机构
        Long orgId = hrOrganizationRequest.getOrgId();
        if (orgId != null) {
            // 查询orgId对应的所有子机构，包含本orgId
            queryWrapper.nested(wrap -> {
                wrap.eq(HrOrganization::getOrgParentId, orgId).or().eq(HrOrganization::getOrgId, orgId);
            });
        }

        // 根据排序正序查询
        queryWrapper.orderByAsc(HrOrganization::getOrgSort);

        return queryWrapper;
    }

}
