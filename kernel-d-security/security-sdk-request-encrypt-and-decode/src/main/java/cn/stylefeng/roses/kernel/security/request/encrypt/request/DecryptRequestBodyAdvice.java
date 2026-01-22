package cn.stylefeng.roses.kernel.security.request.encrypt.request;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 请求参数解密
 *
 * @author luojie
 * @since 2021/3/23 12:53
 */
@Slf4j
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    /**
     * 更新前置条件，判断必须是PostResource注解并且requiredEncryption为true
     *
     * @author fengshuonan
     * @since 2024/6/28 14:07
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        PostResource postResource = methodParameter.getAnnotatedElement().getAnnotation(PostResource.class);
        if (postResource == null) {
            return false;
        }
        return postResource.requiredEncryption();
    }

    /**
     * 在读取RequestBody之前，对请求的内容进行解密
     * <p>
     * 密文如下：
     * <pre>
     * {
     *   "passedKey":"xxxx",
     *   "passedData":"xxxx"
     * }
     * </pre>
     * passedKey是经过SM2非对称加密过的秘钥，这个秘钥用来作为SM4对称加密的秘钥，如果开启了加密，每次passedKey都会随机生成
     * passedData是将原始参数数据，经过SM4对称加密过的，对称加密的秘钥，需要将passedKey进行SM2非对称加密解密即可
     *
     * @author fengshuonan
     * @since 2024/6/28 14:07
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                           MethodParameter parameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        Method method = parameter.getMethod();
        if (method == null) {
            return inputMessage;
        }

        // 获取方法上的PostResource注解
        PostResource postResource = method.getAnnotation(PostResource.class);
        if (postResource == null) {
            return inputMessage;
        }

        // 将原始的请求body解析，解析为JSON字符串
        InputStream body = inputMessage.getBody();
        String encryptedJsonString = IoUtil.readUtf8(body);

        // 请求体为空，原样返回
        if (StrUtil.isBlank(encryptedJsonString)) {
            return inputMessage;
        }

        // 进行解密操作，解密出来HttpInputMessage
        return new CustomDecryptHttpInputMessage(inputMessage.getHeaders(), encryptedJsonString);
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body,
                                  HttpInputMessage inputMessage,
                                  MethodParameter parameter,
                                  Type targetType,
                                  Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}
