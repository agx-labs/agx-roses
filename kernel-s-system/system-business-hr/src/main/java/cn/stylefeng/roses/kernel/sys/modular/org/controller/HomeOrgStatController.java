package cn.stylefeng.roses.kernel.sys.modular.org.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.HomeCompanyInfo;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取组织机构统计信息
 *
 * @author fengshuonan
 * @since 2023/6/26 22:51
 */
@RestController
@ApiResource(name = "获取组织机构统计信息")
public class HomeOrgStatController {

    @Resource
    private HrOrganizationService hrOrganizationService;

    /**
     * 获取组织机构统计信息，包含系统的统计，包含当前用户公司的统计
     * <p>
     * 一般用在首页展示组织机构的统计信息界面
     *
     * @author fengshuonan
     * @since 2023/6/26 22:51
     */
    @GetResource(name = "获取组织机构统计信息", path = "/org/statInfo", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.COMPANY_STAT_INFO)
    public ResponseData<HomeCompanyInfo> orgStatInfo() {
        return new SuccessResponseData<>(hrOrganizationService.orgStatInfo());
    }

}
