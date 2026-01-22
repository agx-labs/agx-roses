package cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单资源绑定封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuResourceRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long menuResourceId;

    /**
     * 绑定资源的类型：1-菜单，2-菜单下按钮
     */
    @NotNull(message = "绑定资源的类型：1-菜单，2-菜单下按钮不能为空", groups = {add.class, edit.class})
    @ChineseDescription("绑定资源的类型：1-菜单，2-菜单下按钮")
    private Integer businessType;

    /**
     * 菜单或按钮id
     */
    @NotNull(message = "菜单或按钮id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("菜单或按钮id")
    private Long businessId;

    /**
     * 资源的编码
     */
    @NotBlank(message = "资源的编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("资源的编码")
    private String resourceCode;

}
