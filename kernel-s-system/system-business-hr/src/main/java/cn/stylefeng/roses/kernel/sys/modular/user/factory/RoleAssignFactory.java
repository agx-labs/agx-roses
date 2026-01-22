package cn.stylefeng.roses.kernel.sys.modular.user.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.sys.api.enums.role.RoleTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindItem;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindResponse;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.UserRoleDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新用户角色绑定的工厂
 *
 * @author fengshuonan
 * @since 2024/1/17 23:16
 */
public class RoleAssignFactory {

    /**
     * 通过用户绑定的机构，创建用户角色绑定的基础数据
     *
     * @author fengshuonan
     * @since 2024/1/17 23:16
     */
    public static List<NewUserRoleBindResponse> createBaseResponse(List<UserOrgDTO> userOrgDTOList) {
        List<NewUserRoleBindResponse> baseResponse = new ArrayList<>();

        if (ObjectUtil.isEmpty(userOrgDTOList)) {
            return baseResponse;
        }

        for (UserOrgDTO userOrgDTO : userOrgDTOList) {
            NewUserRoleBindResponse newUserRoleBindResponse = new NewUserRoleBindResponse();

            Long orgId = userOrgDTO.getDeptId();
            if (orgId == null) {
                orgId = userOrgDTO.getCompanyId();
            }

            // 设置机构id
            newUserRoleBindResponse.setOrgId(orgId);

            // 设置机构名称
            String orgInfoName = userOrgDTO.getCompanyName()
                    + "/"
                    + (userOrgDTO.getDeptName() == null ? "" : userOrgDTO.getDeptName() + "/")
                    + userOrgDTO.getPositionName();
            newUserRoleBindResponse.setOrgName(orgInfoName);

            // 设置是否启用这个机构
            newUserRoleBindResponse.setEnableThisOrg(StatusEnum.ENABLE.getCode().equals(userOrgDTO.getStatusFlag()));

            // 设置公司id（后台组装数据用，前端不用这个字段）
            newUserRoleBindResponse.setCompanyId(userOrgDTO.getCompanyId());

            baseResponse.add(newUserRoleBindResponse);
        }

        return baseResponse;
    }

    /**
     * 初始化每个机构下的角色下拉列表
     *
     * @author fengshuonan
     * @since 2024/1/17 23:33
     */
    public static void fillRoleSelectList(List<NewUserRoleBindResponse> baseResponse, List<SysRoleDTO> businessRoleAndCompanyRole) {

        List<SysRoleDTO> businessRoleList = businessRoleAndCompanyRole.stream().filter(i -> i.getRoleType().equals(RoleTypeEnum.BUSINESS_ROLE.getCode()))
                .collect(Collectors.toList());

        // 1. 首先每个结构下边先添加一遍业务角色，业务角色各个机构下都一样
        for (NewUserRoleBindResponse item : baseResponse) {

            List<NewUserRoleBindItem> roleBindItemList = item.getRoleBindItemList();
            if (ObjectUtil.isEmpty(roleBindItemList)) {
                roleBindItemList = new ArrayList<>();
                item.setRoleBindItemList(roleBindItemList);
            }

            for (SysRoleDTO businessRole : businessRoleList) {
                NewUserRoleBindItem newUserRoleBindItem = new NewUserRoleBindItem();
                newUserRoleBindItem.setRoleId(businessRole.getRoleId());
                newUserRoleBindItem.setRoleName(businessRole.getRoleName());
                newUserRoleBindItem.setRoleType(businessRole.getRoleType());
                newUserRoleBindItem.setCheckedFlag(false);
                roleBindItemList.add(newUserRoleBindItem);
            }
        }

        // 2. 然后再添加公司角色，公司角色每个机构下不一样
        List<SysRoleDTO> companyRoleList = businessRoleAndCompanyRole.stream().filter(i -> i.getRoleType().equals(RoleTypeEnum.COMPANY_ROLE.getCode()))
                .collect(Collectors.toList());

        for (SysRoleDTO companyRole : companyRoleList) {
            for (NewUserRoleBindResponse item : baseResponse) {
                if (!companyRole.getRoleCompanyId().equals(item.getCompanyId())) {
                    continue;
                }
                List<NewUserRoleBindItem> roleBindItemList = item.getRoleBindItemList();
                NewUserRoleBindItem newUserRoleBindItem = new NewUserRoleBindItem();
                newUserRoleBindItem.setRoleId(companyRole.getRoleId());
                newUserRoleBindItem.setRoleName(companyRole.getRoleName());
                newUserRoleBindItem.setRoleType(companyRole.getRoleType());
                newUserRoleBindItem.setCheckedFlag(false);
                roleBindItemList.add(newUserRoleBindItem);
            }
        }
    }

    /**
     * 填充用户已经绑定的角色信息
     *
     * @author fengshuonan
     * @since 2024/1/17 23:49
     */
    public static List<NewUserRoleBindResponse> fillRoleBind(List<NewUserRoleBindResponse> baseResponse, List<UserRoleDTO> userLinkedOrgRoleList) {

        if (ObjectUtil.isEmpty(userLinkedOrgRoleList)) {
            return baseResponse;
        }

        for (NewUserRoleBindResponse item : baseResponse) {

            // 获取指定机构id
            Long orgId = item.getOrgId();

            // 获取这个机构下的填充的所有角色
            List<NewUserRoleBindItem> roleBindItemList = item.getRoleBindItemList();

            // 获取用户是否在这个公司下有相关的角色绑定
            for (UserRoleDTO userAlreadyBind : userLinkedOrgRoleList) {
                if (userAlreadyBind.getRoleOrgId().equals(orgId)) {
                    for (NewUserRoleBindItem newUserRoleBindItem : roleBindItemList) {
                        if (newUserRoleBindItem.getRoleId().equals(userAlreadyBind.getRoleId())) {
                            newUserRoleBindItem.setCheckedFlag(true);
                        }
                    }
                }
            }
        }

        return baseResponse;
    }

}
