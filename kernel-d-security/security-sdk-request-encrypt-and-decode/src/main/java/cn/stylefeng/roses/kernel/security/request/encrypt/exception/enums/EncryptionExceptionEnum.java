package cn.stylefeng.roses.kernel.security.request.encrypt.exception.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import cn.stylefeng.roses.kernel.security.request.encrypt.constants.EncryptionConstants;
import lombok.Getter;

/**
 * 请求解密，响应加密 异常枚举
 *
 * @author luojie
 * @since 2021/3/23 12:54
 */
@Getter
public enum EncryptionExceptionEnum implements AbstractExceptionEnum {

    /**
     * 请求解密失败，原始数据格式错误，原始数据要符合EncryptionDTO类的规范
     */
    RSA_DECRYPT_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + EncryptionConstants.ENCRYPTION_EXCEPTION_STEP_CODE + "01", "请求解密失败，原始数据格式错误"),

    /**
     * 响应数据时获取秘钥失败，无法加密
     */
    GET_SM4_KEY_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + EncryptionConstants.ENCRYPTION_EXCEPTION_STEP_CODE + "02", "响应数据时获取秘钥失败，无法加密");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    EncryptionExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }
}
