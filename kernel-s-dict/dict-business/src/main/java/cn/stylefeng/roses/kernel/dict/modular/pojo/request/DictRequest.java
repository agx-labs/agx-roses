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
package cn.stylefeng.roses.kernel.dict.modular.pojo.request;

import cn.stylefeng.roses.kernel.dict.modular.entity.SysDict;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.validator.api.validators.status.StatusValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 字典请求参数封装
 *
 * @author fengshuonan
 * @since 2020/10/30 11:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictRequest extends BaseRequest {

    /**
     * 字典id
     */
    @NotNull(message = "id不能为空", groups = {edit.class, delete.class, detail.class})
    @ChineseDescription("字典id")
    private Long dictId;

    /**
     * 字典编码
     */
    @NotBlank(message = "字典编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("字典编码")
    private String dictCode;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("字典名称")
    private String dictName;

    /**
     * 字典名称拼音
     */
    @ChineseDescription("字典名称拼音")
    private String dictNamePinYin;

    /**
     * 字典编码
     */
    @ChineseDescription("字典编码")
    private String dictEncode;

    /**
     * 字典类型id
     */
    @NotNull(message = "字典类型id不能为空", groups = {add.class, edit.class, treeList.class})
    @ChineseDescription("字典类型id")
    private Long dictTypeId;

    /**
     * 字典简称
     */
    @ChineseDescription("字典简称")
    private String dictShortName;

    /**
     * 字典简称的编码
     */
    @ChineseDescription("字典简称的编码")
    private String dictShortCode;

    /**
     * 上级字典的id
     * <p>
     * 字典列表是可以有树形结构的，但是字典类型没有树形结构
     * <p>
     * 如果没有上级字典id，则为-1
     */
    @ChineseDescription("上级字典的id")
    @NotNull(message = "上级字典的id不能为空", groups = {edit.class, add.class})
    private Long dictParentId;

    /**
     * 状态(1:启用,2:禁用)，参考 StatusEnum
     */
    @NotNull(message = "状态不能为空", groups = {add.class, edit.class})
    @StatusValue(groups = {add.class, edit.class})
    @ChineseDescription("状态")
    private Integer statusFlag;

    /**
     * 排序，带小数点
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal dictSort;

    /**
     * 所有的父级id,逗号分隔
     */
    @ChineseDescription("所有的父级id")
    private String dictPids;

    /**
     * 搜索条件：字典类型编码
     */
    @ChineseDescription("搜索条件：字典类型编码")
    private String dictTypeCode;

    /**
     * 字典树的整个结构
     * <p>
     * 一般用在更新字典树接口中作为参数
     */
    @ChineseDescription("字典树的整个结构，一般用在更新字典树接口中作为参数")
    @NotEmpty(message = "字典树的整个结构不能为空", groups = updateTree.class)
    private List<SysDict> totalDictStructure;

    /**
     * 字典id集合，用在批量删除
     */
    @NotEmpty(message = "dictId集合不能为空", groups = batchDelete.class)
    @ChineseDescription("dictId集合，用在批量删除")
    private Set<Long> dictIdList;

    /**
     * 获取树形列表
     */
    public @interface treeList {

    }

    /**
     * 更新树结构
     */
    public @interface updateTree {

    }

}
