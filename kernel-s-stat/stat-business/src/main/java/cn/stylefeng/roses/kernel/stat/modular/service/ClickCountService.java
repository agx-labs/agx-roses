package cn.stylefeng.roses.kernel.stat.modular.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.stat.api.ClickCountCalcApi;
import cn.stylefeng.roses.kernel.stat.modular.entity.ClickCount;
import cn.stylefeng.roses.kernel.stat.modular.request.ClickCountRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户点击数量统计 服务类
 *
 * @author fengshuonan
 * @since 2023/03/28 14:52
 */
public interface ClickCountService extends IService<ClickCount>, ClickCountCalcApi {

    /**
     * 新增
     *
     * @param portalClickCountRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    void add(ClickCountRequest portalClickCountRequest);

    /**
     * 删除
     *
     * @param portalClickCountRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    void del(ClickCountRequest portalClickCountRequest);

    /**
     * 编辑
     *
     * @param portalClickCountRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    void edit(ClickCountRequest portalClickCountRequest);

    /**
     * 查询详情
     *
     * @param portalClickCountRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    ClickCount detail(ClickCountRequest portalClickCountRequest);

    /**
     * 获取列表
     *
     * @param portalClickCountRequest 请求参数
     * @return List<PortalClickCount>   返回结果
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    List<ClickCount> findList(ClickCountRequest portalClickCountRequest);

    /**
     * 获取列表（带分页）
     *
     * @param portalClickCountRequest 请求参数
     * @return PageResult<PortalClickCount>   返回结果
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    PageResult<ClickCount> findPage(ClickCountRequest portalClickCountRequest);

    /**
     * 获取某个业务记录的点击次数总数
     *
     * @param businessId 业务id
     * @return 点击次数统计总数
     * @author fengshuonan
     * @since 2023/3/28 15:31
     */
    Long getBusinessCount(Long businessId);

}