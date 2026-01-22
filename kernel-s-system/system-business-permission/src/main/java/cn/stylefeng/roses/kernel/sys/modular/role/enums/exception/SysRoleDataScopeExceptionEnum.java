package cn.stylefeng.roses.kernel.sys.modular.role.enums.exception;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 角色数据范围异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Getter
public enum SysRoleDataScopeExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_ROLE_DATA_SCOPE_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "查询结果不存在"),

    /**
     * 存在参数为空，参数名称为：{}
     */
    PARAM_IS_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "存在参数为空，参数名称为：{}"),

    /**
     * 组织机构层级编码不正确
     */
    LEVEL_CODE_IS_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "组织机构层级编码不正确");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysRoleDataScopeExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}