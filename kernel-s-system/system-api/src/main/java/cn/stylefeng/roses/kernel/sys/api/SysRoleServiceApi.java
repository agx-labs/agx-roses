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
package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleTreeDTO;

import java.util.List;

/**
 * 角色信息相关的Api
 *
 * @author fengshuonan
 * @since 2023/6/25 0:35
 */
public interface SysRoleServiceApi {

    /**
     * 获取系统默认角色id，查询方式为找到角色编码为employee的角色id
     * <p>
     * 一般在添加用户时用到
     *
     * @author fengshuonan
     * @since 2023/6/25 0:35
     */
    Long getDefaultRoleId();

    /**
     * 通过角色id获取角色名称
     *
     * @author fengshuonan
     * @since 2023/7/15 21:54
     */
    String getRoleNameByRoleId(Long roleId);

    /**
     * 获取角色对应的菜单功能编码集合
     *
     * @author fengshuonan
     * @since 2023/10/10 14:33
     */
    List<String> getRoleMenuOptionsByRoleId(String roleCode);

    /**
     * 获取角色，通过传递角色id列表
     *
     * @param roleIds 角色id列表
     * @return 角色信息列表
     * @author fengshuonan
     * @since 2024/1/5 19:33
     */
    List<SysRoleDTO> getRolesByIds(List<Long> roleIds);

    /**
     * 获取系统中所有的业务角色，和指定公司的角色
     *
     * @author fengshuonan
     * @since 2024/1/17 23:28
     */
    List<SysRoleDTO> getBusinessRoleAndCompanyRole(List<Long> companyIdList);

    /**
     * 获取角色分类和角色组成的树结构
     *
     * @author fengshuonan
     * @since 2025/1/24 11:10
     */
    List<SysRoleTreeDTO> roleCategoryAndRoleTreeList(Integer roleType, Long companyId);

}
