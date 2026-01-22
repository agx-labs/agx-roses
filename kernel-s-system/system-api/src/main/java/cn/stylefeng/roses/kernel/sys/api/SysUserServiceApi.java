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

import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.*;

import java.util.List;
import java.util.Set;

/**
 * 基础核心业务Api
 *
 * @author fengshuonan
 * @date 2023-06-10 20:50:43
 */
public interface SysUserServiceApi {

    /**
     * 获取用户的基本信息
     *
     * @author fengshuonan
     * @since 2023/6/11 21:07
     */
    SimpleUserDTO getUserInfoByUserId(Long userId);

    /**
     * 获取用户真实姓名
     *
     * @author fengshuonan
     * @since 2023/6/16 22:26
     */
    String getUserRealName(Long userId);

    /**
     * 获取用于用户校验的
     *
     * @param tenantId 指定租户id
     * @param account  用户账号
     * @author fengshuonan
     * @since 2023/6/17 21:56
     */
    UserValidateDTO getUserLoginValidateDTO(Long tenantId, String account);

    /**
     * 通过用户id创建用户的校验信息
     *
     * @param userId 用户id
     * @author fengshuonan
     * @since 2023/6/17 21:56
     */
    UserValidateDTO getUserLoginValidateDTO(Long userId);

    /**
     * 更新用户的登录ip和最后登录时间
     *
     * @author fengshuonan
     * @since 2023/6/17 22:46
     */
    void updateUserLoginInfo(Long userId, String ip, Long tenantId);

    /**
     * 获取用户是否是超级管理员
     *
     * @author fengshuonan
     * @since 2023/6/17 23:09
     */
    boolean getUserSuperAdminFlag(Long userId);

    /**
     * 查询所有的用户id集合
     *
     * @author fengshuonan
     * @since 2023/6/18 9:05
     */
    List<Long> queryAllUserIdList();

    /**
     * 判断用户是否存在
     *
     * @author fengshuonan
     * @since 2023/6/18 9:06
     */
    Boolean userExist(Long userId);

    /**
     * 获取用户的账号和姓名信息
     * <p>
     * 一般用在获取在线用户列表
     *
     * @author fengshuonan
     * @since 2023/7/2 13:22
     */
    OnlineUserItem getUserNameAccountInfo(Long userId);

    /**
     * 获取用户账号和姓名信息
     * <p>
     * 一般用在获取在线用户列表
     *
     * @param onlineUserItems 查询条件，在此用户id列表中查询
     * @param searchText      查询条件，查询账号或姓名包含此字符串的结果
     * @author fengshuonan
     * @since 2023/7/2 13:36
     */
    List<OnlineUserItem> getUserNameAccountInfoListByCondition(List<OnlineUserItem> onlineUserItems, String searchText);

    /**
     * 获取用户基本信息通过用户id
     *
     * @author fengshuonan
     * @since 2023/7/19 11:08
     */
    UserInfoDetailDTO getUserDetail(Long userId);

    /**
     * 临时锁定用户状态，默认设定1天的自动解锁日期
     *
     * @author fengshuonan
     * @since 2023/10/3 22:11
     */
    void lockUserStatus(String tenantCode, String account);

    /**
     * 创建指定的用户临时身份信息
     *
     * @author fengshuonan
     * @since 2023/11/16 21:39
     */
    TempLoginUserInfo createTempUserInfo(Long userId);

    /**
     * 批量获取用户名称
     *
     * @author fengshuonan
     * @since 2023/12/29 9:33
     */
    List<SimpleDict> batchGetNameByUserIdList(Set<Long> userIdList);

    /**
     * 获取当前用户综合性的身份列表
     * <p>
     * 1. 获取当前用户id，加入到结果
     * 2. 获取用户当前登录的机构id，加入到结果
     * 3. 获取用户拥有的角色id，加入到结果
     *
     * @author fengshuonan
     * @since 2024-01-09 18:54
     */
    Set<Long> getUserComprehensiveIdentitySummary();

    /**
     * 批量获取用户的基础名称和头像信息
     *
     * @author fengshuonan
     * @since 2024/4/2 22:48
     */
    List<SimpleUserDTO> batchGetUserSimpleInfoList(Set<Long> userIdList);

    /**
     * 简单添加用户，基于一些基本信息，直接添加用户，简化版添加用户
     *
     * @param addUserDTO 添加用户的请求
     * @param orgId      用户所属的组织机构id
     * @param positionId 用户职位id
     * @author fengshuonan
     * @since 2024/5/23 23:48
     */
    Long simpleAddUser(AddUserDTO addUserDTO, Long orgId, Long positionId);

    /**
     * 通过用户的邮箱获取用户的详细信息
     *
     * @author fengshuonan
     * @since 2024/5/24 9:30
     */
    UserInfoDetailDTO getUserDetailByEmail(String email);

    /**
     * 重置用户密码
     *
     * @author fengshuonan
     * @since 2023/6/12 14:55
     */
    void resetPassword(Long userId, String newPassword);

}
