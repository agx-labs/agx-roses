package cn.stylefeng.roses.kernel.sys.modular.position.cache;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.sys.modular.position.constants.PositionConstants;

/**
 * 职位信息缓存
 * <p>
 * key是职位id，value是职位名称
 *
 * @author fengshuonan
 * @since 2025/1/10 14:10
 */
public class PositionMemoryCache extends AbstractMemoryCacheOperator<String> {

    public PositionMemoryCache(TimedCache<String, String> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return PositionConstants.POSITION_CACHE_PREFIX;
    }

}