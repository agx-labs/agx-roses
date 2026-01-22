package cn.stylefeng.roses.kernel.sys.modular.message.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.api.enums.message.ReadFlagEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageSendDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageSendToSocketDTO;
import cn.stylefeng.roses.kernel.sys.modular.message.entity.SysMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 创建我的消息
 *
 * @author fengshuonan
 * @since 2024/1/14 22:24
 */
public class MessageFactory {

    /**
     * 批量创建个人消息
     *
     * @author fengshuonan
     * @since 2024/1/14 22:24
     */
    public static List<SysMessage> createBatchMessage(MessageSendDTO messageSendDTO) {

        List<SysMessage> sysMessages = new ArrayList<>();

        if (ObjectUtil.isEmpty(messageSendDTO)) {
            return sysMessages;
        }

        Set<Long> userIdList = messageSendDTO.getUserIdList();
        if (ObjectUtil.isEmpty(userIdList)) {
            return sysMessages;
        }

        for (Long userId : userIdList) {
            SysMessage sysMessage = new SysMessage();
            sysMessage.setSendUserId(messageSendDTO.getSendUserId());
            sysMessage.setReceiveUserId(userId);
            sysMessage.setMessageTitle(messageSendDTO.getMessageTitle());
            sysMessage.setMessageContent(messageSendDTO.getMessageContent());
            sysMessage.setMessageType(messageSendDTO.getMessageType());
            sysMessage.setMessageUrl(messageSendDTO.getMessageUrl());
            sysMessage.setPriorityLevel(messageSendDTO.getPriorityLevel());
            sysMessage.setBusinessId(messageSendDTO.getBusinessId());
            sysMessage.setBusinessType(messageSendDTO.getBusinessType());
            sysMessage.setReadFlag(ReadFlagEnum.NO_READ.getCode());
            sysMessage.setMessageSendTime(new Date());
            sysMessage.setBusinessDetail(messageSendDTO.getBusinessDetail());
            sysMessages.add(sysMessage);
        }

        return sysMessages;
    }

    /**
     * 创建消息发送到socket时候的数据传输bean
     *
     * @author fengshuonan
     * @since 2024-01-16 11:17
     */
    public static List<MessageSendToSocketDTO> createSocketMessage(List<SysMessage> sysMessageList) {

        List<MessageSendToSocketDTO> messageSendToSocketDTOS = new ArrayList<>();

        if (ObjectUtil.isEmpty(sysMessageList)) {
            return messageSendToSocketDTOS;
        }

        for (SysMessage sysMessage : sysMessageList) {
            MessageSendToSocketDTO dto = new MessageSendToSocketDTO();
            dto.setReceiveUserId(sysMessage.getReceiveUserId());
            dto.setMessageId(sysMessage.getMessageId());
            dto.setSendUserId(sysMessage.getSendUserId());
            dto.setMessageTitle(sysMessage.getMessageTitle());
            dto.setMessageContent(sysMessage.getMessageContent());
            dto.setMessageType(sysMessage.getMessageType());
            dto.setMessageUrl(sysMessage.getMessageUrl());
            dto.setPriorityLevel(sysMessage.getPriorityLevel());
            dto.setBusinessId(sysMessage.getBusinessId());
            dto.setBusinessType(sysMessage.getBusinessType());
            dto.setMessageSendTime(sysMessage.getMessageSendTime());
            dto.setBusinessDetail(sysMessage.getBusinessDetail());
            messageSendToSocketDTOS.add(dto);
        }

        return messageSendToSocketDTOS;
    }

}
