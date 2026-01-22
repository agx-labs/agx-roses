package cn.stylefeng.roses.kernel.security.request.encrypt.pojo;

import lombok.Data;

/**
 * 加密数据包装的DTO
 *
 * @author fengshuonan
 * @since 2024/6/28 14:36
 */
@Data
public class EncryptionDTO {

    /**
     * 数据传输的秘钥，此秘钥已经进行了SM2非对称加密，需要SM2解密才能看到真实的秘钥
     */
    private String passedKey;

    /**
     * 数据传输的真实数据，这个数据经过了SM4对称加密，秘钥是passedKey（SM2解密后）
     */
    private String passedData;

}
