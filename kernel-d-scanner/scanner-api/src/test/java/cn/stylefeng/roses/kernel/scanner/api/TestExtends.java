package cn.stylefeng.roses.kernel.scanner.api;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.stylefeng.roses.kernel.scanner.api.factory.ClassMetaFactory;
import cn.stylefeng.roses.kernel.scanner.api.pojo.ExtendsSimpleObject;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.FieldMetadata;
import cn.stylefeng.roses.kernel.scanner.api.util.AdvancedClassTypeUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

/**
 * 更新继承的测试，测试是否能获取父级类的字段
 *
 * @author fengshuonan
 * @since 2024/8/7 14:47
 */
public class TestExtends {

    @BeforeEach
    public void setUp() {
        AdvancedClassTypeUtil.TEMP_SCAN_PACKAGE_LIST = ListUtil.of("cn.stylefeng");
    }

    @Test
    public void testExtends() {
        Type type = new TypeReference<ExtendsSimpleObject>() {
        }.getType();

        FieldMetadata fieldMetadata = ClassMetaFactory.beginCreateFieldMetadata(type, IdUtil.fastSimpleUUID());

        String jsonString = JSON.toJSONString(fieldMetadata, JSONWriter.Feature.PrettyFormat);

        System.out.println(jsonString);
    }


}
