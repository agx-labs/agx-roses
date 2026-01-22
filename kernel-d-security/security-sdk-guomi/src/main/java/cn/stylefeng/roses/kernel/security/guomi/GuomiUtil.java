package cn.stylefeng.roses.kernel.security.guomi;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import cn.stylefeng.roses.kernel.security.guomi.expander.GuomiConfigExpander;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import java.nio.charset.StandardCharsets;

/**
 * 项目中使用的国密算法的封装
 * <p>
 * 非对称加密和签名：SM2
 * 摘要签名算法：SM3
 * 对称加密：SM4
 *
 * @author fengshuonan
 * @since 2024/6/25 10:07
 */
public class GuomiUtil {

    /**
     * 国密SM2，公钥加密
     *
     * @author fengshuonan
     * @since 2024/6/25 10:50
     */
    public static String sm2EncryptWithPublic(String text) {
        // 获取公钥字符串，应该是16进制，130位
        String publicKey = GuomiConfigExpander.getSM2PublicKey();

        // 如果是130位的话，去掉前面的04
        if (publicKey.length() == 130) {
            publicKey = publicKey.substring(2);
        }

        // 16进制的公钥转换为ECPublicKeyParameters
        String xhex = publicKey.substring(0, 64);
        String yhex = publicKey.substring(64, 128);
        ECPublicKeyParameters ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);

        //创建sm2 对象
        SM2 sm2 = new SM2(null, ecPublicKeyParameters);
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        String hex = sm2.encryptHex(text, KeyType.PublicKey).substring(2);
        return Base64.encode(HexUtil.decodeHex(hex));
    }

    /**
     * 国密SM2，私钥解密
     *
     * @author fengshuonan
     * @since 2024/6/25 10:53
     */
    public static String sm2DecryptWithPrivate(String encryptedStr) {
        String sm2PrivateKey = GuomiConfigExpander.getSM2PrivateKey();
        ECPrivateKeyParameters privateKeyParameters = BCUtil.toSm2Params(sm2PrivateKey);
        SM2 sm2 = new SM2(privateKeyParameters, null);
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        return sm2.decryptStr("04" + HexUtil.encodeHexStr(Base64.decode(encryptedStr)), KeyType.PrivateKey);
    }

    /**
     * 国密SM3加密，类似MD5
     *
     * @author fengshuonan
     * @since 2024/6/26 10:10
     */
    public static String sm3(String text) {
        return SmUtil.sm3(text);
    }

    /**
     * 国密SM4加密，对称加密
     *
     * @author fengshuonan
     * @since 2024/6/26 10:16
     */
    public static String sm4Encrypt(String text) {
        String sm4Key = GuomiConfigExpander.getSM4Key();
        SM4 sm4 = SmUtil.sm4(Base64.decode(sm4Key));
        return sm4.encryptBase64(text, StandardCharsets.UTF_8);
    }

    /**
     * 国密SM4解密，对称加密
     *
     * @author fengshuonan
     * @since 2024/6/26 10:16
     */
    public static String sm4Decrypt(String encryptedStr) {
        String sm4Key = GuomiConfigExpander.getSM4Key();
        SM4 sm4 = SmUtil.sm4(Base64.decode(sm4Key));
        return sm4.decryptStr(encryptedStr, StandardCharsets.UTF_8);
    }

}
