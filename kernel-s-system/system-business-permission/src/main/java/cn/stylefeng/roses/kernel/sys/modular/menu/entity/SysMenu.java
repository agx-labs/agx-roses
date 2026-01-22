package cn.stylefeng.roses.kernel.sys.modular.menu.entity;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.tree.buildpids.BasePidBuildModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 系统菜单实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@TableName(value = "sys_menu", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseExpandFieldEntity implements BasePidBuildModel {

    /**
     * 主键
     */
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long menuId;

    /**
     * 父id，顶级节点的父id是-1
     */
    @TableField("menu_parent_id")
    @ChineseDescription("父id，顶级节点的父id是-1")
    private Long menuParentId;

    /**
     * 父id集合，中括号包住，逗号分隔
     */
    @TableField("menu_pids")
    @ChineseDescription("父id集合，中括号包住，逗号分隔")
    private String menuPids;

    /**
     * 菜单的名称
     */
    @TableField("menu_name")
    @ChineseDescription("菜单的名称")
    private String menuName;

    /**
     * 菜单的编码
     */
    @TableField("menu_code")
    @ChineseDescription("菜单的编码")
    private String menuCode;

    /**
     * 所属应用id
     */
    @TableField("app_id")
    @ChineseDescription("所属应用id")
    private Long appId;

    /**
     * 排序
     */
    @TableField("menu_sort")
    @ChineseDescription("排序")
    private BigDecimal menuSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @TableField("status_flag")
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 备注
     */
    @TableField("remark")
    @ChineseDescription("备注")
    private String remark;

    /**
     * 菜单类型：10-后台菜单，20-纯前台路由界面，30-内部链接，40-外部链接，50-应用设计
     */
    @TableField("menu_type")
    @ChineseDescription("菜单类型：10-后台菜单，20-纯前台路由界面，30-内部链接，40-外部链接，50-应用设计")
    private Integer menuType;

    /**
     * 路由地址，浏览器显示的URL，例如/menu
     */
    @TableField("antdv_router")
    @ChineseDescription("路由地址，浏览器显示的URL，例如/menu")
    private String antdvRouter;

    /**
     * 前端组件名
     */
    @TableField("antdv_component")
    @ChineseDescription("前端组件名")
    private String antdvComponent;

    /**
     * 图标编码
     */
    @TableField("antdv_icon")
    @ChineseDescription("图标编码")
    private String antdvIcon;

    /**
     * 外部链接地址
     */
    @TableField("antdv_link_url")
    @ChineseDescription("外部链接地址")
    private String antdvLinkUrl;

    /**
     * 用于非菜单显示页面的重定向url设置
     */
    @TableField("antdv_active_url")
    @ChineseDescription("用于非菜单显示页面的重定向url设置")
    private String antdvActiveUrl;

    /**
     * 是否可见(分离版用)：Y-是，N-否
     */
    @TableField("antdv_visible")
    @ChineseDescription("是否可见(分离版用)：Y-是，N-否")
    private String antdvVisible;

    /**
     * 应用设计的业务id
     */
    @TableField("app_design_business_id")
    @ChineseDescription("应用设计的业务id")
    private Long appDesignBusinessId;

    /**
     * 应用设计的视图id
     */
    @TableField("app_design_view_id")
    @ChineseDescription("应用设计的视图id")
    private Long appDesignViewId;

    /**
     * 当前菜单的子菜单
     * <p>
     * 这个参数一般用在更新菜单树的顺序和上下级结构中
     */
    @TableField(exist = false)
    private List<SysMenu> children;

    /**
     * 父级菜单名称
     */
    @TableField(exist = false)
    @ChineseDescription("父级菜单名称")
    private String menuParentName;

    @Override
    public String pidBuildNodeId() {
        if (ObjectUtil.isEmpty(menuId)) {
            return "";
        }
        return menuId.toString();
    }

    @Override
    public String pidBuildParentId() {
        if (ObjectUtil.isEmpty(menuParentId)) {
            return "";
        }
        return menuParentId.toString();
    }

    @Override
    public void setPidBuildPidStructure(String pids) {
        this.menuPids = pids;
    }
}
