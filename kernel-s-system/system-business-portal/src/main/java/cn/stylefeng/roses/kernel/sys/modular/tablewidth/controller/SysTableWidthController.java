package cn.stylefeng.roses.kernel.sys.modular.tablewidth.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.entity.SysTableWidth;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.pojo.request.SysTableWidthRequest;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.service.SysTableWidthService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用表格列控制配置接口
 *
 * @author fengshuonan
 * @since 2023/6/27 23:19
 */
@RestController
@ApiResource(name = "通用表格列控制配置接口")
public class SysTableWidthController {

    @Resource
    private SysTableWidthService sysTableWidthService;

    /**
     * 获取用户针对某个业务的table的列宽配置
     *
     * @author fengshuonan
     * @since 2023/6/27 23:19
     */
    @GetResource(name = "获取用户针对某个业务的table的列宽配置", path = "/sysTableWidth/getUserConfig")
    public ResponseData<SysTableWidth> getUserConfig(
            @Validated(SysTableWidthRequest.detail.class) SysTableWidthRequest sysTableWidthRequest) {
        return new SuccessResponseData<>(sysTableWidthService.detail(sysTableWidthRequest));
    }

    /**
     * 添加用户针对某个table的列属性配置
     *
     * @author fengshuonan
     * @since 2023/6/27 23:23
     */
    @PostResource(name = "添加用户针对某个table的列属性配置", path = "/sysTableWidth/setTableWidth")
    public ResponseData<SysTableWidth> setTableWidth(
            @RequestBody @Validated(SysTableWidthRequest.add.class) SysTableWidthRequest sysTableWidthRequest) {
        sysTableWidthService.setTableWidth(sysTableWidthRequest);
        return new SuccessResponseData<>();
    }

}
