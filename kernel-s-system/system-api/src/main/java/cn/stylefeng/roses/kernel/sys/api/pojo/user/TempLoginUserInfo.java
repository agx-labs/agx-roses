package cn.stylefeng.roses.kernel.sys.api.pojo.user;

import lombok.Data;

import java.util.Date;

/**
 * 临时用户身份信息的包装
 *
 * @author fengshuonan
 * @since 2023/11/16 21:38
 */
@Data
public class TempLoginUserInfo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 登录时候的IP
     */
    private String loginIp;

    /**
     * 登录时间
     */
    private Date loginTime;

}
