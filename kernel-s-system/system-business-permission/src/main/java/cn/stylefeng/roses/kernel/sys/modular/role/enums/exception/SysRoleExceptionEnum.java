package cn.stylefeng.roses.kernel.sys.modular.role.enums.exception;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 系统角色异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Getter
public enum SysRoleExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_ROLE_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "查询结果不存在"),

    /**
     * 角色编码不能被修改
     */
    SUPER_ADMIN_ROLE_CODE_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "角色编码不能被修改"),

    /**
     * 超级管理员不能被删除
     */
    SYSTEM_ROLE_CANT_DELETE(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "系统角色不能被删除"),

    /**
     * 非管理员，只能删除自己公司的角色
     */
    DEL_PERMISSION_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10004", "非管理员，只能删除自己公司的角色"),

    /**
     * 非管理员，不能添加系统角色
     */
    ROLE_TYPE_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10005", "非管理员，不能添加系统角色"),

    /**
     * 非管理员，不能添加其他公司角色
     */
    ROLE_COMPANY_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10006", "非管理员，不能添加其他公司角色");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysRoleExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}