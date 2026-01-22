package cn.stylefeng.roses.kernel.scanner.api.factory;

import cn.stylefeng.roses.kernel.scanner.api.constants.ScannerConstants;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.FieldMetadata;

/**
 * 针对数组特殊信息的填充
 *
 * @author fengshuonan
 * @since 2024/8/14 11:44
 */
public class ArrayMetadataFactory {

    /**
     * 填充数组的items元素的基本数据
     *
     * @author fengshuonan
     * @since 2024/8/14 11:41
     */
    public static void fillArrayItemFieldMetaData(FieldMetadata fieldMetadata) {
        if (fieldMetadata == null) {
            return;
        }
        fieldMetadata.setChineseName("数组元素类型");
        fieldMetadata.setFieldName(ScannerConstants.ARRAY_FILED_CODE);
    }

}
