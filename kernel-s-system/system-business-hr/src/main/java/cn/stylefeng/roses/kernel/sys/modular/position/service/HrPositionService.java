package cn.stylefeng.roses.kernel.sys.modular.position.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.sys.api.PositionServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.position.entity.HrPosition;
import cn.stylefeng.roses.kernel.sys.modular.position.pojo.request.HrPositionRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 职位信息 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:25
 */
public interface HrPositionService extends IService<HrPosition>, PositionServiceApi {

    /**
     * 新增
     *
     * @param hrPositionRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    void add(HrPositionRequest hrPositionRequest);

    /**
     * 删除
     *
     * @param hrPositionRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    void del(HrPositionRequest hrPositionRequest);

    /**
     * 批量删除职位
     *
     * @author fengshuonan
     * @since 2023/6/11 17:14
     */
    void batchDelete(HrPositionRequest hrPositionRequest);

    /**
     * 编辑
     *
     * @param hrPositionRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    void edit(HrPositionRequest hrPositionRequest);

    /**
     * 查询详情
     *
     * @param hrPositionRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    HrPosition detail(HrPositionRequest hrPositionRequest);

    /**
     * 获取列表
     *
     * @param hrPositionRequest 请求参数
     * @return List<HrPosition>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    List<HrPosition> findList(HrPositionRequest hrPositionRequest);

    /**
     * 获取列表（带分页）
     *
     * @param hrPositionRequest 请求参数
     * @return PageResult<HrPosition>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    PageResult<HrPosition> findPage(HrPositionRequest hrPositionRequest);

    /**
     * 批量获取职位名称
     *
     * @author fengshuonan
     * @since 2023/11/2 10:29
     */
    List<SimpleDict> batchGetName(HrPositionRequest hrPositionRequest);

}