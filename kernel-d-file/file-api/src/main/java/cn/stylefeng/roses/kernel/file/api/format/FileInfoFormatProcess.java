package cn.stylefeng.roses.kernel.file.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.file.api.FileInfoApi;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;

/**
 * 针对文件id的包装处理，返回文件可以访问的地址和文件的名称
 * <p>
 * 通过缓存加速获取文件信息
 *
 * @author fengshuonan
 * @since 2023/11/13 17:49
 */
public class FileInfoFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {
        FileInfoApi fileInfoApi = SpringUtil.getBean(FileInfoApi.class);
        return fileInfoApi.buildAntdvFileInfo(Convert.toLong(businessId));
    }

}
