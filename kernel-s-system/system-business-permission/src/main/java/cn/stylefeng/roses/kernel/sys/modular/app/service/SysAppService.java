package cn.stylefeng.roses.kernel.sys.modular.app.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.app.pojo.request.SysAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.IndexUserAppInfo;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 系统应用 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
public interface SysAppService extends IService<SysApp> {

    /**
     * 新增
     *
     * @param sysAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void add(SysAppRequest sysAppRequest);

    /**
     * 删除
     *
     * @param sysAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void del(SysAppRequest sysAppRequest);

    /**
     * 批量删除应用
     *
     * @author fengshuonan
     * @since 2023/6/12 19:30
     */
    void batchDelete(SysAppRequest sysAppRequest);

    /**
     * 编辑
     *
     * @param sysAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void edit(SysAppRequest sysAppRequest);

    /**
     * 查询详情
     *
     * @param sysAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    SysApp detail(SysAppRequest sysAppRequest);

    /**
     * 获取列表
     *
     * @param sysAppRequest 请求参数
     * @return List<SysApp>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    List<SysApp> findList(SysAppRequest sysAppRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysAppRequest 请求参数
     * @return PageResult<SysApp>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    PageResult<SysApp> findPage(SysAppRequest sysAppRequest);

    /**
     * 获取启用的应用列表（用在给菜单界面用）
     *
     * @author fengshuonan
     * @since 2023/6/14 21:48
     */
    List<AppGroupDetail> getAppList();

    /**
     * 根据应用id获取应用的名称信息，并且按应用的排序字段进行排序
     * <p>
     * 一般用在用户登录后，获取首页应用信息
     *
     * @author fengshuonan
     * @since 2023/6/19 22:30
     */
    List<IndexUserAppInfo> getIndexUserAppList(Set<Long> appIds);

    /**
     * 修改用户状态
     *
     * @author liyanjun
     * @since 2023/6/30 10:59
     */
    void updateStatus(SysAppRequest sysUserRequest);

}