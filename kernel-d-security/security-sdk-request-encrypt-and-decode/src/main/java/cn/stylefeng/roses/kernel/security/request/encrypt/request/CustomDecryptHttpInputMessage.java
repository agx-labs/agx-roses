package cn.stylefeng.roses.kernel.security.request.encrypt.request;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.security.guomi.GuomiUtil;
import cn.stylefeng.roses.kernel.security.request.encrypt.exception.enums.EncryptionExceptionEnum;
import cn.stylefeng.roses.kernel.security.request.encrypt.holder.TempSm4KeyHolder;
import cn.stylefeng.roses.kernel.security.request.encrypt.pojo.EncryptionDTO;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 经过解密过的请求体
 * <p>
 * 用来将带有加密的原始的HttpInputMessage进行解密，解密后Controller层，正常用自己的body参数接受使用即可
 * <p>
 * 从而无感知加密解密过程
 *
 * @author fengshuonan
 * @since 2024/6/28 14:20
 */
public class CustomDecryptHttpInputMessage implements HttpInputMessage {

    /**
     * 原始请求的headers
     */
    private final HttpHeaders originalHeaders;

    /**
     * 原始请求的带加密的body的json字符串
     */
    private final String originalBodyJsonString;

    public CustomDecryptHttpInputMessage(HttpHeaders originalHeaders, String originalBodyJsonString) {
        this.originalHeaders = originalHeaders;
        this.originalBodyJsonString = originalBodyJsonString;
    }

    /**
     * 获取解密后的请求体，返回一个ByteArrayInputStream即可
     *
     * @author fengshuonan
     * @since 2024/6/28 14:22
     */
    @Override
    public InputStream getBody() {

        // 1. 将原始的加密的JSON解析成对象
        EncryptionDTO encryptionDTO = JSON.parseObject(originalBodyJsonString, EncryptionDTO.class);
        if (encryptionDTO == null) {
            throw new ServiceException(EncryptionExceptionEnum.RSA_DECRYPT_ERROR);
        }
        if (StrUtil.isBlank(encryptionDTO.getPassedKey()) || StrUtil.isBlank(encryptionDTO.getPassedData())) {
            throw new ServiceException(EncryptionExceptionEnum.RSA_DECRYPT_ERROR);
        }

        // 2. 获取到passedKey的字符串，先进行SM2非对称解密，解密后的key是用来解密passedData的
        String passedKey = encryptionDTO.getPassedKey();
        String sm4Key = GuomiUtil.sm2DecryptWithPrivate(passedKey);

        // 临时缓存Sm4Key，响应时候还需要加密
        TempSm4KeyHolder.setSm4Key(sm4Key);

        // 3. 解密passedData
        String passedData = encryptionDTO.getPassedData();
        SM4 sm4 = SmUtil.sm4(sm4Key.getBytes());

        // 4. 获取到解密后的真实的json数据，这个json数据可以直接在Controller层的参数中接收
        String decryptAfterData = sm4.decryptStr(passedData, StandardCharsets.UTF_8);

        // 5. 将解密后的json数据转换成流返回
        return new ByteArrayInputStream(decryptAfterData.getBytes(CharsetUtil.CHARSET_UTF_8));
    }

    /**
     * 获取原始的Header参数请求参数，保持不变即可
     *
     * @author fengshuonan
     * @since 2024/6/28 14:22
     */
    @Override
    public HttpHeaders getHeaders() {
        return originalHeaders;
    }

}
