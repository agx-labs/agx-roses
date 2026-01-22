package cn.stylefeng.roses.kernel.sys.modular.message.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.MessageWebsocketApi;
import cn.stylefeng.roses.kernel.sys.api.enums.message.ReadFlagEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageRetractDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageSendDTO;
import cn.stylefeng.roses.kernel.sys.modular.message.entity.SysMessage;
import cn.stylefeng.roses.kernel.sys.modular.message.enums.SysMessageExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.message.factory.MessageFactory;
import cn.stylefeng.roses.kernel.sys.modular.message.mapper.SysMessageMapper;
import cn.stylefeng.roses.kernel.sys.modular.message.pojo.request.SysMessageRequest;
import cn.stylefeng.roses.kernel.sys.modular.message.service.SysMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统消息业务实现层
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {

    @Override
    public void del(SysMessageRequest sysMessageRequest) {
        // 只能清空自己的消息
        LambdaUpdateWrapper<SysMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysMessage::getReceiveUserId, LoginContext.me().getLoginUser().getUserId());
        wrapper.eq(SysMessage::getMessageId, sysMessageRequest.getMessageId());
        this.remove(wrapper);
    }

    @Override
    public SysMessage detail(SysMessageRequest sysMessageRequest) {

        LambdaQueryWrapper<SysMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMessage::getReceiveUserId, LoginContext.me().getLoginUser().getUserId());
        wrapper.eq(SysMessage::getMessageId, sysMessageRequest.getMessageId());

        // 查询关键信息
        wrapper.select(SysMessage::getMessageId, SysMessage::getMessageTitle, SysMessage::getMessageContent, SysMessage::getPriorityLevel,
                SysMessage::getMessageSendTime,
                SysMessage::getMessageType, SysMessage::getMessageUrl, SysMessage::getBusinessType, SysMessage::getBusinessId);

        return this.querySysMessage(sysMessageRequest);
    }

    @Override
    public PageResult<SysMessage> findPage(SysMessageRequest sysMessageRequest) {
        LambdaQueryWrapper<SysMessage> wrapper = createWrapper(sysMessageRequest);

        // 查询关键字段
        wrapper.select(SysMessage::getMessageId, SysMessage::getMessageTitle, SysMessage::getPriorityLevel, SysMessage::getReadFlag,
                SysMessage::getMessageSendTime,
                SysMessage::getMessageType, SysMessage::getMessageUrl, SysMessage::getBusinessType, SysMessage::getBusinessId, SysMessage::getBusinessDetail, SysMessage::getMessageContent);

        Page<SysMessage> pageList = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(pageList);
    }

    @Override
    public List<SysMessage> findList(SysMessageRequest sysMessageRequest) {
        LambdaQueryWrapper<SysMessage> wrapper = createWrapper(sysMessageRequest);

        // 查询关键字段
        wrapper.select(SysMessage::getMessageId, SysMessage::getMessageTitle, SysMessage::getPriorityLevel, SysMessage::getReadFlag,
                SysMessage::getMessageSendTime,
                SysMessage::getMessageType, SysMessage::getMessageUrl, SysMessage::getBusinessType, SysMessage::getBusinessId, SysMessage::getBusinessDetail, SysMessage::getMessageContent);

        return this.list(wrapper);
    }

    @Override
    public void deleteAllMyMessage() {
        // 只能清空自己的消息
        LambdaUpdateWrapper<SysMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysMessage::getReceiveUserId, LoginContext.me().getLoginUser().getUserId());
        this.remove(wrapper);
    }

    @Override
    public void setReadFlag(SysMessageRequest sysMessageRequest) {
        // 只能设置自己的消息
        LambdaUpdateWrapper<SysMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysMessage::getReceiveUserId, LoginContext.me().getLoginUser().getUserId());
        wrapper.eq(SysMessage::getMessageId, sysMessageRequest.getMessageId());
        wrapper.set(SysMessage::getReadFlag, ReadFlagEnum.HAVE_READ.getCode());
        this.update(wrapper);
    }

    @Override
    public void setReadTotalReadFlag() {
        // 只能设置自己的消息
        LambdaUpdateWrapper<SysMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysMessage::getReceiveUserId, LoginContext.me().getLoginUser().getUserId());
        wrapper.set(SysMessage::getReadFlag, ReadFlagEnum.HAVE_READ.getCode());
        this.update(wrapper);
    }

    @Override
    public void batchSendMessage(MessageSendDTO messageSendDTO) {
        // 获取发送到指定哪些用户上，批量创建消息
        List<SysMessage> batchMessage = MessageFactory.createBatchMessage(messageSendDTO);
        if (ObjectUtil.isNotEmpty(batchMessage)) {
            this.getBaseMapper().insertBatchSomeColumn(batchMessage);
        }

        try {
            MessageWebsocketApi messageWebsocketApi = SpringUtil.getBean(MessageWebsocketApi.class);
            messageWebsocketApi.wsSendMessage(MessageFactory.createSocketMessage(batchMessage));
        } catch (Exception e) {
            log.error("【websocket服务】发送消息失败！", e);
        }

    }

    @Override
    public void batchRetractMessage(MessageRetractDTO messageRetractDTO) {
        LambdaUpdateWrapper<SysMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysMessage::getBusinessType, messageRetractDTO.getBusinessType());
        wrapper.eq(SysMessage::getBusinessId, messageRetractDTO.getBusinessId());

        // 根据选中的人进行撤回
        if (ObjectUtil.isNotEmpty(messageRetractDTO.getUserIdList())) {
            wrapper.in(SysMessage::getReceiveUserId, messageRetractDTO.getUserIdList());
        }

        this.remove(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    private SysMessage querySysMessage(SysMessageRequest sysMessageRequest) {
        SysMessage sysMessage = this.getById(sysMessageRequest.getMessageId());
        if (ObjectUtil.isEmpty(sysMessage)) {
            throw new ServiceException(SysMessageExceptionEnum.SYS_MESSAGE_NOT_EXISTED);
        }
        return sysMessage;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    private LambdaQueryWrapper<SysMessage> createWrapper(SysMessageRequest sysMessageRequest) {
        LambdaQueryWrapper<SysMessage> queryWrapper = new LambdaQueryWrapper<>();

        // 根据标题查询
        String searchText = sysMessageRequest.getSearchText();
        if (StrUtil.isNotBlank(searchText)) {
            queryWrapper.like(SysMessage::getMessageTitle, searchText);
        }

        // 根据优先级查询
        String priorityLevel = sysMessageRequest.getPriorityLevel();
        if (ObjectUtil.isNotEmpty(priorityLevel)) {
            queryWrapper.eq(SysMessage::getPriorityLevel, priorityLevel);
        }

        // 根据已读状态
        Integer readFlag = sysMessageRequest.getReadFlag();
        if (ObjectUtil.isNotEmpty(readFlag)) {
            queryWrapper.eq(SysMessage::getReadFlag, readFlag);
        }

        // 只查询自己的
        queryWrapper.eq(SysMessage::getReceiveUserId, LoginContext.me().getLoginUser().getUserId());

        // 未读的放在最上边
        queryWrapper.orderByAsc(SysMessage::getReadFlag);
        queryWrapper.orderByDesc(SysMessage::getMessageSendTime);

        return queryWrapper;
    }

}
