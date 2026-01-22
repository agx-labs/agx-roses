package cn.stylefeng.roses.kernel.stat.modular.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.stat.api.ClickStatusCalcApi;
import cn.stylefeng.roses.kernel.stat.modular.entity.ClickStatus;
import cn.stylefeng.roses.kernel.stat.modular.request.ClickStatusRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户点击状态 服务类
 *
 * @author fengshuonan
 * @since 2023/03/28 14:52
 */
public interface ClickStatusService extends IService<ClickStatus>, ClickStatusCalcApi {

    /**
     * 新增
     *
     * @param portalClickStatusRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    void add(ClickStatusRequest portalClickStatusRequest);

    /**
     * 删除
     *
     * @param portalClickStatusRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    void del(ClickStatusRequest portalClickStatusRequest);

    /**
     * 编辑
     *
     * @param portalClickStatusRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    void edit(ClickStatusRequest portalClickStatusRequest);

    /**
     * 查询详情
     *
     * @param portalClickStatusRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    ClickStatus detail(ClickStatusRequest portalClickStatusRequest);

    /**
     * 获取列表
     *
     * @param portalClickStatusRequest 请求参数
     * @return List<PortalClickStatus>   返回结果
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    List<ClickStatus> findList(ClickStatusRequest portalClickStatusRequest);

    /**
     * 获取列表（带分页）
     *
     * @param portalClickStatusRequest 请求参数
     * @return PageResult<PortalClickStatus>   返回结果
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    PageResult<ClickStatus> findPage(ClickStatusRequest portalClickStatusRequest);

    /**
     * 获取用户是否点击过这条记录
     *
     * @param userId     用户id
     * @param businessId 业务id
     * @return 用户点击状态，true-点击过，false-没点击过
     * @author fengshuonan
     * @since 2023/3/28 15:31
     */
    boolean getUserBusinessClickFlag(Long userId, Long businessId);

}