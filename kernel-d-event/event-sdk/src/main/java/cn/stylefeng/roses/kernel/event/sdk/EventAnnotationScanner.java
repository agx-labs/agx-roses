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
package cn.stylefeng.roses.kernel.event.sdk;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.event.api.annotation.BusinessListener;
import cn.stylefeng.roses.kernel.event.sdk.container.EventContainer;
import cn.stylefeng.roses.kernel.event.sdk.pojo.BusinessListenerDetail;
import cn.stylefeng.roses.kernel.rule.util.AopTargetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描所有的业务监听事件，最终汇总到Event容器中
 *
 * @author fengshuonan
 * @since 2023/7/14 15:09
 */
@Slf4j
public class EventAnnotationScanner implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        // 如果controller是代理对象,则需要获取原始类的信息
        Object aopTarget = AopTargetUtils.getTarget(bean);

        if (aopTarget == null) {
            aopTarget = bean;
        }

        Class<?> clazz = aopTarget.getClass();

        // 判断类中是否有@BusinessListener注解
        this.doScan(clazz, beanName);

        return bean;
    }

    /**
     * 扫描整个类中包含的所有@BusinessListener注解
     *
     * @author fengshuonan
     * @since 2023/7/14 15:24
     */
    private void doScan(Class<?> clazz, String beanName) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            BusinessListener businessListener = declaredMethod.getAnnotation(BusinessListener.class);

            log.debug("扫描到业务事件监听: " + businessListener);

            if (businessListener != null) {
                createEventDetail(beanName, declaredMethod, businessListener);
            }
        }
    }

    /**
     * 讲扫描到的注解信息的调用信息，封装到BusinessListenerDetail
     *
     * @author fengshuonan
     * @since 2023/7/14 15:27
     */
    private void createEventDetail(String beanName, Method method, BusinessListener businessListener) {

        // 获取注解上的业务编码标识
        String businessCode = businessListener.businessCode();

        // 从容器中获取本业务标识是否创建
        List<BusinessListenerDetail> listener = EventContainer.getListener(businessCode);
        if (ObjectUtil.isEmpty(listener)) {
            listener = new ArrayList<>();
        }


        // 创建当前方法的调用信息
        BusinessListenerDetail businessListenerDetail = new BusinessListenerDetail();
        businessListenerDetail.setBeanName(beanName);
        businessListenerDetail.setListenerMethod(method);

        // 获取方法参数信息
        int parameterCount = method.getParameterCount();
        if (parameterCount == 0) {
            businessListenerDetail.setParameterClassType(null);
        } else {
            businessListenerDetail.setParameterClassType(method.getParameterTypes()[0]);
        }

        listener.add(businessListenerDetail);

        // 放到事件容器中
        EventContainer.addListenerList(businessCode, listener);
    }

}
