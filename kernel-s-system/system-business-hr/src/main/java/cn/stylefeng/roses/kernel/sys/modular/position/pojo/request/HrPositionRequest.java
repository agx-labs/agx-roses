package cn.stylefeng.roses.kernel.sys.modular.position.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 职位信息封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrPositionRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class, detail.class})
    @ChineseDescription("主键")
    private Long positionId;

    /**
     * 职位名称
     */
    @NotBlank(message = "职位名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("职位名称")
    private String positionName;

    /**
     * 职位编码
     */
    @NotBlank(message = "职位编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("职位编码")
    private String positionCode;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal positionSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 备注
     */
    @ChineseDescription("备注")
    private String remark;

    /**
     * 职位id集合
     * <p>
     * 用在批量删除
     */
    @NotNull(message = "职位id集合不能为空", groups = {batchDelete.class})
    @ChineseDescription("职位id集合")
    private Set<Long> positionIdList;

}
