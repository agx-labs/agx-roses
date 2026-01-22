package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.response.RoleBindDataScopeResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleDataScope;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleDataScopeRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 角色数据范围 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
public interface SysRoleDataScopeService extends IService<SysRoleDataScope> {

    /**
     * 新增
     *
     * @param sysRoleDataScopeRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void add(SysRoleDataScopeRequest sysRoleDataScopeRequest);

    /**
     * 删除
     *
     * @param sysRoleDataScopeRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void del(SysRoleDataScopeRequest sysRoleDataScopeRequest);

    /**
     * 编辑
     *
     * @param sysRoleDataScopeRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void edit(SysRoleDataScopeRequest sysRoleDataScopeRequest);

    /**
     * 查询详情
     *
     * @param sysRoleDataScopeRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    SysRoleDataScope detail(SysRoleDataScopeRequest sysRoleDataScopeRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysRoleDataScopeRequest 请求参数
     * @return PageResult<SysRoleDataScope>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    PageResult<SysRoleDataScope> findPage(SysRoleDataScopeRequest sysRoleDataScopeRequest);

    /**
     * 获取角色绑定的数据权限
     *
     * @author fengshuonan
     * @since 2023/7/16 23:26
     */
    RoleBindDataScopeResponse getRoleBindDataScope(RoleBindDataScopeRequest roleBindDataScopeRequest);

    /**
     * 角色绑定数据范围
     *
     * @author fengshuonan
     * @since 2023/7/16 23:42
     */
    void updateRoleBindDataScope(RoleBindDataScopeRequest roleBindDataScopeRequest);

    /**
     * 获取角色绑定的组织机构id列表
     *
     * @author fengshuonan
     * @since 2023/7/18 23:37
     */
    Set<Long> getRoleBindOrgIdList(List<Long> roleIdList);

    /**
     * 获取指定角色列表的数据范围列表
     *
     * @author fengshuonan
     * @since 2025/1/25 21:22
     */
    List<SysRoleDataScope> getRoleDataScopeList(List<Long> roleIdList);

}