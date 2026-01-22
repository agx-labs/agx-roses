package cn.stylefeng.roses.kernel.sys.api.pojo.user;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

/**
 * 简单添加用户的请求
 *
 * @author fengshuonan
 * @since 2024/5/23 23:47
 */
@Data
public class AddUserDTO {

    /**
     * 账号
     */
    @ChineseDescription("账号")
    private String account;

    /**
     * 昵称
     */
    @ChineseDescription("昵称")
    private String nickName;

    /**
     * 邮箱
     */
    @ChineseDescription("邮箱")
    private String email;

    /**
     * 密码，加密方式为MD5
     */
    @ChineseDescription("密码，加密方式为MD5")
    private String password;

    /**
     * 密码盐
     */
    @ChineseDescription("密码盐")
    private String passwordSalt;

}
