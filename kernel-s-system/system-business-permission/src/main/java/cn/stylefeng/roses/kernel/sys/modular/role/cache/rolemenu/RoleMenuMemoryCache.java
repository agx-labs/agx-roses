package cn.stylefeng.roses.kernel.sys.modular.role.cache.rolemenu;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.constants.RoleConstants;

import java.util.List;

/**
 * 角色绑定菜单的缓存
 *
 * @author fengshuonan
 * @since 2023/7/14 22:43
 */
public class RoleMenuMemoryCache extends AbstractMemoryCacheOperator<List<Long>> {

    public RoleMenuMemoryCache(TimedCache<String, List<Long>> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return RoleConstants.ROLE_MENU_CACHE_PREFIX;
    }

}