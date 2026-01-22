package cn.stylefeng.roses.kernel.sys.modular.org.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.OrganizationLevelRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.service.OrganizationLevelService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 组织机构层级控制器
 *
 * @author fengshuonan
 * @since 2025/01/22 09:44
 */
@RestController
@ApiResource(name = "组织机构层级")
public class OrganizationLevelController {

    @Resource
    private OrganizationLevelService organizationLevelService;

    /**
     * 获取所有组织机构层级列表
     *
     * @author fengshuonan
     * @since 2025/1/22 9:51
     */
    @GetResource(name = "获取机构层级列表", path = "/organizationLevel/list")
    public ResponseData<List<OrganizationLevel>> list(OrganizationLevelRequest organizationLevelRequest) {
        return new SuccessResponseData<>(organizationLevelService.findList(organizationLevelRequest));
    }

    /**
     * 添加组织机构层级
     *
     * @author fengshuonan
     * @since 2025/01/22 09:44
     */
    @PostResource(name = "添加组织机构层级", path = "/organizationLevel/updateTotal")
    public ResponseData<?> add(@RequestBody @Validated(OrganizationLevelRequest.edit.class) OrganizationLevelRequest organizationLevelRequest) {
        organizationLevelService.updateTotal(organizationLevelRequest);
        return new SuccessResponseData<>();
    }

}
