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
package cn.stylefeng.roses.kernel.rule.tree.factory;

import cn.stylefeng.roses.kernel.rule.tree.factory.base.AbstractSortedTreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 带排序功能的树构造器
 *
 * @author fengshuonan
 * @since 2024/9/7 20:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SortedTreeBuildFactory<T extends AbstractSortedTreeNode<T>> extends DefaultTreeBuildFactory<T> {

    public SortedTreeBuildFactory() {
        super();
    }

    public SortedTreeBuildFactory(String rootParentId) {
        super(rootParentId);
    }

    @Override
    public List<T> doTreeBuild(List<T> nodes) {

        // 先对列表进行排序
        nodes.sort(Comparator.comparing(itemNode -> Optional.ofNullable((itemNode).getSort()).orElse(new BigDecimal(Integer.MAX_VALUE))));

        // 将每个节点构造一个子树
        for (T treeNode : nodes) {
            this.buildChildNodes(nodes, treeNode, new ArrayList<>());
        }

        return findTops(nodes);
    }

    @Override
    protected void buildChildNodes(List<T> totalNodes, T node, List<T> childNodeLists) {
        if (totalNodes == null || node == null) {
            return;
        }

        List<T> nodeSubLists = getSubChildsLevelOne(totalNodes, node);

        // 对子节点进行排序
        nodeSubLists.sort(Comparator.comparing(itemNode -> Optional.ofNullable((itemNode).getSort()).orElse(new BigDecimal(Integer.MAX_VALUE))));

        if (!nodeSubLists.isEmpty()) {
            for (T nodeSubList : nodeSubLists) {
                buildChildNodes(totalNodes, nodeSubList, new ArrayList<>());
            }
        }

        childNodeLists.addAll(nodeSubLists);
        node.setChildrenNodes(childNodeLists);
    }

    /**
     * 找到最顶级的节点，指定node列表中，不存在的父级id则为顶级节点
     *
     * @author fengshuonan
     * @since 2024/12/11 15:35
     */
    private List<T> findTops(List<T> nodes) {

        List<T> totalParentList = nodes.stream().filter((itemNode) -> null != itemNode.getNodeParentId()).collect(Collectors.toList());

        List<String> totalNodeIdList = nodes.stream().map(AbstractSortedTreeNode::getNodeId).collect(Collectors.toList());

        return totalParentList.stream().filter((item) -> !totalNodeIdList.contains(item.getNodeParentId())).distinct().collect(Collectors.toList());
    }

}
