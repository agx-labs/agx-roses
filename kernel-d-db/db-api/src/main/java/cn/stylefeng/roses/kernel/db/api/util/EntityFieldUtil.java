package cn.stylefeng.roses.kernel.db.api.util;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseBusinessEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;

import java.lang.reflect.Field;

/**
 * 实体字段获取工具类
 *
 * @author fengshuonan
 * @since 2023/5/31 0:08
 */
public class EntityFieldUtil {

    /**
     * 获取指定对象的类中是否包含了指定字段名
     * <p>
     * 主要用在BaseExpandFieldEntity的子类中获取delFlag字段
     * 因为直接用getField()或者直接用getDeclaredField()都不能获取到父级的private属性的Field信息
     *
     * @author fengshuonan
     * @since 2023/5/31 0:09
     */
    public static Field getDeclaredField(Object declareObject, String fieldName) throws NoSuchFieldException {
        if (ObjectUtil.isEmpty(declareObject)) {
            throw new NoSuchFieldException();
        }

        Class<?> objectClass = declareObject.getClass();

        // 先获取本类中是否有指定字段
        try {

            return objectClass.getDeclaredField(fieldName);

        } catch (NoSuchFieldException e) {

            // 本类中没有这个字段，再去逐级获取父级类中是否有这个字段
            Class<?> superclass = objectClass.getSuperclass();

            // 如果父级是BaseExpandFieldEntity，则直接获取父父级字段
            if (superclass.equals(BaseExpandFieldEntity.class)) {
                Class<?> baseBusinessEntityClass = superclass.getSuperclass();
                return baseBusinessEntityClass.getDeclaredField(fieldName);
            }

            // 如果父级是BaseBusinessEntity，则直接获取父级的字段
            else if (superclass.equals(BaseBusinessEntity.class)) {
                return superclass.getDeclaredField(fieldName);
            }

            // 其他情况不做判断
            else {
                throw e;
            }
        }
    }

}
