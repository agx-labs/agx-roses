package cn.stylefeng.roses.kernel.sys.modular.user.cache.userrole;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.sys.modular.user.constants.UserConstants;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;

import java.util.List;

/**
 * 用户绑定角色的缓存
 *
 * @author fengshuonan
 * @since 2023/7/14 22:00
 */
public class UserRoleMemoryCache extends AbstractMemoryCacheOperator<List<SysUserRole>> {

    public UserRoleMemoryCache(TimedCache<String, List<SysUserRole>> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return UserConstants.USER_ROLE_CACHE_PREFIX;
    }

}