package cn.stylefeng.roses.kernel.sys.modular.role.cache.roleoptions;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.constants.RoleConstants;

import java.util.List;

/**
 * 角色绑定菜单功能
 *
 * @author fengshuonan
 * @since 2023/7/14 23:23
 */
public class RoleMenuOptionsMemoryCache extends AbstractMemoryCacheOperator<List<Long>> {

    public RoleMenuOptionsMemoryCache(TimedCache<String, List<Long>> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return RoleConstants.ROLE_MENU_OPTIONS_CACHE_PREFIX;
    }

}