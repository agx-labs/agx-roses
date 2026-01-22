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
package cn.stylefeng.roses.kernel.dict.modular.entity;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseBusinessEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.tree.buildpids.BasePidBuildModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 字典实体
 *
 * @author fengshuonan
 * @since 2020/12/26 22:37
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict", autoResultMap = true)
@Data
public class SysDict extends BaseBusinessEntity implements BasePidBuildModel {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    @TableId(value = "dict_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("字典id")
    private Long dictId;

    /**
     * 字典类型的id
     */
    @TableField("dict_type_id")
    @ChineseDescription("字典类型的id")
    private Long dictTypeId;

    /**
     * 字典编码
     */
    @TableField("dict_code")
    @ChineseDescription("字典编码")
    private String dictCode;

    /**
     * 字典名称
     */
    @TableField("dict_name")
    @ChineseDescription("字典名称")
    private String dictName;

    /**
     * 字典名称首字母
     */
    @TableField("dict_name_pinyin")
    @ChineseDescription("字典名称首字母")
    private String dictNamePinyin;

    /**
     * 字典英文编码
     */
    @TableField("dict_encode")
    @ChineseDescription("字典英文编码")
    private String dictEncode;

    /**
     * 字典简称
     */
    @TableField("dict_short_name")
    @ChineseDescription("字典简称")
    private String dictShortName;

    /**
     * 字典简称的编码
     */
    @TableField("dict_short_code")
    @ChineseDescription("字典简称的编码")
    private String dictShortCode;

    /**
     * 上级字典的id(如果没有上级字典id，则为-1)
     */
    @TableField("dict_parent_id")
    @ChineseDescription("上级字典的id(如果没有上级字典id，则为-1)")
    private Long dictParentId;

    /**
     * 父id集合
     */
    @TableField("dict_pids")
    @ChineseDescription("父id集合")
    private String dictPids;

    /**
     * 状态：(1-启用,2-禁用),参考 StatusEnum
     */
    @TableField("status_flag")
    @ChineseDescription("状态：(1-启用,2-禁用)")
    private Integer statusFlag;

    /**
     * 排序，带小数点
     */
    @TableField("dict_sort")
    @ChineseDescription("排序")
    private BigDecimal dictSort;

    /**
     * 字典类型的名称
     */
    @TableField(exist = false)
    @ChineseDescription("字典类型的名称")
    private String dictTypeName;

    /**
     * 字典上级的名称（字典有上下级，字典类型没有上下级）
     */
    @TableField(exist = false)
    @ChineseDescription("字典上级的名称（字典有上下级，字典类型没有上下级）")
    private String parentName;

    /**
     * 字典的下级结构
     */
    @TableField(exist = false)
    @ChineseDescription("字典的下级结构")
    private List<SysDict> children;

    @Override
    public String pidBuildNodeId() {
        if (ObjectUtil.isEmpty(dictId)) {
            return null;
        }
        return dictId.toString();
    }

    @Override
    public String pidBuildParentId() {
        if (ObjectUtil.isEmpty(dictParentId)) {
            return null;
        }
        return dictParentId.toString();
    }

    @Override
    public void setPidBuildPidStructure(String pids) {
        this.dictPids = pids;
    }

}
