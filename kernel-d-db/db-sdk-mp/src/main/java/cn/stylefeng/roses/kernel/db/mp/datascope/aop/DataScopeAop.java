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
package cn.stylefeng.roses.kernel.db.mp.datascope.aop;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.db.mp.datascope.UserRoleDataScopeApi;
import cn.stylefeng.roses.kernel.db.mp.datascope.annotations.DataScope;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.db.mp.datascope.holder.DataScopeHolder;
import cn.stylefeng.roses.kernel.rule.constants.ProjectAopSortConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;


/**
 * 数据范围切换的AOP
 *
 * @author fengshuonan
 * @since 2024-03-01 12:47
 */
@Aspect
@Slf4j
public class DataScopeAop implements Ordered {

    @Pointcut(value = "@annotation(cn.stylefeng.roses.kernel.db.mp.datascope.annotations.DataScope)")
    private void cutService() {

    }

    @Around("cutService()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        // 获取被aop拦截的方法
        Signature signature = point.getSignature();
        MethodSignature methodSignature = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        methodSignature = (MethodSignature) signature;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        // 获取注解
        DataScope datasource = currentMethod.getAnnotation(DataScope.class);

        // 获取当前用户拥有的数据范围
        UserRoleDataScopeApi userRoleDataScopeApi = null;
        try {
            userRoleDataScopeApi = SpringUtil.getBean(UserRoleDataScopeApi.class);
        } catch (Exception e) {
            log.error("无法找到用户权限获取的类！", e);
            return point.proceed();
        }
        DataScopeConfig userRoleDataScopeConfig = userRoleDataScopeApi.getUserRoleDataScopeConfig();

        // 获取不到用户的数据范围配置，也直接返回
        if (userRoleDataScopeConfig == null) {
            return point.proceed();
        }

        // 如果有单独配置特定的字段，以注解单独配置的字段为主
        String userIdFieldName = datasource.userIdFieldName();
        if (StrUtil.isNotBlank(userIdFieldName)) {
            userRoleDataScopeConfig.setUserIdFieldName(userIdFieldName);
        }

        String orgIdFieldName = datasource.orgIdFieldName();
        if (StrUtil.isNotBlank(orgIdFieldName)) {
            userRoleDataScopeConfig.setOrgIdFieldName(orgIdFieldName);
        }

        try {
            // 放入到临时上下文中
            DataScopeHolder.set(userRoleDataScopeConfig);

            // 执行原有业务逻辑
            return point.proceed();
        } finally {
            // 清空数据范围的上下文环境
            DataScopeHolder.remove();
        }
    }

    /**
     * aop的顺序要早于spring的事务
     *
     * @author fengshuonan
     * @since 2020/10/31 22:55
     */
    @Override
    public int getOrder() {
        return ProjectAopSortConstants.DATA_SCOPE_AOP_ORDER;
    }

}
