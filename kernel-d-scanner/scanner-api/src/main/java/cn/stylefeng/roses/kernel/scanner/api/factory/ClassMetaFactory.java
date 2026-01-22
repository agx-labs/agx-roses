package cn.stylefeng.roses.kernel.scanner.api.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.enums.FieldTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.clazz.ClassParseResult;
import cn.stylefeng.roses.kernel.scanner.api.constants.ScannerConstants;
import cn.stylefeng.roses.kernel.scanner.api.context.MetadataContext;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.FieldMetadata;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.SubFieldMetadataDTO;
import cn.stylefeng.roses.kernel.scanner.api.util.AdvancedClassTypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 字段信息创建工具，一般用这个类作为类解析的入口
 *
 * @author fengshuonan
 * @since 2022/1/13 13:49
 */
@Slf4j
public class ClassMetaFactory {

    /**
     * 通过传入的类型（Class或ParameterizedType）进行字段校验，解析出字段的元数据
     *
     * @param type 需要被解析的对象的类型，可以是class也可以是泛型
     * @param uuid 随机字符串，保证唯一性，用来标识从开始到结束一个context周期内的一系列解析
     * @return 传入类型的字段元数据信息
     * @author fengshuonan
     * @since 2022/1/13 13:51
     */
    public static FieldMetadata beginCreateFieldMetadata(Type type, String uuid) {
        ClassParseResult classParseResult = AdvancedClassTypeUtil.getClassFieldType(type);
        return beginCreateFieldMetadata(classParseResult, uuid);
    }

    /**
     * 通过传入ClassParseResult进行字段校验
     *
     * @author fengshuonan
     * @since 2024/8/7 16:27
     */
    public static FieldMetadata beginCreateFieldMetadata(ClassParseResult classParseResult, String uuid) {
        // 1. 如果是基础类型，直接渲染结果
        if (FieldTypeEnum.STRING.equals(classParseResult.getFieldTypeEnum()) || FieldTypeEnum.NUMBER.equals(classParseResult.getFieldTypeEnum())) {
            return MetadataCreateFactory.createBaseClassMetadata((Class<?>) classParseResult.getOriginType(), classParseResult.getFieldTypeEnum(), uuid);
        }

        // 2. 如果是数组类型，则先创建一个数组类型的元数据，再获取数组具体的类型，再继续解析数组的类型
        else if (FieldTypeEnum.ARRAY.equals(classParseResult.getFieldTypeEnum())) {
            return createArrayMetaDateResult(uuid, classParseResult);
        }

        // 3. 如果是collection集合类型，同array类型
        else if (FieldTypeEnum.COLLECTION.equals(classParseResult.getFieldTypeEnum())) {
            return createArrayMetaDateResult(uuid, classParseResult);
        }

        // 4. 如果是Object类型，则找到对象的所有的子字段，填充到genericFieldMetadata这个字段中
        else if (FieldTypeEnum.OBJECT.equals(classParseResult.getFieldTypeEnum())) {
            // 获取对象的类信息
            Class<?> clazz = (Class<?>) classParseResult.getOriginType();

            // 创建对象的基础信息
            FieldMetadata currentClassMetaData = MetadataCreateFactory.createBaseClassMetadata(clazz, classParseResult.getFieldTypeEnum(), uuid);

            // 如果被初始化过，则不继续进行解析
            if (MetadataContext.ensureFieldClassHaveParse(uuid, clazz)) {
                return currentClassMetaData;
            }

            // 获取对象的所有字段信息，这里因为要递归获取本类+父类的字段，所以先创建空集合
            Set<FieldMetadata> totalFieldList = new LinkedHashSet<>();
            ClassMetaFactory.processClassFields(clazz, totalFieldList, uuid);
            currentClassMetaData.setGenericFieldMetadata(totalFieldList);
            return currentClassMetaData;
        }

        // 5. 如果是携带泛型的对象
        else if (FieldTypeEnum.OBJECT_WITH_GENERIC.equals(classParseResult.getFieldTypeEnum())) {

            // 先解析主体的字段信息
            Class<?> rawTypeClass = classParseResult.getRawTypeClass();

            // 解析主体字段的所有信息
            FieldMetadata rawTypeMetadata = ClassMetaFactory.beginCreateFieldMetadata(rawTypeClass, uuid);

            // **这里需要遍历一下所有字段，如果有T类型，则需要单独配置一下T类型的FieldMetadata**
            // 遍历所有字段
            Set<FieldMetadata> genericFieldMetadata = rawTypeMetadata.getGenericFieldMetadata();
            if (ObjectUtil.isEmpty(genericFieldMetadata)) {
                return rawTypeMetadata;
            }
            for (FieldMetadata subField : genericFieldMetadata) {

                // 1. 如果是带T的对象，则需要获取泛型的具体类型
                if (FieldTypeEnum.WITH_UNKNOWN_OBJ_GENERIC.getCode().equals(subField.getFieldType())) {
                    Type subFieldGenericType = classParseResult.getGenericType();
                    ClassMetaFactory.getGenericObjFieldTypeAndFillOriginMeta(uuid, subField, subFieldGenericType);
                }

                // 2. 如果是List<T>也一样，需要从这里获取泛型的具体类型
                if (FieldTypeEnum.WITH_UNKNOWN_ARRAY_GENERIC.getCode().equals(subField.getFieldType())) {
                    Type arrayRealGenericType = classParseResult.getGenericType();
                    ClassMetaFactory.getGenericListFieldTypeAndFillOriginMeta(uuid, subField, arrayRealGenericType);
                }
            }
            return rawTypeMetadata;
        }

        // 6. Map的类型
        else if (FieldTypeEnum.MAP.equals(classParseResult.getFieldTypeEnum())) {
            return MetadataCreateFactory.createBaseClassMetadata(Map.class, FieldTypeEnum.MAP, uuid);
        }

        // 7. 其他情况，返回最基本的数据
        else {
            return MetadataCreateFactory.createBaseClassMetadata(String.class, FieldTypeEnum.STRING, uuid);
        }
    }

    /**
     * 创建数组类型的元数据
     *
     * @author fengshuonan
     * @since 2024/8/7 10:05
     */
    private static FieldMetadata createArrayMetaDateResult(String uuid, ClassParseResult classParseResult) {
        // 先创建一个数组类型的元数据
        FieldMetadata arrayItemMetadata = MetadataCreateFactory.createBaseClassMetadata(List.class, FieldTypeEnum.COLLECTION, uuid);

        // 获取数组元素的类型
        Type genericType = classParseResult.getGenericType();

        // 再将数组的类型进行解析，（递归调用）
        FieldMetadata arrayFieldMetadata = ClassMetaFactory.beginCreateFieldMetadata(genericType, uuid);

        // 【2024年8月14日新增】填充数组特殊处理
        ArrayMetadataFactory.fillArrayItemFieldMetaData(arrayFieldMetadata);
        arrayItemMetadata.setArrayFieldMetadata(arrayFieldMetadata);

        return arrayItemMetadata;
    }

    /**
     * 获取指定类的字段信息，增加可以获取父级的类的字段
     *
     * @author fengshuonan
     * @since 2024/8/7 10:15
     */
    private static void processClassFields(Class<?> clazz, Set<FieldMetadata> fieldMetadata, String uuid) {
        if (clazz == null || Object.class.equals(clazz)) {
            return;
        }

        // 在处理Object中所有字段之前，将当前父类放进context，所有子字段不能含有父类的类型，否则会递归
        MetadataContext.addClassRecord(uuid, clazz.getName());

        // 获取类中的所有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            // 1. 判断字段是否是基础字段例如serialVersionUID，或者delFlag等字段
            if (ScannerConstants.DONT_PARSE_FIELD.contains(field.getName())) {
                continue;
            }

            // 2. 先填充这个字段的基础信息
            FieldMetadata fieldInfo = MetadataCreateFactory.createBaseFieldMetadata(field, uuid);

            // 3. 再根据这个字段的类型，进行递归解析子属性的填充
            Type genericType = field.getGenericType();

            SubFieldMetadataDTO typeSubInfo = ClassMetaFactory.getTypeSubInfo(uuid, genericType);
            fieldInfo.setGenericFieldMetadata(typeSubInfo.getGenericFieldMetadata());

            // 【2024年8月14日新增】填充数组特殊处理
            ArrayMetadataFactory.fillArrayItemFieldMetaData(typeSubInfo.getArrayFieldMetadata());
            fieldInfo.setArrayFieldMetadata(typeSubInfo.getArrayFieldMetadata());

            fieldMetadata.add(fieldInfo);
        }

        // 递归处理父类
        processClassFields(clazz.getSuperclass(), fieldMetadata, uuid);
    }

    /**
     * 获取一个Type是否包含子对象或者数组元素
     * <p>
     * 用来解析针对数组和obj类型的时候
     *
     * @author fengshuonan
     * @since 2024/8/7 11:43
     */
    public static SubFieldMetadataDTO getTypeSubInfo(String uuid, Type type) {
        SubFieldMetadataDTO fieldInfo = new SubFieldMetadataDTO();

        // 1. 获取这个对应type的类型信息
        FieldMetadata validateFieldHaveSubInfo = ClassMetaFactory.beginCreateFieldMetadata(type, uuid);

        // 2. 填充他的子对象，用来针对obj类型的字段进行解析
        Set<FieldMetadata> genericFieldMetadata = validateFieldHaveSubInfo.getGenericFieldMetadata();
        if (ObjectUtil.isNotEmpty(genericFieldMetadata)) {
            fieldInfo.setGenericFieldMetadata(genericFieldMetadata);
        }

        // 3. 填充他的数组对象，用来针对array类型的字段进行解析
        FieldMetadata arrayFieldMetadata = validateFieldHaveSubInfo.getArrayFieldMetadata();
        if (ObjectUtil.isNotEmpty(arrayFieldMetadata)) {
            fieldInfo.setArrayFieldMetadata(arrayFieldMetadata);
        }

        return fieldInfo;
    }

    /**
     * 获取子字段的类型，并填充原有的解析字段的部分内容
     *
     * @author fengshuonan
     * @since 2024/8/7 15:11
     */
    private static void getGenericObjFieldTypeAndFillOriginMeta(String uuid, FieldMetadata subField, Type subFieldType) {
        FieldMetadata realSubFieldMetadata = ClassMetaFactory.beginCreateFieldMetadata(subFieldType, uuid);
        subField.setFieldClassType(realSubFieldMetadata.getFieldClassType());
        subField.setFieldClassPath(realSubFieldMetadata.getFieldClassPath());
        subField.setFieldType(realSubFieldMetadata.getFieldType());
        subField.setGenericFieldMetadata(realSubFieldMetadata.getGenericFieldMetadata());

        // 【2024年8月14日新增】填充数组特殊处理
        ArrayMetadataFactory.fillArrayItemFieldMetaData(realSubFieldMetadata.getArrayFieldMetadata());
        subField.setArrayFieldMetadata(realSubFieldMetadata.getArrayFieldMetadata());
    }

    /**
     * 填充泛型子字段的类型和信息，并填充原有的解析字段的部分内容
     *
     * @author fengshuonan
     * @since 2024/8/7 16:32
     */
    private static void getGenericListFieldTypeAndFillOriginMeta(String uuid, FieldMetadata subField, Type arrayRealGenericType) {
        ClassParseResult arrayRealGenericTypeResult = new ClassParseResult(FieldTypeEnum.ARRAY, arrayRealGenericType);
        FieldMetadata fieldMetadata = ClassMetaFactory.beginCreateFieldMetadata(arrayRealGenericTypeResult, uuid);
        subField.setFieldType(FieldTypeEnum.ARRAY.getCode());

        // 【2024年8月14日新增】填充数组特殊处理
        ArrayMetadataFactory.fillArrayItemFieldMetaData(fieldMetadata.getArrayFieldMetadata());
        subField.setArrayFieldMetadata(fieldMetadata.getArrayFieldMetadata());
    }

}
