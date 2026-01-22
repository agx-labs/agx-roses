package cn.stylefeng.roses.kernel.auth.api.expander;

import cn.stylefeng.roses.kernel.auth.api.constants.LoginCacheConstants;
import cn.stylefeng.roses.kernel.config.api.context.ConfigContext;

/**
 * 登录相关的配置
 *
 * @author fengshuonan
 * @since 2023/10/3 20:24
 */
public class LoginConfigExpander {

    /**
     * 最大错误登录次数的配置code
     */
    public static final String SYS_LOGIN_MAX_ERROR_LOGIN_COUNT = "SYS_LOGIN_MAX_ERROR_LOGIN_COUNT";

    /**
     * 最小密码长度的配置code
     */
    public static final String SYS_LOGIN_MIN_PASSWORD_LENGTH = "SYS_LOGIN_MIN_PASSWORD_LENGTH";

    /**
     * 特殊字符最小数量的配置code
     */
    public static final String SYS_LOGIN_PASSWORD_MIN_SPECIAL_SYMBOL_COUNT = "SYS_LOGIN_PASSWORD_MIN_SPECIAL_SYMBOL_COUNT";

    /**
     * 大写字母最小数量的配置code
     */
    public static final String SYS_LOGIN_PASSWORD_MIN_UPPER_CASE_COUNT = "SYS_LOGIN_PASSWORD_MIN_UPPER_CASE_COUNT";

    /**
     * 小写字母最小数量的配置code
     */
    public static final String SYS_LOGIN_PASSWORD_MIN_LOWER_CASE_COUNT = "SYS_LOGIN_PASSWORD_MIN_LOWER_CASE_COUNT";

    /**
     * 数字最小数量的配置code
     */
    public static final String SYS_LOGIN_PASSWORD_MIN_NUMBER_COUNT = "SYS_LOGIN_PASSWORD_MIN_NUMBER_COUNT";

    /**
     * 多久更新一次密码的配置code
     */
    public static final String SYS_LOGIN_PASSWORD_MIN_UPDATE_DAYS = "SYS_LOGIN_PASSWORD_MIN_UPDATE_DAYS";

    /**
     * 密码最小不能重复次数的配置code
     */
    public static final String SYS_LOGIN_PASSWORD_MIN_CANT_REPEAT_TIMES = "SYS_LOGIN_PASSWORD_MIN_CANT_REPEAT_TIMES";

    /**
     * 获取单账号单端登录的开关
     * <p>
     * 单账号单端登录为限制一个账号多个浏览器登录
     *
     * @return true-开启单端限制，false-关闭单端限制
     * @author fengshuonan
     * @since 2020/10/21 14:31
     */
    public static boolean getSingleAccountLoginFlag() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_SINGLE_ACCOUNT_LOGIN_FLAG", Boolean.class, false);
    }

    /**
     * 登录密码是否进行RSA加密校验，默认关闭
     * <p>
     * 需要前端配合加密后再打开开关
     *
     * @author fengshuonan
     * @since 2022/10/16 23:28
     */
    public static Boolean getPasswordRsaValidateFlag() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_AUTH_PASSWORD_RSA_VALIDATE", Boolean.class, false);
    }

    /**
     * 密码策略：密码最大重试次数
     * <p>
     * 超过此次数则冻结账号
     *
     * @author fengshuonan
     * @since 2023/10/3 20:25
     */
    public static Integer getMaxErrorLoginCount() {
        return ConfigContext.me()
                .getSysConfigValueWithDefault(SYS_LOGIN_MAX_ERROR_LOGIN_COUNT, Integer.class, LoginCacheConstants.MAX_ERROR_LOGIN_COUNT);
    }

    /**
     * 密码策略：口令最小长度
     *
     * @author fengshuonan
     * @since 2023/10/3 20:39
     */
    public static Integer getMinPasswordLength() {
        return ConfigContext.me().getSysConfigValueWithDefault(SYS_LOGIN_MIN_PASSWORD_LENGTH, Integer.class, 6);
    }

    /**
     * 密码策略：最少特殊符号数量
     *
     * @author fengshuonan
     * @since 2023/10/3 20:44
     */
    public static Integer getPasswordMinSpecialSymbolCount() {
        return ConfigContext.me().getSysConfigValueWithDefault(SYS_LOGIN_PASSWORD_MIN_SPECIAL_SYMBOL_COUNT, Integer.class, 0);
    }

    /**
     * 密码策略：最少大写字母数量
     *
     * @author fengshuonan
     * @since 2023/10/3 21:14
     */
    public static Integer getPasswordMinUpperCaseCount() {
        return ConfigContext.me().getSysConfigValueWithDefault(SYS_LOGIN_PASSWORD_MIN_UPPER_CASE_COUNT, Integer.class, 0);
    }

    /**
     * 密码策略：最少小写字母数量
     *
     * @author fengshuonan
     * @since 2023/10/3 21:16
     */
    public static Integer getPasswordMinLowerCaseCount() {
        return ConfigContext.me().getSysConfigValueWithDefault(SYS_LOGIN_PASSWORD_MIN_LOWER_CASE_COUNT, Integer.class, 0);
    }

    /**
     * 密码策略：最少数字符号的数量
     *
     * @author fengshuonan
     * @since 2023/10/3 21:18
     */
    public static Integer getPasswordMinNumberCount() {
        return ConfigContext.me().getSysConfigValueWithDefault(SYS_LOGIN_PASSWORD_MIN_NUMBER_COUNT, Integer.class, 0);
    }

    /**
     * 密码策略：最少多久更新一次密码，单位天
     *
     * @author fengshuonan
     * @since 2023/10/3 21:27
     */
    public static Integer getPasswordMinUpdateDays() {
        return ConfigContext.me().getSysConfigValueWithDefault(SYS_LOGIN_PASSWORD_MIN_UPDATE_DAYS, Integer.class, 180);
    }

    /**
     * 密码策略：修改密码时，不能和历史密码重复的次数
     *
     * @author fengshuonan
     * @since 2023/10/3 21:50
     */
    public static Integer getPasswordMinCantRepeatTimes() {
        return ConfigContext.me().getSysConfigValueWithDefault(SYS_LOGIN_PASSWORD_MIN_CANT_REPEAT_TIMES, Integer.class, 0);
    }

}
