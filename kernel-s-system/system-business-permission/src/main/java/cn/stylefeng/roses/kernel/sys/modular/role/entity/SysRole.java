package cn.stylefeng.roses.kernel.sys.modular.role.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import cn.stylefeng.roses.kernel.sys.api.format.OrgNameFormatProcess;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 系统角色实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@TableName(value = "sys_role", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseExpandFieldEntity {

    /**
     * 主键id
     */
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long roleId;

    /**
     * 角色名称
     */
    @TableField("role_name")
    @ChineseDescription("角色名称")
    private String roleName;

    /**
     * 角色编码
     */
    @TableField("role_code")
    @ChineseDescription("角色编码")
    private String roleCode;

    /**
     * 序号
     */
    @TableField("role_sort")
    @ChineseDescription("序号")
    private BigDecimal roleSort;

    /**
     * 数据范围类型：10-仅本人数据，20-本部门数据，30-本部门及以下数据，31-本公司及以下数据，40-指定部门数据，50-全部数据
     * <p>
     * 角色的数据范围不再是单选，改为多选，用sys_role_data_scope这个表存
     */
    @TableField("data_scope_type")
    @ChineseDescription("数据范围类型：10-仅本人数据，20-本部门数据，30-本部门及以下数据，31-本公司及以下数据，40-指定部门数据，50-全部数据")
    @Deprecated
    private Integer dataScopeType;

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
     * 角色类型：10-系统角色，15-业务角色，20-公司角色
     */
    @TableField("role_type")
    @ChineseDescription("角色类型：10-系统角色，15-业务角色，20-公司角色")
    private Integer roleType;

    /**
     * 所属的角色分类id，如果是业务角色和公司角色可以加上所属分类
     */
    @TableField(value = "role_category_id", updateStrategy = FieldStrategy.ALWAYS, insertStrategy = FieldStrategy.ALWAYS)
    @ChineseDescription("所属的角色分类id，如果是业务角色和公司角色可以加上所属分类")
    private Long roleCategoryId;

    /**
     * 角色所属公司id，当角色类型为20时传此值
     */
    @TableField(value = "role_company_id", updateStrategy = FieldStrategy.ALWAYS, insertStrategy = FieldStrategy.ALWAYS)
    @ChineseDescription("角色所属公司id，当角色类型为20时传此值")
    @SimpleFieldFormat(processClass = OrgNameFormatProcess.class)
    private Long roleCompanyId;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    @ChineseDescription("租户id")
    private Long tenantId;

}
