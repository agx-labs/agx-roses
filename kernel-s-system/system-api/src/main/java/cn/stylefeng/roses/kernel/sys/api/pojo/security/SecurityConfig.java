package cn.stylefeng.roses.kernel.sys.api.pojo.security;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 安全策略配置的DTO
 *
 * @author fengshuonan
 * @since 2023/10/4 16:05
 */
@Data
public class SecurityConfig {

    /**
     * 最大密码重试次数
     */
    @ChineseDescription("最大密码重试次数")
    @NotNull(message = "最大密码重试次数不能为空")
    private Integer maxErrorLoginCount;

    /**
     * 密码策略：最少多久更新一次密码，单位天
     */
    @ChineseDescription("密码策略：最少多久更新一次密码，单位天")
    @NotNull(message = "密码策略：最少多久更新一次密码不能为空")
    private Integer passwordMinUpdateDays;

    /**
     * 密码历史不可重复次数
     */
    @ChineseDescription("密码历史不可重复次数")
    @NotNull(message = "密码历史不可重复次数不能为空")
    private Integer passwordMinCantRepeatTimes;

    /**
     * 密码策略：口令最小长度
     */
    @ChineseDescription("密码策略：口令最小长度")
    @NotNull(message = "密码策略：口令最小长度不能为空")
    private Integer minPasswordLength;

    /**
     * 密码策略：最少特殊符号数量
     */
    @ChineseDescription("密码策略：最少特殊符号数量")
    @NotNull(message = "密码策略：最少特殊符号数量不能为空")
    private Integer passwordMinSpecialSymbolCount;

    /**
     * 密码策略：最少大写字母数量
     */
    @ChineseDescription("密码策略：最少大写字母数量")
    @NotNull(message = "密码策略：最少大写字母数量不能为空")
    private Integer getPasswordMinUpperCaseCount;

    /**
     * 密码策略：最少小写字母数量
     */
    @ChineseDescription("密码策略：最少小写字母数量")
    @NotNull(message = "密码策略：最少小写字母数量不能为空")
    private Integer passwordMinLowerCaseCount;

    /**
     * 密码策略：最少数字符号的数量
     */
    @ChineseDescription("密码策略：最少数字符号的数量")
    @NotNull(message = "密码策略：最少数字符号的数量不能为空")
    private Integer passwordMinNumberCount;

}
