package cn.stylefeng.roses.kernel.scanner.api.factory;

import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.FieldMetadata;
import cn.stylefeng.roses.kernel.scanner.api.util.ClassReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 针对字段注解的解析
 *
 * @author fengshuonan
 * @since 2024/8/7 10:38
 */
public class AnnotationParseFactory {

    /**
     * 解析字段上的注解
     *
     * @author fengshuonan
     * @since 2022/1/14 11:57
     */
    public static void parsingAnnotation(Field declaredField, FieldMetadata fieldDescription) {
        Annotation[] annotations = declaredField.getAnnotations();
        if (annotations != null && annotations.length > 0) {

            // 设置字段的所有注解
            fieldDescription.setAnnotations(ClassReflectUtil.annotationsToStrings(annotations));

            // 遍历字段上的所有注解，找到带groups属性的，按group分类组装注解
            Map<String, Set<String>> groupAnnotations = new HashMap<>();
            for (Annotation annotation : annotations) {
                Class<?>[] validateGroupsClasses = ClassReflectUtil.invokeAnnotationMethodIgnoreError(annotation, "groups", Class[].class);
                if (validateGroupsClasses != null) {
                    for (Class<?> validateGroupsClass : validateGroupsClasses) {
                        ClassReflectUtil.addGroupValidateAnnotation(annotation, validateGroupsClass, groupAnnotations);
                    }
                }
            }
            // 设置分组注解
            fieldDescription.setGroupValidationMessage(groupAnnotations);
        }
    }

}
