package cn.stylefeng.roses.kernel.sys.api;

import java.util.List;
import java.util.Set;

/**
 * 获取角色的权限列表
 *
 * @author fengshuonan
 * @since 2023/9/8 21:43
 */
public interface SysRoleLimitServiceApi {

    /**
     * 获取角色的限制列表（包含菜单id和菜单功能id）
     *
     * @author fengshuonan
     * @since 2023/9/8 14:19
     */
    Set<Long> getRoleBindLimitList(Long roleId);

    /**
     * 获取角色的限制列表（包含菜单id和菜单功能id）
     *
     * @author fengshuonan
     * @since 2023/9/8 14:19
     */
    Set<Long> getRoleBindLimitList(List<Long> roleIdList);

}
