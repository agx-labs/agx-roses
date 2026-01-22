package cn.stylefeng.roses.kernel.group.api.callback;

/**
 * 分组名称回显的方法
 *
 * @author fengshuonan
 * @since 2023/5/30 17:13
 */
public interface GroupNameCallbackApi {

    /**
     * 获取被渲染数据的主键id
     *
     * @author fengshuonan
     * @since 2023/5/30 17:18
     */
    Long getRenderBusinessId();

    /**
     * 设置条件分组名称，供回调用
     *
     * @param groupName 分组名称
     * @author fengshuonan
     * @since 2023/5/30 17:13
     */
    void renderGroupName(String groupName);

}
