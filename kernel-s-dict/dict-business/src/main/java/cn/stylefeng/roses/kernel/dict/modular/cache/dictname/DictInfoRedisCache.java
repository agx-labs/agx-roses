package cn.stylefeng.roses.kernel.dict.modular.cache.dictname;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.dict.api.pojo.DictDetail;
import cn.stylefeng.roses.kernel.dict.modular.constants.DictCacheConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 字典信息的缓存
 * <p>
 * key是职位id，value是字典详情
 *
 * @author fengshuonan
 * @since 2025/1/10 14:10
 */
public class DictInfoRedisCache extends AbstractRedisCacheOperator<DictDetail> {

    public DictInfoRedisCache(RedisTemplate<String, DictDetail> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return DictCacheConstants.DICT_INFO_CACHE_PREFIX;
    }

}
