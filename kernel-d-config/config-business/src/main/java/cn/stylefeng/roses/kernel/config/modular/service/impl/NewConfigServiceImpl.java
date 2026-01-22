package cn.stylefeng.roses.kernel.config.modular.service.impl;

import cn.hutool.system.SystemUtil;
import cn.stylefeng.roses.kernel.config.api.constants.ConfigConstants;
import cn.stylefeng.roses.kernel.config.api.enums.FileStorageTypeEnum;
import cn.stylefeng.roses.kernel.config.modular.pojo.newconfig.StorageConfig;
import cn.stylefeng.roses.kernel.config.modular.service.NewConfigService;
import cn.stylefeng.roses.kernel.config.modular.service.SysConfigService;
import cn.stylefeng.roses.kernel.rule.util.JarPathUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 新版的配置业务
 *
 * @author fengshuonan
 * @since 2024/8/31 17:05
 */
@Service
public class NewConfigServiceImpl implements NewConfigService {

    @Resource
    private SysConfigService sysConfigService;

    @Override
    public StorageConfig getStorageConfig() {

        StorageConfig storageConfig = new StorageConfig();

        // 文件存储类型
        String fileSaveType = sysConfigService.getConfigValueByCode(ConfigConstants.SYS_FILE_SAVE_TYPE_CONFIG_CODE);
        storageConfig.setFileSaveType(Integer.valueOf(fileSaveType));

        // 本地文件存储路径
        if (SystemUtil.getOsInfo().isWindows()) {
            String windowsPath = sysConfigService.getConfigValueByCode(ConfigConstants.SYS_LOCAL_FILE_SAVE_PATH_WINDOWS);
            storageConfig.setLocalFileSavePath(windowsPath);
        } else {
            String linuxPath = sysConfigService.getConfigValueByCode(ConfigConstants.SYS_LOCAL_FILE_SAVE_PATH_LINUX);
            storageConfig.setLocalFileSavePath(linuxPath);
        }

        return storageConfig;
    }

    @Override
    public void updateFileConfig(StorageConfig storageConfig) {
        String jarPath = null;

        // 1. 如果修改为程序默认路径的存储，获取当前jar路径
        if (FileStorageTypeEnum.LOCAL_DEFAULT.getCode().equals(storageConfig.getFileSaveType())) {
            jarPath = JarPathUtil.getJarPath();
        }

        // 2. 如果修改为指定路径
        else if (FileStorageTypeEnum.LOCAL.getCode().equals(storageConfig.getFileSaveType())) {
            jarPath = storageConfig.getLocalFileSavePath();
        }

        // 修改类型
        sysConfigService.updateConfigByCode(ConfigConstants.SYS_FILE_SAVE_TYPE_CONFIG_CODE, String.valueOf(storageConfig.getFileSaveType()));

        // 修改路径配置
        if (SystemUtil.getOsInfo().isWindows()) {
            sysConfigService.updateConfigByCode(ConfigConstants.SYS_LOCAL_FILE_SAVE_PATH_WINDOWS, jarPath);
        } else {
            sysConfigService.updateConfigByCode(ConfigConstants.SYS_LOCAL_FILE_SAVE_PATH_LINUX, jarPath);
        }
    }

}
