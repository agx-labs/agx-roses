package cn.stylefeng.roses.kernel.sys.modular.org.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.tree.factory.base.AbstractSortedTreeNode;
import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 组织机构信息实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@TableName(value = "sys_hr_organization", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class HrOrganization extends BaseExpandFieldEntity implements AbstractSortedTreeNode<HrOrganization> {

    /**
     * 主键
     */
    @TableId(value = "org_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long orgId;

    /**
     * 父id，一级节点父id是0
     */
    @TableField("org_parent_id")
    @ChineseDescription("父id，一级节点父id是0")
    private Long orgParentId;

    /**
     * 父ids
     */
    @TableField("org_pids")
    @ChineseDescription("父ids")
    private String orgPids;

    /**
     * 组织名称
     */
    @TableField("org_name")
    @ChineseDescription("组织名称")
    private String orgName;

    /**
     * 组织机构简称
     */
    @TableField("org_short_name")
    @ChineseDescription("组织机构简称")
    private String orgShortName;

    /**
     * 组织编码
     */
    @TableField("org_code")
    @ChineseDescription("组织编码")
    private String orgCode;

    /**
     * 排序
     */
    @TableField("org_sort")
    @ChineseDescription("排序")
    private BigDecimal orgSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @TableField("status_flag")
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 组织机构类型：1-公司，2-部门
     */
    @TableField("org_type")
    @ChineseDescription("组织机构类型：1-公司，2-部门")
    private Integer orgType;

    /**
     * 税号
     */
    @TableField("tax_no")
    @ChineseDescription("税号")
    private String taxNo;

    /**
     * 描述
     */
    @TableField("remark")
    @ChineseDescription("描述")
    private String remark;

    /**
     * 组织机构层级
     */
    @TableField("org_level")
    @ChineseDescription("组织机构层级")
    private Integer orgLevel;

    /**
     * 对接外部主数据的机构id
     */
    @TableField("master_org_id")
    @ChineseDescription("对接外部主数据的机构id")
    private String masterOrgId;

    /**
     * 对接外部主数据的父级机构id
     */
    @TableField("master_org_parent_id")
    @ChineseDescription("对接外部主数据的父级机构id")
    private String masterOrgParentId;

    /**
     * 组织机构层级编码，来自sys_hr_organization_level表
     */
    @TableField("level_code")
    @ChineseDescription("组织机构层级编码，来自sys_hr_organization_level表")
    private String levelCode;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    @ChineseDescription("租户id")
    private Long tenantId;

    //-------------------------------非实体字段-------------------------------
    //-------------------------------非实体字段-------------------------------
    //-------------------------------非实体字段-------------------------------

    /**
     * 子节点的集合
     */
    @TableField(exist = false)
    @ChineseDescription("子节点的集合")
    private List<HrOrganization> children;

    /**
     * 父级id的名称
     */
    @TableField(exist = false)
    @ChineseDescription("父级id的名称")
    private String parentOrgName;

    /**
     * 组织机构所属公司的名称
     */
    @TableField(exist = false)
    @ChineseDescription("组织机构所属公司的名称")
    private String companyName;

    /**
     * 是否有子级：true-有子级，false-无子级
     */
    @TableField(exist = false)
    @ChineseDescription("是否有子级：true-有子级，false-无子级")
    private Boolean haveSubOrgFlag = false;

    /**
     * 层级的详情
     */
    @TableField(exist = false)
    @ChineseDescription("层级的详情")
    private OrganizationLevel organizationLevel;

    @Override
    public String getNodeId() {
        if (this.orgId == null) {
            return null;
        } else {
            return this.orgId.toString();
        }
    }

    @Override
    public String getNodeParentId() {
        if (this.orgParentId == null) {
            return null;
        } else {
            return this.orgParentId.toString();
        }
    }

    @Override
    public void setChildrenNodes(List<HrOrganization> childrenNodes) {
        this.children = childrenNodes;
    }

    @Override
    public BigDecimal getSort() {
        return this.orgSort;
    }
}
