package cn.stylefeng.roses.kernel.sys.modular.org.controller;

import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrgApprover;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrgApproverRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrgApproverService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 组织机构审批人控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@RestController
@ApiResource(name = "组织机构审批人")
public class HrOrgApproverController {

    @Resource
    private HrOrgApproverService hrOrgApproverService;

    /**
     * 获取组织机构审批人类型列表
     *
     * @author fengshuonan
     * @since 2022/9/26 10:44
     */
    @GetResource(name = "获取组织机构审批人类型列表", path = "/hrOrgApprover/getApproverTypeList", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.ASSIGN_APPROVER)
    public ResponseData<List<SimpleDict>> getApproverTypeList() {
        return new SuccessResponseData<>(hrOrgApproverService.getApproverTypeList());
    }

    /**
     * 获取组织机构审批人绑定列表
     *
     * @author fengshuonan
     * @since 2022/09/13 23:15
     */
    @GetResource(name = "获取组织机构审批人绑定列表", path = "/hrOrgApprover/getBindingList", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.ASSIGN_APPROVER)
    public ResponseData<List<HrOrgApprover>> getOrgApproverList(
            @Validated(HrOrgApproverRequest.list.class) HrOrgApproverRequest hrOrgApproverRequest) {
        return new SuccessResponseData<>(hrOrgApproverService.getOrgApproverList(hrOrgApproverRequest));
    }

    /**
     * 更新组织机构绑定审批人
     *
     * @author fengshuonan
     * @since 2022/09/13 23:15
     */
    @PostResource(name = "更新组织机构绑定审批人", path = "/hrOrgApprover/bindUserList", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.ASSIGN_APPROVER)
    public ResponseData<HrOrgApprover> bindUserList(
            @RequestBody @Validated(HrOrgApproverRequest.add.class) HrOrgApproverRequest hrOrgApproverRequest) {
        hrOrgApproverService.bindUserList(hrOrgApproverRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除审批人的绑定
     *
     * @author fengshuonan
     * @since 2022/09/13 23:15
     */
    @PostResource(name = "删除审批人的绑定", path = "/hrOrgApprover/delete", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.ASSIGN_APPROVER)
    public ResponseData<?> delete(@RequestBody @Validated(HrOrgApproverRequest.delete.class) HrOrgApproverRequest hrOrgApproverRequest) {
        hrOrgApproverService.del(hrOrgApproverRequest);
        return new SuccessResponseData<>();
    }

}
