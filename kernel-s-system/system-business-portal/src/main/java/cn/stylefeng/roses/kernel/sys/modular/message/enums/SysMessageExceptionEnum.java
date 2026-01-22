package cn.stylefeng.roses.kernel.sys.modular.message.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 系统消息异常相关枚举
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
@Getter
public enum SysMessageExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_MESSAGE_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysMessageExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
