package cn.stylefeng.roses.kernel.log.security.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.pojo.security.LogSecurityRequest;
import cn.stylefeng.roses.kernel.log.security.entity.LogSecurity;
import cn.stylefeng.roses.kernel.log.security.service.LogSecurityService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import static cn.stylefeng.roses.kernel.log.api.constants.LogPermissionCodeConstants.SECURITY_LOG;

/**
 * 安全日志控制器
 *
 * @author fengshuonan
 * @since 2024/07/11 15:56
 */
@RestController
@ApiResource(name = "安全日志", requiredPermission = true, requirePermissionCode = SECURITY_LOG)
public class LogSecurityController {

    @Resource
    private LogSecurityService logSecurityService;

    /**
     * 获取安全日志列表（带分页）
     *
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    @GetResource(name = "获取安全日志列表（带分页）", path = "/logSecurity/page")
    public ResponseData<PageResult<LogSecurity>> page(LogSecurityRequest logSecurityRequest) {
        return new SuccessResponseData<>(logSecurityService.findPage(logSecurityRequest));
    }

}
