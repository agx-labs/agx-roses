package cn.stylefeng.roses.kernel.sys.api.exception.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 获取最大排序数量
 *
 * @author fengshuonan
 * @since 2023/10/29 17:46
 */
@Getter
public enum MaxSortExceptionEnum implements AbstractExceptionEnum {

    /**
     * 编码不存在
     */
    CANT_FIND_CODE(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "编码不存在，无法获取");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    MaxSortExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}