package cn.stylefeng.roses.kernel.sys.modular.menu.service;

import cn.stylefeng.roses.kernel.sys.api.SysMenuServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
public interface SysMenuService extends IService<SysMenu>, SysMenuServiceApi {

    /**
     * 新增
     *
     * @param sysMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void add(SysMenuRequest sysMenuRequest);

    /**
     * 删除
     *
     * @param sysMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void del(SysMenuRequest sysMenuRequest);

    /**
     * 编辑
     *
     * @param sysMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void edit(SysMenuRequest sysMenuRequest);

    /**
     * 查询详情
     *
     * @param sysMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    SysMenu detail(SysMenuRequest sysMenuRequest);

    /**
     * 获取菜单管理界面的每个应用组下的菜单信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    List<AppGroupDetail> getAppMenuGroupDetail(SysMenuRequest sysMenuRequest);

    /**
     * 校验菜单是否绑定到某个app下
     *
     * @return true-该app下有菜单，false-该app下没菜单
     * @author fengshuonan
     * @since 2023/6/12 19:23
     */
    boolean validateMenuBindApp(Set<Long> appIdList);

    /**
     * 获取所有的菜单信息，用在角色绑定权限界面
     *
     * @author fengshuonan
     * @since 2023/6/15 9:24
     */
    List<SysMenu> getTotalMenus();

    /**
     * 获取所有的菜单信息，用在角色绑定权限界面
     *
     * @param limitMenuIds 指定筛选的菜单的范围
     * @author fengshuonan
     * @since 2023/6/15 9:24
     */
    List<SysMenu> getTotalMenus(Set<Long> limitMenuIds);

    /**
     * 调整菜单上下级结构和菜单的顺序
     *
     * @author fengshuonan
     * @since 2023/6/15 11:28
     */
    void updateMenuTree(SysMenuRequest sysMenuRequest);

    /**
     * 获取所有菜单id
     * <p>
     * 一般用在项目启动，管理员自动绑定所有菜单
     *
     * @return 结果只包含菜单id和应用id
     * @author fengshuonan
     * @since 2023/6/18 20:37
     */
    List<SysMenu> getTotalMenuList();

    /**
     * 获取菜单对应的菜单编码，以及菜单id、父级id，应用id，菜单名称、菜单图标、菜单是否隐藏、菜单激活地址、菜单路由、组件路径、排序信息
     * <p>
     * 用在用户登录后，获取用户首页信息接口
     *
     * @author fengshuonan
     * @since 2023/6/19 12:53
     */
    List<SysMenu> getIndexMenuInfoList(List<Long> menuIdList);

    /**
     * 通过菜单id，获取菜单的编码集合
     *
     * @author fengshuonan
     * @since 2023/6/21 0:44
     */
    List<String> getMenuCodeList(List<Long> menuIdList);

    /**
     * 将指定的菜单补全父级菜单
     * <p>
     * 一般用在参数是叶子节点的情况下，向上补全，为了方便展示菜单树
     *
     * @author fengshuonan
     * @since 2024/10/30 20:57
     */
    List<SysMenu> completeParentMenu(List<SysMenu> menuList);

}