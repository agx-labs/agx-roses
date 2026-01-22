package cn.stylefeng.roses.kernel.sys.modular.role.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 角色分类异常相关枚举
 *
 * @author fengshuonan
 * @since 2025/01/22 17:40
 */
@Getter
public enum RoleCategoryExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    ROLE_CATEGORY_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    RoleCategoryExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
