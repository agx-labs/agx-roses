package cn.stylefeng.roses.kernel.sys.modular.role.cache;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.api.annotation.BusinessListener;
import cn.stylefeng.roses.kernel.sys.api.constants.RoleConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 监听角色绑定菜单的事件
 *
 * @author fengshuonan
 * @since 2023/7/14 22:48
 */
@Service
public class RoleMenuClearListener {

    @Resource(name = "roleMenuCache")
    private CacheOperatorApi<List<Long>> roleMenuCache;

    @Resource(name = "roleMenuOptionsCache")
    private CacheOperatorApi<List<Long>> roleMenuOptionsCache;

    /**
     * 监听角色绑定权限的事件
     *
     * @author fengshuonan
     * @since 2023/7/14 23:06
     */
    @BusinessListener(businessCode = RoleConstants.ROLE_BIND_MENU_EVENT)
    public void roleBindMenuEvent(Long roleId) {
        if (ObjectUtil.isNotEmpty(roleId)) {
            roleMenuCache.remove(roleId.toString());
        }
    }

    /**
     * 角色绑定功能的事件
     *
     * @author fengshuonan
     * @since 2023/7/14 23:44
     */
    @BusinessListener(businessCode = RoleConstants.ROLE_BIND_MENU_OPTIONS_EVENT)
    public void roleBindMenuOptionsEvent(Long roleId) {
        if (ObjectUtil.isNotEmpty(roleId)) {
            roleMenuOptionsCache.remove(roleId.toString());
        }
    }

}
