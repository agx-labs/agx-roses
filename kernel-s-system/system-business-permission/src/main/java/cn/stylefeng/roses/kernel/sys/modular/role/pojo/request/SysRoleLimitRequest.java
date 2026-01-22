package cn.stylefeng.roses.kernel.sys.modular.role.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色权限限制封装类
 *
 * @author fengshuonan
 * @date 2023/09/08 12:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleLimitRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long roleLimitId;

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 角色限制类型：1-角色可分配的菜单，2-角色可分配的功能
     */
    @NotNull(message = "角色限制类型：1-角色可分配的菜单，2-角色可分配的功能不能为空", groups = {add.class, edit.class})
    @ChineseDescription("角色限制类型：1-角色可分配的菜单，2-角色可分配的功能")
    private Integer limitType;

    /**
     * 业务id，为菜单id或菜单功能id
     */
    @NotNull(message = "业务id，为菜单id或菜单功能id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("业务id，为菜单id或菜单功能id")
    private Long businessId;

}
