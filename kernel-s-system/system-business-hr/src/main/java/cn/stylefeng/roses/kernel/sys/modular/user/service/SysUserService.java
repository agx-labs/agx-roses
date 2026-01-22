package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.response.PersonalInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统用户 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
public interface SysUserService extends IService<SysUser>, SysUserServiceApi {

    /**
     * 新增
     *
     * @param sysUserRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void add(SysUserRequest sysUserRequest);

    /**
     * 删除
     *
     * @param sysUserRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void del(SysUserRequest sysUserRequest);

    /**
     * 批量删除用户
     *
     * @author fengshuonan
     * @since 2023/6/12 10:41
     */
    void batchDel(SysUserRequest sysUserRequest);

    /**
     * 编辑
     *
     * @param sysUserRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void edit(SysUserRequest sysUserRequest);

    /**
     * 查询详情
     *
     * @param sysUserRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    SysUser detail(SysUserRequest sysUserRequest);

    /**
     * 获取列表
     *
     * @param sysUserRequest 请求参数
     * @return List<SysUser>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    List<SysUser> findList(SysUserRequest sysUserRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysUserRequest 请求参数
     * @return PageResult<SysUser>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    PageResult<SysUser> findPage(SysUserRequest sysUserRequest);

    /**
     * 修改用户状态
     *
     * @author fengshuonan
     * @since 2023/6/12 10:59
     */
    void updateStatus(SysUserRequest sysUserRequest);

    /**
     * 获取当前用户的个人信息详情
     *
     * @author fengshuonan
     * @since 2023/6/26 22:28
     */
    PersonalInfo getPersonalInfo();

    /**
     * 更新用户信息（一般用于更新个人信息）
     *
     * @param sysUserRequest 请求参数封装
     * @author fengshuonan
     * @date 2020/11/21 12:32
     */
    void editInfo(SysUserRequest sysUserRequest);

    /**
     * 修改个人头像
     *
     * @author fengshuonan
     * @since 2023/6/26 22:25
     */
    void editAvatar(SysUserRequest sysUserRequest);

    /**
     * 修改个人密码
     *
     * @author fengshuonan
     * @since 2023/6/26 22:25
     */
    void editPassword(SysUserRequest sysUserRequest);

    /**
     * 批量获取用户名称
     *
     * @author fengshuonan
     * @since 2023/11/2 10:25
     */
    List<SimpleDict> batchGetName(SysUserRequest sysUserRequest);

    /**
     * 批量保存用户信息
     *
     * @author fengshuonan
     * @since 2024/2/12 22:41
     */
    void quickBatchSaveUser(List<SysUser> batchUser);

}