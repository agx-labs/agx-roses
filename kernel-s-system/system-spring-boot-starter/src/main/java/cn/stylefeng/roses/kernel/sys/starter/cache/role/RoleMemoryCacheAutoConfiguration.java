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
package cn.stylefeng.roses.kernel.sys.starter.cache.role;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.modular.role.cache.rolemenu.RoleMenuMemoryCache;
import cn.stylefeng.roses.kernel.sys.modular.role.cache.rolename.RoleNameMemoryCache;
import cn.stylefeng.roses.kernel.sys.modular.role.cache.roleoptions.RoleMenuOptionsMemoryCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 角色缓存
 *
 * @author fengshuonan
 * @since 2023/7/14 23:07
 */
@Configuration
@ConditionalOnMissingClass("org.springframework.data.redis.connection.RedisConnectionFactory")
public class RoleMemoryCacheAutoConfiguration {

    /**
     * 角色绑定菜单的缓存
     *
     * @author fengshuonan
     * @since 2023/7/14 23:08
     */
    @Bean
    public CacheOperatorApi<List<Long>> roleMenuCache() {
        // 1小时过期
        TimedCache<String, List<Long>> cache = CacheUtil.newTimedCache(1000 * SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        return new RoleMenuMemoryCache(cache);
    }

    /**
     * 角色绑定菜单功能的缓存
     *
     * @author fengshuonan
     * @since 2023/7/14 23:56
     */
    @Bean
    public CacheOperatorApi<List<Long>> roleMenuOptionsCache() {
        // 1小时过期
        TimedCache<String, List<Long>> cache = CacheUtil.newTimedCache(1000 * SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        return new RoleMenuOptionsMemoryCache(cache);
    }

    /**
     * 角色名称
     *
     * @author fengshuonan
     * @since 2025/1/10 14:37
     */
    @Bean
    public CacheOperatorApi<String> roleNameCache() {
        TimedCache<String, String> cache = CacheUtil.newTimedCache(1000 * SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        return new RoleNameMemoryCache(cache);
    }

}
