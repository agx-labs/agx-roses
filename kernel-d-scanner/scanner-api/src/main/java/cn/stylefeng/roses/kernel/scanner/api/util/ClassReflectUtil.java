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
package cn.stylefeng.roses.kernel.scanner.api.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 类的反射工具
 *
 * @author fengshuonan
 * @since 2020/12/8 18:23
 */
public class ClassReflectUtil {

    /**
     * 注解转化为string名称
     * <p>
     * 例如：
     * NotBlack注解 > NotBlack
     *
     * @author fengshuonan
     * @since 2020/12/9 13:39
     */
    public static Set<String> annotationsToStrings(Annotation[] annotations) {
        Set<String> strings = new HashSet<>();

        if (annotations == null || annotations.length == 0) {
            return strings;
        }

        for (Annotation annotation : annotations) {
            strings.add(annotation.annotationType().getSimpleName());
        }

        return strings;
    }

    /**
     * 调用注解上的某个方法，并获取结果，忽略异常
     *
     * @author fengshuonan
     * @since 2020/12/8 17:13
     */
    public static <T> T invokeAnnotationMethodIgnoreError(Annotation apiResource, String methodName, Class<T> resultType) {
        try {
            Class<? extends Annotation> annotationType = apiResource.annotationType();
            Method method = annotationType.getMethod(methodName);
            return (T) method.invoke(apiResource);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // 忽略异常
        }
        return null;
    }

    /**
     * 将字段上的分组注解添加到对应的组中
     *
     * @param fieldAnnotation     字段上的注解
     * @param validateGroupsClass 校验分组
     * @param groupAnnotations    分组注解集合
     * @author fengshuonan
     * @since 2020/12/8 19:12
     */
    public static void addGroupValidateAnnotation(Annotation fieldAnnotation, Class<?> validateGroupsClass, Map<String, Set<String>> groupAnnotations) {
        Set<String> annotations = groupAnnotations.get(validateGroupsClass.getSimpleName());
        if (annotations == null) {
            annotations = new HashSet<>();
        }
        String messageTip = invokeAnnotationMethodIgnoreError(fieldAnnotation, "message", String.class);
        annotations.add(messageTip);
        groupAnnotations.put(validateGroupsClass.getSimpleName(), annotations);
    }

}
