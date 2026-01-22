package cn.stylefeng.roses.kernel.sys.api.pojo.message;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * 消息发送的信息包装
 *
 * @author fengshuonan
 * @since 2024/1/14 22:15
 */
@Data
public class MessageSendDTO {

    /**
     * 接收用户id集合，可以一次发给多个人
     */
    @ChineseDescription("接收用户id集合，可以一次发给多个人")
    private Set<Long> userIdList;

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

}
