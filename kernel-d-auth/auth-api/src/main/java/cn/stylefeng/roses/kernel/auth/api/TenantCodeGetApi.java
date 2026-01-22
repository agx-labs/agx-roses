package cn.stylefeng.roses.kernel.auth.api;

/**
 * 租户编码的接口，通过租户编码获取到租户ID
 * <p>
 * 一般用在登录接口，通过租户的编码获取租户的id信息
 *
 * @author fengshuonan
 * @since 2023/8/31 23:49
 */
public interface TenantCodeGetApi {

    /**
     * 通过租户编码获取租户的ID
     *
     * @author fengshuonan
     * @since 2023/8/31 23:50
     */
    Long getTenantIdByCode(String tenantCode);

}
