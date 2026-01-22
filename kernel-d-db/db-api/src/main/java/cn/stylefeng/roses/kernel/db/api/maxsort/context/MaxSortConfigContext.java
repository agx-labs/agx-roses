package cn.stylefeng.roses.kernel.db.api.maxsort.context;

import cn.stylefeng.roses.kernel.db.api.maxsort.MaxCountConfig;

import java.util.Map;

/**
 * 获取最大排序字段配置的上下文容器
 *
 * @author fengshuonan
 * @since 2023/10/29 17:24
 */
public class MaxSortConfigContext {

    private static Map<String, MaxCountConfig> MAX_COUNT_CONFIG_MAP = null;

    /**
     * 从配置容器获取配置
     *
     * @author fengshuonan
     * @since 2023/10/29 17:24
     */
    public static MaxCountConfig getConfig(String code) {
        return MAX_COUNT_CONFIG_MAP.get(code);
    }

    /**
     * 初始化配置
     *
     * @author fengshuonan
     * @since 2023/10/29 17:24
     */
    public static void initConfig(Map<String, MaxCountConfig> totalConfigs) {
        MAX_COUNT_CONFIG_MAP = totalConfigs;
    }

}
