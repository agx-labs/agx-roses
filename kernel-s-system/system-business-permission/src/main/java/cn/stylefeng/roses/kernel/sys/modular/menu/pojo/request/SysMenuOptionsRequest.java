package cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单下的功能操作封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuOptionsRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long menuOptionId;

    /**
     * 菜单id
     */
    @NotNull(message = "菜单id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("菜单id")
    private Long menuId;

    /**
     * 功能或操作的名称
     */
    @NotBlank(message = "功能或操作的名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("功能或操作的名称")
    private String optionName;

    /**
     * 功能或操作的编码
     */
    @NotBlank(message = "功能或操作的编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("功能或操作的编码")
    private String optionCode;

}
