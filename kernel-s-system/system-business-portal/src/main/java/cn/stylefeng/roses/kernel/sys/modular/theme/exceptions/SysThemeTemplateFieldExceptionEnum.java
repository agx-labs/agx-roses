package cn.stylefeng.roses.kernel.sys.modular.theme.exceptions;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import lombok.Getter;

/**
 * 系统主题模板属性异常类
 *
 * @author xixiaowei
 * @since 2021/12/23 17:01
 */
@Getter
public enum SysThemeTemplateFieldExceptionEnum implements AbstractExceptionEnum {

    /**
     * 系统主题模板属性不存在
     */
    FIELD_NOT_EXIST(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SysConstants.SYS_EXCEPTION_STEP_CODE + "101", "系统主题模板属性不存在"),

    /**
     * 系统主题模板属性被使用
     */
    FIELD_IS_USED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + SysConstants.SYS_EXCEPTION_STEP_CODE + "102",
            "系统主题模板属性正在被使用，不允许操作");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysThemeTemplateFieldExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }
}
