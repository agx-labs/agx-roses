package cn.stylefeng.roses.kernel.sys.modular.security.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户历史密码记录封装类
 *
 * @author fengshuonan
 * @date 2023/10/04 23:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserPasswordRecordRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long recordId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 历史密码记录
     */
    @NotBlank(message = "历史密码记录不能为空", groups = {add.class, edit.class})
    @ChineseDescription("历史密码记录")
    private String historyPassword;

    /**
     * 历史密码记录盐值
     */
    @NotBlank(message = "历史密码记录盐值不能为空", groups = {add.class, edit.class})
    @ChineseDescription("历史密码记录盐值")
    private String historyPasswordSalt;

    /**
     * 修改密码时间
     */
    @NotNull(message = "修改密码时间不能为空", groups = {add.class, edit.class})
    @ChineseDescription("修改密码时间")
	private String updatePasswordTime;

}
