package cn.stylefeng.roses.kernel.sys.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.PositionServiceApi;

/**
 * 职位名称的格式化
 * <p>
 * 采用职位名称缓存，加速获取
 *
 * @author fengshuonan
 * @since 2023/7/15 21:58
 */
public class PositionNameFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {

        if (businessId == null) {
            return null;
        }

        Long positionId = Convert.toLong(businessId);

        PositionServiceApi positionServiceApi = SpringUtil.getBean(PositionServiceApi.class);

        return positionServiceApi.getPositionName(positionId);
    }

}
