package cn.stylefeng.roses.kernel.sys.modular.user.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色关联实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@TableName("sys_user_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserRole extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "user_role_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long userRoleId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 角色id
     */
    @TableField("role_id")
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 角色类型：10-系统角色，15-业务角色，20-公司角色
     */
    @TableField("role_type")
    @ChineseDescription("角色类型：10-系统角色，15-业务角色，20-公司角色")
    private Integer roleType;

    /**
     * 用户所属机构id
     */
    @TableField(value = "role_org_id")
    @ChineseDescription("用户所属机构id")
    private Long roleOrgId;

}
