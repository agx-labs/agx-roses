package cn.stylefeng.roses.kernel.sys.modular.userapp.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 用户常用功能异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
@Getter
public enum PortalUserAppExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    PORTAL_USER_APP_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    PortalUserAppExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}