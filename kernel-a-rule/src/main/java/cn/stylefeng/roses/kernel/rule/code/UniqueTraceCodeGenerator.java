package cn.stylefeng.roses.kernel.rule.code;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 绝对唯一的溯源编码生成器
 * 基于 UUID 和哈希算法实现，支持四种编码策略
 *
 * @author fengshuonan
 * @since 2025/4/20 18:42
 */
public class UniqueTraceCodeGenerator {

    /**
     * 单例控制
     */
    private static volatile UniqueTraceCodeGenerator instance;

    private UniqueTraceCodeGenerator() {
    }

    /**
     * 单例实例的创建
     *
     * @author fengshuonan
     * @since 2025/4/20 18:41
     */
    public static UniqueTraceCodeGenerator getInstance() {
        if (instance == null) {
            synchronized (UniqueTraceCodeGenerator.class) {
                if (instance == null) {
                    instance = new UniqueTraceCodeGenerator();
                }
            }
        }
        return instance;
    }

    /**
     * 生成唯一编码
     *
     * @param strategy 编码策略
     * @param length   固定长度（不足补首字符）
     * @return 生成的编码
     * @author fengshuonan
     * @since 2025/4/20 18:42
     */
    public String generateCode(CodeStrategy strategy, int length) {
        // 生成 UUID
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 对 UUID 进行哈希处理
        String hashedUuid = hash(uuid);

        // 转换为指定编码策略
        return convert(hashedUuid, strategy, length);
    }

    /**
     * 哈希算法（SHA-256）
     *
     * @param input 输入字符串
     * @return 哈希后的字符串
     * @author fengshuonan
     * @since 2025/4/20 18:42
     */
    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("哈希算法异常", e);
        }
    }

    /**
     * 进制转换核心算法
     *
     * @author fengshuonan
     * @since 2025/4/20 18:42
     */
    private String convert(String input, CodeStrategy strategy, int fixLength) {
        StringBuilder sb = new StringBuilder();
        int base = strategy.base;
        String charset = strategy.charset;

        // 将输入的哈希值（16进制字符串）转换为指定编码策略
        for (int i = 0; i < input.length(); i += 2) {
            // 每次取两个字符（一个字节）
            String hex = input.substring(i, i + 2);
            int byteValue = Integer.parseInt(hex, 16);

            // 将字节值转换为指定编码字符
            sb.append(charset.charAt(byteValue % base));
        }

        // 如果长度不足，补足到固定长度
        while (sb.length() < fixLength) {
            sb.append(charset.charAt(0));
        }

        // 如果长度超过固定长度，截取前 fixLength 个字符
        if (sb.length() > fixLength) {
            sb.setLength(fixLength);
        }

        return sb.toString();
    }

}