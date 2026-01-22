package cn.stylefeng.roses.kernel.log.business.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.constants.LogPermissionCodeConstants;
import cn.stylefeng.roses.kernel.log.api.pojo.business.SysLogBusinessContentRequest;
import cn.stylefeng.roses.kernel.log.api.pojo.entity.SysLogBusinessContent;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessContentService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务日志详情记录控制器
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
@RestController
@ApiResource(name = "业务日志详情记录控制器", requiredPermission = true, requirePermissionCode = LogPermissionCodeConstants.BUSINESS_LOG)
public class SysLogBusinessContentController {

    @Resource
    private SysLogBusinessContentService sysLogBusinessContentService;

    /**
     * 分页获取业务日志详情
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    @GetResource(name = "分页获取业务日志详情", path = "/sysLogBusinessContent/page")
    public ResponseData<PageResult<SysLogBusinessContent>> page(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        return new SuccessResponseData<>(sysLogBusinessContentService.findPage(sysLogBusinessContentRequest));
    }

}
