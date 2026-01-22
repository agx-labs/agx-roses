package cn.stylefeng.roses.kernel.sys.api.callback;

import java.util.Set;

/**
 * 删除菜单的回调
 *
 * @author fengshuonan
 * @since 2023/6/15 10:27
 */
public interface RemoveMenuCallbackApi {

    /**
     * 删除菜单关联的业务绑定操作
     *
     * @author fengshuonan
     * @since 2023/6/15 10:27
     */
    void removeMenuAction(Set<Long> beRemovedMenuIdList);

}
