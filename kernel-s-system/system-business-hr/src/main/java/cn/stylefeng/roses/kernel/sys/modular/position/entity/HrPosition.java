package cn.stylefeng.roses.kernel.sys.modular.position.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 职位信息实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:25
 */
@TableName(value = "sys_hr_position", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class HrPosition extends BaseExpandFieldEntity {

    /**
     * 主键
     */
    @TableId(value = "position_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long positionId;

    /**
     * 职位名称
     */
    @TableField("position_name")
    @ChineseDescription("职位名称")
    private String positionName;

    /**
     * 职位编码
     */
    @TableField("position_code")
    @ChineseDescription("职位编码")
    private String positionCode;

    /**
     * 排序
     */
    @TableField("position_sort")
    @ChineseDescription("排序")
    private BigDecimal positionSort;

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
     * 租户id
     */
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    @ChineseDescription("租户id")
    private Long tenantId;

}
