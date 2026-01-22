package cn.stylefeng.roses.kernel.city.modular.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 行政区域异常相关枚举
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
@Getter
public enum AreaExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    AREA_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    AreaExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}