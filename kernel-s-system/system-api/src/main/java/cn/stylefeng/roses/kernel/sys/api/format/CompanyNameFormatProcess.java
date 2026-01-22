package cn.stylefeng.roses.kernel.sys.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;

/**
 * 获取公司名称的处理
 * <p>
 * 优先从缓存获取数据，进行名称的获取
 *
 * @author fengshuonan
 * @since 2024-01-16 20:10
 */
public class CompanyNameFormatProcess extends BaseSimpleFieldFormatProcess {

    private static final String NULL_ORG_NAME = "空";

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {

        if (businessId == null) {
            return NULL_ORG_NAME;
        }

        Long orgId = Convert.toLong(businessId);

        OrganizationServiceApi organizationServiceApi = SpringUtil.getBean(OrganizationServiceApi.class);

        CompanyDeptDTO orgCompanyInfo = organizationServiceApi.getOrgCompanyInfo(orgId);
        if (ObjectUtil.isEmpty(orgCompanyInfo)) {
            return NULL_ORG_NAME;
        }

        return orgCompanyInfo.getCompanyName();
    }

}
