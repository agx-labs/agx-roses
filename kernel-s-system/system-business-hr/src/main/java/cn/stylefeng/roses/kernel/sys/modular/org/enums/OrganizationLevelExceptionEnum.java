package cn.stylefeng.roses.kernel.sys.modular.org.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 组织机构层级异常相关枚举
 *
 * @author fengshuonan
 * @since 2025/01/22 09:44
 */
@Getter
public enum OrganizationLevelExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    ORGANIZATION_LEVEL_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "查询结果不存在"),

    /**
     * 层级不能重复
     */
    NUMBER_CANT_REPEAT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "层级不能重复"),

    /**
     * 组织机构层级编码不能重复
     */
    CODE_CANT_REPEAT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "层级编码不能重复"),

    /**
     * 组织机构层级名称不能重复
     */
    NAME_CANT_REPEAT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "层级名称不能重复");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    OrganizationLevelExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}
