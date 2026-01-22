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
package cn.stylefeng.roses.kernel.sys.api.pojo.user;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

/**
 * 用在用户登录校验结果的包装
 *
 * @author fengshuonan
 * @since 2023/6/17 21:55
 */
@Data
public class UserValidateDTO {

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 加密后的密码，存在sys_user表的password字段
     */
    @ChineseDescription("加密后的密码")
    private String userPasswordHexed;

    /**
     * 密码盐，存在sys_user表的password_salt字段
     */
    @ChineseDescription("加密后的密码")
    private String userPasswordSalt;

    /**
     * 用户状态，状态在UserStatusEnum维护
     */
    @ChineseDescription("用户状态")
    private Integer userStatus;

    /**
     * 账号
     * <p>
     * 2023年9月1日新增，用在createNewLoginInfo方法中，为指定token创建用户信息时候补全信息
     */
    @ChineseDescription("账号")
    private String account;

    /**
     * 租户id
     * <p>
     * 2023年9月1日新增，用在createNewLoginInfo方法中，为指定token创建用户信息时候补全信息
     */
    @ChineseDescription("租户id")
    private Long tenantId;

    public UserValidateDTO() {
    }

    public UserValidateDTO(Long userId, String userPasswordHexed, String salt, Integer userStatus, Long tenantId, String account) {
        this.userId = userId;
        this.userPasswordHexed = userPasswordHexed;
        this.userPasswordSalt = salt;
        this.userStatus = userStatus;
        this.tenantId = tenantId;
        this.account = account;
    }

}
