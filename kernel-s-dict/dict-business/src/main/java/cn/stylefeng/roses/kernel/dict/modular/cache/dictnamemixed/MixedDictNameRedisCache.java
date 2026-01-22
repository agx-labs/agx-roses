package cn.stylefeng.roses.kernel.dict.modular.cache.dictnamemixed;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.dict.modular.constants.DictCacheConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 字典名称的缓存
 * <p>
 * key是混合类型的，value是字典名称
 * <p>
 * key可能有两种，1. 字典类型id+字典编码 2. 字典类型编码+字典编码
 *
 * @author fengshuonan
 * @since 2025/1/10 14:10
 */
public class MixedDictNameRedisCache extends AbstractRedisCacheOperator<String> {

    public MixedDictNameRedisCache(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return DictCacheConstants.DICT_MIXED_NAME_CACHE_PREFIX;
    }

}
