package cn.stylefeng.roses.kernel.sys.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;

import java.util.HashMap;

/**
 * 组织机构id格式化为获取机构的详情
 * <p>
 * 优先从缓存获取数据，进行名称的获取
 *
 * @author fengshuonan
 * @since 2023/7/17 15:36
 */
public class OrgDetailFormatProcess extends BaseSimpleFieldFormatProcess {

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
        String orgName = organizationServiceApi.getOrgNameById(orgId);

        HashMap<String, Object> orgDetail = new HashMap<>();
        orgDetail.put("orgId", orgId);
        orgDetail.put("orgName", orgName);

        return orgDetail;
    }

}
