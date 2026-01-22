package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色菜单关联 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 给角色绑定某些菜单
     *
     * @author fengshuonan
     * @since 2023/6/18 20:46
     */
    void bindRoleMenus(Long roleId, List<SysMenu> menuList);

    /**
     * 获取角色绑定的菜单id集合，返回菜单id的集合
     *
     * @author fengshuonan
     * @since 2023/6/19 12:45
     */
    List<Long> getRoleBindMenuIdList(List<Long> roleIdList);

    /**
     * 判断指定角色集合，是否有对应应用的权限
     *
     * @param roleIdList 角色id集合，一般指的是用户拥有的角色id集合
     * @param appId      应用id
     * @return true-角色id集合中包含该应用的权限，false-角色id集合中不包含权限
     * @author fengshuonan
     * @since 2023/6/21 16:27
     */
    boolean validateRoleHaveAppIdPermission(List<Long> roleIdList, Long appId);

}