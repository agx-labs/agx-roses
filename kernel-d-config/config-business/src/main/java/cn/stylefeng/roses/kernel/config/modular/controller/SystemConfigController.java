package cn.stylefeng.roses.kernel.config.modular.controller;

import cn.stylefeng.roses.kernel.config.api.pojo.ConfigInitRequest;
import cn.stylefeng.roses.kernel.config.modular.pojo.InitConfigResponse;
import cn.stylefeng.roses.kernel.config.modular.service.SysConfigService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端框架对系统配置的调用接口
 *
 * @author fengshuonan
 * @since 2023/6/27 21:33
 */
@RestController
@ApiResource(name = "前端框架对系统配置的调用接口")
public class SystemConfigController {

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 获取系统配置是否初始化的标志
     *
     * @author fengshuonan
     * @since 2021/7/8 17:20
     */
    @GetResource(name = "获取系统配置是否初始化的标志", path = "/sysConfig/getInitConfigFlag")
    public ResponseData<Boolean> getInitConfigFlag() {
        return new SuccessResponseData<>(sysConfigService.getInitConfigFlag());
    }

    /**
     * 初始化系统配置参数，用在系统第一次登录时
     *
     * @author fengshuonan
     * @since 2021/7/8 16:36
     */
    @PostResource(name = "初始化系统配置参数，用在系统第一次登录时", path = "/sysConfig/initConfig", requiredPermission = true,
            requirePermissionCode = SysConfigTypeController.SYS_CONFIG)
    public ResponseData<?> initConfig(@RequestBody ConfigInitRequest configInitRequest) {
        sysConfigService.initConfig(configInitRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 获取需要初始化的配置列表
     *
     * @author fengshuonan
     * @since 2021/7/8 16:36
     */
    @GetResource(name = "获取需要初始化的配置列表", path = "/sysConfig/getInitConfigList")
    public ResponseData<InitConfigResponse> getInitConfigList() {
        return new SuccessResponseData<>(sysConfigService.getInitConfigs());
    }

}
