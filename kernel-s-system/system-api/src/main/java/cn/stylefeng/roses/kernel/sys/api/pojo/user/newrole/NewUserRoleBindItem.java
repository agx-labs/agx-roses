package cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

/**
 * 角色绑定的详情
 *
 * @author fengshuonan
 * @since 2024/1/17 21:08
 */
@Data
public class NewUserRoleBindItem {

    /**
     * 角色id
     */
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 名称
     */
    @ChineseDescription("名称")
    private String roleName;

    /**
     * 角色类型：10-系统角色，15-业务角色，20-公司角色
     */
    @ChineseDescription("角色类型：10-系统角色，15-业务角色，20-公司角色")
    private Integer roleType;

    /**
     * 是否选中
     */
    @ChineseDescription("是否选中")
    private Boolean checkedFlag;

}
