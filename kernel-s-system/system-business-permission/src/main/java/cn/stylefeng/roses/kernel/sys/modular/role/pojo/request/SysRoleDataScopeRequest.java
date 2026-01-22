package cn.stylefeng.roses.kernel.sys.modular.role.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色数据范围封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleDataScopeRequest extends BaseRequest {

    /**
     * 主键
     */
    @ChineseDescription("主键")
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class, detail.class})
    private Long roleDataScopeId;

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空", groups = {page.class, add.class, edit.class})
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 数据范围类型：10-仅本人数据，20-本部门数据，30-本部门及以下数据，31-本公司及以下数据，32-指定机构层级及以下
     * <p>
     * 40-指定机构集合数据，41-指定机构及以下，50-全部数据
     */
    @ChineseDescription("数据范围类型：10-仅本人数据，20-本部门数据，30-本部门及以下数据，31-本公司及以下数据，32-指定机构层级及以下，40-指定机构集合数据，41-指定机构及以下，50-全部数据")
    @NotNull(message = "数据范围类型不能为空", groups = {add.class, edit.class})
    private Integer dataScopeType;

    /**
     * 层级的编码，用在类型为32-指定层级及以下，情况时使用
     */
    @ChineseDescription("层级的编码，用在类型为32-指定层级及以下，情况时使用")
    private String orgLevelCode;

    /**
     * 指定机构集合列表，用在类型为40-指定机构集合数据，情况时使用
     */
    @ChineseDescription("指定机构集合列表，用在类型为40-指定机构集合数据，情况时使用")
    private List<Long> defineOrgList;

    /**
     * 指定机构的id，用在类型为41-指定机构及以下，情况时使用
     */
    @ChineseDescription("指定机构的id，用在类型为41-指定机构及以下，情况时使用")
    private Long defineOrgId;

}
