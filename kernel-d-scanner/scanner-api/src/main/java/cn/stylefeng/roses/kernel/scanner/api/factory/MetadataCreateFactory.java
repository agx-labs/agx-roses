package cn.stylefeng.roses.kernel.scanner.api.factory;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.enums.FieldTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.clazz.ClassParseResult;
import cn.stylefeng.roses.kernel.scanner.api.context.MetadataContext;
import cn.stylefeng.roses.kernel.scanner.api.enums.ParamTypeEnum;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.FieldMetadata;
import cn.stylefeng.roses.kernel.scanner.api.util.AdvancedClassTypeUtil;

import java.lang.reflect.Field;

/**
 * 元数据的生成工厂
 *
 * @author fengshuonan
 * @since 2024/8/7 9:49
 */
public class MetadataCreateFactory {

    /**
     * 根据一个类，创建metadata
     * <p>
     * 一般这个用在fieldTypeEnum为基础字段的时候使用
     *
     * @author fengshuonan
     * @since 2024/8/7 9:48
     */
    public static FieldMetadata createBaseClassMetadata(Class<?> clazz, FieldTypeEnum fieldTypeEnum, String uuid) {
        FieldMetadata fieldMetadataItem = new FieldMetadata();

        // 设置唯一id
        fieldMetadataItem.setMetadataId(IdUtil.fastSimpleUUID());

        // 设置字段中文含义
        fieldMetadataItem.setChineseName(clazz.getSimpleName());

        // 设置字段类类型
        fieldMetadataItem.setFieldClassType(clazz.getSimpleName());

        // 设置类的全路径
        fieldMetadataItem.setFieldClassPath(clazz.getName());

        // 根据uuid获取参数的名称
        String paramName = MetadataContext.getParamName(uuid);
        if (StrUtil.isNotBlank(paramName)) {
            fieldMetadataItem.setFieldName(paramName);
        }

        // 设置字段类型，基本类型
        fieldMetadataItem.setFieldType(fieldTypeEnum.getCode());

        // 设置当前context构造的参数类型
        ParamTypeEnum paramTypeMetadata = MetadataContext.getParamTypeMetadata(uuid);
        if (paramTypeMetadata != null) {
            fieldMetadataItem.setRequestParamType(paramTypeMetadata.getCode());
        }

        // 设置字段
        return fieldMetadataItem;
    }

    /**
     * 创建针对字段的元数据描述
     *
     * @author fengshuonan
     * @since 2024/8/7 10:36
     */
    public static FieldMetadata createBaseFieldMetadata(Field field, String uuid) {
        FieldMetadata fieldMetadataItem = new FieldMetadata();

        // 设置唯一id
        fieldMetadataItem.setMetadataId(IdUtil.fastSimpleUUID());

        // 设置字段中文含义
        ChineseDescription annotation = field.getAnnotation(ChineseDescription.class);
        if (annotation != null) {
            fieldMetadataItem.setChineseName(annotation.value());
        }

        // 设置字段类类型
        Class<?> classType = field.getType();
        fieldMetadataItem.setFieldClassType(classType.getSimpleName());

        // 设置类的全路径
        fieldMetadataItem.setFieldClassPath(classType.getName());

        // 设置对应字段名称
        fieldMetadataItem.setFieldName(field.getName());

        // 处理注解信息
        AnnotationParseFactory.parsingAnnotation(field, fieldMetadataItem);

        // 设置字段类型，基本、数组、还是object
        ClassParseResult classParseResult = AdvancedClassTypeUtil.getClassFieldType(field.getGenericType());
        fieldMetadataItem.setFieldType(classParseResult.getFieldTypeEnum().getCode());

        // 设置当前context构造的参数类型
        ParamTypeEnum paramTypeMetadata = MetadataContext.getParamTypeMetadata(uuid);
        if (paramTypeMetadata != null) {
            fieldMetadataItem.setRequestParamType(paramTypeMetadata.getCode());
        }

        return fieldMetadataItem;
    }

}
