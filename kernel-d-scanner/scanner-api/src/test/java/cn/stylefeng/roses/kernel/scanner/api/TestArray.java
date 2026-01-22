package cn.stylefeng.roses.kernel.scanner.api;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.scanner.api.factory.ClassMetaFactory;
import cn.stylefeng.roses.kernel.scanner.api.pojo.SimpleObject;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.FieldMetadata;
import cn.stylefeng.roses.kernel.scanner.api.util.AdvancedClassTypeUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * 测试数组元素的类型解析
 *
 * @author fengshuonan
 * @since 2024/8/7 14:39
 */
public class TestArray {

    @BeforeEach
    public void setUp() {
        AdvancedClassTypeUtil.TEMP_SCAN_PACKAGE_LIST = ListUtil.of("cn.stylefeng");
    }

    @Test
    public void testSimpleArray() {
        Type type = new TypeReference<List<String>>() {
        }.getType();

        FieldMetadata fieldMetadata = ClassMetaFactory.beginCreateFieldMetadata(type, IdUtil.fastSimpleUUID());

        String jsonString = JSON.toJSONString(fieldMetadata, JSONWriter.Feature.PrettyFormat);

        System.out.println(jsonString);
    }

    @Test
    public void testObjectArray() {
        Type type = new TypeReference<List<SimpleObject>>() {
        }.getType();

        FieldMetadata fieldMetadata = ClassMetaFactory.beginCreateFieldMetadata(type, IdUtil.fastSimpleUUID());

        String jsonString = JSON.toJSONString(fieldMetadata, JSONWriter.Feature.PrettyFormat);

        System.out.println(jsonString);
    }

    @Test
    public void testGenArray() {
        Type type = new TypeReference<ResponseData<List<SimpleObject>>>() {
        }.getType();

        FieldMetadata fieldMetadata = ClassMetaFactory.beginCreateFieldMetadata(type, IdUtil.fastSimpleUUID());

        String jsonString = JSON.toJSONString(fieldMetadata, JSONWriter.Feature.PrettyFormat);

        System.out.println(jsonString);
    }

    @Test
    public void testMultiArray() {
        Type type = new TypeReference<Set<List<List<SimpleObject>>>>() {
        }.getType();

        FieldMetadata fieldMetadata = ClassMetaFactory.beginCreateFieldMetadata(type, IdUtil.fastSimpleUUID());

        String jsonString = JSON.toJSONString(fieldMetadata, JSONWriter.Feature.PrettyFormat);

        System.out.println(jsonString);
    }

}
