package cn.stylefeng.roses.kernel.sys.api.pojo.message;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 发送到ws时候用的传输bean
 *
 * @author fengshuonan
 * @since 2024-01-16 11:16
 */
@Data
public class MessageSendToSocketDTO {

    /**
     * 收消息的用户id
     */
    @ChineseDescription("收消息的用户id")
    private Long receiveUserId;

    /**
     * 消息的id，存sys_message表的主键id
     */
    private Long messageId;

    /**
     * 发送用户id
     */
    @ChineseDescription("发送用户id")
    private Long sendUserId;

    /**
     * 消息标题
     */
    @ChineseDescription("消息标题")
    private String messageTitle;

    /**
     * 消息内容
     */
    @ChineseDescription("消息内容")
    private String messageContent;

    /**
     * 消息类型：NORMAL-普通类型，URL-带链接跳转
     */
    @ChineseDescription("消息类型：NORMAL-普通类型，URL-带链接跳转")
    private String messageType;

    /**
     * 消息跳转的URL
     */
    @ChineseDescription("消息跳转的URL")
    private String messageUrl;

    /**
     * 优先级：high-高优先级，middle-中，low-低
     */
    @ChineseDescription("优先级：high-高优先级，middle-中，low-低")
    private String priorityLevel;

    /**
     * 关联业务id
     */
    @ChineseDescription("关联业务id")
    private String businessId;

    /**
     * 业务类型(根据业务id和业务类型可以确定业务数据)
     */
    @ChineseDescription("业务类型(根据业务id和业务类型可以确定业务数据)")
    private String businessType;

    /**
     * 业务的详细信息自定义字段
     */
    @ChineseDescription("业务的详细信息自定义字段")
    private Map<String, Object> businessDetail;

    /**
     * 消息发送时间
     */
    @ChineseDescription("消息发送时间")
    private Date messageSendTime;

}
