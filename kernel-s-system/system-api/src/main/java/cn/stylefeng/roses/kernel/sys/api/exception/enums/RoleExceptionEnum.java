package cn.stylefeng.roses.kernel.sys.api.exception.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 角色相关的异常
 *
 * @author fengshuonan
 * @since 2023/6/21 16:34
 */
@Getter
public enum RoleExceptionEnum implements AbstractExceptionEnum {

    /**
     * 用户没有该应用的权限，无法跳转到该应用
     */
    USER_HAVE_NO_APP_ID(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "用户没有该应用的权限，无法跳转到该应用"),

    /**
     * 数据范围类型转化异常
     */
    DATA_SCOPE_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "数据范围类型转化异常，数据范围类型为：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    RoleExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}