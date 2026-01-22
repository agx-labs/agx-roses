package cn.stylefeng.roses.kernel.dict.api.pojo;

import lombok.Data;

import java.util.List;

/**
 * 字典值
 *
 * @author fengshuonan
 * @since 2023/11/15 18:44
 */
@Data
public class DictTreeDto {

    /**
     * 字典的名称
     */
    private String dictLabel;

    /**
     * 字典的编码
     */
    private String dictValue;

    /**
     * 字典的子集
     */
    private List<DictTreeDto> children;

}
