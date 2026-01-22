package cn.stylefeng.roses.kernel.sys.modular.notice.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.sys.modular.notice.pojo.NoticeUserScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 通知管理封装类
 *
 * @author fengshuonan
 * @since 2024/01/12 16:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysNoticeRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class, updateStatus.class})
    @ChineseDescription("主键")
    private Long noticeId;

    /**
     * 通知标题
     */
    @NotBlank(message = "通知标题不能为空", groups = {add.class, edit.class})
    @ChineseDescription("通知标题")
    private String noticeTitle;

    /**
     * 通知内容
     */
    @ChineseDescription("通知内容")
    @NotBlank(message = "通知内容不能为空", groups = {add.class, edit.class})
    private String noticeContent;

    /**
     * 优先级，来自字典：high-高优先级，middle-中，low-低
     */
    @NotBlank(message = "优先级不能为空", groups = {add.class, edit.class})
    @ChineseDescription("优先级，来自字典：high-高优先级，middle-中，low-低")
    private String priorityLevel;

    /**
     * 开始时间
     */
    @ChineseDescription("开始时间")
    private String noticeBeginTime;

    /**
     * 结束时间
     */
    @ChineseDescription("结束时间")
    private String noticeEndTime;

    /**
     * 是否发布：1-已发布，2-未发布
     */
    @ChineseDescription("是否发布：1-已发布，2-未发布")
    @NotNull(message = "发布状态不能为空", groups = updateStatus.class)
    private Integer publishStatus;

    /**
     * 批量删除用的id集合
     */
    @NotNull(message = "批量删除id集合不能为空", groups = batchDelete.class)
    @ChineseDescription("批量删除用的id集合")
    private List<Long> batchDeleteIdList;

    /**
     * 通知人员和部门的范围
     */
    @ChineseDescription("通知人员和部门的范围")
    private NoticeUserScope noticeUserScope;

}
