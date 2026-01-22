package cn.stylefeng.roses.kernel.log.business.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.pojo.business.SysLogBusinessContentRequest;
import cn.stylefeng.roses.kernel.log.api.pojo.entity.SysLogBusinessContent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 业务日志记录 服务类
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
public interface SysLogBusinessContentService extends IService<SysLogBusinessContent> {

    /**
     * 获取列表（带分页）
     *
     * @param sysLogBusinessContentRequest 请求参数
     * @return PageResult<SysLogBusinessContent>   返回结果
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    PageResult<SysLogBusinessContent> findPage(SysLogBusinessContentRequest sysLogBusinessContentRequest);

    /**
     * 批量保存业务日志详情
     *
     * @author fengshuonan
     * @since 2023/7/21 19:07
     */
    void batchSaveContent(List<SysLogBusinessContent> sysLogBusinessContentList);

}