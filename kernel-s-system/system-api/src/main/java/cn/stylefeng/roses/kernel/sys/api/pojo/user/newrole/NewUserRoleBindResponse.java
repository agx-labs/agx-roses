package cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.util.List;

/**
 * 新的授权界面，用的传输结构
 *
 * @author fengshuonan
 * @since 2024/1/17 20:57
 */
@Data
public class NewUserRoleBindResponse {

    /**
     * 用户所属机构id
     */
    @ChineseDescription("用户所属机构id")
    private Long orgId;

    /**
     * 组织机构名称标识
     */
    @ChineseDescription("组织机构名称标识")
    private String orgName;

    /**
     * 是否启用本公司身份
     */
    @ChineseDescription("是否启用本公司身份")
    private Boolean enableThisOrg;

    /**
     * 角色绑定的详情
     */
    @ChineseDescription("角色绑定的详情")
    private List<NewUserRoleBindItem> roleBindItemList;

    /**
     * （后端组装参数临时使用的字段，前端不用管）
     */
    @ChineseDescription("（后端组装参数临时使用的字段，前端不用管）")
    private Long companyId;

}
