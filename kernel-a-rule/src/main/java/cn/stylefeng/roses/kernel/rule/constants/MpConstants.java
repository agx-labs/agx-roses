package cn.stylefeng.roses.kernel.rule.constants;

/**
 * 用于mybatis-plus的typeHandler常量
 *
 * @author fengshuonan
 * @since 2023/8/2 13:55
 */
public class MpConstants {

    /**
     * jackson模式
     * <p>
     * 一般用在手动使用LambdaUpdateWrapper时候，手动更新json字段，需要手动指定typeHandler
     */
    public static final String TYPE_HANDLER_STRING_JACKSON = "typeHandler=com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler";

    /**
     * gson模式
     * <p>
     * 一般用在手动使用LambdaUpdateWrapper时候，手动更新json字段，需要手动指定typeHandler
     */
    public static final String TYPE_HANDLER_STRING_GSON = "typeHandler=com.baomidou.mybatisplus.extension.handlers.GsonTypeHandler";

    /**
     * fastjson模式
     * <p>
     * 一般用在手动使用LambdaUpdateWrapper时候，手动更新json字段，需要手动指定typeHandler
     */
    public static final String TYPE_HANDLER_STRING_FASTJSON = "typeHandler=com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler";

}