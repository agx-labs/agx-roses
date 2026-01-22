package cn.stylefeng.roses.kernel.scanner.api.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 我的请求类
 *
 * @author fengshuonan
 * @since 2024/8/4 15:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExtendsSimpleObject extends SimpleObject {

    /**
     * 单位地址
     */
    @ChineseDescription("单位地址")
    private String companyAddress;

    /**
     * 工作项列表
     */
    @ChineseDescription("工作项列表")
    private List<WorkItem> workItemList;

}
