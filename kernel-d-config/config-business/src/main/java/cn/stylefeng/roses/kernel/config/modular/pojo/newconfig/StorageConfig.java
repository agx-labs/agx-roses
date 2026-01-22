package cn.stylefeng.roses.kernel.config.modular.pojo.newconfig;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文件存储的配置
 *
 * @author fengshuonan
 * @since 2024/8/31 17:02
 */
@Data
public class StorageConfig {

    /**
     * 文件存储类型的配置
     * <p>
     * 10-本地，存储到默认路径（jar所在目录）
     * 11-本地，存储到指定路径下（需要配置linux和windows的路径）
     * 20-存储到MinIO
     * 30-存储到阿里云
     * 40-存储到腾讯云
     * 50-存储到青云
     */
    @ChineseDescription("文件存储类型的配置：10-本地，存储到默认路径（jar所在目录），11-本地，存储到指定路径下（需要配置linux和windows的路径），20-存储到MinIO，30-存储到阿里云，40-存储到腾讯云，50-存储到青云")
    @NotNull(message = "文件存储类型的配置不能为空")
    private Integer fileSaveType;

    /**
     * 本地存储路径
     */
    @ChineseDescription("本地存储路径")
    private String localFileSavePath;

}
