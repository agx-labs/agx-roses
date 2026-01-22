/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.file.minio;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.file.api.FileOperatorApi;
import cn.stylefeng.roses.kernel.file.api.constants.FileConstants;
import cn.stylefeng.roses.kernel.file.api.enums.BucketAuthEnum;
import cn.stylefeng.roses.kernel.file.api.enums.FileLocationEnum;
import cn.stylefeng.roses.kernel.file.api.exception.FileException;
import cn.stylefeng.roses.kernel.file.api.exception.enums.FileExceptionEnum;
import cn.stylefeng.roses.kernel.file.api.expander.FileConfigExpander;
import cn.stylefeng.roses.kernel.file.api.pojo.props.MinIoProperties;
import cn.stylefeng.roses.kernel.file.minio.factory.MinIoConfigFactory;
import cn.stylefeng.roses.kernel.rule.util.HttpServletUtil;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.Map;

/**
 * MinIo文件操作客户端
 *
 * @author fengshuonan
 * @since 2020/10/31 10:35
 */
@Slf4j
public class MinIoFileOperator implements FileOperatorApi {

    private final Object LOCK = new Object();

    /**
     * 文件ContentType对应关系
     */
    Map<String, String> contentType = new HashMap<>();

    /**
     * MinIo文件操作客户端
     */
    private MinioClient minioClient;

    /**
     * MinIo的配置
     */
    private final MinIoProperties minIoProperties;

    public MinIoFileOperator(MinIoProperties minIoProperties) {
        this.minIoProperties = minIoProperties;
        this.initClient();
    }

    @Override
    public void initClient() {
        String endpoint = minIoProperties.getEndpoint();
        String accessKey = minIoProperties.getAccessKey();
        String secretKey = minIoProperties.getSecretKey();

        // 创建minioClient实例
        minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }

    @Override
    public void destroyClient() {
        // empty
    }

    @Override
    public Object getClient() {
        return minioClient;
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("MinIo校验桶是否存在时异常：", e);
            return false;
        }
    }

    @Override
    public void setBucketAcl(String bucketName, BucketAuthEnum bucketAuthEnum) {
        try {
            // 创建规则的json
            String policyJson = MinIoConfigFactory.createPolicyJson(bucketAuthEnum, bucketName);

            // 使用 SetBucketPolicyArgs 设置策略
            SetBucketPolicyArgs args = SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config(policyJson)
                    .build();
            minioClient.setBucketPolicy(args);
        } catch (Exception e) {
            log.error("Error occurred while setting bucket ACL: ", e);
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean isExistingFile(String bucketName, String key) {
        try {
            // 使用 StatObjectArgs 构建查询参数
            StatObjectResponse response = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .build()
            );
            // 如果文件存在，statObject 不会抛出异常，返回 true
            return true;
        } catch (NoSuchObjectException e) {
            // 如果文件不存在，会抛出 NoSuchObjectException，返回 false
            return false;
        } catch (Exception e) {
            log.error("MINIO无法找到文件！ ", e);
            return false;
        }
    }

    @Override
    public void storageFile(String bucketName, String key, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return;
        }

        try {
            // 检查存储桶是否存在，不存在直接返回
            boolean isBucketExist = this.doesBucketExist(bucketName);
            if (!isBucketExist) {
                log.error("无法存储文件到桶，桶不存在！桶名称：{}", bucketName);
                return;
            }

            // 将字节数组转换为输入流
            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(bytes);

            String type = FileTypeUtil.getType(byteArrayInputStream);
            String fileContentType = getFileContentType(String.format("%s%s", ".", type));

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            // -1 表示不限制流的大小
                            .stream(byteArrayInputStream, bytes.length, -1)
                            .contentType(fileContentType)
                            .build()
            );
        } catch (Exception e) {
            log.error("MINIO文件操作异常：", e);
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void storageFile(String bucketName, String key, InputStream inputStream) {
        if (inputStream != null) {
            byte[] bytes = IoUtil.readBytes(inputStream);
            storageFile(bucketName, key, bytes);
        }
    }

    @Override
    public byte[] getFileBytes(String bucketName, String key) {
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(key).build());
            return IoUtil.readBytes(inputStream);
        } catch (Exception e) {
            log.error("MINIO文件操作异常：", e);
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void setFileAcl(String bucketName, String key, BucketAuthEnum bucketAuthEnum) {
        try {
            // 根据枚举值设置对应的策略
            String policyJson = MinIoConfigFactory.createKeyPolicyJson(bucketName, key, bucketAuthEnum);
            SetBucketPolicyArgs args = SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config(policyJson)
                    .build();
            minioClient.setBucketPolicy(args);
        } catch (Exception e) {
            log.error("MINIO文件操作异常：", e);
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public void copyFile(String originBucketName, String originFileKey, String newBucketName, String newFileKey) {
        try {
            // 构建源文件的 CopySource 对象
            CopySource copySource = CopySource.builder()
                    .bucket(originBucketName)
                    .object(originFileKey)
                    .build();

            // 构建拷贝文件的目标参数
            CopyObjectArgs copyObjectArgs = CopyObjectArgs.builder()
                    .source(copySource)
                    .bucket(newBucketName)
                    .object(newFileKey)
                    .build();

            // 执行拷贝操作
            minioClient.copyObject(copyObjectArgs);
        } catch (Exception e) {
            log.error("MINIO文件操作异常：", e);
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public String getFileAuthUrl(String bucketName, String key, Long timeoutMillis) {

        // 获取登录用户的token
        String token = LoginContext.me().getToken();

        // 获取context-path
        String contextPath = HttpServletUtil.getRequest().getContextPath();

        return FileConfigExpander.getServerDeployHost()
                + contextPath
                + FileConstants.FILE_PREVIEW_BY_OBJECT_NAME
                + "?fileBucket=" + bucketName
                + "&fileObjectName=" + key
                + "&token=" + token;
    }

    @Override
    public String getFileUnAuthUrl(String bucketName, String key) {
        // 获取context-path
        String contextPath = HttpServletUtil.getRequest().getContextPath();

        return FileConfigExpander.getServerDeployHost()
                + contextPath
                + FileConstants.FILE_PREVIEW_BY_OBJECT_NAME
                + "?fileBucket=" + bucketName
                + "&fileObjectName=" + key;
    }

    @Override
    public void deleteFile(String bucketName, String key) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(key).build());
        } catch (Exception e) {
            log.error("MINIO文件操作异常：", e);
            throw new FileException(FileExceptionEnum.MINIO_FILE_ERROR, e.getMessage());
        }
    }

    @Override
    public FileLocationEnum getFileLocationEnum() {
        return FileLocationEnum.MINIO;
    }

    /**
     * 获取文件后缀对应的contentType
     *
     * @author fengshuonan
     * @since 2020/11/2 18:08
     */
    private Map<String, String> getFileContentType() {
        synchronized (LOCK) {
            if (contentType.isEmpty()) {
                contentType.put(".bmp", "application/x-bmp");
                contentType.put(".gif", "image/gif");
                contentType.put(".fax", "image/fax");
                contentType.put(".ico", "image/x-icon");
                contentType.put(".jfif", "image/jpeg");
                contentType.put(".jpe", "image/jpeg");
                contentType.put(".jpeg", "image/jpeg");
                contentType.put(".jpg", "image/jpeg");
                contentType.put(".png", "image/png");
                contentType.put(".rp", "image/vnd.rn-realpix");
                contentType.put(".tif", "image/tiff");
                contentType.put(".tiff", "image/tiff");
                contentType.put(".doc", "application/msword");
                contentType.put(".ppt", "application/x-ppt");
                contentType.put(".pdf", "application/pdf");
                contentType.put(".xls", "application/vnd.ms-excel");
                contentType.put(".txt", "text/plain");
                contentType.put(".java", "java/*");
                contentType.put(".html", "text/html");
                contentType.put(".avi", "video/avi");
                contentType.put(".movie", "video/x-sgi-movie");
                contentType.put(".mp4", "video/mpeg4");
                contentType.put(".mp3", "audio/mp3");
            }
        }
        return contentType;
    }

    /**
     * 获取文件后缀对应的contentType
     *
     * @author fengshuonan
     * @since 2020/11/2 18:05
     */
    private String getFileContentType(String fileSuffix) {
        String contentType = getFileContentType().get(fileSuffix);
        if (ObjectUtil.isEmpty(contentType)) {
            return "application/octet-stream";
        } else {
            return contentType;
        }
    }

}
