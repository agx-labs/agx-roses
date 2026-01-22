package cn.stylefeng.roses.kernel.log.business.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.constants.LogPermissionCodeConstants;
import cn.stylefeng.roses.kernel.log.api.pojo.business.SysLogBusinessRequest;
import cn.stylefeng.roses.kernel.log.api.pojo.entity.SysLogBusiness;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务日志记录控制器
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
@RestController
@ApiResource(name = "业务日志记录", requiredPermission = true, requirePermissionCode = LogPermissionCodeConstants.BUSINESS_LOG)
public class SysLogBusinessController {

    @Resource
    private SysLogBusinessService sysLogBusinessService;

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    @GetResource(name = "分页查询", path = "/sysLogBusiness/page")
    public ResponseData<PageResult<SysLogBusiness>> page(SysLogBusinessRequest sysLogBusinessRequest) {
        return new SuccessResponseData<>(sysLogBusinessService.findPage(sysLogBusinessRequest));
    }

}
