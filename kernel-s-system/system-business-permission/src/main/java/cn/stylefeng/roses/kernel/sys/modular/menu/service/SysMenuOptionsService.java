package cn.stylefeng.roses.kernel.sys.modular.menu.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuOptionsRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 菜单下的功能操作 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
public interface SysMenuOptionsService extends IService<SysMenuOptions> {

    /**
     * 新增
     *
     * @param sysMenuOptionsRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void add(SysMenuOptionsRequest sysMenuOptionsRequest);

    /**
     * 删除
     *
     * @param sysMenuOptionsRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void del(SysMenuOptionsRequest sysMenuOptionsRequest);

    /**
     * 编辑
     *
     * @param sysMenuOptionsRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void edit(SysMenuOptionsRequest sysMenuOptionsRequest);

    /**
     * 查询详情
     *
     * @param sysMenuOptionsRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    SysMenuOptions detail(SysMenuOptionsRequest sysMenuOptionsRequest);

    /**
     * 获取列表
     *
     * @param sysMenuOptionsRequest 请求参数
     * @return List<SysMenuOptions>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    List<SysMenuOptions> findList(SysMenuOptionsRequest sysMenuOptionsRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysMenuOptionsRequest 请求参数
     * @return PageResult<SysMenuOptions>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    PageResult<SysMenuOptions> findPage(SysMenuOptionsRequest sysMenuOptionsRequest);

    /**
     * 获取所有的菜单功能id
     * <p>
     * 一般用在项目启动管理员角色绑定所有的菜单功能
     *
     * @author fengshuonan
     * @since 2023/6/18 20:37
     */
    List<SysMenuOptions> getTotalMenuOptionsList();

    /**
     * 获取所有的菜单功能id
     * <p>
     * 一般用在项目启动管理员角色绑定所有的菜单功能
     *
     * @param roleLimitMenuIdsAndOptionIds 角色限制的菜单id和功能id集合
     * @author fengshuonan
     * @since 2023/6/18 20:37
     */
    List<SysMenuOptions> getTotalMenuOptionsList(Set<Long> roleLimitMenuIdsAndOptionIds);

    /**
     * 获取功能编码集合，通过功能id集合
     *
     * @author fengshuonan
     * @since 2023/6/19 12:56
     */
    List<String> getOptionsCodeList(List<Long> optionsIdList);

}