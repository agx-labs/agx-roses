package cn.stylefeng.roses.kernel.dict.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.dict.api.DictApi;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;

/**
 * JSON响应对组织字典id的转化
 * <p>
 * 走缓存加速字典名称的获取
 *
 * @author fengshuonan
 * @since 2023/5/4 21:20
 */
public class DictFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {
        Long dictId = Convert.toLong(businessId);
        DictApi bean = SpringUtil.getBean(DictApi.class);
        return bean.getDictNameByDictId(dictId);
    }

}
