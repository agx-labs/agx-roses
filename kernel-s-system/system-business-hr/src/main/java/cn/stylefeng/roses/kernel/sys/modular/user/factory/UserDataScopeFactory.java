package cn.stylefeng.roses.kernel.sys.modular.user.factory;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.db.mp.datascope.UserRoleDataScopeApi;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.Set;

/**
 * 用户数据范围条件的拼装
 *
 * @author fengshuonan
 * @since 2024-03-01 16:29
 */
public class UserDataScopeFactory {

    /**
     * 创建用户的带数据范围的条件
     *
     * @author fengshuonan
     * @since 2024-03-01 16:30
     */
    public static void getUserDataScopeCondition(LambdaQueryWrapper<SysUser> queryWrapper) {

        UserRoleDataScopeApi userRoleDataScopeApi = SpringUtil.getBean(UserRoleDataScopeApi.class);
        SysUserOrgService sysUserOrgService = SpringUtil.getBean(SysUserOrgService.class);

        // 获取当前用户的数据范围
        DataScopeConfig userRoleDataScopeConfig = userRoleDataScopeApi.getUserRoleDataScopeConfig();

        // 如果是获取全部数据，则不限制
        if (userRoleDataScopeConfig.isTotalDataScope()) {
            return;
        }

        // 如果限制了查询当前人
        if (userRoleDataScopeConfig.isDoCreateUserValidate()) {
            queryWrapper.eq(SysUser::getUserId, userRoleDataScopeConfig.getUserId());
        }

        // 如果限制了查询指定部门
        if (userRoleDataScopeConfig.isDoOrgScopeValidate()) {
            Set<Long> specificOrgUserIdList = sysUserOrgService.getOrgUserIdList(userRoleDataScopeConfig.getUserOrgIdList());
            if (ObjectUtil.isEmpty(specificOrgUserIdList)) {
                specificOrgUserIdList = CollectionUtil.set(false, -1L);
            }
            queryWrapper.in(SysUser::getUserId, specificOrgUserIdList);
        }
    }

}
