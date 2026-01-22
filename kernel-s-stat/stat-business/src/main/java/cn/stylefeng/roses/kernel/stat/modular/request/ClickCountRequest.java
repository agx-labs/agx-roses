package cn.stylefeng.roses.kernel.stat.modular.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户点击数量统计封装类
 *
 * @author fengshuonan
 * @since 2023/03/28 14:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClickCountRequest extends BaseRequest {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键id")
    private Long clickCountId;

    /**
     * 业务的分类标识
     */
    @ChineseDescription("业务的分类标识")
    private String businessType;

    /**
     * 业务的主键id
     */
    @ChineseDescription("业务的主键id")
    private Long businessKeyId;

    /**
     * 点击次数
     */
    @ChineseDescription("点击次数")
    private Long clickCount;

}
