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
package cn.stylefeng.roses.kernel.sys.api.pojo.role;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.tree.factory.base.AbstractSortedTreeNode;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 角色和角色类型组成的机构树的传输bean
 *
 * @author fengshuonan
 * @since 2025/1/24 11:16
 */
@Data
public class SysRoleTreeDTO implements AbstractSortedTreeNode<SysRoleTreeDTO> {

    /**
     * 角色分类id或者角色id
     */
    @ChineseDescription("角色分类id或者角色id")
    private Long roleTreeNodeId;

    /**
     * 上级id
     */
    @ChineseDescription("上级id")
    private Long roleTreeParentNodeId;

    /**
     * 角色分类名称或者角色名称
     */
    @ChineseDescription("角色分类名称或者角色名称")
    private String roleTreeNodeName;

    /**
     * 节点类型：1-角色分类，2-角色
     */
    @ChineseDescription("节点类型：1-角色分类，2-角色")
    private Integer nodeType;

    /**
     * 角色类型：10-系统角色，15-业务角色，20-公司角色
     */
    @ChineseDescription("角色类型：10-系统角色，15-业务角色，20-公司角色")
    private Integer roleType;

    /**
     * 是否选中
     * <p>
     * 用在用户绑定角色上，选中的则是绑定的
     */
    @ChineseDescription("是否选中")
    private Boolean checkedFlag = false;

    /**
     * 子节点
     */
    @ChineseDescription("子节点")
    private List<SysRoleTreeDTO> children;

    @Override
    public String getNodeId() {
        if (roleTreeNodeId == null) {
            return "";
        }
        return roleTreeNodeId.toString();
    }

    @Override
    public String getNodeParentId() {
        if (roleTreeParentNodeId == null) {
            return "";
        }
        return roleTreeParentNodeId.toString();
    }

    @Override
    public void setChildrenNodes(List<SysRoleTreeDTO> childrenNodes) {
        this.children = childrenNodes;
    }

    @Override
    public BigDecimal getSort() {
        if (nodeType == null) {
            return new BigDecimal(0);
        }
        return new BigDecimal(nodeType);
    }
}
