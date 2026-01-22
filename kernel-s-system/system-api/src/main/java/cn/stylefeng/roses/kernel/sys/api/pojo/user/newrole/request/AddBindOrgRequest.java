package cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新的授权界面，点击单个启用禁用的操作
 *
 * @author fengshuonan
 * @since 2024/1/17 21:25
 */
@Data
public class AddBindOrgRequest {

    /**
     * 用户id
     */
    @NotNull(message = "用户id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 机构id
     */
    @NotNull(message = "机构id不能为空，请检查orgId参数")
    @ChineseDescription("机构id")
    private Long orgId;

    /**
     * 职位id
     */
    @NotNull(message = "职位id不能为空，请检查positionId参数")
    @ChineseDescription("职位id")
    private Long positionId;

    /**
     * 是否是主部门：Y-是，N-不是
     */
    @ChineseDescription("是否是主部门：Y-是，N-不是")
    @NotNull(message = "是否是主部门不能为空")
    private String mainFlag;

    /**
     * 是否启用：1-启用，2-禁用
     */
    @ChineseDescription("是否启用：1-启用，2-禁用")
    @NotNull(message = "是否启用不能为空")
    private Integer statusFlag;

}
