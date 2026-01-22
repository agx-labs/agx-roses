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
package cn.stylefeng.roses.kernel.config.modular.pojo.param;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 系统配置类型请求参数
 *
 * @author fengshuonan
 * @since 2023/6/28 17:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysConfigTypeParam extends BaseRequest {

    /**
     * 配置类型id
     */
    @NotNull(message = "配置类型id不能为空", groups = {edit.class, delete.class, detail.class})
    @ChineseDescription("配置类型id")
    private Long configTypeId;

    /**
     * 配置类型名称
     */
    @NotBlank(message = "配置类型名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("配置类型名称")
    private String configTypeName;

    /**
     * 配置类型编码
     */
    @NotBlank(message = "配置类型编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("配置类型编码")
    private String configTypeCode;

    /**
     * 配置类型顺序
     */
    @NotNull(message = "配置类型顺序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("配置类型顺序")
    private BigDecimal configTypeSort;

}
