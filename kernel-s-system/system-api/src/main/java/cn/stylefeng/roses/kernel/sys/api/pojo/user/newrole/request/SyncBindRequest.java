package cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindItem;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 同步到其他公司的绑定的请求参数
 *
 * @author fengshuonan
 * @since 2024-01-18 17:12
 */
@Data
public class SyncBindRequest {

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 用户所属机构id
     */
    @ChineseDescription("用户所属机构id")
    @NotNull(message = "用户所属机构id不能为空")
    private Long orgId;

    /**
     * 用户所属机构的状态，true-启用
     */
    @ChineseDescription("用户所属机构的状态，true-启用")
    @NotNull(message = "用户所属机构的状态不能为空")
    private Boolean statusFlag;

    /**
     * 角色绑定的详情
     */
    @ChineseDescription("角色绑定的详情")
    private List<NewUserRoleBindItem> roleBindItemList;

}
