package cn.stylefeng.roses.kernel.sys.modular.user.constants;

/**
 * 用户相关的常量
 *
 * @author fengshuonan
 * @since 2023/7/14 22:06
 */
public interface UserConstants {

    /**
     * 缓存前缀：用户绑定的角色
     */
    String USER_ROLE_CACHE_PREFIX = "SYS:USER_ROLE:";

    /**
     * 修改用户绑定角色的事件
     */
    String UPDATE_USER_ROLE_EVENT = "UPDATE_USER_ROLE_EVENT";

    /**
     * 用户信息详情的缓存
     */
    String USER_INFO_CACHE_PREFIX = "SYS:USER_INFO:";

    /**
     * 更新用户姓名的事件（包含新增、修改、删除用户）
     */
    String UPDATE_USER_INFO_EVENT = "UPDATE_USER_INFO_EVENT";

}
