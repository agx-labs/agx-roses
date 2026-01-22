package cn.stylefeng.roses.kernel.sys.modular.notice.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.notice.entity.SysNotice;
import cn.stylefeng.roses.kernel.sys.modular.notice.pojo.request.SysNoticeRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 通知管理服务类
 *
 * @author fengshuonan
 * @since 2024/01/12 16:06
 */
public interface SysNoticeService extends IService<SysNotice> {

    /**
     * 新增通知管理
     *
     * @param sysNoticeRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    void add(SysNoticeRequest sysNoticeRequest);

    /**
     * 删除通知管理
     *
     * @param sysNoticeRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    void del(SysNoticeRequest sysNoticeRequest);

    /**
     * 批量删除通知管理
     *
     * @param sysNoticeRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    void batchDelete(SysNoticeRequest sysNoticeRequest);

    /**
     * 编辑通知管理
     *
     * @param sysNoticeRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    void edit(SysNoticeRequest sysNoticeRequest);

    /**
     * 查询详情通知管理
     *
     * @param sysNoticeRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    SysNotice detail(SysNoticeRequest sysNoticeRequest);

    /**
     * 获取通知管理列表
     *
     * @param sysNoticeRequest 请求参数
     * @return List<SysNotice>  返回结果
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    List<SysNotice> findList(SysNoticeRequest sysNoticeRequest);

    /**
     * 获取通知管理分页列表
     *
     * @param sysNoticeRequest 请求参数
     * @return PageResult<SysNotice>   返回结果
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    PageResult<SysNotice> findPage(SysNoticeRequest sysNoticeRequest);

    /**
     * 发送通知给接收人
     *
     * @author fengshuonan
     * @since 2024-01-12 17:15
     */
    void publishNotice(SysNoticeRequest sysNoticeRequest);

    /**
     * 撤回通知
     *
     * @author fengshuonan
     * @since 2024-01-12 17:16
     */
    void retractNotice(SysNoticeRequest sysNoticeRequest);

}
