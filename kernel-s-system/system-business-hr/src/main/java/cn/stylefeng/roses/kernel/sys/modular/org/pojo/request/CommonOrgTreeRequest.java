package cn.stylefeng.roses.kernel.sys.modular.org.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * 组织机构树的查询条件
 *
 * @author fengshuonan
 * @since 2023/7/13 20:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonOrgTreeRequest extends BaseRequest {

    /**
     * 父级id
     * <p>
     * 因为树是懒加载树，所以通过此参数控制查询哪个节点下的所有机构
     * <p>
     * 不传此参数，则默认查询所有一级的结构
     */
    @ChineseDescription("父级id，一级节点父id是-1")
    private Long orgParentId;

    /**
     * 定位组织机构状态的组织机构id集合
     * <p>
     * 当树展开时，更新一个树的基本信息，需要从新定位到展开节点时，就使用此参数
     */
    @ChineseDescription("定位组织机构状态的组织机构id集合")
    private Set<Long> indexOrgIdList;

    /**
     * 是否只查询公司列表
     * <p>
     * true-查询结果只返回公司，false-查询结果返回公司或部门，如果没传这个参数，则都返回
     */
    @ChineseDescription("是否只查询公司列表")
    private Boolean companySearchFlag;

}
