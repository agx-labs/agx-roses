package cn.stylefeng.roses.kernel.sys.modular.role.enums.exception;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 角色和菜单下的功能关联异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Getter
public enum SysRoleMenuOptionsExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_ROLE_MENU_OPTIONS_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysRoleMenuOptionsExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}