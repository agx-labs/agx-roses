package cn.stylefeng.roses.kernel.sys.modular.login.service;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户权限信息获取的综合业务
 *
 * @author fengshuonan
 * @since 2023/6/21 0:47
 */
@Service
public class UserPermissionService {

    @Resource
    private SysUserRoleServiceApi sysUserRoleServiceApi;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    /**
     * 填充用户的权限编码集合
     *
     * @author fengshuonan
     * @since 2023/6/19 12:38
     */
    public List<String> getUserPermissionCodeList(LoginUser loginUser) {

        Long userId = loginUser.getUserId();

        // 获取用户的角色集合
        List<Long> roleIdList = sysUserRoleServiceApi.getUserRoleIdList(userId);

        if (ObjectUtil.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }

        // 获取角色对应的菜单id和菜单功能id
        List<Long> menuIdList = sysRoleMenuService.getRoleBindMenuIdList(roleIdList);
        List<Long> menuOptionsIdList = sysRoleMenuOptionsService.getRoleBindMenuOptionsIdList(roleIdList);

        List<String> permissionCodeList = new ArrayList<>();

        // 获取菜单对应的菜单编码集合
        List<String> userMenuCodeList = sysMenuService.getMenuCodeList(menuIdList);
        permissionCodeList.addAll(userMenuCodeList);

        // 获取功能对应的功能编码集合
        List<String> optionsCodeList = sysMenuOptionsService.getOptionsCodeList(menuOptionsIdList);
        permissionCodeList.addAll(optionsCodeList);

        return permissionCodeList;
    }

}
