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
package cn.stylefeng.roses.kernel.rule.annotation;

import cn.stylefeng.roses.kernel.rule.enums.FormatTypeEnum;

import java.lang.annotation.*;

/**
 * json字段的格式化，可以将字典编码转化为具体的中文含义
 * <p>
 * 因为不同字典类型，编码可能有重复的，所以需要指定一个字典类型编码，确定是在哪个字典类型下的转义
 *
 * @author fengshuonan
 * @since 2023/6/27 21:01
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DictCodeFieldFormat {

    /**
     * 字段格式化的类型 详情见：{@link FormatTypeEnum}
     * <p>
     * 默认采用包装型，不改变原有的字段
     */
    FormatTypeEnum formatType() default FormatTypeEnum.ADD_FIELD;

    /**
     * 字典类型的编码【必传】
     */
    String dictTypeCode();

}
