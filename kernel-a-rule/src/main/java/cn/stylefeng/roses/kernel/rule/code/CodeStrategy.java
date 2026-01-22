package cn.stylefeng.roses.kernel.rule.code;

/**
 * 编码策略定义
 *
 * @author fengshuonan
 * @since 2025/4/20 18:40
 */
public enum CodeStrategy {

    /**
     * 混合编码
     */
    MIXED(62, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"),

    /**
     * 大写字母
     */
    UPPERCASE(26, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),

    /**
     * 纯小写字母
     */
    LOWERCASE(26, "abcdefghijklmnopqrstuvwxyz"),

    /**
     * 数字
     */
    DIGITS(10, "0123456789");

    final int base;

    final String charset;

    CodeStrategy(int base, String charset) {
        this.base = base;
        this.charset = charset;
    }

    /**
     * 生成规则的枚举
     *
     * @author fengshuonan
     * @since 2025/4/20 19:42
     */
    public static CodeStrategy getByCode(Integer codeStrategyType) {
        if (codeStrategyType.equals(1)) {
            return DIGITS;
        } else if (codeStrategyType.equals(2)) {
            return UPPERCASE;
        } else if (codeStrategyType.equals(3)) {
            return LOWERCASE;
        } else if (codeStrategyType.equals(4)) {
            return MIXED;
        }
        return null;
    }

}