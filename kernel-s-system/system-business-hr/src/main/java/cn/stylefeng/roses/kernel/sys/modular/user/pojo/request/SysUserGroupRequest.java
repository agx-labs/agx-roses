package cn.stylefeng.roses.kernel.sys.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户组封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserGroupRequest extends BaseRequest {

    /**
     * 用户组id
     */
    @NotNull(message = "用户组id不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("用户组id")
    private Long userGroupId;

    /**
     * 用户分组标题简称
     */
    @ChineseDescription("用户分组标题简称")
    private String userGroupTitle;

    /**
     * 组内选择项的合并
     */
    @ChineseDescription("组内选择项的合并")
    private String userGroupDetailName;

}
