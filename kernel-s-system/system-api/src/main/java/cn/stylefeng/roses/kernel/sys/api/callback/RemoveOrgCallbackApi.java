package cn.stylefeng.roses.kernel.sys.api.callback;

import java.util.Set;

/**
 * 删除组织机构的回调方法
 *
 * @author fengshuonan
 * @since 2023/6/11 13:49
 */
public interface RemoveOrgCallbackApi {

    /**
     * 校验指定orgId集合是否有和业务的绑定关系
     * <p>
     * 如果有绑定关系直接抛出异常即可
     *
     * @param beRemovedOrgIdList 被删除的组织机构id集合
     * @author fengshuonan
     * @since 2023/6/11 14:04
     */
    void validateHaveOrgBind(Set<Long> beRemovedOrgIdList);

    /**
     * 删除组织机构的回调，删除组织机构后自动触发
     *
     * @param beRemovedOrgIdList 被删除的组织机构id集合
     * @author fengshuonan
     * @since 2023/6/11 13:50
     */
    void removeOrgAction(Set<Long> beRemovedOrgIdList);

}
