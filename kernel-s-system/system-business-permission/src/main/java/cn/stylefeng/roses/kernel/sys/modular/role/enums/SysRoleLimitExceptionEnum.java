package cn.stylefeng.roses.kernel.sys.modular.role.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 角色权限限制异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/09/08 12:55
 */
@Getter
public enum SysRoleLimitExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_ROLE_LIMIT_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysRoleLimitExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}