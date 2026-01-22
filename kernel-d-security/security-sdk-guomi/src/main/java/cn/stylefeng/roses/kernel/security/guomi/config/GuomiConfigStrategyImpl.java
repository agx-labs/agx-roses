package cn.stylefeng.roses.kernel.security.guomi.config;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import cn.stylefeng.roses.kernel.config.api.ConfigInitStrategyApi;
import cn.stylefeng.roses.kernel.config.api.pojo.ConfigInitItem;
import cn.stylefeng.roses.kernel.security.guomi.constants.GuomiConstants;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 国密算法相关秘钥初始化
 *
 * @author fengshuonan
 * @since 2024/6/25 10:15
 */
@Component
public class GuomiConfigStrategyImpl implements ConfigInitStrategyApi {

    @Override
    public String getTitle() {
        return "国密算法配置";
    }

    @Override
    public String getDescription() {
        return "系统自带国密算法工具的秘钥初始化";
    }

    @Override
    public List<ConfigInitItem> getInitConfigs() {

        ArrayList<ConfigInitItem> configInitItems = new ArrayList<>();

        // 生成一个公钥私钥对
        SM2 sm2 = SmUtil.sm2();
        String hutoolPrivateKeyHex = HexUtil.encodeHexStr(BCUtil.encodeECPrivateKey(sm2.getPrivateKey()));
        String hutoolPublicKeyHex = HexUtil.encodeHexStr(((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false));

        configInitItems.add(new ConfigInitItem("国密算法SM2-公钥", GuomiConstants.GUOMI_SM2_PUBLIC_KEY, hutoolPublicKeyHex, "国密SM2非对称加密，公钥生成"));
        configInitItems.add(new ConfigInitItem("国密算法SM2-私钥", GuomiConstants.GUOMI_SM2_PRIVATE_KEY, hutoolPrivateKeyHex, "国密SM2非对称加密，私钥生成"));

        // 生成SM4的对称加密的秘钥
        byte[] sm4Key = KeyUtil.generateKey(SM4.ALGORITHM_NAME, 128).getEncoded();
        configInitItems.add(new ConfigInitItem("国密算法SM4-对称秘钥", GuomiConstants.GUOMI_SM4_KEY, Base64.encode(sm4Key), "国密SM4对称加密，秘钥生成"));

        return configInitItems;
    }

}
