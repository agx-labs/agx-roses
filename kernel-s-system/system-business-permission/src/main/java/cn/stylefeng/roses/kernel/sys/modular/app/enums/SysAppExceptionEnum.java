package cn.stylefeng.roses.kernel.sys.modular.app.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 系统应用异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Getter
public enum SysAppExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_APP_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "查询结果不存在"),

    /**
     * 不允许修改应用编码
     */
    APP_CODE_CANT_EDIT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "不允许修改应用编码"),

    /**
     * 该应用下有绑定菜单，无法删除应用
     */
    APP_BIND_MENU(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "该应用下有绑定菜单，无法删除应用");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysAppExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}