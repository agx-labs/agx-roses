package cn.stylefeng.roses.kernel.sys.modular.menu.cache.menucode;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.sys.modular.menu.constants.MenuConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 菜单编码的缓存
 * <p>
 * 同时用在，菜单功能编码缓存
 *
 * @author fengshuonan
 * @since 2023/7/15 0:11
 */
public class MenuCodeRedisCache extends AbstractRedisCacheOperator<String> {

    public MenuCodeRedisCache(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return MenuConstants.MENU_CODE_CACHE_PREFIX;
    }

}
