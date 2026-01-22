package cn.stylefeng.roses.kernel.db.mp.datascope;

import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;

/**
 * 获取用户角色的数据范围
 *
 * @author fengshuonan
 * @since 2024-03-01 13:39
 */
public interface UserRoleDataScopeApi {

    /**
     * 获取当前用户的数据范围
     *
     * @author fengshuonan
     * @since 2024-03-01 13:41
     */
    DataScopeConfig getUserRoleDataScopeConfig();

    /**
     * 获取当前用户的，规定指定类型的数据范围的配置
     *
     * @author fengshuonan
     * @since 2025/1/27 23:32
     */
    DataScopeConfig getUserPointDataScopeConfig(DataScopeTypeEnum dataScopeTypeEnum);

}
