package cn.stylefeng.roses.kernel.file.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.file.api.FileInfoApi;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;

/**
 * Json响应，针对返回的文件信息，响应一个文件的具体url
 * <p>
 * 通过缓存加速获取文件详情
 *
 * @author fengshuonan
 * @since 2023/3/28 9:30
 */
public class FileUrlFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {
        FileInfoApi fileInfoApi = SpringUtil.getBean(FileInfoApi.class);
        return fileInfoApi.getFileUnAuthUrl(Convert.toLong(businessId));
    }

}
