package cn.stylefeng.roses.kernel.dict.modular.cache.dictname;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.dict.api.pojo.DictDetail;
import cn.stylefeng.roses.kernel.dict.modular.constants.DictCacheConstants;

/**
 * 字典信息的缓存
 * <p>
 * key是职位id，value是字典详情
 *
 * @author fengshuonan
 * @since 2025/1/10 14:55
 */
public class DictInfoMemoryCache extends AbstractMemoryCacheOperator<DictDetail> {

    public DictInfoMemoryCache(TimedCache<String, DictDetail> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return DictCacheConstants.DICT_INFO_CACHE_PREFIX;
    }

}