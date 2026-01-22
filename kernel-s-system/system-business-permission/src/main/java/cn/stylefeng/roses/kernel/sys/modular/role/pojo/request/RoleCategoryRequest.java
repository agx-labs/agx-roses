package cn.stylefeng.roses.kernel.sys.modular.role.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 角色分类封装类
 *
 * @author fengshuonan
 * @since 2025/01/22 17:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleCategoryRequest extends BaseRequest {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键id")
    private Long id;

    /**
     * 父级角色分类id
     */
    @NotNull(message = "父级角色分类id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("父级角色分类id")
    private Long categoryParentId;

    /**
     * 角色分类名称
     */
    @NotBlank(message = "角色分类名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("角色分类名称")
    private String roleCategoryName;

    /**
     * 角色分类类型：15-业务角色，20-公司角色
     */
    @NotNull(message = "角色分类类型：15-业务角色，20-公司角色不能为空", groups = {add.class, edit.class, list.class})
    @ChineseDescription("角色分类类型：15-业务角色，20-公司角色")
    private Integer categoryType;

    /**
     * 所属公司id，当类型为20-公司角色时使用
     */
    @ChineseDescription("所属公司id，当类型为20-公司角色时使用")
    private Long companyId;

    /**
     * 角色分类排序
     */
    @ChineseDescription("角色分类排序")
    private BigDecimal fldSort;

    /**
     * 用在树查询上，用来作为忽略当条记录的传参
     */
    @ChineseDescription("用在树查询上，用来作为忽略当条记录的传参")
    private Long ignoreCategoryId;

}