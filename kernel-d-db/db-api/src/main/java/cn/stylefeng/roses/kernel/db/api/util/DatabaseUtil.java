package cn.stylefeng.roses.kernel.db.api.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.api.exception.DaoException;
import cn.stylefeng.roses.kernel.db.api.exception.enums.DatabaseExceptionEnum;
import cn.stylefeng.roses.kernel.db.api.pojo.db.TableFieldInfo;
import cn.stylefeng.roses.kernel.db.api.pojo.db.TableInfo;
import cn.stylefeng.roses.kernel.db.api.pojo.druid.DruidProperties;
import cn.stylefeng.roses.kernel.db.api.sqladapter.database.CreateDatabaseSql;
import cn.stylefeng.roses.kernel.db.api.sqladapter.database.GetDatabasesSql;
import cn.stylefeng.roses.kernel.db.api.sqladapter.table.TableFieldListSql;
import cn.stylefeng.roses.kernel.db.api.sqladapter.table.TableListSql;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库操作工具类，可用来获取一些元数据
 *
 * @author fengshuonan
 * @since 2021/5/19 10:35
 */
@Slf4j
public class DatabaseUtil {

    /**
     * 获取数据库中的所有数据库列表
     *
     * @author fengshuonan
     * @since 2021/5/26 20:42
     */
    public static List<String> getDatabases(DruidProperties druidProperties) {
        Connection conn = null;
        List<String> databasesList = new ArrayList<>();
        try {
            Class.forName(druidProperties.getDriverClassName());
            conn = DriverManager.getConnection(
                    druidProperties.getUrl(), druidProperties.getUsername(), druidProperties.getPassword());
            PreparedStatement preparedStatement = conn.prepareStatement(new GetDatabasesSql().getSql(druidProperties.getUrl()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String database = resultSet.getString("database");
                if (StrUtil.isNotBlank(database)) {
                    databasesList.add(database);
                }
            }
            return databasesList;
        } catch (Exception e) {
            log.error("查询所有库错误！", e);
            throw new DaoException(DatabaseExceptionEnum.DATABASE_LIST_ERROR, e.getMessage());
        } finally {
            IoUtil.close(conn);
        }
    }

    /**
     * 查询某个数据库连接的所有表
     *
     * @author fengshuonan
     * @since 2021/5/19 10:35
     */
    public static List<TableInfo> selectTables(DruidProperties druidProperties) {
        List<TableInfo> tables = new ArrayList<>();
        Connection conn = null;
        try {
            Class.forName(druidProperties.getDriverClassName());
            conn = DriverManager.getConnection(
                    druidProperties.getUrl(), druidProperties.getUsername(), druidProperties.getPassword());

            // 获取数据库名称
            String dbName = getDbName(druidProperties);

            // 构造查询语句
            PreparedStatement preparedStatement = conn.prepareStatement(new TableListSql().getSql(druidProperties.getUrl()));

            // 拼接设置数据库名称
            if (!druidProperties.getUrl().contains("sqlserver") && !druidProperties.getUrl().contains("postgresql")) {
                preparedStatement.setString(1, dbName);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TableInfo tableInfo = new TableInfo();
                String tableName = resultSet.getString("tableName");
                String tableComment = resultSet.getString("tableComment");
                tableInfo.setTableName(tableName);
                tableInfo.setTableComment(tableComment);
                tables.add(tableInfo);
            }
            return tables;
        } catch (Exception ex) {
            log.error("查询所有表错误！", ex);
            throw new DaoException(DatabaseExceptionEnum.TABLE_LIST_ERROR, ex.getMessage());
        } finally {
            IoUtil.close(conn);
        }
    }

    /**
     * 查询某个表的所有字段
     *
     * @author fengshuonan
     * @since 2021/5/19 11:01
     */
    public static List<TableFieldInfo> getTableFields(DruidProperties druidProperties, String tableName) {
        ArrayList<TableFieldInfo> fieldList = new ArrayList<>();
        Connection conn = null;
        try {
            Class.forName(druidProperties.getDriverClassName());
            conn = DriverManager.getConnection(
                    druidProperties.getUrl(), druidProperties.getUsername(), druidProperties.getPassword());

            PreparedStatement preparedStatement = conn.prepareStatement(new TableFieldListSql().getSql(druidProperties.getUrl()));

            if (druidProperties.getUrl().contains("oracle")) {
                preparedStatement.setString(1, tableName);
            } else if (druidProperties.getUrl().contains("postgresql")) {
                preparedStatement.setString(1, tableName);
            } else if (druidProperties.getUrl().contains("sqlserver")) {
                preparedStatement.setString(1, tableName);
            } else {
                String dbName = getDbName(druidProperties);
                preparedStatement.setString(1, tableName);
                preparedStatement.setString(2, dbName);
            }

            //执行查询
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                TableFieldInfo tableFieldInfo = new TableFieldInfo();
                String columnName = resultSet.getString("columnName");
                String columnComment = resultSet.getString("columnComment");
                tableFieldInfo.setColumnName(columnName);
                tableFieldInfo.setColumnComment(columnComment);
                tableFieldInfo.setCamelFieldName(StrUtil.toCamelCase(columnName));
                fieldList.add(tableFieldInfo);
            }
            return fieldList;
        } catch (Exception ex) {
            log.error("查询表的所有字段错误！", ex);
            throw new DaoException(DatabaseExceptionEnum.FIELD_GET_ERROR, ex.getMessage());
        } finally {
            IoUtil.close(conn);
        }
    }

    /**
     * 创建数据库
     *
     * @author fengshuonan
     * @since 2021/5/19 10:39
     */
    public static void createDatabase(DruidProperties druidProperties, String databaseName) {
        Connection conn = null;
        try {
            Class.forName(druidProperties.getDriverClassName());
            conn = DriverManager.getConnection(druidProperties.getUrl(), druidProperties.getUsername(), druidProperties.getPassword());

            //创建sql
            String sql = new CreateDatabaseSql().getSql(druidProperties.getUrl());
            sql = sql.replaceAll("\\?", databaseName);

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            int i = preparedStatement.executeUpdate();
            log.info("创建数据库！数量：" + i);

        } catch (Exception ex) {
            log.error("执行sql出现问题！", ex);
            throw new DaoException(DatabaseExceptionEnum.CREATE_DATABASE_ERROR, ex.getMessage());
        } finally {
            IoUtil.close(conn);
        }
    }

    /**
     * 根据数据库配置，获取数据库名称
     * <p>
     * oracle的数据库会直接返回username作为数据库名称，这里需要注意一下，如果用户名不是数据库名则返回不准确
     *
     * @author fengshuonan
     * @since 2025/5/7 21:45
     */
    public static String getDbName(DruidProperties druidProperties) {
        String url = druidProperties.getUrl().toLowerCase();
        return getDbName(url);
    }

    /**
     * 根据数据库URL获取数据库配置
     *
     * @author fengshuonan
     * @since 2025/5/7 21:45
     */
    public static String getDbName(String url) {
        try {
            // 如果是oracle
            if (url.contains(DbTypeEnum.ORACLE.getUrlWords())) {
                Pattern pattern = Pattern.compile(":(thin|oci|kprb):@(//)?[^:/]+(:\\d+)?[:/]([^?]+)");
                Matcher matcher = pattern.matcher(url);
                return matcher.find() ? matcher.group(4) : parseAfterLastSlash(url);
            }

            // 如果是PostgreSQL
            else if (url.contains(DbTypeEnum.PG_SQL.getUrlWords())) {
                return parseAfterLastSlash(url);
            }

            // 如果是sql server
            else if (url.contains(DbTypeEnum.MS_SQL.getUrlWords())) {
                // SQL Server: databaseName= 或 Initial Catalog=
                return parseParameter(url, "databasename=", "initial catalog=");
            }

            // 如果是Mysql
            else if (url.contains(DbTypeEnum.MYSQL.getUrlWords())) {
                int first = url.lastIndexOf("/") + 1;
                int last = url.indexOf("?");
                return url.substring(first, last == -1 ? url.length() : last);
            }

            // 如果是达梦数据库
            else if (url.contains(DbTypeEnum.DM.getUrlWords())) {
                return parseParameter(url, "dbname=", "database=");
            }

            // 人大金仓、openGauss、瀚高数据库
            else if (url.contains(DbTypeEnum.KING_BASE.getUrlWords()) || url.contains(DbTypeEnum.OPEN_GAUSS.getUrlWords()) || url.contains(DbTypeEnum.HIGH_GO.getUrlWords())) {
                return parseAfterLastSlash(url);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse database name from URL: " + url, e);
        }

        // 尝试通用解析作为最后手段
        try {
            return parseAfterLastSlash(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unsupported or malformed database URL: " + url);
        }
    }

    /**
     * 解析URL中最后一个/之后的部分
     *
     * @author fengshuonan
     * @since 2025/5/7 22:04
     */
    private static String parseAfterLastSlash(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            return url; // 如果没有 /，返回整个 URL（或抛异常）
        }
        int paramStart = url.indexOf('?', lastSlashIndex);
        if (paramStart == -1) {
            return url.substring(lastSlashIndex + 1);
        }
        return url.substring(lastSlashIndex + 1, paramStart);
    }

    /**
     * 解析URL参数
     *
     * @author fengshuonan
     * @since 2025/5/7 22:04
     */
    private static String parseParameter(String url, String... paramNames) {
        for (String param : paramNames) {
            // 匹配 databaseName=xxx 或 ;databaseName=xxx 或 &databaseName=xxx
            Pattern pattern = Pattern.compile("(?:[;&?]|^)" + param + "([^;&?]+)");
            Matcher matcher = pattern.matcher(url.toLowerCase());
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return parseAfterLastSlash(url);
    }

    public static void main(String[] args) {
        System.out.println(getDbName("jdbc:oracle:thin:@//127.0.0.1:1521/right_oracle"));
        System.out.println(getDbName("jdbc:postgresql://192.168.1.1:5432/right_pg"));
        System.out.println(getDbName("jdbc:jtds:sqlserver://172.1.1.1:1433;DatabaseName=rightsqlserver"));
        System.out.println(getDbName("jdbc:mysql://localhost:3306/right_mysql?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true"));
        System.out.println(getDbName("jdbc:dm://172.1.1.1:5236?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=true&characterEncoding=UTF-8"));
        System.out.println(getDbName("jdbc:kingbase8://192.168.1.1:5432/right_king"));
        System.out.println(getDbName("jdbc:opengauss://192.168.1.1:5432/right_open_gauss"));
        System.out.println(getDbName("jdbc:highgo://localhost:5866/right_highgo"));
        System.out.println(getDbName("jdbc:postgresql://172.1.1.1:5432/right_hailiang"));
    }

}
