package cn.stylefeng.roses.kernel.scanner.api.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.enums.FieldTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.clazz.ClassParseResult;
import cn.stylefeng.roses.kernel.rule.util.ClassTypeUtil;
import cn.stylefeng.roses.kernel.scanner.api.pojo.scanner.ScannerProperties;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基于原有的ClassTypeUtil升级
 * <p>
 * 可以判断指定的包下的类是否是pojo类
 *
 * @author fengshuonan
 * @since 2022/1/14 16:42
 */
@Slf4j
public class AdvancedClassTypeUtil {

    /**
     * scan包的缓存
     */
    public static List<String> TEMP_SCAN_PACKAGE_LIST = null;

    /**
     * 判断类类型是否是扫描的包范围之内
     *
     * @author fengshuonan
     * @since 2022/1/13 17:49
     */
    public static boolean ensureEntityFlag(Class<?> clazz) {

        if (TEMP_SCAN_PACKAGE_LIST == null) {
            String packageStr = null;
            try {
                ScannerProperties properties = SpringUtil.getBean(ScannerProperties.class);
                String entityScanPackage = properties.getEntityScanPackage();

                // 查询是否配置了包扫描
                if (ObjectUtil.isEmpty(entityScanPackage)) {
                    TEMP_SCAN_PACKAGE_LIST = new ArrayList<>();
                    return false;
                }

                // 获取包扫描
                String[] scanPackages = entityScanPackage.split(",");
                TEMP_SCAN_PACKAGE_LIST = ListUtil.toLinkedList(scanPackages);

            } catch (Exception e) {
                // 找不到相关类，则直接返回false
                TEMP_SCAN_PACKAGE_LIST = new ArrayList<>();
                return false;
            }
        }

        for (String packageName : TEMP_SCAN_PACKAGE_LIST) {
            if (clazz.getName().startsWith(packageName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取类类型的类别
     *
     * @author fengshuonan
     * @since 2022/1/14 0:25
     */
    public static ClassParseResult getClassFieldType(Type type) {

        if(type == null){
            return new ClassParseResult(FieldTypeEnum.OTHER, null);
        }

        // 如果是具体类，不带泛型
        if (type instanceof Class) {

            Class<?> clazz = (Class<?>) type;

            // 判断数字类型
            if (ClassTypeUtil.validateNumericTypeFlag(clazz)) {
                ClassParseResult classParseResult = new ClassParseResult(FieldTypeEnum.NUMBER, null);
                classParseResult.setOriginType(type);
                return classParseResult;
            }

            // 判断是否是其他基本类型，如果是其他类型，认为是字符串
            else if (ClassUtil.isSimpleValueType(clazz)) {
                ClassParseResult classParseResult = new ClassParseResult(FieldTypeEnum.STRING, null);
                classParseResult.setOriginType(type);
                return classParseResult;
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

            // 如果是实体对象类型
            else if (AdvancedClassTypeUtil.ensureEntityFlag(clazz)) {
                ClassParseResult classParseResult = new ClassParseResult(FieldTypeEnum.OBJECT, null);
                classParseResult.setOriginType(type);
                return classParseResult;
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
                if (actualTypeArgument.getTypeName().equals("T")) {
                    return new ClassParseResult(FieldTypeEnum.WITH_UNKNOWN_ARRAY_GENERIC, null);
                }
                return new ClassParseResult(FieldTypeEnum.COLLECTION, actualTypeArgument);
            }

            // 如果是map类型，则认定为基本类型，不做处理，不解析他的元数据
            else if (Map.class.isAssignableFrom(rawTypeClass)) {
                return new ClassParseResult(FieldTypeEnum.MAP, null);
            }

            // 如果泛型的主体是实体包装类
            else if (AdvancedClassTypeUtil.ensureEntityFlag(rawTypeClass)) {
                return new ClassParseResult(FieldTypeEnum.OBJECT_WITH_GENERIC, actualTypeArgument, rawTypeClass);
            }

            // 泛型的主体情况不确定，不处理
            else {
                log.debug("泛型的主体情况不确定，不处理，打印出rawTypeClass：{}", rawTypeClass.getName());
                return new ClassParseResult(FieldTypeEnum.OTHER, null);
            }
        }

        // 带T的参数，例如解析到ResponseData<T>中的data字段就是这种情况
        else if (type.getTypeName().equals("T")) {
            return new ClassParseResult(FieldTypeEnum.WITH_UNKNOWN_OBJ_GENERIC, null);
        }

        // 其他情况，既不是class也不是ParameterizedType
        else {
            log.debug("未知类型的处理，既不是class也不是ParameterizedType，打印出类的信息如下：{}", type.getTypeName());
            return new ClassParseResult(FieldTypeEnum.OTHER, null);
        }
    }

}
