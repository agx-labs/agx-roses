package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色和菜单下的功能关联 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
public interface SysRoleMenuOptionsService extends IService<SysRoleMenuOptions> {

    /**
     * 删除角色绑定的菜单功能
     *
     * @author fengshuonan
     * @since 2023/6/15 23:38
     */
    void removeRoleBindOptions(Long optionsId);

    /**
     * 给角色绑定菜单功能
     *
     * @author fengshuonan
     * @since 2023/6/18 20:52
     */
    void bindRoleMenuOptions(Long roleId, List<SysMenuOptions> sysMenuOptionsList);

    /**
     * 获取角色绑定的菜单功能id
     *
     * @author fengshuonan
     * @since 2023/6/19 12:49
     */
    List<Long> getRoleBindMenuOptionsIdList(List<Long> roleIdList);

}