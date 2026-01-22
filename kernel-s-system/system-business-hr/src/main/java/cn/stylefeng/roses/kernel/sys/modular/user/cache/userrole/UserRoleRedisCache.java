package cn.stylefeng.roses.kernel.sys.modular.user.cache.userrole;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.sys.modular.user.constants.UserConstants;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 用户绑定角色的缓存
 *
 * @author fengshuonan
 * @since 2023/7/14 21:58
 */
public class UserRoleRedisCache extends AbstractRedisCacheOperator<List<SysUserRole>> {

    public UserRoleRedisCache(RedisTemplate<String, List<SysUserRole>> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return UserConstants.USER_ROLE_CACHE_PREFIX;
    }

}
