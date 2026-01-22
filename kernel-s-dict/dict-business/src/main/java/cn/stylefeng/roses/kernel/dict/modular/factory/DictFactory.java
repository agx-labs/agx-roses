package cn.stylefeng.roses.kernel.dict.modular.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.dict.modular.entity.SysDict;

import java.math.BigDecimal;
import java.util.List;

/**
 * 字典构建工厂
 *
 * @author fengshuonan
 * @since 2023/6/27 18:32
 */
public class DictFactory {

    /**
     * 更新字典的排序
     *
     * @param tree  被更新的字典树
     * @param level 当前被更新的字典的层级（level从1开始）
     * @author fengshuonan
     * @since 2023/6/27 18:32
     */
    public static void updateSort(List<SysDict> tree, Integer level) {

        // 初始的排序值
        int i = 1;

        // 倍数，第1层级从100开始排列
        // 第2层是1000开始
        int beishu = 10;

        for (int integer = 0; integer < level; integer++) {
            beishu = beishu * 10;
        }

        // 第1层级是110，120，130
        // 第2层级是1010，1020，1030
        // 第3层级是10010，10020，10030
        for (SysDict sysDict : tree) {
            BigDecimal bigDecimal = new BigDecimal(1);
            bigDecimal = bigDecimal.multiply(new BigDecimal(beishu));
            bigDecimal = bigDecimal.add(new BigDecimal(i * 10));
            sysDict.setDictSort(bigDecimal);
            i++;

            // 递归修改子树
            List<SysDict> children = sysDict.getChildren();
            if (children != null && children.size() > 0) {
                updateSort(children, level + 1);
            }
        }
    }

    /**
     * 填充节点的父级id【递归】
     *
     * @author fengshuonan
     * @since 2023/6/27 18:35
     */
    public static void fillParentId(Long parentDictId, List<SysDict> treeList) {

        if (ObjectUtil.isEmpty(treeList)) {
            return;
        }

        for (SysDict sysMenu : treeList) {

            sysMenu.setDictParentId(parentDictId);

            if (ObjectUtil.isNotEmpty(sysMenu.getChildren())) {
                fillParentId(sysMenu.getDictId(), sysMenu.getChildren());
            }
        }

    }

    /**
     * 将指定的树形结构，平行展开，添加到指定的参数totalDictList
     *
     * @author fengshuonan
     * @since 2023/6/27 18:36
     */
    public static void collectTreeTasks(List<SysDict> dictTree, List<SysDict> totalDictList) {

        if (ObjectUtil.isEmpty(dictTree)) {
            return;
        }

        for (SysDict sysDict : dictTree) {

            totalDictList.add(sysDict);

            if (ObjectUtil.isNotEmpty(sysDict.getChildren())) {
                collectTreeTasks(sysDict.getChildren(), totalDictList);
            }
        }

    }

}
