package cn.stylefeng.roses.kernel.rule.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测传递参数是否有注入漏洞风险
 *
 * @author fengshuonan
 * @since 2023/5/30 16:10
 */
public class SqlInjectionDetector {

    private static final String SQL_KEYWORD_PATTERN = "\\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT(\\s+INTO){0,1}|MERGE|SELECT|UPDATE)\\b.*";

    /**
     * 检查字符串参数是否存在 SQL 注入风险
     *
     * @param param 待检查的字符串参数
     * @return 如果存在 SQL 注入风险返回 true，否则返回 false
     */
    public static boolean hasSqlInjection(String param) {
        if (param == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(SQL_KEYWORD_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(param);

        return matcher.matches();
    }

}
