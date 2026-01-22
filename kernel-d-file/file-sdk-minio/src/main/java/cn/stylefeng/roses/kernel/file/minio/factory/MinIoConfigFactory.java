package cn.stylefeng.roses.kernel.file.minio.factory;

import cn.stylefeng.roses.kernel.file.api.enums.BucketAuthEnum;

/**
 * MinIO配置工厂
 *
 * @author fengshuonan
 * @since 2025/4/30 21:56
 */
public class MinIoConfigFactory {

    /**
     * 生成桶的策略JSON
     *
     * @author fengshuonan
     * @since 2025/4/30 21:53
     */
    public static String createPolicyJson(BucketAuthEnum bucketAuthEnum, String bucketName) {
        switch (bucketAuthEnum) {
            case PRIVATE:
                return "{\n" +
                        "    \"Version\": \"2012-10-17\",\n" +
                        "    \"Statement\": [\n" +
                        "        {\n" +
                        "            \"Effect\": \"Deny\",\n" +
                        "            \"Principal\": \"*\",\n" +
                        "            \"Action\": \"s3:*\",\n" +
                        "            \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
            case PUBLIC_READ:
                return "{\n" +
                        "    \"Version\": \"2012-10-17\",\n" +
                        "    \"Statement\": [\n" +
                        "        {\n" +
                        "            \"Effect\": \"Allow\",\n" +
                        "            \"Principal\": \"*\",\n" +
                        "            \"Action\": \"s3:GetObject\",\n" +
                        "            \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
            case PUBLIC_READ_WRITE:
                return "{\n" +
                        "    \"Version\": \"2012-10-17\",\n" +
                        "    \"Statement\": [\n" +
                        "        {\n" +
                        "            \"Effect\": \"Allow\",\n" +
                        "            \"Principal\": \"*\",\n" +
                        "            \"Action\": [\n" +
                        "                \"s3:GetObject\",\n" +
                        "                \"s3:PutObject\"\n" +
                        "            ],\n" +
                        "            \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
            case MINIO_WRITE_ONLY:
                return "{\n" +
                        "    \"Version\": \"2012-10-17\",\n" +
                        "    \"Statement\": [\n" +
                        "        {\n" +
                        "            \"Effect\": \"Allow\",\n" +
                        "            \"Principal\": \"*\",\n" +
                        "            \"Action\": \"s3:PutObject\",\n" +
                        "            \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"Effect\": \"Deny\",\n" +
                        "            \"Principal\": \"*\",\n" +
                        "            \"Action\": \"s3:GetObject\",\n" +
                        "            \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";
            default:
                throw new IllegalArgumentException("Unsupported bucket auth enum: " + bucketAuthEnum);
        }
    }

    /**
     * 生成文件的策略JSON
     *
     * @author fengshuonan
     * @since 2025/4/30 22:15
     */
    public static String createKeyPolicyJson(String bucketName, String key, BucketAuthEnum bucketAuthEnum) {
        switch (bucketAuthEnum) {
            case PRIVATE:
                return String.format(
                        "{\n" +
                                "    \"Version\": \"2012-10-17\",\n" +
                                "    \"Statement\": [\n" +
                                "        {\n" +
                                "            \"Effect\": \"Deny\",\n" +
                                "            \"Principal\": \"*\",\n" +
                                "            \"Action\": \"s3:*\",\n" +
                                "            \"Resource\": \"arn:aws:s3:::%s/%s\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}", bucketName, key);
            case PUBLIC_READ:
                return String.format(
                        "{\n" +
                                "    \"Version\": \"2012-10-17\",\n" +
                                "    \"Statement\": [\n" +
                                "        {\n" +
                                "            \"Effect\": \"Allow\",\n" +
                                "            \"Principal\": \"*\",\n" +
                                "            \"Action\": \"s3:GetObject\",\n" +
                                "            \"Resource\": \"arn:aws:s3:::%s/%s\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}", bucketName, key);
            case PUBLIC_READ_WRITE:
                return String.format(
                        "{\n" +
                                "    \"Version\": \"2012-10-17\",\n" +
                                "    \"Statement\": [\n" +
                                "        {\n" +
                                "            \"Effect\": \"Allow\",\n" +
                                "            \"Principal\": \"*\",\n" +
                                "            \"Action\": [\n" +
                                "                \"s3:GetObject\",\n" +
                                "                \"s3:PutObject\"\n" +
                                "            ],\n" +
                                "            \"Resource\": \"arn:aws:s3:::%s/%s\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}", bucketName, key);
            case MINIO_WRITE_ONLY:
                return String.format(
                        "{\n" +
                                "    \"Version\": \"2012-10-17\",\n" +
                                "    \"Statement\": [\n" +
                                "        {\n" +
                                "            \"Effect\": \"Allow\",\n" +
                                "            \"Principal\": \"*\",\n" +
                                "            \"Action\": \"s3:PutObject\",\n" +
                                "            \"Resource\": \"arn:aws:s3:::%s/%s\"\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"Effect\": \"Deny\",\n" +
                                "            \"Principal\": \"*\",\n" +
                                "            \"Action\": \"s3:GetObject\",\n" +
                                "            \"Resource\": \"arn:aws:s3:::%s/%s\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}", bucketName, key);
            default:
                throw new IllegalArgumentException("Unsupported bucket auth enum: " + bucketAuthEnum);
        }
    }

}
