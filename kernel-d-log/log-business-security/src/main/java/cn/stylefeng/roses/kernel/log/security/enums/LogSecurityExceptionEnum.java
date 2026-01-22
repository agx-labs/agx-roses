package cn.stylefeng.roses.kernel.log.security.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 安全日志异常相关枚举
 *
 * @author fengshuonan
 * @since 2024/07/11 15:56
 */
@Getter
public enum LogSecurityExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    LOG_SECURITY_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    LogSecurityExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
