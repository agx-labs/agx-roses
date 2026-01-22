package cn.stylefeng.roses.kernel.sys.api.exception.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 组织机构信息异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@Getter
public enum OrgExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    HR_ORGANIZATION_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "查询结果不存在"),

    /**
     * 删除机构失败，该机构下有绑定员工
     */
    DELETE_ORGANIZATION_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "删除机构失败，该机构下有绑定员工"),

    /**
     * 用户没有该组织机构的权限，无法切换组织机构
     */
    UPDATE_LOGIN_USER_ORG_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "用户没有该组织机构的权限，无法切换组织机构"),

    /**
     * 机构层级参数为空，参数为：{}
     */
    ORG_LEVEL_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10004", "机构层级参数为空，参数为：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    OrgExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}