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
package cn.stylefeng.roses.kernel.sys.api.pojo.role.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色绑定权限的请求
 *
 * @author fengshuonan
 * @since 2023/6/13 13:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleBindPermissionRequest extends BaseRequest {

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空", groups = {roleBindPermission.class, detail.class})
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 节点ID，可以是菜单id和按钮id，或者是应用id
     * <p>
     * 如果permissionNodeType = -1，则不用传递此值
     */
    @ChineseDescription("节点ID")
    private Long nodeId;

    /**
     * 节点类型：1-应用，2-菜单，3-功能，-1-所有权限
     */
    @ChineseDescription("节点类型：1-应用，2-菜单，3-功能，-1-所有权限")
    @NotNull(message = "节点类型不能为空", groups = {roleBindPermission.class})
    private Integer permissionNodeType;

    /**
     * 是否选中
     */
    @ChineseDescription("是否选中")
    @NotNull(message = "是否选中不能为空", groups = {roleBindPermission.class})
    private Boolean checked;

    /**
     * 参数校验分组：角色绑定权限
     */
    public @interface roleBindPermission {
    }

}
