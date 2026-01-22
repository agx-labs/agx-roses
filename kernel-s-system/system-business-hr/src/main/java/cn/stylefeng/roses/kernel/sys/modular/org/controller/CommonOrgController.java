package cn.stylefeng.roses.kernel.sys.modular.org.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.CommonOrgTreeRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.CommonOrgTreeResponse;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用组织机构接口
 *
 * @author fengshuonan
 * @since 2023/6/11 10:04
 */
@RestController
@ApiResource(name = "通用组织机构接口")
public class CommonOrgController {

    @Resource
    private HrOrganizationService hrOrganizationService;

    /**
     * 通用获取组织机构树
     * <p>
     * ps：用在获取用户管理和组织机构管理界面左侧树
     * <p>
     * 如果传递了orgParentId，则为懒加载，传递-1，获取所有一级节点
     *
     * @author fengshuonan
     * @since 2023/6/11 10:31
     */
    @PostResource(name = "通用获取组织机构树", path = "/common/org/tree")
    public ResponseData<CommonOrgTreeResponse> commonOrgTree(@RequestBody CommonOrgTreeRequest commonOrgTreeRequest) {
        return new SuccessResponseData<>(hrOrganizationService.commonOrgTree(commonOrgTreeRequest));
    }

    /**
     * 分页获取组织机构信息（用在通用选择机构组件中）
     *
     * @author fengshuonan
     * @since 2023/6/29 9:24
     */
    @GetResource(name = "分页获取组织机构信息（用在通用选择机构组件中）", path = "/common/org/pageList")
    public ResponseData<PageResult<HrOrganization>> commonOrgPage(HrOrganizationRequest hrOrganizationRequest) {
        return new SuccessResponseData<>(hrOrganizationService.commonOrgPage(hrOrganizationRequest));
    }

    /**
     * 获取机构名称集合，通过机构id的列表
     *
     * @author fengshuonan
     * @since 2023/11/2 10:16
     */
    @PostResource(name = "获取机构名称集合，通过机构id的列表", path = "/common/org/getOrgListName")
    public ResponseData<List<SimpleDict>> getOrgListName(
            @RequestBody @Validated(BaseRequest.batchDelete.class) HrOrganizationRequest hrOrganizationRequest) {
        return new SuccessResponseData<>(hrOrganizationService.getOrgListName(hrOrganizationRequest));
    }

}
