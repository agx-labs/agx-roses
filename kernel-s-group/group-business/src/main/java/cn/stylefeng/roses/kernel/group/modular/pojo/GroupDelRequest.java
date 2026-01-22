package cn.stylefeng.roses.kernel.group.modular.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 删除分组的请求
 *
 * @author fengshuonan
 * @since 2025/10/27 11:44
 */
@Data
public class GroupDelRequest {

    /**
     * 所属业务编码
     */
    @ChineseDescription("所属业务编码")
    @NotBlank(message = "groupBizCode业务编码不能为空")
    private String groupBizCode;

    /**
     * 分组名称
     */
    @ChineseDescription("分组名称")
    @NotBlank(message = "分组名称不能为空")
    private String groupName;

    /**
     * 业务主键id集合
     */
    @ChineseDescription("业务主键id集合")
    @NotEmpty(message = "业务主键id集合不能为空")
    private List<Long> businessIdList;

}
