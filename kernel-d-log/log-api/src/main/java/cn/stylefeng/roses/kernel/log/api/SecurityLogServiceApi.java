package cn.stylefeng.roses.kernel.log.api;

import cn.stylefeng.roses.kernel.log.api.pojo.security.LogSecurityRequest;

/**
 * 安全日志记录
 *
 * @author fengshuonan
 * @since 2024/7/11 18:30
 */
public interface SecurityLogServiceApi {

    /**
     * 新增安全日志
     *
     * @param logSecurityRequest 请求参数
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    void add(LogSecurityRequest logSecurityRequest);

}
