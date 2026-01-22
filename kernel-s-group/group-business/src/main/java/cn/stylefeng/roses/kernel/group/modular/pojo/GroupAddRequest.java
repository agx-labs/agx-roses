package cn.stylefeng.roses.kernel.group.modular.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 添加绑定分组的请求
 *
 * @author fengshuonan
 * @since 2025/10/27 11:06
 */
@Data
public class GroupAddRequest {

    /**
     * 所属业务编码
     */
    @ChineseDescription("所属业务编码")
    @NotBlank(message = "groupBizCode业务编码不能为空")
    private String groupBizCode;

    /**
     * 分组名称，可以绑定多个
     */
    @ChineseDescription("分组名称")
    @NotEmpty(message = "分组名称不能为空")
    private List<String> groupNameList;

    /**
     * 业务主键id集合
     */
    @ChineseDescription("业务主键id集合")
    @NotEmpty(message = "业务主键id集合不能为空")
    private List<Long> businessIdList;

}
