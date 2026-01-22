package cn.stylefeng.roses.kernel.sys.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleTreeDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysRoleAssignV2Service;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 一套新的用户绑定角色的接口
 *
 * @author fengshuonan
 * @since 2025/1/24 13:55
 */
@RestController
@ApiResource(name = "用户绑定角色v2")
public class NewRoleAssignController {

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource
    private SysRoleAssignV2Service sysRoleAssignV2Service;

    /**
     * 获取用户的所有机构列表
     *
     * @author fengshuonan
     * @since 2025/1/24 14:11
     */
    @GetResource(name = "获取左侧机构列表", path = "/sysRoleAssign/v2/getUserOrgList")
    public ResponseData<List<UserOrgDTO>> getUserOrgList(@RequestParam("userId") Long userId) {
        List<UserOrgDTO> list = sysUserOrgService.getUserOrgList(userId, true);

        // 清空一些名称的返回
        for (UserOrgDTO userOrgDTO : list) {
            userOrgDTO.setCompanyId(null);
            userOrgDTO.setDeptId(null);
            userOrgDTO.setPositionId(null);
        }

        return new SuccessResponseData<>(list);
    }

    /**
     * 获取用户指定机构下的角色树（包含角色分类和角色）
     * <p>
     * 已经绑定的角色会checked
     *
     * @author fengshuonan
     * @since 2025/1/24 14:33
     */
    @GetResource(name = "获取用户指定机构下的业务角色树", path = "/sysRoleAssign/v2/getCompanyBusinessRoleTree")
    public ResponseData<List<SysRoleTreeDTO>> getCompanyBusinessRoleTree(@RequestParam("userId") Long userId, @RequestParam("orgId") Long orgId) {
        List<SysRoleTreeDTO> list = sysRoleAssignV2Service.getCompanyBusinessRoleTree(userId, orgId);
        return new SuccessResponseData<>(list);
    }

    /**
     * 获取用户指定机构公司角色树
     *
     * @author fengshuonan
     * @since 2025/1/24 15:40
     */
    @GetResource(name = "获取用户指定机构下的公司角色树", path = "/sysRoleAssign/v2/getCompanyRoleTree")
    public ResponseData<List<SysRoleTreeDTO>> getCompanyRoleTree(@RequestParam("userId") Long userId, @RequestParam("orgId") Long orgId) {
        List<SysRoleTreeDTO> list = sysRoleAssignV2Service.getCompanyRoleTree(userId, orgId);
        return new SuccessResponseData<>(list);
    }

}
