/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.sys.api.enums;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import lombok.Getter;

/**
 * 节点类型：1-应用，2-菜单，3-功能，-1-所有权限
 *
 * @author fengshuonan
 * @since 2023/6/13 14:19
 */
@Getter
public enum PermissionNodeTypeEnum implements ReadableEnum<PermissionNodeTypeEnum> {

    /**
     * 应用
     */
    APP(1, "应用"),

    /**
     * 菜单
     */
    MENU(2, "菜单"),

    /**
     * 功能
     */
    OPTIONS(3, "功能"),

    /**
     * 所有权限
     */
    TOTAL(-1, "所有权限");

    private final Integer code;

    private final String message;

    PermissionNodeTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Object getKey() {
        return this.code;
    }

    @Override
    public Object getName() {
        return this.message;
    }

    @Override
    public PermissionNodeTypeEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (PermissionNodeTypeEnum value : PermissionNodeTypeEnum.values()) {
            if (value.code.equals(Convert.toInt(originValue))) {
                return value;
            }
        }
        return null;
    }
}
