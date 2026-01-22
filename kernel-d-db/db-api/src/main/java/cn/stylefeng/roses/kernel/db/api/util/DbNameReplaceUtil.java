package cn.stylefeng.roses.kernel.db.api.util;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.rule.util.DatabaseTypeUtil;

/**
 * 数据库名称替换工具，替换url中的数据库名称
 *
 * @author fengshuonan
 * @since 2025/1/16 16:21
 */
public class DbNameReplaceUtil {

    /**
     * 替换URL中的数据库名称
     *
     * @param databaseUrl  数据源URL
     * @param databaseName 需要替换成的数据库名称
     * @return 替换后的数据源URL
     * @author fengshuonan
     * @since 2025/5/4 17:44
     */
    public static String replaceDatabaseName(String databaseUrl, String databaseName) {

        if (StrUtil.isBlank(databaseName)) {
            return databaseUrl;
        }

        // 如果是mysql
        if (DbTypeEnum.MYSQL.equals(DatabaseTypeUtil.getDbType(databaseUrl))) {
            // 获取url中最后一个“/”的位置
            int firstPosition = databaseUrl.lastIndexOf("/");

            // 获取url中“?”的位置
            int lastPosition = databaseUrl.lastIndexOf("?");

            // 将传入的数据库名称替换url中的数据库名称
            StringBuilder sbu = new StringBuilder(databaseUrl);
            sbu.replace(firstPosition + 1, lastPosition, databaseName);
            return sbu.toString();
        }

        // 如果是postgreSQL
        else if (DbTypeEnum.PG_SQL.equals(DatabaseTypeUtil.getDbType(databaseUrl))) {
            // 获取url中最后一个“/”的位置
            int firstPosition = databaseUrl.lastIndexOf("/");
            return databaseUrl.substring(0, firstPosition + 1) + databaseName;
        }

        // 如果是SQL SERVER
        else if (DbTypeEnum.MS_SQL.equals(DatabaseTypeUtil.getDbType(databaseUrl))) {
            // 获取url中最后一个“=”的位置
            int firstPosition = databaseUrl.lastIndexOf("=");
            return databaseUrl.substring(0, firstPosition + 1) + databaseName;
        }

        // 如果是Oracle，不做处理
        else if (DbTypeEnum.ORACLE.equals(DatabaseTypeUtil.getDbType(databaseUrl))) {
            return databaseUrl;
        }

        return databaseUrl;
    }

}
