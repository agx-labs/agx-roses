package cn.stylefeng.roses.kernel.file.api.util;

import cn.hutool.system.SystemUtil;
import cn.stylefeng.roses.kernel.file.api.expander.FileConfigExpander;

import java.io.File;

/**
 * 文件路径获取
 * <p>
 * 针对文件存储在本地的情况下的时候，获取本地的文件存储目录用
 *
 * @author fengshuonan
 * @since 2025/4/11 13:54
 */
public class LocalFilePathUtil {

    /**
     * 获取文件当前存储路径
     *
     * @author fengshuonan
     * @since 2025/4/11 13:54
     */
    public static String getCurrentSavePath() {
        if (SystemUtil.getOsInfo().isWindows()) {
            return FileConfigExpander.getLocalFileSavePathWindows();
        } else {
            return FileConfigExpander.getLocalFileSavePathLinux();
        }
    }

    /**
     * 获取指定bucket下，指定文件的路径
     *
     * @author fengshuonan
     * @since 2025/4/11 13:57
     */
    public static String getObjectPath(String bucket, String objectName) {
        return LocalFilePathUtil.getCurrentSavePath() + File.separator + bucket + File.separator + objectName;
    }

}
