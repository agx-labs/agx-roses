package cn.stylefeng.roses.kernel.sys.modular.role.cache.rolemenu;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.constants.RoleConstants;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 角色绑定菜单的缓存
 *
 * @author fengshuonan
 * @since 2023/7/14 22:42
 */
public class RoleMenuRedisCache extends AbstractRedisCacheOperator<List<Long>> {

    public RoleMenuRedisCache(RedisTemplate<String, List<Long>> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return RoleConstants.ROLE_MENU_CACHE_PREFIX;
    }

}
