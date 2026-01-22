package cn.stylefeng.roses.kernel.rule.tree.factory.base;

import java.math.BigDecimal;

/**
 * 带排序功能的树排序接口
 *
 * @author fengshuonan
 * @since 2024/9/7 20:42
 */
public interface AbstractSortedTreeNode<T> extends AbstractTreeNode<T> {

    /**
     * 获取排序
     *
     * @author fengshuonan
     * @since 2024/9/7 20:43
     */
    BigDecimal getSort();

}
