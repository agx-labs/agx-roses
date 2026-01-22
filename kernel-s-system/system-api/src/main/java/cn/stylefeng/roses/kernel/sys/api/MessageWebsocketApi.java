package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageSendToSocketDTO;

import java.util.List;

/**
 * 调用websocket发送实时消息
 *
 * @author fengshuonan
 * @since 2024-01-15 18:55
 */
public interface MessageWebsocketApi {

    /**
     * 调用websocket发送实时消息
     *
     * @author fengshuonan
     * @since 2024-01-15 18:55
     */
    void wsSendMessage(List<MessageSendToSocketDTO> messageSendToSocketDTOList);

}
