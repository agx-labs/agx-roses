package cn.stylefeng.roses.kernel.sys.modular.resource.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.ResourceReportApi;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.scanner.api.constants.ScannerConstants;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.ReportResourceParam;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.SysResourcePersistencePojo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 接收微服务远程资源
 *
 * @author fengshuonan
 * @since 2023/8/9 0:30
 */
@RestController
@ApiResource(name = "接收微服务远程资源", requiredPermission = true,
        requirePermissionCode = ResourceReceiveController.RES_REQUEST_PERMISSION_CODE)
public class ResourceReceiveController {

    /**
     * 接口访问权限标识
     */
    public static final String RES_REQUEST_PERMISSION_CODE = "SYS_CONFIG";

    @Resource
    private ResourceReportApi resourceReportApi;

    /**
     * 接受资源
     *
     * @author fengshuonan
     * @date 2021/8/26 17:28
     */
    @PostResource(name = "接受资源", path = ScannerConstants.REPORT_RES_URL)
    public ResponseData<?> reportResources(@RequestBody ReportResourceParam reportResourceReq) {
        resourceReportApi.reportResources(reportResourceReq);
        return new SuccessResponseData<>();
    }

    /**
     * 接受资源并获取返回的结果
     *
     * @author fengshuonan
     * @since 2023/8/9 20:38
     */
    @PostResource(name = "接受资源并获取返回的结果", path = ScannerConstants.REPORT_RES_AND_GET_RESULT_URL)
    public List<SysResourcePersistencePojo> reportResourcesAndGetResult(@RequestBody ReportResourceParam reportResourceReq) {
        return resourceReportApi.reportResourcesAndGetResult(reportResourceReq);
    }

}