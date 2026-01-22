package cn.stylefeng.roses.kernel.rule.pidset.callback;

import cn.stylefeng.roses.kernel.rule.pidset.pojo.ParentIdInfoPojo;

/**
 * 获取指定item的详情（详情包含父级的pids信息）
 *
 * @author fengshuonan
 * @since 2024/8/30 13:50
 */
public interface PidGetterService {

    /**
     * 获取指定节点的pids信息
     *
     * @author fengshuonan
     * @since 2024/8/30 13:50
     */
    ParentIdInfoPojo getPointNodePidInfo(Long itemId);

}
