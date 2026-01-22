package cn.stylefeng.roses.kernel.sys.modular.role.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色菜单关联实例类
 *
 * @author fengshuonan
 * @date 2023/06/13 22:39
 */
@TableName("sys_role_menu")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleMenu extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "role_menu_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long roleMenuId;

    /**
     * 角色id
     */
    @TableField("role_id")
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 冗余字段，菜单所属的应用id
     */
    @TableField("app_id")
    @ChineseDescription("冗余字段，菜单所属的应用id")
    private Long appId;

    /**
     * 菜单id
     */
    @TableField("menu_id")
    @ChineseDescription("菜单id")
    private Long menuId;

}
