package cn.stylefeng.roses.kernel.db.api.maxsort;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 获取表中排序字段最大数的封装
 *
 * @author fengshuonan
 * @since 2023/10/29 16:58
 */
@Data
@AllArgsConstructor
public class MaxCountConfig {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 排序字段的名称
     */
    private String sortFieldName;

}
