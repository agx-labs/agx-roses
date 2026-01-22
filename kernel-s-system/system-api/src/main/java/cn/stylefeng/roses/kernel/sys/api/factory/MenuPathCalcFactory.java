package cn.stylefeng.roses.kernel.sys.api.factory;

import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 菜单路径计算
 *
 * @author fengshuonan
 * @since 2024/6/24 12:52
 */
public class MenuPathCalcFactory {

    /**
     * 获取指定菜单id对应的父级id路径上，所有id的集合
     *
     * @return 返回的id集合包含参数的menuId
     * @author fengshuonan
     * @since 2024/6/24 12:52
     */
    public static Set<Long> getMenuParentIds(Long menuId, Map<Long, Long> menuIdParentIdMap) {
        Set<Long> uniquePathMenuIds = new HashSet<>();
        uniquePathMenuIds.add(menuId);

        findUniquePathMenuIdsRecursive(menuId, menuIdParentIdMap, uniquePathMenuIds);
        return uniquePathMenuIds;
    }

    /**
     * 递归查询
     *
     * @author fengshuonan
     * @since 2024/6/24 13:01
     */
    private static void findUniquePathMenuIdsRecursive(Long currentMenuId, Map<Long, Long> menuParentMap, Set<Long> uniquePathMenuIds) {

        // 获取当前菜单的父菜单ID
        Long parentId = menuParentMap.get(currentMenuId);

        // 如果父菜单ID为null或者-1，说明已经到达顶级节点，结束递归
        if (parentId == null || TreeConstants.DEFAULT_PARENT_ID.equals(parentId)) {
            return;
        }

        // 将当前菜单ID加入结果集合
        uniquePathMenuIds.add(parentId);

        // 继续递归向上找
        findUniquePathMenuIdsRecursive(parentId, menuParentMap, uniquePathMenuIds);
    }

}
