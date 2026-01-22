package cn.stylefeng.roses.kernel.rule.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 * 获取代理原始对象的工具
 *
 * @author fengshuonan
 * @since 2020/10/19 16:20
 */
@Slf4j
public class AopTargetUtils {

    /**
     * 获取代理对象的原始对象
     *
     * @author fengshuonan
     * @since 2020/10/19 16:21
     */
    public static Object getTarget(Object proxy) {

        // 不是代理对象，直接返回参数对象
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }

        // 判断是否是jdk还是cglib代理的对象
        try {
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else {
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            log.error("获取代理对象异常", e);
            return null;
        }
    }

    /**
     * 获取cglib代理的对象
     *
     * @author fengshuonan
     * @since 2020/10/19 16:21
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        if (proxy instanceof Advised advised) {
            return advised.getTargetSource().getTarget();
        }
        throw new IllegalArgumentException("Not a CGLIB proxy");
    }

    /**
     * 获取jdk代理的对象
     *
     * @author fengshuonan
     * @since 2020/10/19 16:22
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        if (proxy instanceof Advised advised) {
            return advised.getTargetSource().getTarget();
        }
        throw new IllegalArgumentException("Not a JDK dynamic proxy");
    }

}