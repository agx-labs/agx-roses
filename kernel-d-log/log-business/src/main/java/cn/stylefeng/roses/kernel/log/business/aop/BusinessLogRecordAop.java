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
package cn.stylefeng.roses.kernel.log.business.aop;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.log.api.context.BusinessLogHolder;
import cn.stylefeng.roses.kernel.log.api.pojo.entity.SysLogBusiness;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessService;
import cn.stylefeng.roses.kernel.rule.annotation.BizLog;
import cn.stylefeng.roses.kernel.rule.constants.ProjectAopSortConstants;
import cn.stylefeng.roses.kernel.rule.util.HttpServletUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 业务日志的AOP
 *
 * @author fengshuonan
 * @since 2023/7/21 15:06
 */
@Aspect
@Slf4j
public class BusinessLogRecordAop implements Ordered {

    @Resource
    private SysLogBusinessService sysLogBusinessService;

    /**
     * 切面为切BizLog注解
     *
     * @author fengshuonan
     * @since 2023/7/21 15:49
     */
    @Pointcut(value = "@annotation(cn.stylefeng.roses.kernel.rule.annotation.BizLog)")
    public void cutService() {
    }

    @Around("cutService()")
    public Object aroundPost(ProceedingJoinPoint joinPoint) throws Throwable {

        try {

            // 初始化日志的环境信息
            initBusinessContext(joinPoint);

            // 执行真正业务逻辑，这期间会调用BusinessLogHolder类进行业务日志存储
            Object result = joinPoint.proceed();

            // 业务日志进行保存
            SysLogBusiness logContext = BusinessLogHolder.getContext();
            List<String> contentList = BusinessLogHolder.getContent();
            if (ObjectUtil.isNotEmpty(logContext) && ObjectUtil.isNotEmpty(contentList)) {
                sysLogBusinessService.saveBatchLogs(logContext, contentList);
            }

            return result;

        } catch (Exception e) {

            log.error("业务日志过程中出现异常！", e);

            // 这里不拦截异常，给上级处理异常
            throw e;

        } finally {
            // 清除所有日志缓存记录
            BusinessLogHolder.clearContext();
        }
    }

    /**
     * 初始化业务日志的环境
     *
     * @author fengshuonan
     * @since 2023/7/21 15:55
     */
    private void initBusinessContext(ProceedingJoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BizLog bizLog = method.getAnnotation(BizLog.class);

        SysLogBusiness sysLogBusiness = new SysLogBusiness();

        // 设置主键
        sysLogBusiness.setBusinessLogId(IdWorker.getId());

        // 设置业务日志的类型编码
        sysLogBusiness.setLogTypeCode(bizLog.logTypeCode());

        // http相关的信息可以为空
        try {
            // 设置请求的URL
            sysLogBusiness.setRequestUrl(HttpServletUtil.getRequest().getServletPath());

            // 设置HTTP请求方式
            sysLogBusiness.setHttpMethod(HttpServletUtil.getRequest().getMethod());

            // 设置客户端IP
            String requestClientIp = HttpServletUtil.getRequestClientIp(HttpServletUtil.getRequest());
            sysLogBusiness.setClientIp(requestClientIp);
        } catch (Exception e) {
            // ignore
        }

        // 设置当前登录用户id
        LoginUser loginUserNullable = LoginContext.me().getLoginUserNullable();
        if (loginUserNullable != null) {
            sysLogBusiness.setUserId(loginUserNullable.getUserId());
        }

        // 添加到线程环境变量
        BusinessLogHolder.setContext(sysLogBusiness);
    }

    @Override
    public int getOrder() {
        return ProjectAopSortConstants.DEFAULT_BUSINESS_LOG_AOP_SORT;
    }

}
