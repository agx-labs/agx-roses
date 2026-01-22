package cn.stylefeng.roses.kernel.sys.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;

/**
 * 用户姓名包装
 * <p>
 * 优先从缓存读取，再从数据库读取
 *
 * @author fengshuonan
 * @since 2023/6/16 22:26
 */
public class UserNameFormatProcess extends BaseSimpleFieldFormatProcess {

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

        SysUserServiceApi sysUserServiceApi = SpringUtil.getBean(SysUserServiceApi.class);

        return sysUserServiceApi.getUserRealName(userId);
    }

}
