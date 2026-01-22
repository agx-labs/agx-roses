package cn.stylefeng.roses.kernel.rule.format;

import cn.stylefeng.roses.kernel.rule.base.SimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.rule.enums.FieldTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.clazz.ClassParseResult;
import cn.stylefeng.roses.kernel.rule.util.ClassTypeUtil;
import cn.stylefeng.roses.kernel.rule.util.MixFieldTypeUtil;
import cn.stylefeng.roses.kernel.rule.util.ObjectConvertUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 针对一般业务型的主键id的转化
 *
 * @author fengshuonan
 * @since 2022/9/7 11:50
 */
public abstract class BaseSimpleFieldFormatProcess implements SimpleFieldFormatProcess {

    @Override
    public boolean canFormat(Object originValue) {
        return MixFieldTypeUtil.whetherAssignClass(originValue, this.getItemClass());
    }

    @Override
    public Object formatProcess(Object originValue) {

        // 先获取是基础类型还是集合类型
        ClassParseResult classParseResult = ClassTypeUtil.getClassFieldType(originValue.getClass());

        // 基础类型，直接转化
        if (FieldTypeEnum.STRING.equals(classParseResult.getFieldTypeEnum())
                || FieldTypeEnum.NUMBER.equals(classParseResult.getFieldTypeEnum())) {
            return this.simpleItemFormat(originValue);
        }

        // 集合类型
        if (FieldTypeEnum.COLLECTION.equals(classParseResult.getFieldTypeEnum())) {
            Collection<?> originValueList = (Collection<?>) originValue;
            List<Object> parsedList = new ArrayList<>();
            for (Object itemObject : originValueList) {
                parsedList.add(this.simpleItemFormat(itemObject));
            }
            return parsedList;
        }

        // 数组类型
        if (FieldTypeEnum.ARRAY.equals(classParseResult.getFieldTypeEnum())) {
            Object[] originValueArray = ObjectConvertUtil.objToArray(originValue);
            List<Object> parsedList = new ArrayList<>();
            for (Object itemObject : originValueArray) {
                parsedList.add(this.simpleItemFormat(itemObject));
            }
            return parsedList;
        }

        return originValue;
    }

    /**
     * 原始值得类型
     *
     * @author fengshuonan
     * @since 2022/9/7 11:52
     */
    public abstract Class<?> getItemClass();

    /**
     * 格式化转化的过程
     * <p>
     * 注意，这里只需写基本类型转化的逻辑，例如userId -> 用户名称
     *
     * @param businessId 业务id
     * @return 转化之后的值
     * @author fengshuonan
     * @since 2022/9/7 12:58
     */
    public abstract Object simpleItemFormat(Object businessId);

}
