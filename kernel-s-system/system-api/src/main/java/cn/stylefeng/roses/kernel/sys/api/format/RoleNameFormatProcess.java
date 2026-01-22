package cn.stylefeng.roses.kernel.sys.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;

/**
 * JSON响应对角色id的转化
 * <p>
 * 通过缓存加快角色名称获取
 *
 * @author fengshuonan
 * @since 2023/5/4 21:20
 */
public class RoleNameFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {
        Long roleId = Convert.toLong(businessId);
        SysRoleServiceApi bean = SpringUtil.getBean(SysRoleServiceApi.class);
        return bean.getRoleNameByRoleId(roleId);
    }

}
