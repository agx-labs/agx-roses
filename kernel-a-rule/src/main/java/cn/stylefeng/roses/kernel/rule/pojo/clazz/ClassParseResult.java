package cn.stylefeng.roses.kernel.rule.pojo.clazz;

import cn.stylefeng.roses.kernel.rule.enums.FieldTypeEnum;
import lombok.Data;

import java.lang.reflect.Type;

/**
 * class解析的结果
 *
 * @author fengshuonan
 * @since 2024/8/6 21:49
 */
@Data
public class ClassParseResult {

    /**
     * 字段的类型
     */
    private FieldTypeEnum fieldTypeEnum;

    /**
     * 原始解析的type
     * <p>
     * 如果 FieldTypeEnum = FieldTypeEnum.OBJECT ，则这个字段代表object的类型
     */
    private Type originType;

    /**
     * 数组、集合、泛型的具体类型
     * <p>
     * 如果 FieldTypeEnum = FieldTypeEnum.ARRAY ，则这个字段代表数组的具体类型
     * 如果 FieldTypeEnum = FieldTypeEnum.COLLECTION ，则这个字段代表集合的具体类型
     * 如果 FieldTypeEnum = FieldTypeEnum.OBJECT_WITH_GENERIC ，则这个字段代表 泛型 的具体类型
     * 如果 FieldTypeEnum = FieldTypeEnum.WITH_UNKNOWN_GENERIC ，则这个字段代表 泛型 的具体类型
     */
    private Type genericType;

    /**
     * 针对 FieldTypeEnum = FieldTypeEnum.OBJECT_WITH_GENERIC ，返回主体的class类型
     */
    private Class<?> rawTypeClass;

    public ClassParseResult() {
    }

    public ClassParseResult(FieldTypeEnum fieldTypeEnum, Type genericType) {
        this.fieldTypeEnum = fieldTypeEnum;
        this.genericType = genericType;
    }

    public ClassParseResult(FieldTypeEnum fieldTypeEnum, Type genericType, Class<?> rawTypeClass) {
        this.fieldTypeEnum = fieldTypeEnum;
        this.genericType = genericType;
        this.rawTypeClass = rawTypeClass;
    }

}
