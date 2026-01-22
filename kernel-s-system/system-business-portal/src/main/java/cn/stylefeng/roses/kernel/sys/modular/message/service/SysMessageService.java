package cn.stylefeng.roses.kernel.sys.modular.message.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.api.MessagePublishApi;
import cn.stylefeng.roses.kernel.sys.modular.message.entity.SysMessage;
import cn.stylefeng.roses.kernel.sys.modular.message.pojo.request.SysMessageRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统消息服务类
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
public interface SysMessageService extends IService<SysMessage>, MessagePublishApi {

    /**
     * 删除系统消息
     *
     * @param sysMessageRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    void del(SysMessageRequest sysMessageRequest);

    /**
     * 查询详情系统消息
     *
     * @param sysMessageRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    SysMessage detail(SysMessageRequest sysMessageRequest);

    /**
     * 获取系统消息分页列表
     *
     * @param sysMessageRequest 请求参数
     * @return PageResult<SysMessage>   返回结果
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    PageResult<SysMessage> findPage(SysMessageRequest sysMessageRequest);

    /**
     * 获取消息列表
     *
     * @author fengshuonan
     * @since 2024/3/21 0:37
     */
    List<SysMessage> findList(SysMessageRequest sysMessageRequest);

    /**
     * 清空我的消息
     *
     * @author fengshuonan
     * @since 2024/1/14 21:23
     */
    void deleteAllMyMessage();

    /**
     * 设置信息为已读
     *
     * @author fengshuonan
     * @since 2024/1/14 21:36
     */
    void setReadFlag(SysMessageRequest sysMessageRequest);

    /**
     * 将自己的消息全部设置为已读
     *
     * @author fengshuonan
     * @since 2024/1/14 21:38
     */
    void setReadTotalReadFlag();

}
