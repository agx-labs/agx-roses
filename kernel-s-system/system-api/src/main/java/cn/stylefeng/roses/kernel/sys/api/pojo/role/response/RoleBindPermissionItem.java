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
package cn.stylefeng.roses.kernel.sys.api.pojo.role.response;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.tree.factory.base.AbstractTreeNode;
import lombok.Data;

import java.util.List;

/**
 * 角色绑定权限界面的响应封装
 * <p>
 * 本结构是个树形结构，第1层级是应用，第2层级是应用下的菜单（菜单只显示最子节点），第3层级是菜单下的功能options
 *
 * @author fengshuonan
 * @since 2023/6/13 13:54
 */
@Data
public class RoleBindPermissionItem implements AbstractTreeNode<RoleBindPermissionItem> {

    /**
     * 节点ID，可以是菜单id和按钮id
     */
    @ChineseDescription("节点ID")
    private Long nodeId;

    /**
     * 父级节点id
     */
    @ChineseDescription("父级节点id")
    private Long nodeParentId;

    /**
     * 节点名称
     */
    @ChineseDescription("节点名称")
    private String nodeName;

    /**
     * 节点类型：1-应用，2-菜单，-1-所有权限
     * <p>
     * 【2024年10月30日修改】去掉了3-功能，功能现在单独放在functionList这个字段
     */
    @ChineseDescription("节点类型：1-应用，2-菜单，-1-所有权限")
    private Integer permissionNodeType;

    /**
     * 是否选择(已拥有的是true)
     */
    @ChineseDescription("是否选择(已拥有的是true)")
    private Boolean checked;

    /**
     * 子节点集合
     */
    @ChineseDescription("子节点集合")
    private List<RoleBindPermissionItem> children;

    /**
     * 是否是叶子节点
     */
    @ChineseDescription("是否是叶子节点")
    private Boolean leafFlag;

    /**
     * 功能集合
     */
    @ChineseDescription("功能集合")
    private List<RoleBindPermissionItem> functionList;

    public RoleBindPermissionItem() {

    }

    public RoleBindPermissionItem(Long nodeId, Long parentId, String nodeName, Integer permissionNodeType, Boolean checked) {
        this.nodeId = nodeId;
        this.nodeParentId = parentId;
        this.nodeName = nodeName;
        this.permissionNodeType = permissionNodeType;
        this.checked = checked;
    }

    @Override
    public String getNodeId() {
        if (this.nodeId == null) {
            return "";
        }
        return this.nodeId.toString();
    }

    @Override
    public String getNodeParentId() {
        if (this.nodeParentId == null) {
            return "";
        }
        return this.nodeParentId.toString();
    }

    @Override
    public void setChildrenNodes(List<RoleBindPermissionItem> childrenNodes) {
        this.children = childrenNodes;
    }
}
