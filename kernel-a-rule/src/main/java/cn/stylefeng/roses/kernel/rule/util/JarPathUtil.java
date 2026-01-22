package cn.stylefeng.roses.kernel.rule.util;

import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * Jar包路径工具类
 *
 * @author fengshuonan
 * @since 2024/8/29 22:54
 */
@Slf4j
public class JarPathUtil {

    /**
     * 获取当前jar的路径
     *
     * @author fengshuonan
     * @since 2024/8/29 22:54
     */
    public static String getJarPath() {

        // 获取当前类的URL
        String jarPath = ClassUtil.getClassPath();

        // 如果以nested:/开头
        if (jarPath.contains("nested:")) {
            jarPath = jarPath.substring(7);
        }

        // 如果路径以"file:/"开头，去掉前缀
        if (jarPath.startsWith("file:")) {
            jarPath = jarPath.substring(5);
        }

        // 如果路径以"!"结尾，去掉后缀
        if (jarPath.contains("!")) {
            jarPath = jarPath.substring(0, jarPath.indexOf("!"));
        }

        // 获取文件对象
        File jarFile = new File(jarPath);

        // jar文件所在的目录
        File parentDirectory = jarFile.getParentFile();

        // 返回绝对路径
        return parentDirectory.getAbsolutePath();
    }

}
