package cn.stylefeng.roses.kernel.db.mp.tenant.holder;

/**
 * 租户id临时存放holder，这个优先级高于LoginUser获取的配置
 *
 * @author fengshuonan
 * @since 2023/8/31 17:03
 */
public class TenantIdHolder {

    private static final ThreadLocal<Long> TENANT_ID_TEMP_HOLDER = new ThreadLocal<>();

    /**
     * 设置租户id
     *
     * @author fengshuonan
     * @since 2023/8/31 17:04
     */
    public static void set(Long tenantId) {
        TENANT_ID_TEMP_HOLDER.set(tenantId);
    }

    /**
     * 获取租户id
     *
     * @author fengshuonan
     * @since 2023/8/31 17:04
     */
    public static Long get() {
        return TENANT_ID_TEMP_HOLDER.get();
    }

    /**
     * 删除租户id
     *
     * @author fengshuonan
     * @since 2023/8/31 17:05
     */
    public static void remove() {
        TENANT_ID_TEMP_HOLDER.remove();
    }

}
