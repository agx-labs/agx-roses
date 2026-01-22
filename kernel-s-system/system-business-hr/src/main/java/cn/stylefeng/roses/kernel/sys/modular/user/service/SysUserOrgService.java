package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.api.SysUserOrgServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserOrgRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户组织机构关联 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
public interface SysUserOrgService extends IService<SysUserOrg>, SysUserOrgServiceApi {

    /**
     * 新增
     *
     * @param sysUserOrgRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void add(SysUserOrgRequest sysUserOrgRequest);

    /**
     * 删除
     *
     * @param sysUserOrgRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void del(SysUserOrgRequest sysUserOrgRequest);

    /**
     * 编辑
     *
     * @param sysUserOrgRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void edit(SysUserOrgRequest sysUserOrgRequest);

    /**
     * 查询详情
     *
     * @param sysUserOrgRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    SysUserOrg detail(SysUserOrgRequest sysUserOrgRequest);

    /**
     * 获取列表
     *
     * @param sysUserOrgRequest 请求参数
     * @return List<SysUserOrg>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    List<SysUserOrg> findList(SysUserOrgRequest sysUserOrgRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysUserOrgRequest 请求参数
     * @return PageResult<SysUserOrg>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    PageResult<SysUserOrg> findPage(SysUserOrgRequest sysUserOrgRequest);

    /**
     * 更新用户的任职信息
     *
     * @param userId      用户id
     * @param userOrgList 用户绑定的组织机构列表
     * @author fengshuonan
     * @since 2023/6/11 22:23
     */
    void updateUserOrg(Long userId, List<SysUserOrg> userOrgList);

    /**
     * 获取用户绑定的机构信息
     *
     * @author fengshuonan
     * @since 2024-01-18 10:36
     */
    SysUserOrg getUserOrgInfo(Long userId, Long orgId);

    /**
     * 删除用户机构的关联
     *
     * @author fengshuonan
     * @since 2024-01-18 13:42
     */
    void removeUserOrgLink(Long userId, Long orgId);

    /**
     * 删除全部的机构绑定
     *
     * @author fengshuonan
     * @since 2024-01-18 15:48
     */
    void deleteAllOrgBind(SysUserOrgRequest sysUserOrgRequest);

    /**
     * 禁用全部组织机构的状态
     *
     * @author fengshuonan
     * @since 2024-01-18 16:57
     */
    void disableAllOrgStatus(Long userId);

    /**
     * 批量保存用户机构
     *
     * @author fengshuonan
     * @since 2024/2/12 23:31
     */
    void quickBatchSaveUserOrg(List<SysUserOrg> batchUserOrg);

}