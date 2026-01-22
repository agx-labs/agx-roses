package cn.stylefeng.roses.kernel.sys.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户数据范围封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserDataScopeRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long userDataScopeId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 机构id
     */
    @NotNull(message = "机构id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("机构id")
    private Long orgId;

}
