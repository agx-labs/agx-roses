package cn.stylefeng.roses.kernel.sys.modular.userapp.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户常用功能实例类
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
@TableName("sys_portal_user_app")
@Data
@EqualsAndHashCode(callSuper = true)
public class PortalUserApp extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "app_link_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long appLinkId;

    /**
     * 所属用户id
     */
    @TableField("user_id")
    @ChineseDescription("所属用户id")
    private Long userId;

    /**
     * 冗余字段，菜单所属的应用id
     */
    @TableField("app_id")
    @ChineseDescription("冗余字段，菜单所属的应用id")
    private Long appId;

    /**
     * 关联的菜单id
     */
    @TableField("menu_id")
    @ChineseDescription("关联的菜单id")
    private Long menuId;

}
