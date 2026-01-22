package cn.stylefeng.roses.kernel.sys.modular.user.cache.userrole.clear;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.api.annotation.BusinessListener;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.stylefeng.roses.kernel.sys.modular.user.constants.UserConstants.UPDATE_USER_ROLE_EVENT;

/**
 * 监听用户绑定角色的事件，清空相关缓存
 *
 * @author fengshuonan
 * @since 2023/7/14 22:05
 */
@Service
public class UserRoleClearListener {

    @Resource(name = "userRoleCache")
    private CacheOperatorApi<List<SysUserRole>> userRoleCache;

    /**
     * 监听更新用户角色
     *
     * @author fengshuonan
     * @since 2023/7/14 22:10
     */
    @BusinessListener(businessCode = UPDATE_USER_ROLE_EVENT)
    public void updateUserRole(Long userId) {
        if (ObjectUtil.isNotEmpty(userId)) {
            userRoleCache.remove(String.valueOf(userId));
        }
    }

}
