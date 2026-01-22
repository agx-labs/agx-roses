package cn.stylefeng.roses.kernel.sys.modular.notice.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.notice.entity.SysNotice;
import cn.stylefeng.roses.kernel.sys.modular.notice.pojo.request.SysNoticeRequest;
import cn.stylefeng.roses.kernel.sys.modular.notice.service.SysNoticeService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通知管理控制器
 *
 * @author fengshuonan
 * @since 2024/01/12 16:06
 */
@RestController
@ApiResource(name = "通知管理")
public class SysNoticeController {

    @Resource
    private SysNoticeService sysNoticeService;

    /**
     * 添加通知管理
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    @PostResource(name = "添加通知管理", path = "/sysNotice/add")
    public ResponseData<SysNotice> add(@RequestBody @Validated(SysNoticeRequest.add.class) SysNoticeRequest sysNoticeRequest) {
        sysNoticeService.add(sysNoticeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除通知管理
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    @PostResource(name = "删除通知管理", path = "/sysNotice/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysNoticeRequest.delete.class) SysNoticeRequest sysNoticeRequest) {
        sysNoticeService.del(sysNoticeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量删除通知管理
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    @PostResource(name = "批量删除通知管理", path = "/sysNotice/batchDelete")
    public ResponseData<?> batchDelete(@RequestBody @Validated(BaseRequest.batchDelete.class) SysNoticeRequest sysNoticeRequest) {
        sysNoticeService.batchDelete(sysNoticeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑通知管理
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    @PostResource(name = "编辑通知管理", path = "/sysNotice/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysNoticeRequest.edit.class) SysNoticeRequest sysNoticeRequest) {
        sysNoticeService.edit(sysNoticeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看通知管理详情
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    @GetResource(name = "查看通知管理详情", path = "/sysNotice/detail")
    public ResponseData<SysNotice> detail(@Validated(SysNoticeRequest.detail.class) SysNoticeRequest sysNoticeRequest) {
        return new SuccessResponseData<>(sysNoticeService.detail(sysNoticeRequest));
    }

    /**
     * 获取通知管理列表（带分页）
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    @GetResource(name = "获取通知管理列表（带分页）", path = "/sysNotice/page")
    public ResponseData<PageResult<SysNotice>> page(SysNoticeRequest sysNoticeRequest) {
        return new SuccessResponseData<>(sysNoticeService.findPage(sysNoticeRequest));
    }

    /**
     * 发送通知
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    @PostResource(name = "发送通知", path = "/sysNotice/publishNotice")
    public ResponseData<?> publishNotice(@RequestBody @Validated(BaseRequest.detail.class) SysNoticeRequest sysNoticeRequest) {
        this.sysNoticeService.publishNotice(sysNoticeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 撤回通知
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    @PostResource(name = "撤回通知", path = "/sysNotice/retractNotice")
    public ResponseData<?> retractNotice(@RequestBody @Validated(BaseRequest.detail.class) SysNoticeRequest sysNoticeRequest) {
        this.sysNoticeService.retractNotice(sysNoticeRequest);
        return new SuccessResponseData<>();
    }

}
