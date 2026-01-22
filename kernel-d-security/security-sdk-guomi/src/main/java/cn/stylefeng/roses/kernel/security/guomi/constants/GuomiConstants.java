package cn.stylefeng.roses.kernel.security.guomi.constants;

/**
 * 国密算法的常量
 *
 * @author fengshuonan
 * @since 2024/6/25 10:11
 */
public interface GuomiConstants {

    /**
     * 国密，SM2需要的私钥
     */
    String GUOMI_SM2_PRIVATE_KEY = "GUOMI_SM2_PRIVATE_KEY";

    /**
     * 国密，SM2需要的公钥
     */
    String GUOMI_SM2_PUBLIC_KEY = "GUOMI_SM2_PUBLIC_KEY";

    /**
     * 国密，SM4对称加密需要的秘钥
     */
    String GUOMI_SM4_KEY = "GUOMI_SM4_KEY";

}
