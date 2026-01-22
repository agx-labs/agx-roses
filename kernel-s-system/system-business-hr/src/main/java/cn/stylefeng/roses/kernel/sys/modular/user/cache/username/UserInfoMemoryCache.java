package cn.stylefeng.roses.kernel.sys.modular.user.cache.username;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserInfoDetailDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.constants.UserConstants;

/**
 * 用户的缓存
 * <p>
 * key是用户id，value是用户
 *
 * @author fengshuonan
 * @since 2025/1/10 11:06
 */
public class UserInfoMemoryCache extends AbstractMemoryCacheOperator<UserInfoDetailDTO> {

    public UserInfoMemoryCache(TimedCache<String, UserInfoDetailDTO> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return UserConstants.USER_INFO_CACHE_PREFIX;
    }

}