package cn.stylefeng.roses.kernel.sys.modular.role.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.annotation.EnumFieldFormat;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import cn.stylefeng.roses.kernel.sys.api.format.OrgNameFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.format.UserNameFormatProcess;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色数据范围实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@TableName(value = "sys_role_data_scope", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleDataScope extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "role_data_scope_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long roleDataScopeId;

    /**
     * 角色id
     */
    @TableField("role_id")
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 数据范围类型：10-仅本人数据，20-本部门数据，30-本部门及以下数据，31-本公司及以下数据，32-指定机构层级及以下
     * <p>
     * 40-指定机构集合数据，41-指定机构及以下，50-全部数据
     */
    @TableField("data_scope_type")
    @ChineseDescription("数据范围类型：10-仅本人数据，20-本部门数据，30-本部门及以下数据，31-本公司及以下数据，32-指定机构层级及以下，40-指定机构集合数据，41-指定机构及以下，50-全部数据")
    @EnumFieldFormat(processEnum = DataScopeTypeEnum.class)
    private Integer dataScopeType;

    /**
     * 层级的编码，用在类型为32-指定层级及以下，情况时使用
     */
    @TableField("org_level_code")
    @ChineseDescription("层级的编码，用在类型为32-指定层级及以下，情况时使用")
    private String orgLevelCode;

    /**
     * 指定机构集合列表，用在类型为40-指定机构集合数据，情况时使用
     */
    @TableField(value = "define_org_list", typeHandler = JacksonTypeHandler.class)
    @ChineseDescription("指定机构集合列表，用在类型为40-指定机构集合数据，情况时使用")
    @SimpleFieldFormat(processClass = OrgNameFormatProcess.class)
    private List<Long> defineOrgList;

    /**
     * 指定机构的id，用在类型为41-指定机构及以下，情况时使用
     */
    @TableField("define_org_id")
    @ChineseDescription("指定机构的id，用在类型为41-指定机构及以下，情况时使用")
    @SimpleFieldFormat(processClass = OrgNameFormatProcess.class)
    private Long defineOrgId;

    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    @SimpleFieldFormat(processClass = UserNameFormatProcess.class)
    private Long createUser;

    //-------------------------------非实体字段-------------------------------

    /**
     * 层级的详情
     */
    @TableField(exist = false)
    @ChineseDescription("层级的详情")
    private OrganizationLevel organizationLevel;

    //-------------------------------移除掉的字段-------------------------------

    /**
     * 机构id
     * <p>
     * 角色的数据范围改为多条记录，不再绑定单独的机构id
     */
    @TableField(exist = false)
    @ChineseDescription("机构id")
    @Deprecated
    private Long organizationId;

}
