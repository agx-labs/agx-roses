package cn.stylefeng.roses.kernel.sys.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.annotation.BizLog;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.SysBizLogConstants;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindResponse;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.DeleteRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.RoleControlRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.StatusControlRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.SyncBindRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserOrgRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysRoleAssignService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户授权界面接口
 *
 * @author fengshuonan
 * @since 2024/1/17 23:01
 */
@RestController
@ApiResource(name = "用户授权界面接口")
public class UserRoleAssignPageController {

    @Resource
    private SysRoleAssignService sysRoleAssignService;

    @Resource
    private SysUserOrgService sysUserOrgService;

    /**
     * 获取用户的角色授权信息列表
     *
     * @author fengshuonan
     * @since 2024/1/17 23:57
     */
    @GetResource(name = "获取用户的角色授权信息列表", path = "/sysRoleAssign/getUserAssignList")
    public ResponseData<List<NewUserRoleBindResponse>> getUserAssignList(@RequestParam("userId") Long userId) {
        List<NewUserRoleBindResponse> list = sysRoleAssignService.getUserAssignList(userId);
        return new SuccessResponseData<>(list);
    }

    /**
     * 修改用户针对某个公司的是否启用状态
     *
     * @author fengshuonan
     * @since 2024-01-18 9:31
     */
    @PostResource(name = "修改用户针对某个公司的是否启用状态", path = "/sysRoleAssign/changeStatus")
    public ResponseData<?> changeStatus(@RequestBody @Validated StatusControlRequest statusControlRequest) {
        sysRoleAssignService.changeUserBindStatus(statusControlRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 绑定或取消绑定角色
     *
     * @author fengshuonan
     * @since 2024-01-18 9:31
     */
    @PostResource(name = "绑定或取消绑定角色", path = "/sysRoleAssign/changeRoleSelect")
    @BizLog(logTypeCode = SysBizLogConstants.BIND_COMPANY_ROLE_BIZ_ROLE)
    public ResponseData<?> changeRoleSelect(@RequestBody @Validated RoleControlRequest roleControlRequest) {
        sysRoleAssignService.changeRoleSelect(roleControlRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除用户机构的绑定
     *
     * @author fengshuonan
     * @since 2024-01-18 13:38
     */
    @PostResource(name = "删除用户机构的绑定", path = "/sysRoleAssign/removeUserOrgBind")
    public ResponseData<?> removeUserOrgBind(@RequestBody @Validated DeleteRequest deleteRequest) {
        sysRoleAssignService.removeUserOrgBind(deleteRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 添加用户机构的绑定
     *
     * @author fengshuonan
     * @since 2024-01-18 14:50
     */
    @PostResource(name = "添加用户机构的绑定", path = "/sysRoleAssign/addUserOrgBind")
    public ResponseData<?> addUserOrgBind(@RequestBody @Validated(BaseRequest.add.class) SysUserOrgRequest sysUserOrgRequest) {
        sysUserOrgService.add(sysUserOrgRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除全部机构绑定
     *
     * @author fengshuonan
     * @since 2024-01-18 15:47
     */
    @PostResource(name = "删除全部机构绑定", path = "/sysRoleAssign/deleteAllOrgBind")
    public ResponseData<?> deleteAllOrgBind(@RequestBody @Validated(BaseRequest.delete.class) SysUserOrgRequest sysUserOrgRequest) {
        sysRoleAssignService.clearAllOrgAndRoleBind(sysUserOrgRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 禁用全部组织机构
     *
     * @author fengshuonan
     * @since 2024-01-18 15:47
     */
    @PostResource(name = "禁用全部组织机构", path = "/sysRoleAssign/disableAllOrg")
    public ResponseData<?> disableAllOrg(@RequestBody @Validated(BaseRequest.delete.class) SysUserOrgRequest sysUserOrgRequest) {
        sysRoleAssignService.disableAllOrgStatus(sysUserOrgRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 同步到其他公司绑定信息
     *
     * @author fengshuonan
     * @since 2024-01-18 15:47
     */
    @PostResource(name = "同步到其他公司绑定信息", path = "/sysRoleAssign/syncOtherOrgStatusAndBusinessRole")
    public ResponseData<?> syncOtherOrgStatusAndBusinessRole(@RequestBody @Validated SyncBindRequest syncBindRequest) {
        sysRoleAssignService.syncOtherOrgStatusAndBusinessRole(syncBindRequest);
        return new SuccessResponseData<>();
    }

}
