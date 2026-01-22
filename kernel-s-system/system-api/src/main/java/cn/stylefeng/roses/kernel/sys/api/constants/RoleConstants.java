package cn.stylefeng.roses.kernel.sys.api.constants;

/**
 * 角色的常量
 *
 * @author fengshuonan
 * @since 2023/7/14 22:37
 */
public interface RoleConstants {

    /**
     * 角色绑定菜单的缓存常量前缀
     */
    String ROLE_MENU_CACHE_PREFIX = "ROLE:MENU:";

    /**
     * 角色绑定菜单的事件
     */
    String ROLE_BIND_MENU_EVENT = "ROLE_BIND_MENU_EVENT";

    /**
     * 角色绑定菜单功能的缓存前缀
     */
    String ROLE_MENU_OPTIONS_CACHE_PREFIX = "ROLE:MENU_OPTIONS:";

    /**
     * 角色绑定菜单功能的事件
     */
    String ROLE_BIND_MENU_OPTIONS_EVENT = "ROLE_BIND_MENU_OPTIONS_EVENT";

    //-------------------------------角色名称的缓存-------------------------------

    /**
     * 角色名称的缓存
     */
    String ROLE_NAME_CACHE_PREFIX = "ROLE:NAME:";

}
