package cn.stylefeng.roses.kernel.scanner.api.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 递归调用的测试
 *
 * @author fengshuonan
 * @since 2024/8/5 15:28
 */
@Data
public class CircularObject {

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 用户名
     */
    @ChineseDescription("用户名")
    @NotEmpty(message = "用户名不能为空")
    private String userName;

    /**
     * 年龄
     */
    @ChineseDescription("年龄")
    @NotNull(message = "年龄不能为空")
    private Integer age;

    /**
     * 用户分数
     */
    @ChineseDescription("用户分数")
    @NotNull(message = "用户分数不能为空")
    private BigDecimal userScore;

    /**
     * 递归列表
     */
    @ChineseDescription("递归列表")
    @NotEmpty(message = "递归列表")
    private List<CircularObject> circularObjectList;


}
