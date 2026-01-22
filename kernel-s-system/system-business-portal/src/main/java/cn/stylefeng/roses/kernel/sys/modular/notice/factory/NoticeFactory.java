package cn.stylefeng.roses.kernel.sys.modular.notice.factory;

import cn.stylefeng.roses.kernel.sys.api.enums.message.MessageBusinessTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.enums.message.MessageTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageSendDTO;
import cn.stylefeng.roses.kernel.sys.modular.notice.entity.SysNotice;

import java.util.Set;

/**
 * 通知的创建工厂
 *
 * @author fengshuonan
 * @since 2024/1/14 22:47
 */
public class NoticeFactory {

    /**
     * 创建消息发送的参数封装
     *
     * @author fengshuonan
     * @since 2024/1/14 22:47
     */
    public static MessageSendDTO createMessageSendDTO(Set<Long> userIdList, SysNotice noticeInfo) {
        MessageSendDTO messageSendDTO = new MessageSendDTO();

        messageSendDTO.setUserIdList(userIdList);
        messageSendDTO.setSendUserId(noticeInfo.getCreateUser());
        messageSendDTO.setMessageTitle(noticeInfo.getNoticeTitle());
        messageSendDTO.setMessageContent(noticeInfo.getNoticeContent());
        messageSendDTO.setMessageType(MessageTypeEnum.NORMAL.getCode());

        // 优先级
        messageSendDTO.setPriorityLevel(noticeInfo.getPriorityLevel());

        // 业务类型：系统通知
        messageSendDTO.setBusinessType(MessageBusinessTypeEnum.SYS_NOTICE.getCode());
        messageSendDTO.setBusinessId(String.valueOf(noticeInfo.getNoticeId()));

        return messageSendDTO;
    }

}
