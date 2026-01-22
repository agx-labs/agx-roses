package cn.stylefeng.roses.kernel.rule.pidset.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 批量更新子节点的操作
 *
 * @author fengshuonan
 * @since 2024/8/30 22:56
 */
public interface CommonUpdatePidMapper {

    /**
     * 更新子节点的parentIdListString
     *
     * @author fengshuonan
     * @since 2024/8/30 17:22
     */
    void updateSubParentIdListString(@Param("tableName") String tableName,
                                     @Param("pidsFieldName") String pidsFieldName,
                                     @Param("oldParentIdListString") String oldParentIdListString,
                                     @Param("newParentIdListString") String newParentIdListString,
                                     @Param("updateTime") String updateTime,
                                     @Param("updateUser") Long updateUser);

}