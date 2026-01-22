package cn.stylefeng.roses.kernel.sys.modular.message.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseBusinessEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * 系统消息实例类
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
@TableName(value = "sys_message", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMessage extends BaseBusinessEntity {

    /**
     * 主键
     */
    @TableId(value = "message_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long messageId;

    /**
     * 接收用户id
     */
    @TableField("receive_user_id")
    @ChineseDescription("接收用户id")
    private Long receiveUserId;

    /**
     * 发送用户id
     */
    @TableField("send_user_id")
    @ChineseDescription("发送用户id")
    private Long sendUserId;

    /**
     * 消息标题
     */
    @TableField("message_title")
    @ChineseDescription("消息标题")
    private String messageTitle;

    /**
     * 消息内容
     */
    @TableField("message_content")
    @ChineseDescription("消息内容")
    private String messageContent;

    /**
     * 消息类型：NORMAL-普通类型，URL-带链接跳转
     */
    @TableField("message_type")
    @ChineseDescription("消息类型：NORMAL-普通类型，URL-带链接跳转")
    private String messageType;

    /**
     * 消息跳转的URL
     */
    @TableField("message_url")
    @ChineseDescription("消息跳转的URL")
    private String messageUrl;

    /**
     * 优先级：high-高优先级，middle-中，low-低
     */
    @TableField("priority_level")
    @ChineseDescription("优先级：high-高优先级，middle-中，low-低")
    private String priorityLevel;

    /**
     * 消息发送时间
     */
    @TableField("message_send_time")
    @ChineseDescription("消息发送时间")
    private Date messageSendTime;

    /**
     * 关联业务id
     */
    @TableField("business_id")
    @ChineseDescription("关联业务id")
    private String businessId;

    /**
     * 业务类型(根据业务id和业务类型可以确定业务数据)
     */
    @TableField("business_type")
    @ChineseDescription("业务类型(根据业务id和业务类型可以确定业务数据)")
    private String businessType;

    /**
     * 业务的详细信息自定义字段
     */
    @TableField(value = "business_detail", typeHandler = JacksonTypeHandler.class)
    @ChineseDescription("业务的详细信息自定义字段")
    private Map<String, Object> businessDetail;

    /**
     * 阅读状态：0-未读，1-已读
     */
    @TableField("read_flag")
    @ChineseDescription("阅读状态：0-未读，1-已读")
    private Integer readFlag;

    /**
     * 租户号
     */
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    @ChineseDescription("租户号")
    private Long tenantId;

}
