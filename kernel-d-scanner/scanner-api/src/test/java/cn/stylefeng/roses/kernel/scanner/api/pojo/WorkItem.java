package cn.stylefeng.roses.kernel.scanner.api.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

/**
 * 工作项
 *
 * @author fengshuonan
 * @since 2024/8/5 10:35
 */
@Data
public class WorkItem {

    /**
     * 键
     */
    @ChineseDescription("键")
    private String key;

    /**
     * 值
     */
    @ChineseDescription("值")
    private Object value;

}
