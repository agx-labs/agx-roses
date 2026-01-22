package cn.stylefeng.roses.kernel.sys.api.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织机构层级实例类
 *
 * @author fengshuonan
 * @since 2025/01/22 09:44
 */
@TableName("sys_hr_organization_level")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationLevel extends BaseEntity {

    /**
     * 层级的id
     */
    @TableId(value = "org_level_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("层级的id")
    private Long orgLevelId;

    /**
     * 层级的级别，例如：1、2
     */
    @TableField("level_number")
    @ChineseDescription("层级的级别，例如：1、2")
    private Integer levelNumber;

    /**
     * 层级的名称
     */
    @TableField("level_name")
    @ChineseDescription("层级的名称")
    private String levelName;

    /**
     * 层级的编码，需要填在org表中
     */
    @TableField("level_code")
    @ChineseDescription("层级的编码，需要填在org表中")
    private String levelCode;

    /**
     * 层级的颜色，16进制，不带#
     */
    @TableField("level_color")
    @ChineseDescription("层级的颜色，16进制，不带#")
    private String levelColor;

    /**
     * 删除标记：Y-已删除，N-未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ChineseDescription("删除标记：Y-已删除，N-未删除")
    @TableLogic
    private String delFlag;

    /**
     * 租户号
     */
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    @ChineseDescription("租户号")
    private Long tenantId;

}