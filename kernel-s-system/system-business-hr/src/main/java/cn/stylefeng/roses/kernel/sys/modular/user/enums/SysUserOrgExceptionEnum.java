package cn.stylefeng.roses.kernel.sys.modular.user.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 用户组织机构关联异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Getter
public enum SysUserOrgExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_USER_ORG_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "用户机构信息查询结果不存在"),

    /**
     * 绑定用户组织机构失败，参数存在缺失，请检查orgId，positionId，mainFlag
     */
    EMPTY_PARAM(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "绑定用户组织机构失败，参数存在缺失，请检查orgId，positionId，mainFlag"),

    /**
     * 用户主部门数量错误，请确保用户有且只有一个主部门
     */
    MAIN_FLAG_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "用户主部门数量错误，请确保用户有且只有一个主部门"),

    /**
     * 用户主部门数量错误，用户存在多个主部门信息，请纠正数据错误
     */
    MAIN_FLAG_COUNT_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10004", "用户主部门数量错误，用户存在多个主部门信息，请纠正数据错误，用户id为：{}"),

    /**
     * 用户所属主部门不能被禁用
     */
    MAIN_FLAG_DISABLE_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10005", "用户所属主部门不能被禁用，请修改状态");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysUserOrgExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}