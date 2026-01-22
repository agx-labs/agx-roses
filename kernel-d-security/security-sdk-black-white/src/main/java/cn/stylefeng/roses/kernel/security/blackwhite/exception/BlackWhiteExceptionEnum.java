package cn.stylefeng.roses.kernel.security.blackwhite.exception;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 黑白名单校验限制
 *
 * @author fengshuonan
 * @since 2024/7/11 10:42
 */
@Getter
public enum BlackWhiteExceptionEnum implements AbstractExceptionEnum {

    /**
     * IP校验不通过，IP已被限制
     */
    IP_INVALID(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "IP校验不通过，IP已被限制");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    BlackWhiteExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}