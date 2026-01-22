package cn.stylefeng.roses.kernel.log.business.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.pojo.business.SysLogBusinessRequest;
import cn.stylefeng.roses.kernel.log.api.pojo.entity.SysLogBusiness;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 业务日志记录 服务类
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
public interface SysLogBusinessService extends IService<SysLogBusiness> {

	/**
     * 新增
     *
     * @param sysLogBusinessRequest 请求参数
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    void add(SysLogBusinessRequest sysLogBusinessRequest);

	/**
     * 删除
     *
     * @param sysLogBusinessRequest 请求参数
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    void del(SysLogBusinessRequest sysLogBusinessRequest);

	/**
     * 编辑
     *
     * @param sysLogBusinessRequest 请求参数
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    void edit(SysLogBusinessRequest sysLogBusinessRequest);

	/**
     * 查询详情
     *
     * @param sysLogBusinessRequest 请求参数
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    SysLogBusiness detail(SysLogBusinessRequest sysLogBusinessRequest);

	/**
     * 获取列表
     *
     * @param sysLogBusinessRequest        请求参数
     * @return List<SysLogBusiness>   返回结果
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    List<SysLogBusiness> findList(SysLogBusinessRequest sysLogBusinessRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysLogBusinessRequest              请求参数
     * @return PageResult<SysLogBusiness>   返回结果
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    PageResult<SysLogBusiness> findPage(SysLogBusinessRequest sysLogBusinessRequest);

    /**
     * 批量保存业务日志
     *
     * @author fengshuonan
     * @since 2023/7/21 17:01
     */
    void saveBatchLogs(SysLogBusiness context, List<String> batchContentList);

}