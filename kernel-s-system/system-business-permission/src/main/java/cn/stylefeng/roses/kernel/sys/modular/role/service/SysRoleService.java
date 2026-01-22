package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 系统角色 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
public interface SysRoleService extends IService<SysRole>, SysRoleServiceApi {

    /**
     * 新增
     *
     * @param sysRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void add(SysRoleRequest sysRoleRequest);

    /**
     * 删除
     *
     * @param sysRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void del(SysRoleRequest sysRoleRequest);

    /**
     * 批量删除角色
     *
     * @author fengshuonan
     * @since 2023/6/12 21:11
     */
    void batchDelete(SysRoleRequest sysRoleRequest);

    /**
     * 编辑
     *
     * @param sysRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void edit(SysRoleRequest sysRoleRequest);

    /**
     * 查询详情
     *
     * @param sysRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    SysRole detail(SysRoleRequest sysRoleRequest);

    /**
     * 获取所有角色列表
     * <p>
     * 用在权限分配界面，左侧的角色列表
     *
     * @param sysRoleRequest 请求参数
     * @return List<SysRole>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    List<SysRole> permissionGetRoleList(SysRoleRequest sysRoleRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysRoleRequest 请求参数
     * @return PageResult<SysRole>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    PageResult<SysRole> findPage(SysRoleRequest sysRoleRequest);

    /**
     * 获取角色的数据范围类型
     *
     * @param roleId 角色id
     * @return 数据范围类型
     * @author fengshuonan
     * @since 2023/7/16 23:28
     */
    Integer getRoleDataScopeType(Long roleId);

    /**
     * 更新角色的数据范围类型
     *
     * @param roleId        角色id
     * @param dataScopeType 数据范围类型
     * @author fengshuonan
     * @since 2023/7/16 23:28
     */
    void updateRoleDataScopeType(Long roleId, Integer dataScopeType);

    /**
     * 获取角色对应的数据范围，如果是多个角色，则取最大类型的数据范围
     *
     * @author fengshuonan
     * @since 2023/7/18 23:19
     */
    DataScopeTypeEnum getRoleDataScope(List<Long> roleIds);

    /**
     * 用户分配角色界面，获取角色列表
     *
     * @author fengshuonan
     * @since 2024-01-16 19:07
     */
    List<SysRole> userAssignRoleList(SysRoleRequest sysRoleRequest);

    /**
     * 将指定角色id，进行权限刷新，从新绑定对应的菜单和功能，并将角色权限范围也绑定指定的功能id
     *
     * @author fengshuonan
     * @since 2024-01-23 13:59
     */
    void refreshRoleAuth(Set<Long> roleIdList, Set<Long> menuIdList, Set<Long> menuOptionIdList);

    /**
     * 获取角色对应数据范围的类型
     *
     * @author fengshuonan
     * @since 2024-03-01 14:18
     */
    List<SysRole> getRoleDataScopeType(List<Long> roleIdList);

}