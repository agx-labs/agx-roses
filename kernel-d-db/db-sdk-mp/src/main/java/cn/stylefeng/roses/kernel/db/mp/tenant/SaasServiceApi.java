package cn.stylefeng.roses.kernel.db.mp.tenant;

import cn.stylefeng.roses.kernel.db.mp.tenant.pojo.TenantSwitchInfo;

/**
 * 租户业务的相关API
 *
 * @author fengshuonan
 * @since 2025/5/4 18:35
 */
public interface SaasServiceApi {

    /**
     * 通过租户id获取租户的切换信息
     *
     * @author fengshuonan
     * @since 2025/5/4 18:35
     */
    TenantSwitchInfo getTenantSwitchInfo(Long tenantId);

    /**
     * 通过租户编码获取租户的去切换信息
     *
     * @author fengshuonan
     * @since 2025/5/5 11:31
     */
    TenantSwitchInfo getTenantSwitchInfo(String tenantCode);

}
