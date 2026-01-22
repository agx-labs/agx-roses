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
 * 角色权限限制实例类
 *
 * @author fengshuonan
 * @date 2023/09/08 12:55
 */
@TableName("sys_role_limit")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleLimit extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "role_limit_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long roleLimitId;

    /**
     * 角色id
     */
    @TableField("role_id")
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 角色限制类型：1-角色可分配的菜单，2-角色可分配的功能
     */
    @TableField("limit_type")
    @ChineseDescription("角色限制类型：1-角色可分配的菜单，2-角色可分配的功能")
    private Integer limitType;

    /**
     * 业务id，为菜单id或菜单功能id
     */
    @TableField("business_id")
    @ChineseDescription("业务id，为菜单id或菜单功能id")
    private Long businessId;

}
