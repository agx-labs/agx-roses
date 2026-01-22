package cn.stylefeng.roses.kernel.sys.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户组织机构关联封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserOrgRequest extends BaseRequest {

    /**
     * 企业员工主键id
     */
    @ChineseDescription("企业员工主键id")
    private Long userOrgId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {add.class, delete.class})
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 对接外部主数据的用户id
     */
    @ChineseDescription("对接外部主数据的用户id")
    private String masterUserId;

    /**
     * 所属机构id
     */
    @NotNull(message = "所属机构id不能为空", groups = {add.class})
    @ChineseDescription("所属机构id")
    private Long orgId;

    /**
     * 对接外部组织机构id
     */
    @ChineseDescription("对接外部组织机构id")
    private String masterOrgId;

    /**
     * 职位id
     */
    @ChineseDescription("职位id")
    private Long positionId;

    /**
     * 是否是主部门：Y-是，N-不是
     */
    @NotBlank(message = "是否是主部门：Y-是，N-不是不能为空", groups = {add.class})
    @ChineseDescription("是否是主部门：Y-是，N-不是")
    private String mainFlag;

    /**
     * 是否启用：1-启用，2-禁用
     */
    @ChineseDescription("是否启用：1-启用，2-禁用")
    @NotNull(message = "是否启用不能为空", groups = {add.class})
    private Integer statusFlag;

}
