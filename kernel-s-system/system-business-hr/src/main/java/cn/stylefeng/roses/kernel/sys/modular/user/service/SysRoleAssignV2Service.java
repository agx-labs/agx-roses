package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleTreeDTO;

import java.util.List;

/**
 * 直接在用户管理界面，进行角色授权的业务
 *
 * @author fengshuonan
 * @since 2025/1/24 14:00
 */
public interface SysRoleAssignV2Service {

    /**
     * 获取用户指定机构下的角色树（包含角色分类和角色）
     * <p>
     * 已经绑定的角色会checked
     *
     * @author fengshuonan
     * @since 2025/1/24 14:44
     */
    List<SysRoleTreeDTO> getCompanyBusinessRoleTree(Long userId, Long orgId);

    /**
     * 获取用户指定机构下的公司角色树（包含角色分类和角色）
     *
     * @author fengshuonan
     * @since 2025/1/24 15:45
     */
    List<SysRoleTreeDTO> getCompanyRoleTree(Long userId, Long orgId);

}