package cn.stylefeng.roses.kernel.sys.modular.role.cache.roleoptions;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.constants.RoleConstants;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 角色绑定菜单功能
 *
 * @author fengshuonan
 * @since 2023/7/14 23:23
 */
public class RoleMenuOptionsRedisCache extends AbstractRedisCacheOperator<List<Long>> {

    public RoleMenuOptionsRedisCache(RedisTemplate<String, List<Long>> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return RoleConstants.ROLE_MENU_OPTIONS_CACHE_PREFIX;
    }

}
