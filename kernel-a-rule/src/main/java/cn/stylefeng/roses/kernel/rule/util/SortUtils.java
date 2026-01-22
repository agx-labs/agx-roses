package cn.stylefeng.roses.kernel.rule.util;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.util.sort.GetSortKey;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 排序工具类
 * <p>
 * 一般用来弥补数据库排序功能不足的情况
 *
 * @author fengshuonan
 * @since 2023/8/3 14:43
 */
public class SortUtils {

    /**
     * 对list进行排序，以keys数组传的顺序为准
     *
     * @author fengshuonan
     * @since 2023/8/3 14:44
     */
    public static <T extends GetSortKey> List<T> sortListByObjectKey(List<T> originList, List<?> keys) {

        if (ObjectUtil.isEmpty(originList) || ObjectUtil.isEmpty(keys)) {
            return originList;
        }

        List<T> newSortList = new LinkedList<>();
        for (Object key : keys) {
            for (T listItem : originList) {
                Object sortKey = listItem.getSortKey();
                if (ObjectUtil.equal(key, sortKey)) {
                    newSortList.add(listItem);
                }
            }
        }

        return newSortList;
    }

    /**
     * 对Set数组进行排序
     *
     * @author fengshuonan
     * @since 2023/11/1 18:41
     */
    public static <T extends GetSortKey> Set<T> sortListByObjectKey(Set<T> originList, Set<?> keys) {

        if (ObjectUtil.isEmpty(originList) || ObjectUtil.isEmpty(keys)) {
            return originList;
        }

        Set<T> newSortList = new LinkedHashSet<>();
        for (Object key : keys) {
            for (T listItem : originList) {
                Object sortKey = listItem.getSortKey();
                if (ObjectUtil.equal(key, sortKey)) {
                    newSortList.add(listItem);
                }
            }
        }

        return newSortList;
    }

}
