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
package cn.stylefeng.roses.kernel.auth.api;

import cn.stylefeng.roses.kernel.auth.api.exception.AuthException;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;

import java.util.function.Consumer;

/**
 * 当前登陆用户相关的一系列方法
 *
 * @author fengshuonan
 * @since 2020/10/17 10:27
 */
public interface LoginUserApi {

    /**
     * 获取当前登陆用户的token
     * <p>
     * 如果获取不到，返回null
     *
     * @return 当前用户的token或null
     * @author fengshuonan
     * @since 2020/10/17 11:05
     */
    String getToken();

    /**
     * 获取当前登陆用户
     * <p>
     * 如果获取不到当前登陆用户会抛出 AuthException
     *
     * @return 当前登陆用户信息
     * @throws AuthException 权限异常
     * @author fengshuonan
     * @since 2020/10/17 10:27
     */
    LoginUser getLoginUser() throws AuthException;

    /**
     * 获取当前登陆用户
     * <p>
     * 如果获取不到当前登陆用户返回null
     *
     * @return 当前登录用户信息
     * @author fengshuonan
     * @since 2020/10/17 11:00
     */
    LoginUser getLoginUserNullable();

    /**
     * 判断当前用户是否登录
     *
     * @return 是否登录，true是，false否
     * @author fengshuonan
     * @since 2020/10/17 11:02
     */
    boolean hasLogin();

    /**
     * 获取是否是超级管理员的标识
     *
     * @return true-是超级管理员，false-不是超级管理员
     * @author fengshuonan
     * @since 2020/11/4 15:45
     */
    boolean getSuperAdminFlag();

    /**
     * 获取当前登录用户当前机构对应的公司id
     *
     * @author fengshuonan
     * @since 2023/8/1 14:03
     */
    Long getCurrentUserCompanyId();

    /**
     * 切换当前登录用户为指定用户id
     * <p>
     * 仅应用在当前线程环境中，使用后请及时调用endSwitch()方法
     *
     * @param userId 切换到指定用户身份
     * @author fengshuonan
     * @since 2023/11/16 21:03
     */
    LoginUser switchTo(Long userId);

    /**
     * 结束临时身份切换
     * <p>
     * 仅应用在当前线程环境中
     *
     * @author fengshuonan
     * @since 2023/11/16 21:03
     */
    void endSwitch();

    /**
     * 切换身份并执行一次函数回调
     *
     * @param userId   指定账号id
     * @param consumer 要执行的方法
     * @author fengshuonan
     * @since 2023/11/16 21:12
     */
    void switchTo(Long userId, Consumer<Long> consumer);

}
