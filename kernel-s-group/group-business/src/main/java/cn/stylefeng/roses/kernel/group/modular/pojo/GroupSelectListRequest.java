package cn.stylefeng.roses.kernel.group.modular.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 获取下拉选择列表的请求
 *
 * @author fengshuonan
 * @since 2025/10/27 11:01
 */
@Data
public class GroupSelectListRequest {

    /**
     * 所属业务编码
     */
    @ChineseDescription("所属业务编码")
    @NotBlank(message = "groupBizCode业务编码不能为空")
    private String groupBizCode;

}
