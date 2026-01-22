package cn.stylefeng.roses.kernel.event.sdk.publish;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.event.api.exception.enums.EventExceptionEnum;
import cn.stylefeng.roses.kernel.event.sdk.container.EventContainer;
import cn.stylefeng.roses.kernel.event.sdk.pojo.BusinessListenerDetail;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 业务事件发布工具
 *
 * @author fengshuonan
 * @since 2023/7/14 16:21
 */
@Slf4j
public class BusinessEventPublisher {

    /**
     * 发布一个业务事件
     * <p>
     * 如果有方法上加了@BusinessListener，则会自动回调那些方法
     *
     * @author fengshuonan
     * @since 2023/7/14 15:37
     */
    public static <T> void publishEvent(String businessCode, T businessObject) {

        // 获取该业务是否有监听器
        List<BusinessListenerDetail> listener = EventContainer.getListener(businessCode);

        // 没有监听器则直接返回
        if (ObjectUtil.isEmpty(listener)) {
            return;
        }

        // 依次调用监听器方法，进行回调调用
        for (BusinessListenerDetail businessListenerDetail : listener) {

            // 监听器的Spring Bean
            String beanName = businessListenerDetail.getBeanName();
            Object bean = SpringUtil.getBean(beanName);

            // 监听器的具体方法
            Method listenerMethod = businessListenerDetail.getListenerMethod();
            Class<?> parameterClassType = businessListenerDetail.getParameterClassType();

            // 如果发布事件的时候参数是空的，则直接调用无参的方法
            if (ObjectUtil.isEmpty(businessObject)) {

                // 如果方法的参数数量为0，则直接调用，如果不为0，则跳过执行下个
                if (parameterClassType == null) {
                    try {
                        listenerMethod.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("方法调用失败，反射调用异常", e);
                        throw new ServiceException(EventExceptionEnum.ERROR_INVOKE);
                    }
                }

            }

            // 如果发布事件的时候，包含参数，则判断method的第一个参数是否和businessObject的class一样，不一样则不调用
            else {
                if (parameterClassType != null) {
                    if (parameterClassType.equals(businessObject.getClass())
                            || parameterClassType.isAssignableFrom(businessObject.getClass())) {
                        try {
                            listenerMethod.invoke(bean, businessObject);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            log.error("方法调用失败，反射调用异常", e);
                            throw new ServiceException(EventExceptionEnum.ERROR_INVOKE);
                        }
                    }
                }
            }
        }
    }

}
