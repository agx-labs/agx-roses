package cn.stylefeng.roses.kernel.config.modular.factory;

import cn.stylefeng.roses.kernel.config.api.SysConfigDataApi;
import cn.stylefeng.roses.kernel.config.modular.sqladapter.MssqlSysConfigData;
import cn.stylefeng.roses.kernel.config.modular.sqladapter.MysqlSysConfigData;
import cn.stylefeng.roses.kernel.config.modular.sqladapter.OracleSysConfigData;
import cn.stylefeng.roses.kernel.config.modular.sqladapter.PgsqlSysConfigData;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.rule.util.DatabaseTypeUtil;

/**
 * SysConfigDataApi的创建工厂
 *
 * @author fengshuonan
 * @since 2021/3/27 21:27
 */
public class SysConfigDataFactory {

    /**
     * 通过jdbc url获取api
     *
     * @author fengshuonan
     * @since 2021/3/27 21:27
     */
    public static SysConfigDataApi getSysConfigDataApi(String jdbcUrl) {
        DbTypeEnum dbType = DatabaseTypeUtil.getDbType(jdbcUrl);

        // mysql数据库
        if (DbTypeEnum.MYSQL.equals(dbType)) {
            return new MysqlSysConfigData();
        }

        // pgsql数据库
        else if (DbTypeEnum.PG_SQL.equals(dbType)) {
            return new PgsqlSysConfigData();
        }

        // sql server数据库
        else if (DbTypeEnum.MS_SQL.equals(dbType)) {
            return new MssqlSysConfigData();
        }

        // oracle数据库
        else if (DbTypeEnum.ORACLE.equals(dbType)) {
            return new OracleSysConfigData();
        }

        // 达梦数据库
        else if (DbTypeEnum.DM.equals(dbType)) {
            return new OracleSysConfigData();
        }

        // 海量数据库
        else if (DbTypeEnum.VAST_DATA.equals(dbType)) {
            return new PgsqlSysConfigData();
        }

        // 人大进仓数据库
        else if (DbTypeEnum.KING_BASE.equals(dbType)) {
            return new PgsqlSysConfigData();
        }

        // 华为OpenGauss
        else if (DbTypeEnum.OPEN_GAUSS.equals(dbType)) {
            return new PgsqlSysConfigData();
        }

        // 瀚高数据库
        else if (DbTypeEnum.HIGH_GO.equals(dbType)) {
            return new PgsqlSysConfigData();
        }

        return new MysqlSysConfigData();
    }

}
