package cn.stylefeng.roses.kernel.sys.api.pojo.menu;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

/**
 * 菜单详情
 * <p>
 * 用在渲染用户的常用功能
 *
 * @author fengshuonan
 * @since 2023/6/26 21:35
 */
@Data
public class UserAppMenuInfo {

    /**
     * 菜单id
     */
    @ChineseDescription("菜单id")
    private Long menuId;

    /**
     * 菜单名称
     */
    @ChineseDescription("菜单名称")
    private String menuName;

    /**
     * 菜单图标
     */
    @ChineseDescription("菜单图标")
    private String menuIcon;

    /**
     * 菜单路由
     */
    @ChineseDescription("菜单路由")
    private String menuRouter;

}
