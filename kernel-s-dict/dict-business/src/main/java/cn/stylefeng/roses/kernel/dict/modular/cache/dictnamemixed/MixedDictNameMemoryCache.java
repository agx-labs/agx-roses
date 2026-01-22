package cn.stylefeng.roses.kernel.dict.modular.cache.dictnamemixed;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.dict.modular.constants.DictCacheConstants;

/**
 * 字典名称的缓存
 * <p>
 * key是混合类型的，value是字典名称
 * <p>
 * key可能有两种，1. 字典类型id+字典编码 2. 字典类型编码+字典编码
 *
 * @author fengshuonan
 * @since 2025/1/10 16:33
 */
public class MixedDictNameMemoryCache extends AbstractMemoryCacheOperator<String> {

    public MixedDictNameMemoryCache(TimedCache<String, String> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return DictCacheConstants.DICT_MIXED_NAME_CACHE_PREFIX;
    }

}