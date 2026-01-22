package cn.stylefeng.roses.kernel.sys.modular.user.cache.username;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserInfoDetailDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.constants.UserConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 用户的缓存
 * <p>
 * key是用户id，value是用户
 *
 * @author fengshuonan
 * @since 2025/1/10 11:05
 */
public class UserInfoRedisCache extends AbstractRedisCacheOperator<UserInfoDetailDTO> {

    public UserInfoRedisCache(RedisTemplate<String, UserInfoDetailDTO> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return UserConstants.USER_INFO_CACHE_PREFIX;
    }

}
