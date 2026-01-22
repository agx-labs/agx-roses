package cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新的授权界面，点击单个启用禁用的操作
 *
 * @author fengshuonan
 * @since 2024/1/17 21:25
 */
@Data
public class StatusControlRequest {

    /**
     * 用户id
     */
    @NotNull(message = "用户id")
    private Long userId;

    /**
     * 所操作的机构id
     */
    @NotNull(message = "所操作的机构id不能为空，请检查orgId参数")
    private Long orgId;

    /**
     * 是否选中
     */
    @NotNull(message = "是否选中不能为空")
    private Boolean checkedFlag;

}
