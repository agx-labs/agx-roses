package cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response;

import cn.stylefeng.roses.kernel.rule.tree.factory.base.AbstractTreeNode;
import lombok.Data;

import java.util.List;

/**
 * 用在菜单列表界面，描述菜单树上的菜单详情
 *
 * @author fengshuonan
 * @since 2023/6/14 21:37
 */
@Data
public class MenuItemDetail implements AbstractTreeNode<MenuItemDetail> {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 菜单父级id
     */
    private Long menuParentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单类型：10-后台菜单，20-纯前台路由界面，30-内部链接，40-外部链接，50-应用设计
     */
    private Integer menuType;

    /**
     * 菜单的子菜单
     */
    private List<MenuItemDetail> children;

    public MenuItemDetail() {
    }

    public MenuItemDetail(Long menuId, Long menuParentId, String menuName, Integer menuType) {
        this.menuId = menuId;
        this.menuParentId = menuParentId;
        this.menuName = menuName;
        this.menuType = menuType;
    }

    @Override
    public String getNodeId() {
        if (menuId != null) {
            return menuId.toString();
        }
        return null;
    }

    @Override
    public String getNodeParentId() {
        if (menuParentId != null) {
            return menuParentId.toString();
        }
        return null;
    }

    @Override
    public void setChildrenNodes(List<MenuItemDetail> childrenNodes) {
        this.children = childrenNodes;
    }

}
