package cn.stylefeng.roses.kernel.file.modular.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 业务关联的文件异常相关枚举
 *
 * @author fengshuonan
 * @since 2023/03/31 13:30
 */
@Getter
public enum SysFileBusinessExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_FILE_BUSINESS_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysFileBusinessExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}