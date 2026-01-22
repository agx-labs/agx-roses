package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserDataScope;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserDataScopeRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户数据范围 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
public interface SysUserDataScopeService extends IService<SysUserDataScope> {

	/**
     * 新增
     *
     * @param sysUserDataScopeRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void add(SysUserDataScopeRequest sysUserDataScopeRequest);

	/**
     * 删除
     *
     * @param sysUserDataScopeRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void del(SysUserDataScopeRequest sysUserDataScopeRequest);

	/**
     * 编辑
     *
     * @param sysUserDataScopeRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void edit(SysUserDataScopeRequest sysUserDataScopeRequest);

	/**
     * 查询详情
     *
     * @param sysUserDataScopeRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    SysUserDataScope detail(SysUserDataScopeRequest sysUserDataScopeRequest);

	/**
     * 获取列表
     *
     * @param sysUserDataScopeRequest        请求参数
     * @return List<SysUserDataScope>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    List<SysUserDataScope> findList(SysUserDataScopeRequest sysUserDataScopeRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysUserDataScopeRequest              请求参数
     * @return PageResult<SysUserDataScope>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    PageResult<SysUserDataScope> findPage(SysUserDataScopeRequest sysUserDataScopeRequest);

}