package cn.stylefeng.roses.kernel.sys.modular.role.cache.rolename;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.constants.RoleConstants;

/**
 * 角色名称的缓存
 * <p>
 * key是角色id，value是角色名称
 *
 * @author fengshuonan
 * @since 2025/1/10 14:33
 */
public class RoleNameMemoryCache extends AbstractMemoryCacheOperator<String> {

    public RoleNameMemoryCache(TimedCache<String, String> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return RoleConstants.ROLE_NAME_CACHE_PREFIX;
    }

}