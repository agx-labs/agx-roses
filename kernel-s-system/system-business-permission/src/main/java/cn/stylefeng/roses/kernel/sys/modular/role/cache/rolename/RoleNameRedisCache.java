package cn.stylefeng.roses.kernel.sys.modular.role.cache.rolename;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.constants.RoleConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 角色名称的缓存
 * <p>
 * key是角色id，value是角色名称
 *
 * @author fengshuonan
 * @since 2025/1/10 14:33
 */
public class RoleNameRedisCache extends AbstractRedisCacheOperator<String> {

    public RoleNameRedisCache(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return RoleConstants.ROLE_NAME_CACHE_PREFIX;
    }

}
