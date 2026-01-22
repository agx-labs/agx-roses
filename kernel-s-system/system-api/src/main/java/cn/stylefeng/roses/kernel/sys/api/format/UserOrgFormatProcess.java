package cn.stylefeng.roses.kernel.sys.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.SysUserOrgServiceApi;

/**
 * 用户所属组织机构的信息包装
 *
 * @author fengshuonan
 * @since 2023/6/11 21:11
 */
public class UserOrgFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {

        if (businessId == null) {
            return null;
        }

        Long userId = Convert.toLong(businessId);

        SysUserOrgServiceApi sysUserOrgServiceApi = SpringUtil.getBean(SysUserOrgServiceApi.class);

        return sysUserOrgServiceApi.getUserMainOrgInfo(userId);
    }

}
