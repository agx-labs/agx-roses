package cn.stylefeng.roses.kernel.sys.modular.menu.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 系统菜单异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Getter
public enum SysMenuExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_MENU_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "查询结果不存在"),

    /**
     * 菜单编码全局唯一，不能重复，请更换编码
     */
    MENU_CODE_REPEAT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "菜单编码全局唯一，不能重复，请更换编码"),

    /**
     * 路由地址不能为空
     */
    URL_CANT_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "路由地址不能为空"),

    /**
     * 组件代码路径不能为空
     */
    COMPONENT_PATH_CANT_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10004", "组件代码路径不能为空"),

    /**
     * 是否隐藏不能为空
     */
    HIDDEN_FLAG_CANT_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10005", "是否隐藏不能为空"),

    /**
     * 链接地址不能为空
     */
    LINK_URL_CANT_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10006", "链接地址不能为空");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysMenuExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}