package cn.stylefeng.roses.kernel.sys.api.enums.role;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import lombok.Getter;

/**
 * 角色类型
 *
 * @author fengshuonan
 * @since 2024/1/15 23:27
 */
@Getter
public enum RoleTypeEnum implements ReadableEnum<RoleTypeEnum> {

    /**
     * 系统角色
     */
    SYSTEM_ROLE(10, "系统角色"),

    /**
     * 业务角色
     */
    BUSINESS_ROLE(15, "业务角色"),

    /**
     * 公司角色
     */
    COMPANY_ROLE(20, "公司角色");

    private final Integer code;

    private final String name;

    RoleTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Object getKey() {
        return code;
    }

    @Override
    public Object getName() {
        return name;
    }

    @Override
    public RoleTypeEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (RoleTypeEnum value : RoleTypeEnum.values()) {
            if (value.code.equals(Convert.toInt(originValue))) {
                return value;
            }
        }
        return null;
    }

}
