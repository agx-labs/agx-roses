package cn.stylefeng.roses.kernel.wrapper.field.dict;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.dict.api.context.DictContext;
import cn.stylefeng.roses.kernel.rule.enums.FormatTypeEnum;
import cn.stylefeng.roses.kernel.wrapper.field.util.CommonFormatUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 针对@DictCodeFieldFormat注解的具体序列化过程
 *
 * @author fengshuonan
 * @since 2023/6/27 21:05
 */
@Slf4j
public class DictCodeFormatSerializer extends JsonSerializer<Object> {

    /**
     * 序列化类型，覆盖还是wrapper模式
     */
    private final FormatTypeEnum formatTypeEnum;

    /**
     * 字典类型编码
     */
    private final String dictTypeCode;

    public DictCodeFormatSerializer(FormatTypeEnum formatTypeEnum, String dictTypeCode) {
        this.formatTypeEnum = formatTypeEnum;
        this.dictTypeCode = dictTypeCode;
    }

    @Override
    public void serialize(Object originValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            this.action(originValue, jsonGenerator, serializerProvider);
        } catch (Exception e) {
            log.error("执行json的字段序列化出错", e);
            // 报错后继续写入原始值，否则会响应的json不是规范的json
            jsonGenerator.writeObject(originValue);
        }
    }

    /**
     * 真正处理序列化的逻辑
     *
     * @author fengshuonan
     * @since 2022/9/7 11:11
     */
    private void action(Object originValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws
            InstantiationException, IllegalAccessException, IOException {

        // 参数值为空，直接跳过格式化
        if (ObjectUtil.isEmpty(originValue)) {
            jsonGenerator.writeObject(originValue);
            return;
        }

        // 判断当前字段类型是否是String类型，如果不是String类型则直接跳过，因为字典编码是String类型的
        String dictCode = StrUtil.toString(originValue);

        // 执行转化，根据字典类型编码，以及字典的编码，获取指定字典的中文名称
        String dictName = DictContext.me().getDictName(this.dictTypeCode, dictCode);

        // 将转化的值，根据策略，进行写入到渲染的json中
        CommonFormatUtil.writeField(formatTypeEnum, originValue, dictName, jsonGenerator);
    }

}
