package cn.stylefeng.roses.kernel.sys.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * 用户角色关联封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserRoleRequest extends BaseRequest {

    /**
     * 主键
     */
    @ChineseDescription("主键")
    private Long userRoleId;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    @NotNull(message = "用户id不能为空", groups = {bindRoles.class})
    private Long userId;

    /**
     * 角色id
     */
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 角色id集合，用在绑定用户角色
     */
    @ChineseDescription("角色id集合，用在绑定用户角色")
    @NotEmpty(message = "角色id集合不能为空", groups = bindRoles.class)
    private Set<Long> roleIdList;

    /**
     * 参数校验分组：用户绑定角色
     */
    public @interface bindRoles {
    }

}
