package cn.stylefeng.roses.kernel.auth.api.pojo.sso;

import lombok.Data;

import java.util.Map;

/**
 * 统一认证中心登录用户信息
 *
 * @author fengshuonan
 * @date 2021/1/21 14:04
 */
@Data
public class DecryptCaLoginUser {

    /**
     * 统一认证中心的会话的token
     */
    private String caToken;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户账号id
     */
    private Long userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 其他属性
     */
    private Map<String, Object> otherProperties;

}
