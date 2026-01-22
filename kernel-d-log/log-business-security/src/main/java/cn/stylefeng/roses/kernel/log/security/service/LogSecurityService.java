package cn.stylefeng.roses.kernel.log.security.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.SecurityLogServiceApi;
import cn.stylefeng.roses.kernel.log.api.pojo.security.LogSecurityRequest;
import cn.stylefeng.roses.kernel.log.security.entity.LogSecurity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 安全日志服务类
 *
 * @author fengshuonan
 * @since 2024/07/11 15:56
 */
public interface LogSecurityService extends IService<LogSecurity>, SecurityLogServiceApi {

    /**
     * 删除安全日志
     *
     * @param logSecurityRequest 请求参数
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    void del(LogSecurityRequest logSecurityRequest);

    /**
     * 批量删除安全日志
     *
     * @param logSecurityRequest 请求参数
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    void batchDelete(LogSecurityRequest logSecurityRequest);

    /**
     * 编辑安全日志
     *
     * @param logSecurityRequest 请求参数
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    void edit(LogSecurityRequest logSecurityRequest);

    /**
     * 查询详情安全日志
     *
     * @param logSecurityRequest 请求参数
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    LogSecurity detail(LogSecurityRequest logSecurityRequest);

    /**
     * 获取安全日志列表
     *
     * @param logSecurityRequest 请求参数
     * @return List<LogSecurity>  返回结果
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    List<LogSecurity> findList(LogSecurityRequest logSecurityRequest);

    /**
     * 获取安全日志分页列表
     *
     * @param logSecurityRequest 请求参数
     * @return PageResult<LogSecurity>   返回结果
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    PageResult<LogSecurity> findPage(LogSecurityRequest logSecurityRequest);

}
