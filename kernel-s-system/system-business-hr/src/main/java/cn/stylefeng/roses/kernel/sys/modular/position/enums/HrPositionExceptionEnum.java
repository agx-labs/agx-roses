package cn.stylefeng.roses.kernel.sys.modular.position.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 职位信息异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:25
 */
@Getter
public enum HrPositionExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    HR_POSITION_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    HrPositionExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}