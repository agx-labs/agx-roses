package cn.stylefeng.roses.kernel.security.request.encrypt.holder;

/**
 * 存储加解密的临时秘钥，秘钥用来SM4加解密
 *
 * @author fengshuonan
 * @since 2024/6/28 15:13
 */
public class TempSm4KeyHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置SM4秘钥
     *
     * @author fengshuonan
     * @since 2024/6/28 15:13
     */
    public static void setSm4Key(String sm4Key) {
        CONTEXT_HOLDER.set(sm4Key);
    }

    /**
     * 获取SM4秘钥
     *
     * @author fengshuonan
     * @since 2024/6/28 15:13
     */
    public static String getSm4Key() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除SM4秘钥
     *
     * @author fengshuonan
     * @since 2020/8/24
     */
    public static void clearSm4Key() {
        CONTEXT_HOLDER.remove();
    }

}
