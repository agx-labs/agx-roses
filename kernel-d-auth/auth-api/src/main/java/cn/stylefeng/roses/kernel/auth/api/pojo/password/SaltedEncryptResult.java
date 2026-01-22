package cn.stylefeng.roses.kernel.auth.api.pojo.password;

import lombok.Data;

/**
 * 密码加密结果
 *
 * @author fengshuonan
 * @since 2023/6/25 8:48
 */
@Data
public class SaltedEncryptResult {

    /**
     * 加密后的密码
     */
    private String encryptPassword;

    /**
     * 密码盐
     */
    private String passwordSalt;

}
