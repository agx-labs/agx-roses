package cn.stylefeng.roses.kernel.sys.modular.message.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.message.entity.SysMessage;
import cn.stylefeng.roses.kernel.sys.modular.message.pojo.request.SysMessageRequest;
import cn.stylefeng.roses.kernel.sys.modular.message.service.SysMessageService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人消息控制器
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
@RestController
@ApiResource(name = "个人消息")
public class SysMessageController {

    @Resource
    private SysMessageService sysMessageService;

    /**
     * 获取个人消息列表（带分页）
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @GetResource(name = "获取个人消息列表（带分页）", path = "/sysMessage/page")
    public ResponseData<PageResult<SysMessage>> page(SysMessageRequest sysMessageRequest) {
        return new SuccessResponseData<>(sysMessageService.findPage(sysMessageRequest));
    }

    /**
     * 删除个人消息
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @PostResource(name = "删除个人消息", path = "/sysMessage/delete")
    public ResponseData<?> delete(@RequestBody @Validated(BaseRequest.delete.class) SysMessageRequest sysMessageRequest) {
        sysMessageService.del(sysMessageRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 清空我的消息
     *
     * @author fengshuonan
     * @since 2024-01-12 18:21
     */
    @PostResource(name = "清空我的消息", path = "/sysMessage/cleanMyMessage")
    public ResponseData<?> cleanMyMessage() {
        sysMessageService.deleteAllMyMessage();
        return new SuccessResponseData<>();
    }

    /**
     * 设为已读消息
     *
     * @author fengshuonan
     * @since 2024-01-12 18:12
     */
    @PostResource(name = "设为已读消息", path = "/sysMessage/setRead")
    public ResponseData<?> setRead(@RequestBody @Validated(BaseRequest.detail.class) SysMessageRequest sysMessageRequest) {
        sysMessageService.setReadFlag(sysMessageRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 全部已读
     *
     * @author fengshuonan
     * @since 2024-01-12 18:12
     */
    @PostResource(name = "全部已读", path = "/sysMessage/setTotalRead")
    public ResponseData<?> setTotalRead() {
        this.sysMessageService.setReadTotalReadFlag();
        return new SuccessResponseData<>();
    }

    /**
     * 查看个人消息详情
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @GetResource(name = "查看个人消息详情", path = "/sysMessage/detail")
    public ResponseData<SysMessage> detail(@Validated(SysMessageRequest.detail.class) SysMessageRequest sysMessageRequest) {
        return new SuccessResponseData<>(sysMessageService.detail(sysMessageRequest));
    }

}
