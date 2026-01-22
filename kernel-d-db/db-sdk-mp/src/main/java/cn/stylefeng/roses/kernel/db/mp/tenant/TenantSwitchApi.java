package cn.stylefeng.roses.kernel.db.mp.tenant;

import cn.stylefeng.roses.kernel.db.mp.tenant.pojo.TenantSwitchInfo;

import java.util.function.Supplier;

/**
 * 统一切换当前租户的API
 * <p>
 * 根据租户的数据源配置，判断是否切分租户id或者租户的独立数据源
 *
 * @author fengshuonan
 * @since 2025/5/5 10:59
 */
public interface TenantSwitchApi {

    /**
     * 根据租户编码切换租户，并执行相关业务
     *
     * @param tenantCode 租户的编码
     * @param action     需要切换租户的代码
     * @author fengshuonan
     * @since 2025/5/4 17:23
     */
    <T> T changeTenant(String tenantCode, Supplier<T> action);

    /**
     * 根据租户id切换租户，并执行相关业务
     *
     * @param tenantId 租户的id
     * @param action   需要切换租户的代码
     * @author fengshuonan
     * @since 2025/5/4 17:23
     */
    <T> T changeTenant(Long tenantId, Supplier<T> action);

    /**
     * 切换租户，并执行相关业务
     *
     * @param tenantSwitchInfo 租户的详细信息
     * @param action           需要切换租户的代码
     * @author fengshuonan
     * @since 2025/5/4 17:23
     */
    <T> T changeTenant(TenantSwitchInfo tenantSwitchInfo, Supplier<T> action);

    /**
     * 不进行租户切换逻辑，执行相关业务
     *
     * @author fengshuonan
     * @since 2025/5/5 23:02
     */
    <T> T doNoTenantChange(Supplier<T> action);

}
