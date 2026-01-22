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
package cn.stylefeng.roses.kernel.sys.api.constants;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;

/**
 * 基础核心业务业务
 *
 * @author fengshuonan
 * @date 2023-06-10 20:50:43
 */
public interface SysConstants {

    /**
     * 基础核心业务模块名称
     */
    String SYS_MODULE_NAME = "kernel-s-system";

    /**
     * 异常枚举的步进值
     */
    String SYS_EXCEPTION_STEP_CODE = "99";

    /**
     * 默认登录密码
     */
    String DEFAULT_LOGIN_PASSWORD = "Aa123456!";

    /**
     * 后台管理员的角色编码
     */
    String BACKEND_ADMIN_ROLE_CODE = "backendAdmin";

    /**
     * 默认用户的初始角色编码
     */
    String DEFAULT_ROLE_CODE = "employee";

    /**
     * 初始化超级管理员的监听器顺序
     */
    Integer SUPER_ADMIN_INIT_LISTENER_SORT = 400;

    /**
     * 管理员角色的账号
     */
    String ADMIN_USER_ACCOUNT = "admin";

    /**
     * 默认的sys模块的缓存超时时间
     */
    Long DEFAULT_SYS_CACHE_TIMEOUT_SECONDS = 3600L;

    /**
     * 默认租户ID
     */
    Long DEFAULT_ROOT_TENANT_ID = RuleConstants.DEFAULT_ROOT_TENANT_ID;

}
