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
package cn.stylefeng.roses.kernel.rule.enums;

import lombok.Getter;

/**
 * 解析字段元数据时，各种情况的枚举
 *
 * @author fengshuonan
 * @since 2022/1/13 16:37
 */
@Getter
public enum FieldTypeEnum {

    /**
     * 字符串，描述java中的String字段，Long归属为字符串，前端识别不了Long
     */
    STRING(10),

    /**
     * 数字类型，描述java中的int、Integer、Double、Float、BigDecimal等
     */
    NUMBER(11),

    /**
     * 基础数组类型，描述java中的数组，例如long[]、SysUser[]
     */
    ARRAY(20),

    /**
     * 集合类型，List、List<SysUser>、List<String>
     */
    COLLECTION(30),

    /**
     * 单纯对象类型，不带泛型，例如SysUser
     */
    OBJECT(40),

    /**
     * 对象类型携带泛型的，需要再解析泛型中的实体，例如ResponseData<SysUser>
     */
    OBJECT_WITH_GENERIC(50),

    /**
     * 带T类型的泛型对象，需要从所属类上再拿到具体泛型的类型，例如字段T
     */
    WITH_UNKNOWN_OBJ_GENERIC(60),

    /**
     * 带T类型的泛型对象，需要从所属类上再拿到具体泛型的类型，例如字段List<T>
     */
    WITH_UNKNOWN_ARRAY_GENERIC(61),

    /**
     * Map类型，例如Map<String, Object>、Map<String, SysUser>
     */
    MAP(70),

    /**
     * 其他类型，未知
     */
    OTHER(80);

    FieldTypeEnum(Integer code) {
        this.code = code;
    }

    private final Integer code;

}
