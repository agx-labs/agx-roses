package cn.stylefeng.roses.kernel.auth.auth;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.TenantCodeGetApi;
import cn.stylefeng.roses.kernel.sys.api.expander.TenantConfigExpander;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取默认的租户id
 * <p>
 * 开源版不提供租户管理的功能，只能提供单租户编码的维护，也就是默认租户
 *
 * @author fengshuonan
 * @since 2023/9/1 0:38
 */
@Slf4j
public class DefaultTenantCodeProvider implements TenantCodeGetApi {

    /**
     * 一个不存在的租户id
     */
    public static final Long DEFAULT_NONE_TENANT_ID = -999L;

    @Override
    public Long getTenantIdByCode(String tenantCode) {

        Long defaultRootTenantId = TenantConfigExpander.getDefaultRootTenantId();

        // 如果租户编码为空，那么返回默认的租户id
        if (ObjectUtil.isEmpty(tenantCode)) {
            return defaultRootTenantId;
        }

        // 判断请求的租户编码是否是默认的租户id一致，字符串比较，如果一致则返回默认的租户id
        if (defaultRootTenantId.toString().equals(tenantCode)) {
            return defaultRootTenantId;
        }

        // 如果不一致，则返回不存在的租户id
        else {
            log.warn("当前系统为单租户系统，不支持多租户功能！");
            return DEFAULT_NONE_TENANT_ID;
        }
    }

}
