package cn.stylefeng.roses.kernel.sys.modular.role.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * 判断当前用户的所有角色，是否包含了被操作的角色，并且被操作角色的菜单功能id包含了禁用修改权限的菜单功能id
 *
 * @author fengshuonan
 * @since 2023/9/8 23:31
 */
public class AssertAssignUtil {

    /**
     * 修改权限的操作id
     */
    public static final Long DISABLED_MENU_OPTIONS = 1677229379281846273L;

    /**
     * 执行判断过程，并增加筛选条件
     *
     * @param roleId  被操作的角色id
     * @param wrapper 拼接条件
     * @author fengshuonan
     * @since 2023/9/8 23:34
     */
    public static void assertAssign(Long roleId, LambdaQueryWrapper<SysRoleMenuOptions> wrapper) {

        // 获取当前登录用户的所有角色列表
        SysUserRoleServiceApi sysUserRoleServiceApi = SpringUtil.getBean(SysUserRoleServiceApi.class);
        List<Long> userRoleIdList = sysUserRoleServiceApi.getUserRoleIdList(LoginContext.me().getLoginUser().getUserId());

        if (ObjectUtil.isEmpty(userRoleIdList)) {
            return;
        }

        if (userRoleIdList.contains(roleId)) {
            wrapper.notIn(SysRoleMenuOptions::getMenuOptionId, DISABLED_MENU_OPTIONS);
        }
    }

}
