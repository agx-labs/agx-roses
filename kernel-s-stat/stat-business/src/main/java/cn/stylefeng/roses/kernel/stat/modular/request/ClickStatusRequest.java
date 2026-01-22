package cn.stylefeng.roses.kernel.stat.modular.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户点击状态封装类
 *
 * @author fengshuonan
 * @since 2023/03/28 14:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClickStatusRequest extends BaseRequest {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键id")
    private Long clickStatusId;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 业务的主键id
     */
    @ChineseDescription("业务的主键id")
    private Long businessKeyId;

    /**
     * 业务的分类标识
     */
    @ChineseDescription("业务的分类标识")
    private String businessType;

}
