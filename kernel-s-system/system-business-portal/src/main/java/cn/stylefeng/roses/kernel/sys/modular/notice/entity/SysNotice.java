package cn.stylefeng.roses.kernel.sys.modular.notice.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseBusinessEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import cn.stylefeng.roses.kernel.sys.api.format.UserNameFormatProcess;
import cn.stylefeng.roses.kernel.sys.modular.notice.pojo.NoticeUserScope;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * 通知管理实例类
 *
 * @author fengshuonan
 * @since 2024/01/12 16:06
 */
@TableName(value = "sys_notice", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNotice extends BaseBusinessEntity {

    /**
     * 主键
     */
    @TableId(value = "notice_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long noticeId;

    /**
     * 通知标题
     */
    @TableField("notice_title")
    @ChineseDescription("通知标题")
    private String noticeTitle;

    /**
     * 通知摘要
     */
    @TableField("notice_summary")
    @ChineseDescription("通知摘要")
    private String noticeSummary;

    /**
     * 通知内容
     */
    @TableField("notice_content")
    @ChineseDescription("通知内容")
    private String noticeContent;

    /**
     * 优先级，来自字典：high-高优先级，middle-中，low-低
     */
    @TableField("priority_level")
    @ChineseDescription("优先级，来自字典：high-高优先级，middle-中，low-低")
    private String priorityLevel;

    /**
     * 开始时间
     */
    @TableField("notice_begin_time")
    @ChineseDescription("开始时间")
    private Date noticeBeginTime;

    /**
     * 结束时间
     */
    @TableField("notice_end_time")
    @ChineseDescription("结束时间")
    private Date noticeEndTime;

    /**
     * 通知范围，存用户id集合
     */
    @TableField(value = "notice_user_scope", typeHandler = JacksonTypeHandler.class)
    @ChineseDescription("通知范围，存用户id集合")
    private NoticeUserScope noticeUserScope;

    /**
     * 是否发布：1-已发布，2-未发布
     */
    @TableField("publish_status")
    @ChineseDescription("是否发布：1-已发布，2-未发布")
    private Integer publishStatus;

    /**
     * 拓展字段
     */
    @TableField(value = "expand_field", typeHandler = JacksonTypeHandler.class)
    @ChineseDescription("拓展字段")
    private Map<String, Object> expandField;

    /**
     * 租户号
     */
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    @ChineseDescription("租户号")
    private Long tenantId;

    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    @SimpleFieldFormat(processClass = UserNameFormatProcess.class)
    private Long createUser;

}
