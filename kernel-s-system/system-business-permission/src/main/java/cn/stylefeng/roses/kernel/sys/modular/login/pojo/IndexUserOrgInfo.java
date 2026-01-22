package cn.stylefeng.roses.kernel.sys.modular.login.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;


/**
 * 用户组织机构的绑定关系，用在首页获取用户信息
 *
 * @author fengshuonan
 * @since 2023/6/18 22:12
 */
@Data
public class IndexUserOrgInfo {

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 组织机构id
     */
    @ChineseDescription("组织机构id")
    private Long orgId;

    /**
     * 组织机构所属公司id
     */
    @ChineseDescription("组织机构所属公司id")
    private Long companyId;

    /**
     * 职务id，组织机构下对应的职务id
     */
    @ChineseDescription("职务id，组织机构下对应的职务id")
    private Long positionId;

    /**
     * 公司的名称
     */
    @ChineseDescription("公司的名称")
    private String companyName;

    /**
     * 部门的名称
     */
    @ChineseDescription("部门的名称")
    private String deptName;

    /**
     * 职务名称
     */
    @ChineseDescription("职务名称")
    private String positionName;

    /**
     * 是否是主要任职部门
     */
    @ChineseDescription("是否是主要任职部门")
    private Boolean mainFlag;

    /**
     * 是否是当前登录用户激活的（选中的）
     */
    @ChineseDescription("是否是当前登录用户激活的（选中的）")
    private Boolean currentSelectFlag;

}
