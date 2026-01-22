package cn.stylefeng.roses.kernel.sys.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;

/**
 * 快速获取组织机构的全路径组织机构名称
 * <p>
 * 通过缓存加快名称获取
 *
 * @author fengshuonan
 * @since 2025/1/11 0:11
 */
public class OrgFullPathNameFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {

        if (businessId == null) {
            return null;
        }

        Long orgId = Convert.toLong(businessId);

        OrganizationServiceApi organizationServiceApi = SpringUtil.getBean(OrganizationServiceApi.class);

        return organizationServiceApi.getOrgTotalPathName(orgId);
    }

}
