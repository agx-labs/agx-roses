package cn.stylefeng.roses.kernel.rule.tree.buildpids;

/**
 * 构造pids结构需要的实体规范
 *
 * @author fengshuonan
 * @since 2023/6/15 15:01
 */
public interface BasePidBuildModel {

    /**
     * 获取树形节点id
     *
     * @author fengshuonan
     * @since 2023/6/15 15:02
     */
    String pidBuildNodeId();

    /**
     * 获取树形节点的父级id
     *
     * @author fengshuonan
     * @since 2023/6/15 15:02
     */
    String pidBuildParentId();

    /**
     * 设置pids结构
     *
     * @author fengshuonan
     * @since 2023/6/15 15:02
     */
    void setPidBuildPidStructure(String pids);
}
