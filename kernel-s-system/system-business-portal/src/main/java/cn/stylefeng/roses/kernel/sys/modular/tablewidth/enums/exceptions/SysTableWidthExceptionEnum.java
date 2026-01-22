package cn.stylefeng.roses.kernel.sys.modular.tablewidth.enums.exceptions;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 业务中表的宽度异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/02/23 22:21
 */
@Getter
public enum SysTableWidthExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_TABLE_WIDTH_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "查询结果不存在"),

    /**
     * 无法修改全体人员的table配置，非超级管理员
     */
    PERMISSION_NOT_ALLOW(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "无法修改全体人员的table配置，非超级管理员");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysTableWidthExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}