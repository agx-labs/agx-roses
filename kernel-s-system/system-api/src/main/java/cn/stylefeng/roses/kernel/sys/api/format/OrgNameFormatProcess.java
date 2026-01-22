package cn.stylefeng.roses.kernel.sys.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;

/**
 * 获取机构名称的处理
 * <p>
 * 采用组织机构详情缓存，加速获取
 *
 * @author fengshuonan
 * @since 2023/7/14 16:46
 */
public class OrgNameFormatProcess extends BaseSimpleFieldFormatProcess {

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

        return organizationServiceApi.getOrgNameById(orgId);
    }

}
