package cn.stylefeng.roses.kernel.sys.modular.notice.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 通知管理异常相关枚举
 *
 * @author fengshuonan
 * @since 2024/01/12 16:06
 */
@Getter
public enum SysNoticeExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_NOTICE_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "通知不存在"),

    /**
     * 通知范围不能为空
     */
    SYS_NOTICE_SCOPE_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "通知范围不能为空"),

    /**
     * 通知已经发布，无法修改
     */
    SYS_NOTICE_CANT_EDIT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "通知已经发布，无法修改");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysNoticeExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
