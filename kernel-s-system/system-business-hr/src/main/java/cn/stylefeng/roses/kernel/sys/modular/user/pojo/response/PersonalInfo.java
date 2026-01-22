package cn.stylefeng.roses.kernel.sys.modular.user.pojo.response;

import cn.stylefeng.roses.kernel.file.api.format.FileUrlFormatProcess;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户的个人信息
 *
 * @author fengshuonan
 * @since 2023/6/26 22:31
 */
@Data
public class PersonalInfo {

    /**
     * 姓名
     */
    @ChineseDescription("姓名")
    private String realName;

    /**
     * 账号
     */
    @ChineseDescription("账号")
    private String account;

    /**
     * 头像，存的为文件id
     */
    @SimpleFieldFormat(processClass = FileUrlFormatProcess.class)
    private Long avatar;

    /**
     * 生日
     */
    @ChineseDescription("生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别：M-男，F-女
     */
    @ChineseDescription("性别：M-男，F-女")
    private String sex;

    /**
     * 邮箱
     */
    @ChineseDescription("邮箱")
    private String email;

    /**
     * 手机
     */
    @ChineseDescription("手机")
    private String phone;

}
