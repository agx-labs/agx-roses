package cn.stylefeng.roses.kernel.db.mp.tenant.holder;

/**
 * 是否开启租户过滤的开关，此优先级最高
 *
 * @author fengshuonan
 * @since 2023/9/1 0:09
 */
public class TenantSwitchHolder {

    private static final ThreadLocal<Boolean> TENANT_SWITCH_FLAG_HOLDER = new ThreadLocal<>();

    /**
     * 设置是否开启租户过滤，true开启，false关闭
     *
     * @author fengshuonan
     * @since 2023/9/1 0:11
     */
    public static void set(Boolean tenantSwitch) {
        TENANT_SWITCH_FLAG_HOLDER.set(tenantSwitch);
    }

    /**
     * 获取租户开启开关
     *
     * @author fengshuonan
     * @since 2023/9/1 0:11
     */
    public static Boolean get() {
        return TENANT_SWITCH_FLAG_HOLDER.get();
    }

    /**
     * 清空开关
     *
     * @author fengshuonan
     * @since 2023/9/1 0:11
     */
    public static void remove() {
        TENANT_SWITCH_FLAG_HOLDER.remove();
    }

}
