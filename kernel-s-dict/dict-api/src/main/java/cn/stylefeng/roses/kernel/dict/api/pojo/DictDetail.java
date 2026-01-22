package cn.stylefeng.roses.kernel.dict.api.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 字典信息详情
 *
 * @author fengshuonan
 * @since 2023/6/28 18:41
 */
@Data
public class DictDetail {

    /**
     * 字典id
     */
    @ChineseDescription("字典id")
    private Long dictId;

    /**
     * 字典名称
     */
    @ChineseDescription("字典名称")
    private String dictName;

    /**
     * 字典编码
     */
    @ChineseDescription("字典编码")
    private String dictCode;

    /**
     * 排序，带小数点
     */
    @ChineseDescription("排序")
    private BigDecimal dictSort;

    public DictDetail() {
    }

    public DictDetail(Long dictId, String dictCode, String dictName, BigDecimal dictSort) {
        this.dictId = dictId;
        this.dictCode = dictCode;
        this.dictName = dictName;
        this.dictSort = dictSort;
    }
}
