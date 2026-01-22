package cn.stylefeng.roses.kernel.db.mp.datascope.holder;

import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;

/**
 * 数据范围配置上下文，通过使用这个类进行数据范围的配置
 *
 * @author fengshuonan
 * @since 2024-02-29 10:16
 */
public class DataScopeHolder {

    private static final ThreadLocal<DataScopeConfig> DATA_SCOPE_CONFIG_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据范围配置
     *
     * @author fengshuonan
     * @since 2024-02-29 10:17
     */
    public static void set(DataScopeConfig tenantId) {
        DATA_SCOPE_CONFIG_HOLDER.set(tenantId);
    }

    /**
     * 获取数据范围的配置
     *
     * @author fengshuonan
     * @since 2024-02-29 10:17
     */
    public static DataScopeConfig get() {
        return DATA_SCOPE_CONFIG_HOLDER.get();
    }

    /**
     * 删除数据范围的配置
     *
     * @author fengshuonan
     * @since 2024-02-29 10:17
     */
    public static void remove() {
        DATA_SCOPE_CONFIG_HOLDER.remove();
    }

}
