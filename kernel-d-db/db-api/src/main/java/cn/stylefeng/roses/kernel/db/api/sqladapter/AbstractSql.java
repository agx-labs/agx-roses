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
package cn.stylefeng.roses.kernel.db.api.sqladapter;


import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;

/**
 * 异构sql获取基类，通过继承此类，编写使用不同数据库的sql
 *
 * @author fengshuonan
 * @since 2020/10/31 23:44
 */
public abstract class AbstractSql {

    /**
     * 获取异构sql
     *
     * @param jdbcUrl 数据连接的url
     * @return 具体的sql
     * @author fengshuonan
     * @since 2020/10/31 23:44
     */
    public String getSql(String jdbcUrl) {
        if (jdbcUrl.contains(DbTypeEnum.MYSQL.getUrlWords())) {
            return mysql();
        }
        if (jdbcUrl.contains(DbTypeEnum.PG_SQL.getUrlWords())) {
            return pgSql();
        }
        if (jdbcUrl.contains(DbTypeEnum.ORACLE.getUrlWords())) {
            return oracle();
        }
        if (jdbcUrl.contains(DbTypeEnum.DM.getUrlWords())) {
            return dm();
        }
        if (jdbcUrl.contains(DbTypeEnum.MS_SQL.getUrlWords())) {
            return sqlServer();
        }
        if (jdbcUrl.contains(DbTypeEnum.VAST_DATA.getUrlWords())) {
            return vastbase();
        }
        if (jdbcUrl.contains(DbTypeEnum.KING_BASE.getUrlWords())) {
            return kingbase();
        }
        if (jdbcUrl.contains(DbTypeEnum.OPEN_GAUSS.getUrlWords())) {
            return openGauss();
        }
        if (jdbcUrl.contains(DbTypeEnum.HIGH_GO.getUrlWords())) {
            return highGo();
        }
        return mysql();
    }

    /**
     * 获取mysql的sql语句
     *
     * @return 具体的sql
     * @author fengshuonan
     * @since 2020/10/31 23:45
     */
    protected abstract String mysql();

    /**
     * 获取sqlServer的sql语句
     *
     * @return 具体的sql
     * @author fengshuonan
     * @since 2020/10/31 23:45
     */
    protected abstract String sqlServer();

    /**
     * 获取pgSql的sql语句
     *
     * @return 具体的sql
     * @author fengshuonan
     * @since 2020/10/31 23:45
     */
    protected abstract String pgSql();

    /**
     * 获取oracle的sql语句
     *
     * @return 具体的sql
     * @author fengshuonan
     * @since 2020/10/31 23:45
     */
    protected abstract String oracle();

    /**
     * 达梦的sql
     *
     * @return 具体的sql
     * @author fengshuonan
     * @since 2023/9/22 14:12
     */
    protected abstract String dm();

    /**
     * 人大金仓的sql
     *
     * @return 具体的sql
     * @author fengshuonan
     * @since 2023/9/22 14:12
     */
    protected abstract String kingbase();

    /**
     * 海量数据库的sql
     *
     * @return 具体的sql
     * @author fengshuonan
     * @since 2023/9/22 14:12
     */
    protected abstract String vastbase();

    /**
     * 华为的OpenGauss sql
     *
     * @return 具体的sql
     * @author fengshuonan
     * @since 2023/10/20 13:46
     */
    protected abstract String openGauss();

    /**
     * 瀚高数据库的sql
     *
     * @author fengshuonan
     * @since 2025/4/30 17:18
     */
    protected String highGo() {
        return pgSql();
    }

}
