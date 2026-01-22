package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.pojo.security.SecurityConfig;

/**
 * 系统安全配置的业务
 *
 * @author fengshuonan
 * @since 2023/10/4 16:22
 */
public interface SecurityConfigService {

    /**
     * 获取系统安全配置
     *
     * @author fengshuonan
     * @since 2023/10/4 16:23
     */
    SecurityConfig getSecurityConfig();

    /**
     * 更新系统安全配置
     *
     * @author fengshuonan
     * @since 2023/10/4 16:25
     */
    void updateSecurityConfig(SecurityConfig securityConfig);

    /**
     * 校验密码是否符合当前配置的安全规则，如果不符合规则，直接抛出异常
     *
     * @param updatePasswordFlag 是否是修改密码的标识
     * @param password           新密码
     * @author fengshuonan
     * @since 2023/10/4 22:40
     */
    void validatePasswordSecurityRule(boolean updatePasswordFlag, String password);

    /**
     * 记录用户密码修改的日志
     *
     * @author fengshuonan
     * @since 2023/10/5 17:16
     */
    void recordPasswordEditLog(Long userId, String md5, String salt);

}
