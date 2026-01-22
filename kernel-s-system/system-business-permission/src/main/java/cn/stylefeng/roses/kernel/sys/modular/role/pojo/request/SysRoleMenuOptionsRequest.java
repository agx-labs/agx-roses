package cn.stylefeng.roses.kernel.sys.modular.role.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色和菜单下的功能关联封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleMenuOptionsRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long roleMenuOptionId;

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 菜单功能id，关联sys_menu_options主键id
     */
    @NotNull(message = "菜单功能id，关联sys_menu_options主键id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("菜单功能id，关联sys_menu_options主键id")
    private Long menuOptionId;

}
