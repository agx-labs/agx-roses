package cn.stylefeng.roses.kernel.sys.modular.user.cache.username.clear;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.api.annotation.BusinessListener;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserInfoDetailDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.constants.UserConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 监听用户姓名的更新，清空掉用户名称相关的缓存
 *
 * @author fengshuonan
 * @since 2025/1/10 11:10
 */
@Service
public class UserInfoClearListener {

    @Resource(name = "userInfoCache")
    private CacheOperatorApi<UserInfoDetailDTO> userInfoCache;

    /**
     * 监听更新用户
     *
     * @author fengshuonan
     * @since 2025/1/10 11:11
     */
    @BusinessListener(businessCode = UserConstants.UPDATE_USER_INFO_EVENT)
    public void updateUserInfo(Long userId) {
        if (ObjectUtil.isNotEmpty(userId)) {
            userInfoCache.remove(String.valueOf(userId));
        }
    }

}
