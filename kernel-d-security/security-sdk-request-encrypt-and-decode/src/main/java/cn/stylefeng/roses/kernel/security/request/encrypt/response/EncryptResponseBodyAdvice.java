package cn.stylefeng.roses.kernel.security.request.encrypt.response;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.security.request.encrypt.exception.EncryptionException;
import cn.stylefeng.roses.kernel.security.request.encrypt.exception.enums.EncryptionExceptionEnum;
import cn.stylefeng.roses.kernel.security.request.encrypt.holder.TempSm4KeyHolder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.StandardCharsets;

/**
 * 相应数据加密
 *
 * @author fengshuonan
 * @since 2024/6/28 14:51
 */
@Slf4j
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断必须是PostResource注解并且requiredEncryption为true
     *
     * @author fengshuonan
     * @since 2024/6/28 14:54
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        PostResource postResource = methodParameter.getAnnotatedElement().getAnnotation(PostResource.class);
        if (postResource == null) {
            return false;
        }
        return postResource.requiredEncryption();
    }

    @Override
    @SuppressWarnings("rawtypes,unchecked")
    public Object beforeBodyWrite(@Nullable Object originBody,
                                  MethodParameter methodParameter,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 原始数据为空，直接返回
        if (originBody == null) {
            return null;
        }

        // 判断响应实体是否是ResponseData，只针对ResponseData进行加密
        if (!(originBody instanceof ResponseData)) {
            return originBody;
        }

        // 转换类型，获取ResponseData中的具体数据
        ResponseData responseData = (ResponseData) originBody;
        Object data = responseData.getData();
        if (data == null) {
            return originBody;
        }

        // 从ThreadLocal中获取解密出的SM4对称加密的秘钥
        String sm4Key = TempSm4KeyHolder.getSm4Key();
        if (StrUtil.isBlank(sm4Key)) {
            throw new EncryptionException(EncryptionExceptionEnum.GET_SM4_KEY_ERROR);
        }

        // 原始的Data转化为字符串，准备加密
        String originJsonString = JSON.toJSONString(data);

        // 进行SM4加密
        SM4 sm4 = SmUtil.sm4(sm4Key.getBytes());
        String encryptBase64 = sm4.encryptBase64(originJsonString, StandardCharsets.UTF_8);

        // 将加密后的数据放入ResponseData中
        responseData.setData(encryptBase64);

        // 清除当前线程中的aes key
        TempSm4KeyHolder.clearSm4Key();

        return responseData;

    }

}
