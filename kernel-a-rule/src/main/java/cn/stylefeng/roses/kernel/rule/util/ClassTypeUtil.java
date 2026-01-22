package cn.stylefeng.roses.kernel.rule.util;

import cn.hutool.core.util.ClassUtil;
import cn.stylefeng.roses.kernel.rule.enums.FieldTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.clazz.ClassParseResult;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * 从新改良class类型解析
 * <p>
 * 返回的类型更简单，但是携带的信息更丰富一些，带具体的泛型的类型
 *
 * @author fengshuonan
 * @since 2024/8/6 21:48
 */
@Slf4j
public class ClassTypeUtil {

    /**
     * 获取类的类型
     *
     * @author fengshuonan
     * @since 2024/8/6 21:49
     */
    public static ClassParseResult getClassFieldType(Type type) {

        if(type == null){
            return new ClassParseResult(FieldTypeEnum.OTHER, null);
        }

        // 如果是具体类，不带泛型
        if (type instanceof Class) {

            Class<?> clazz = (Class<?>) type;

            // 判断数字类型
            if (validateNumericTypeFlag(clazz)) {
                return new ClassParseResult(FieldTypeEnum.NUMBER, null);
            }

            // 判断是否是其他基本类型，如果是其他类型，认为是字符串
            else if (ClassUtil.isSimpleValueType(clazz)) {
                return new ClassParseResult(FieldTypeEnum.STRING, null);
            }

            // 判断是否是数组类型
            else if (clazz.isArray()) {
                // 获取array的具体类型
                Class<?> componentType = clazz.getComponentType();
                return new ClassParseResult(FieldTypeEnum.ARRAY, componentType);
            }

            // 如果是集合类型，纯集合类型，不带泛型
            else if (Collection.class.isAssignableFrom(clazz)) {
                return new ClassParseResult(FieldTypeEnum.COLLECTION, null);
            }

            // 如果是Object类型，则认定为基本类型，不解析他的具体内容
            // 【注意】如果是解析具体实体类型，用AdvancedClassTypeUtil类
            else if (Object.class.isAssignableFrom(clazz)) {
                return new ClassParseResult(FieldTypeEnum.OBJECT, null);
            }

            // 其他类型，暂不处理
            else {
                log.debug("类型是Class，但有处理不到的情况，打印出类的信息如下：{}", clazz.toGenericString());
                return new ClassParseResult(FieldTypeEnum.OTHER, null);
            }
        }

        // 如果带具体泛型的类
        else if (type instanceof ParameterizedType) {

            ParameterizedType parameterizedType = (ParameterizedType) type;

            // 泛型的主体，例如 List<String>，主体是List
            Class<?> rawTypeClass = (Class<?>) parameterizedType.getRawType();

            // 泛型的类型，例如 List<String>，类型是String
            Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];

            // 如果泛型主体是集合
            if (Collection.class.isAssignableFrom(rawTypeClass)) {
                return new ClassParseResult(FieldTypeEnum.COLLECTION, actualTypeArgument);
            }

            // 如果是map类型，则认定为基本类型，不做处理，不解析他的元数据
            else if (Map.class.isAssignableFrom(rawTypeClass)) {
                return new ClassParseResult(FieldTypeEnum.STRING, null);
            }

            // 如果是Object类型，则认定为基本类型，不解析他的具体内容
            // 【注意】如果是解析具体实体类型，用AdvancedClassTypeUtil类
            else if (Object.class.isAssignableFrom(rawTypeClass)) {
                return new ClassParseResult(FieldTypeEnum.OBJECT_WITH_GENERIC, actualTypeArgument);
            }

            // 泛型的主体情况不确定，不处理
            else {
                log.debug("泛型的主体情况不确定，不处理，打印出rawTypeClass：{}", rawTypeClass.getName());
                return new ClassParseResult(FieldTypeEnum.OTHER, null);
            }
        }

        // 带T的参数，例如解析到ResponseData<T>中的data字段就是这种情况
        // 这种情况，单纯从参数中是拿不到T的，需要从外部的参数中获取T的类型
        else if (type.getTypeName().equals("T")) {
            return new ClassParseResult(FieldTypeEnum.WITH_UNKNOWN_OBJ_GENERIC, null);
        }

        // 其他情况，既不是class也不是ParameterizedType
        else {
            log.debug("未知类型的处理，既不是class也不是ParameterizedType，打印出类的信息如下：{}", type.getTypeName());
            return new ClassParseResult(FieldTypeEnum.OTHER, null);
        }
    }

    /**
     * 判断一个class是否为数字类型
     *
     * @author fengshuonan
     * @since 2024/8/25 22:27
     */
    public static boolean validateNumericTypeFlag(Class<?> clazz) {

        // 排除掉Long类型，前端不识别Long类型
        if (clazz == Long.class || clazz == long.class) {
            return false;
        }

        // 如果是原始类型
        else if (clazz.isPrimitive()) {
            return clazz == byte.class || clazz == short.class || clazz == int.class || clazz == float.class || clazz == double.class;
        }

        // 如果是包装类型
        else {
            return Number.class.isAssignableFrom(clazz);
        }
    }

}