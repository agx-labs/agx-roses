package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindResponse;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.DeleteRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.RoleControlRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.StatusControlRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.SyncBindRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserOrgRequest;

import java.util.List;

/**
 * 新的用户授权界面业务
 *
 * @author fengshuonan
 * @since 2024/1/17 23:08
 */
public interface SysRoleAssignService {

    /**
     * 获取用户的角色授权信息列表
     *
     * @author fengshuonan
     * @since 2024/1/17 23:09
     */
    List<NewUserRoleBindResponse> getUserAssignList(Long userId);

    /**
     * 修改用户绑定机构状态`
     *
     * @author fengshuonan
     * @since 2024-01-18 9:34
     */
    void changeUserBindStatus(StatusControlRequest statusControlRequest);

    /**
     * 设置角色的选中状态的填充
     *
     * @author fengshuonan
     * @since 2024-01-18 10:50
     */
    void changeRoleSelect(RoleControlRequest roleControlRequest);

    /**
     * 删除机构的绑定
     *
     * @author fengshuonan
     * @since 2024-01-18 13:39
     */
    void removeUserOrgBind(DeleteRequest deleteRequest);

    /**
     * 删除所有的组织机构的绑定，同时清空机构下的角色绑定
     *
     * @author fengshuonan
     * @since 2024-01-18 15:50
     */
    void clearAllOrgAndRoleBind(SysUserOrgRequest sysUserOrgRequest);

    /**
     * 禁用全部组织机构的状态
     *
     * @author fengshuonan
     * @since 2024-01-18 16:55
     */
    void disableAllOrgStatus(SysUserOrgRequest sysUserOrgRequest);

    /**
     * 同步用户的指定机构的绑定状态和基础业务角色，到其他的组织机构中
     *
     * @author fengshuonan
     * @since 2024-01-18 17:16
     */
    void syncOtherOrgStatusAndBusinessRole(SyncBindRequest syncBindRequest);

}