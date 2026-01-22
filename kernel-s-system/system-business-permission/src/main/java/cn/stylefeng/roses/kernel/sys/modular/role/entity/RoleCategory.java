package cn.stylefeng.roses.kernel.sys.modular.role.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pidset.callback.PidSettable;
import cn.stylefeng.roses.kernel.rule.tree.factory.base.AbstractTreeNode;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 角色分类实例类
 *
 * @author fengshuonan
 * @since 2025/01/22 17:51
 */
@TableName("sys_role_category")
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleCategory extends BaseEntity implements AbstractTreeNode<RoleCategory>, PidSettable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long id;

    /**
     * 父级角色分类id
     */
    @TableField("category_parent_id")
    @ChineseDescription("父级角色分类id")
    private Long categoryParentId;

    /**
     * 父级角色分类id集合
     */
    @TableField("category_pids")
    @ChineseDescription("父级角色分类id集合")
    private String categoryPids;

    /**
     * 角色分类名称
     */
    @TableField("role_category_name")
    @ChineseDescription("角色分类名称")
    private String roleCategoryName;

    /**
     * 角色分类类型：15-业务角色，20-公司角色
     */
    @TableField("category_type")
    @ChineseDescription("角色分类类型：15-业务角色，20-公司角色")
    private Integer categoryType;

    /**
     * 所属公司id，当类型为20-公司角色时使用
     */
    @TableField("company_id")
    @ChineseDescription("所属公司id，当类型为20-公司角色时使用")
    private Long companyId;

    /**
     * 角色分类排序
     */
    @TableField("fld_sort")
    @ChineseDescription("角色分类排序")
    private BigDecimal fldSort;

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

    /**
     * 角色类型子节点
     */
    @TableField(exist = false)
    @ChineseDescription("角色类型子节点")
    private List<RoleCategory> children;

    @Override
    public String getNodeId() {
        if (id == null) {
            return "";
        }
        return id.toString();
    }

    @Override
    public String getNodeParentId() {
        if (this.categoryParentId == null) {
            return "";
        }
        return this.categoryParentId.toString();
    }

    @Override
    public void setChildrenNodes(List<RoleCategory> childrenNodes) {
        this.children = childrenNodes;
    }

    @Override
    public Long getCurrentId() {
        return id;
    }

    @Override
    public Long getParentId() {
        return categoryParentId;
    }

    @Override
    public void setParentIdListString(String parentIdListString) {
        this.categoryPids = parentIdListString;
    }

    @Override
    public String getParentIdListString() {
        return this.categoryPids;
    }

}