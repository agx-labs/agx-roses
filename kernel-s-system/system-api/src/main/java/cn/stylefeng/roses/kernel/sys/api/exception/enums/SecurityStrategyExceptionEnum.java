package cn.stylefeng.roses.kernel.sys.api.exception.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 安全规则相关的异常
 *
 * @author fengshuonan
 * @since 2023/10/4 22:43
 */
@Getter
public enum SecurityStrategyExceptionEnum implements AbstractExceptionEnum {

    /**
     * 密码最小长度不符合规定
     */
    PASSWORD_LENGTH(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "密码最小长度不符合规定，最小长度为：{}"),

    /**
     * 密码特殊符号数量不能低于{}位
     */
    SPECIAL_SYMBOL(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "密码特殊符号数量不能低于{}位"),

    /**
     * 密码大写字母数量不能低于{}位
     */
    UPPER_CASE(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "密码大写字母数量不能低于{}位"),

    /**
     * 密码小写字母数量不能低于{}位
     */
    LOWER_CASE(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10004", "密码小写字母数量不能低于{}位"),

    /**
     * 密码数字字符数量不能低于{}位
     */
    NUMBER_SYMBOL(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10005", "密码数字字符数量不能低于{}位"),

    /**
     * 密码历史不可重复次数为{}次，请重新更换密码
     */
    PASSWORD_REPEAT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10006", "密码历史不可重复次数为{}次，请重新更换密码");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SecurityStrategyExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}