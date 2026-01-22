package cn.stylefeng.roses.kernel.sys.modular.message.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统消息封装类
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMessageRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {delete.class})
    @ChineseDescription("主键")
    private Long messageId;

    /**
     * 接收用户id
     */
    @ChineseDescription("接收用户id")
    private Long receiveUserId;

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
     * 消息发送时间
     */
    @ChineseDescription("消息发送时间")
    private String messageSendTime;

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
     * 阅读状态：0-未读，1-已读
     */
    @ChineseDescription("阅读状态：0-未读，1-已读")
    private Integer readFlag;

    /**
     * 批量删除用的id集合
     */
    @NotNull(message = "批量删除id集合不能为空", groups = batchDelete.class)
    @ChineseDescription("批量删除用的id集合")
    private List<Long> batchDeleteIdList;

}
