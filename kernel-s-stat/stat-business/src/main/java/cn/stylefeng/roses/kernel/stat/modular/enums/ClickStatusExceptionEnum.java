package cn.stylefeng.roses.kernel.stat.modular.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 用户点击状态异常相关枚举
 *
 * @author fengshuonan
 * @since 2023/03/28 14:52
 */
@Getter
public enum ClickStatusExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    PORTAL_CLICK_STATUS_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    ClickStatusExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}