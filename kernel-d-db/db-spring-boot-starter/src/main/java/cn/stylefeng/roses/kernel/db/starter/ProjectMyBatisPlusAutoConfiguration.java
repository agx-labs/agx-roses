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
package cn.stylefeng.roses.kernel.db.starter;

import cn.stylefeng.roses.kernel.db.api.pojo.tenant.TenantTableProperties;
import cn.stylefeng.roses.kernel.db.mp.datascope.ProjectDataScopeHandler;
import cn.stylefeng.roses.kernel.db.mp.dbid.CustomDatabaseIdProvider;
import cn.stylefeng.roses.kernel.db.mp.fieldfill.CustomMetaObjectHandler;
import cn.stylefeng.roses.kernel.db.mp.injector.CustomInsertBatchSqlInjector;
import cn.stylefeng.roses.kernel.db.mp.tenant.ProjectTenantInterceptor;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus的插件配置
 *
 * @author fengshuonan
 * @since 2020/11/30 22:40
 */
@Configuration
@AutoConfigureBefore(MybatisPlusAutoConfiguration.class)
public class ProjectMyBatisPlusAutoConfiguration {

    /**
     * 设置租户的表的集合，在yml配置中配置  tenant.businessTableList
     *
     * @author fengshuonan
     * @since 2023/8/30 10:39
     */
    @Bean
    @ConfigurationProperties(prefix = "tenant")
    @ConditionalOnMissingBean(TenantTableProperties.class)
    public TenantTableProperties tenantTableProperties() {
        return new TenantTableProperties();
    }

    /**
     * 新的分页插件
     *
     * @author fengshuonan
     * @since 2020/12/24 13:13
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantTableProperties tenantTableProperties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 使用多租户插件
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor(tenantTableProperties));

        // 使用数据权限插件
        interceptor.addInnerInterceptor(dataPermissionInterceptor());

        // 使用分页插插件
        interceptor.addInnerInterceptor(paginationInterceptor());

        // 使用乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());

        return interceptor;
    }

    /**
     * 分页插件
     *
     * @author fengshuonan
     * @since 2020/11/30 22:41
     */
    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }

    /**
     * 乐观锁插件
     *
     * @author fengshuonan
     * @since 2021/10/28 17:52
     */
    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    /**
     * 公共字段填充插件
     *
     * @author fengshuonan
     * @since 2020/11/30 22:41
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new CustomMetaObjectHandler();
    }

    /**
     * 数据库id选择器，兼容多个数据库sql脚本
     *
     * @author fengshuonan
     * @since 2020/11/30 22:42
     */
    @Bean
    public CustomDatabaseIdProvider customDatabaseIdProvider() {
        return new CustomDatabaseIdProvider();
    }

    /**
     * 自定义sqlInjector，针对批量插入优化
     *
     * @author fengshuonan
     * @since 2022/9/17 14:28
     */
    @Bean
    public CustomInsertBatchSqlInjector customInsertBatchSqlInjector() {
        return new CustomInsertBatchSqlInjector();
    }

    /**
     * 多租户插件
     *
     * @author fengshuonan
     * @since 2023/8/30 10:17
     */
    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantTableProperties tenantTableProperties) {
        return new TenantLineInnerInterceptor(new ProjectTenantInterceptor(tenantTableProperties));
    }

    /**
     * 数据范围处理插件
     *
     * @author fengshuonan
     * @since 2024/2/28 21:17
     */
    @Bean
    public DataPermissionInterceptor dataPermissionInterceptor() {
        return new DataPermissionInterceptor(new ProjectDataScopeHandler());
    }

}
