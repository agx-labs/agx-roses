package cn.stylefeng.roses.kernel.db.api.maxsort;

import java.util.Map;

/**
 * 获取最大排序字段配置的接口
 *
 * @author fengshuonan
 * @since 2023/10/29 17:03
 */
public interface MaxSortCollectorApi {

    /**
     * 通过此接口采集各个模块的最大排序数字的配置
     *
     * @author fengshuonan
     * @since 2023/10/29 17:05
     */
    Map<String, MaxCountConfig> createMaxSortConfigs();

}
