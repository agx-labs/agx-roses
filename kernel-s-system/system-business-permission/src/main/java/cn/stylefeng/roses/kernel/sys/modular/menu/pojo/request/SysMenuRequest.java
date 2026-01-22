package cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 系统菜单封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class, detail.class})
    @ChineseDescription("主键")
    private Long menuId;

    /**
     * 父id，如果没有父级，则父级id为-1
     */
    @NotNull(message = "父id，顶级节点的父id是-1不能为空", groups = {add.class})
    @ChineseDescription("父id，顶级节点的父id是-1")
    private Long menuParentId;

    /**
     * 父id集合，中括号包住，逗号分隔
     */
    @ChineseDescription("父id集合，中括号包住，逗号分隔")
    private String menuPids;

    /**
     * 菜单的名称
     */
    @NotBlank(message = "菜单的名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("菜单的名称")
    private String menuName;

    /**
     * 菜单的编码
     */
    @NotBlank(message = "菜单的编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("菜单的编码")
    private String menuCode;

    /**
     * 所属应用id
     */
    @NotNull(message = "所属应用id不能为空", groups = {add.class})
    @ChineseDescription("所属应用id")
    private Long appId;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal menuSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag = 1;

    /**
     * 备注
     */
    @ChineseDescription("备注")
    private String remark;

    /**
     * 菜单类型：10-后台菜单，20-纯前台路由界面，30-内部链接，40-外部链接，50-应用设计
     */
    @ChineseDescription("菜单类型：10-后台菜单，20-纯前台路由界面，30-内部链接，40-外部链接，50-应用设计")
    @NotNull(message = "菜单类型不能为空", groups = {add.class, edit.class})
    private Integer menuType;

    /**
     * 路由地址，浏览器显示的URL，例如/menu
     */
    @ChineseDescription("路由地址，浏览器显示的URL，例如/menu")
    private String antdvRouter;

    /**
     * 前端组件名
     */
    @ChineseDescription("前端组件名")
    private String antdvComponent;

    /**
     * 图标编码
     */
    @ChineseDescription("图标编码")
    private String antdvIcon;

    /**
     * 外部链接地址
     */
    @ChineseDescription("外部链接地址")
    private String antdvLinkUrl;

    /**
     * 用于非菜单显示页面的重定向url设置
     */
    @ChineseDescription("用于非菜单显示页面的重定向url设置")
    private String antdvActiveUrl;

    /**
     * 是否可见(分离版用)：Y-是，N-否
     */
    @ChineseDescription("是否可见(分离版用)：Y-是，N-否")
    private String antdvVisible;

    /**
     * 应用设计的业务id
     */
    @ChineseDescription("应用设计的业务id")
    private Long appDesignBusinessId;

    /**
     * 应用设计的视图id
     */
    @ChineseDescription("应用设计的视图id")
    private Long appDesignViewId;

    /**
     * 指定应用的所有菜单集合（树结构）
     */
    @ChineseDescription("指定应用的所有菜单集合（树结构）")
    @NotEmpty(message = "指定应用的所有菜单集合不能为空", groups = updateMenuTree.class)
    private List<SysMenu> updateMenuTree;

    /**
     * 参数校验分组：更新菜单树
     */
    public @interface updateMenuTree {
    }

}
