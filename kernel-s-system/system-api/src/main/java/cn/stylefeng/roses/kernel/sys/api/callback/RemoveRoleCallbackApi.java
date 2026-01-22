package cn.stylefeng.roses.kernel.sys.api.callback;

import java.util.Set;

/**
 * 删除角色时的回调方法
 *
 * @author fengshuonan
 * @since 2023/6/12 20:51
 */
public interface RemoveRoleCallbackApi {

    /**
     * 校验指定角色id集合是否有和业务的绑定关系
     * <p>
     * 如果有绑定关系直接抛出异常即可
     *
     * @param beRemovedRoleIdList 被删除的角色id集合
     * @author fengshuonan
     * @since 2023/6/12 20:52
     */
    void validateHaveRoleBind(Set<Long> beRemovedRoleIdList);

    /**
     * 删除角色之后的回调方法
     *
     * @param beRemovedRoleIdList 被删除的角色id集合
     * @author fengshuonan
     * @since 2023/6/12 20:53
     */
    void removeRoleAction(Set<Long> beRemovedRoleIdList);

}
