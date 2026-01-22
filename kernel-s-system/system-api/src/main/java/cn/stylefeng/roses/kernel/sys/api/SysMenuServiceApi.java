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

import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.api.pojo.menu.UserAppMenuInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单信息的Api
 *
 * @author fengshuonan
 * @since 2023/6/26 21:36
 */
public interface SysMenuServiceApi {

    /**
     * 通过菜单id获取菜单对应的菜单信息集合
     * <p>
     * 一般用在渲染首页的用户常用功能
     *
     * @author fengshuonan
     * @since 2023/6/26 21:37
     */
    List<UserAppMenuInfo> getUserAppMenuDetailList(Set<Long> menuIdList);

    /**
     * 获取菜单对应的应用id（可以批量获取）
     *
     * @return key是菜单id，value是应用id
     * @author fengshuonan
     * @since 2023/6/26 21:47
     */
    Map<Long, Long> getMenuAppId(List<Long> menuIdList);

    /**
     * 获取指定应用下的所有菜单和功能的id
     *
     * @author fengshuonan
     * @since 2024/10/31 0:22
     */
    Set<Long> getAppMenuAndOptionIds(Long appId);

    /**
     * 获取所有菜单的id和父级id的映射关系
     *
     * @return 菜单id和父级id的映射关系
     * @author fengshuonan
     * @since 2024/6/24 12:38
     */
    Map<Long, Long> getMenuIdParentIdMap();

    /**
     * 获取菜单下的所有功能
     *
     * @param menuId                       指定菜单id
     * @param roleLimitMenuIdsAndOptionIds 被限制的功能和id集合
     * @author fengshuonan
     * @since 2024/10/31 9:51
     */
    List<Long> getMenuOptions(Long menuId, Set<Long> roleLimitMenuIdsAndOptionIds);

    /**
     * 获取应用下的所有菜单id
     *
     * @param appId                        指定应用id
     * @param roleLimitMenuIdsAndOptionIds 被限制指定范围的菜单集合
     * @author fengshuonan
     * @since 2023/9/8 15:03
     */
    Set<Long> getAppMenuIds(Long appId, Set<Long> roleLimitMenuIdsAndOptionIds);

    /**
     * 获取应用下的所有菜单功能
     *
     * @param appId                        应用id
     * @param roleLimitMenuIdsAndOptionIds 被限制的功能和id集合
     * @author fengshuonan
     * @since 2023/9/8 15:13
     */
    List<SysMenuOptions> getAppMenuOptions(Long appId, Set<Long> roleLimitMenuIdsAndOptionIds);

}
