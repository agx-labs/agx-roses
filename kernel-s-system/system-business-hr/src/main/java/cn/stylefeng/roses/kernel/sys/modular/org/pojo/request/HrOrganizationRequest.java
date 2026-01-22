package cn.stylefeng.roses.kernel.sys.modular.org.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.validator.api.validators.unique.TableUniqueValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 组织机构信息封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrOrganizationRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class, detail.class})
    @ChineseDescription("主键")
    private Long orgId;

    /**
     * 父id，一级节点父id是0
     */
    @NotNull(message = "父id，一级节点父id是-1不能为空", groups = {add.class, edit.class})
    @ChineseDescription("父id，一级节点父id是-1")
    private Long orgParentId;

    /**
     * 父ids
     */
    @ChineseDescription("父ids")
    private String orgPids;

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("组织名称")
    private String orgName;

    /**
     * 组织机构简称
     */
    @ChineseDescription("组织机构简称")
    private String orgShortName;

    /**
     * 组织编码
     */
    @NotBlank(message = "组织编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("组织编码")
    @TableUniqueValue(message = "组织编码存在重复", groups = {add.class, edit.class}, tableName = "sys_hr_organization",
            columnName = "org_code", idFieldName = "org_id", excludeLogicDeleteItems = true)
    private String orgCode;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal orgSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @ChineseDescription("状态：1-启用，2-禁用")
    @NotNull(message = "状态不能为空", groups = {add.class, edit.class})
    private Integer statusFlag;

    /**
     * 组织机构类型：1-公司，2-部门
     */
    @ChineseDescription("组织机构类型：1-公司，2-部门")
    @NotNull(message = "组织机构类型不能为空", groups = {add.class, edit.class})
    private Integer orgType;

    /**
     * 税号
     */
    @ChineseDescription("税号")
    private String taxNo;

    /**
     * 描述
     */
    @ChineseDescription("描述")
    private String remark;

    /**
     * 组织机构层级
     */
    @ChineseDescription("组织机构层级")
    private Integer orgLevel;

    /**
     * 对接外部主数据的机构id
     */
    @ChineseDescription("对接外部主数据的机构id")
    private String masterOrgId;

    /**
     * 组织机构层级编码，来自sys_hr_organization_level表
     */
    @ChineseDescription("组织机构层级编码，来自sys_hr_organization_level表")
    private String levelCode;

    /**
     * 对接外部主数据的父级机构id
     */
    @ChineseDescription("对接外部主数据的父级机构id")
    private String masterOrgParentId;

    /**
     * 组织机构id集合
     * <p>
     * 用在批量删除
     */
    @NotEmpty(message = "组织机构id集合不能为空", groups = {batchDelete.class})
    @ChineseDescription("组织机构id集合")
    private Set<Long> orgIdList;

    /**
     * 是否只查询公司列表
     * <p>
     * true-查询结果只返回公司，false-查询结果返回公司或部门，如果没传这个参数，则都返回
     */
    @ChineseDescription("是否只查询公司列表")
    private Boolean companySearchFlag;

}
