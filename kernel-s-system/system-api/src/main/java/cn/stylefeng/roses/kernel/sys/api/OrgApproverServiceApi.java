package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.enums.org.DetectModeEnum;

import java.util.List;

/**
 * 组织机构审批人获取相关的方法
 *
 * @author fengshuonan
 * @since 2023/5/26 10:35
 */
public interface OrgApproverServiceApi {

    /**
     * 获取指定用户所属主部门的审批人列表
     * <p>
     * 用户可能有多组组织机构id，所以这里只取主部门
     *
     * @param userId          指定用户id
     * @param orgApproverType 审批人类型
     * @param parentLevel     从0开始，0为获取指定用户同部门的领导，1为上一级部门的领导，以此类推
     * @param detectModeEnum  指定查找的模式，自上而下找对应的负责人，还是自下而上
     * @return 用户的部门负责人id集合
     * @author fengshuonan
     * @since 2022/9/18 14:52
     */
    List<Long> getUserMainOrgApprover(Long userId, Integer orgApproverType, Integer parentLevel, DetectModeEnum detectModeEnum);

    /**
     * 获取指定部门的部门负责人
     *
     * @param deptId          指定部门id
     * @param orgApproverType 审批人类型
     * @param parentLevel     从0开始，0为获取指定同部门的领导，1为上一级部门的领导，以此类推
     * @param detectModeEnum  指定查找的模式，自上而下找对应的负责人，还是自下而上
     * @return 负责人id集合
     * @author fengshuonan
     * @since 2022/9/18 14:52
     */
    List<Long> getDeptOrgApprover(Long deptId, Integer orgApproverType, Integer parentLevel, DetectModeEnum detectModeEnum);

}
