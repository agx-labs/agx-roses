package cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新的授权界面，点击单个角色绑定的请求参数
 *
 * @author fengshuonan
 * @since 2024/1/17 21:25
 */
@Data
public class RoleControlRequest {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空，请检查userId参数")
    private Long userId;

    /**
     * 所操作的机构id
     */
    @NotNull(message = "所操作的机构id不能为空，请检查orgId参数")
    private Long orgId;

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空，请检查roleId参数")
    private Long roleId;

    /**
     * 角色类型：10-系统角色，15-业务角色，20-公司角色
     */
    @NotNull(message = "角色类型不能为空，请检查roleType参数")
    private Integer roleType;

    /**
     * 是否选中
     */
    @NotNull(message = "是否选中不能为空")
    private Boolean checkedFlag;

}
