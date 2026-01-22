package cn.stylefeng.roses.kernel.rule.pidset.callback;

/**
 * pid string填充回调
 *
 * @author fengshuonan
 * @since 2024/8/30 13:46
 */
public interface PidSettable {

    /**
     * 获取当前节点的本身的id
     *
     * @author fengshuonan
     * @since 2024/8/30 17:16
     */
    Long getCurrentId();

    /**
     * 获取当前节点的上级id
     *
     * @author fengshuonan
     * @since 2024/8/30 13:51
     */
    Long getParentId();

    /**
     * 填充pids的值
     *
     * @author fengshuonan
     * @since 2024/8/30 13:47
     */
    void setParentIdListString(String parentIdListString);

    /**
     * 获取当前节点的pids值
     *
     * @author fengshuonan
     * @since 2024/8/30 17:14
     */
    String getParentIdListString();

}
