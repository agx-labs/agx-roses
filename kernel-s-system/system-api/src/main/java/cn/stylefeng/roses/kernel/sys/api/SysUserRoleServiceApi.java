package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.UserRoleDTO;

import java.util.List;
import java.util.Set;

/**
 * 单独编写用户角色关联关系的Api
 *
 * @author fengshuonan
 * @since 2023/6/18 23:21
 */
public interface SysUserRoleServiceApi {

    /**
     * 获取用户的所有角色id列表
     *
     * @author fengshuonan
     * @since 2023/6/12 11:29
     */
    List<Long> getUserRoleIdList(Long userId);

    /**
     * 获取用户系统级别的角色，用在用户管理界面，分配角色时使用
     *
     * @author fengshuonan
     * @since 2024/1/17 22:35
     */
    Set<Long> getUserSystemRoleIdList(Long userId);

    /**
     * 获取用户所有关联了机构id的角色列表，用在新的用户授权界面
     *
     * @author fengshuonan
     * @since 2024/1/17 23:45
     */
    List<UserRoleDTO> getUserLinkedOrgRoleList(Long userId);

    /**
     * 获取用户的当前所在机构的所有角色id列表
     *
     * @author fengshuonan
     * @since 2024-01-17 16:24
     */
    List<Long> getUserRoleIdListCurrentCompany(Long userId, Long orgId);

    /**
     * 根据角色id找到角色对应的用户id集合
     *
     * @author fengshuonan
     * @since 2023/5/26 14:08
     */
    List<Long> findUserIdsByRoleId(Long roleId);

    /**
     * 获取用户的角色范围列表，角色范围指用户的角色可分配的权限列表
     *
     * @param userId 用户id
     * @author fengshuonan
     * @since 2023/9/8 21:41
     */
    Set<Long> findUserRoleLimitScope(Long userId);

    /**
     * 获取当前登录用户的角色范围列表，角色范围指用户的角色可分配的权限列表
     *
     * @author fengshuonan
     * @since 2023/9/8 21:41
     */
    Set<Long> findCurrentUserRoleLimitScope();

    /**
     * 清楚用户在本组织机构下的角色绑定
     *
     * @author fengshuonan
     * @since 2024-01-18 14:06
     */
    void removeUserOrgRoleLink(Long userId, Long orgId);

    /**
     * 删除用户所有关联机构的角色
     *
     * @author fengshuonan
     * @since 2024-01-18 16:07
     */
    void deleteUserAllOrgBind(Long userId);

}
