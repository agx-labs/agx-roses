package cn.stylefeng.roses.kernel.rule.code;


/**
 * 二维码的唯一编码生成的规则
 *
 * @author fengshuonan
 * @since 2025/4/20 18:49
 */
public class CodeGenerateUtil {

    /**
     * 生成不同规则的二维码编码字符串
     *
     * @author fengshuonan
     * @since 2025/4/20 18:50
     */
    public static String generateQrCode(CodeStrategy codeStrategy, Integer codeLength) {

        // 初始化生成器（需确保workerId和datacenterId集群唯一）
        UniqueTraceCodeGenerator generator = UniqueTraceCodeGenerator.getInstance();

        // 生成固定15位混合编码
        return generator.generateCode(codeStrategy, codeLength);
    }

    /**
     * 使用示例
     *
     * @author fengshuonan
     * @since 2025/4/20 18:49
     */
    public static void main(String[] args) {

        for (int i = 0; i < 10000; i++) {
            System.out.println("混合编码: " + CodeGenerateUtil.generateQrCode(CodeStrategy.MIXED, 15));
            System.out.println("大写编码: " + CodeGenerateUtil.generateQrCode(CodeStrategy.UPPERCASE, 15));
            System.out.println("小写编码: " + CodeGenerateUtil.generateQrCode(CodeStrategy.LOWERCASE, 15));
            System.out.println("数字编码: " + CodeGenerateUtil.generateQrCode(CodeStrategy.DIGITS, 15));
        }

    }

}
