package cn.stylefeng.roses.kernel.sys.modular.login.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.tree.factory.base.AbstractTreeNode;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户所拥有的菜单信息
 *
 * @author fengshuonan
 * @since 2023/6/18 22:26
 */
@Data
public class IndexUserMenuInfo implements AbstractTreeNode<IndexUserMenuInfo> {

    /**
     * 菜单id
     */
    @ChineseDescription("菜单id")
    private Long menuId;

    /**
     * 菜单父级id
     */
    @ChineseDescription("菜单父级id")
    private Long menuParentId;

    /**
     * 菜单类型：10-后台菜单，20-纯前台路由界面，30-内部链接，40-外部链接，50-应用设计
     */
    @ChineseDescription("菜单类型：10-后台菜单，20-纯前台路由界面，30-内部链接，40-外部链接，50-应用设计")
    private Integer menuType;

    /**
     * 菜单的名称
     */
    @ChineseDescription("菜单的名称")
    private String title;

    /**
     * 菜单的图标
     */
    @ChineseDescription("菜单的图标")
    private String icon;

    /**
     * 是否隐藏, 0否,1是(仅注册路由不显示左侧菜单)
     */
    @ChineseDescription("是否隐藏, 0否,1是(仅注册路由不显示左侧菜单)")
    private Boolean hide;

    /**
     * 配置选中的path地址，比如修改页面不在侧栏，打开后侧栏就没有选中，这个配置选中地址，非必须
     */
    @ChineseDescription("配置选中的path地址")
    private String active;

    /**
     * 路由地址(要以/开头)，必填
     */
    @ChineseDescription("路由地址(要以/开头)，必填")
    private String path;

    /**
     * 组件地址(组件要放在view目录下)，父级可以省略
     */
    @ChineseDescription("组件地址(组件要放在view目录下)，父级可以省略")
    private String component;

    /**
     * 排序
     */
    @ChineseDescription("排序")
    private BigDecimal sortNumber;

    /**
     * 路由元信息
     */
    @ChineseDescription("路由元信息")
    private String meta;

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
     * 子级菜单
     */
    @ChineseDescription("子级菜单")
    private List<IndexUserMenuInfo> children;

    @Override
    public String getNodeId() {
        if (this.menuId == null) {
            return null;
        }
        return this.menuId.toString();
    }

    @Override
    public String getNodeParentId() {
        if (this.menuParentId == null) {
            return TreeConstants.DEFAULT_PARENT_ID.toString();
        }
        return this.menuParentId.toString();
    }

    @Override
    public void setChildrenNodes(List<IndexUserMenuInfo> childrenNodes) {
        this.children = childrenNodes;
    }

}
