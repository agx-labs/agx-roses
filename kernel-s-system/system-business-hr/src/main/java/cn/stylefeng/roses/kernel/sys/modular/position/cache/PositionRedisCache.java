package cn.stylefeng.roses.kernel.sys.modular.position.cache;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.sys.modular.position.constants.PositionConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 职位信息缓存
 * <p>
 * key是职位id，value是职位名称
 *
 * @author fengshuonan
 * @since 2025/1/10 14:10
 */
public class PositionRedisCache extends AbstractRedisCacheOperator<String> {

    public PositionRedisCache(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return PositionConstants.POSITION_CACHE_PREFIX;
    }

}
