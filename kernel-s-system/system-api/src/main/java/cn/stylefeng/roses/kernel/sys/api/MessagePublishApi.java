package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageRetractDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageSendDTO;

/**
 * 适用于框架上，给个人消息模块发送的消息
 *
 * @author fengshuonan
 * @since 2024/1/14 22:06
 */
public interface MessagePublishApi {

    /**
     * 消息发送的接口
     *
     * @author fengshuonan
     * @since 2024/1/14 22:16
     */
    void batchSendMessage(MessageSendDTO messageSendDTO);

    /**
     * 消息撤回的接口，删除指定业务id关联的消息
     *
     * @author fengshuonan
     * @since 2024/1/14 22:16
     */
    void batchRetractMessage(MessageRetractDTO messageRetractDTO);

}
