package cn.stylefeng.roses.kernel.sys.api.constants;

/**
 * 权限编码（菜单功能的编码），一般用在给@ApiResource注解加一些权限控制标识
 *
 * @author fengshuonan
 * @since 2023/7/7 14:20
 */
public interface PermissionCodeConstants {

    /**
     * 首页-公司信息统计信息
     */
    String COMPANY_STAT_INFO = "COMPANY_STAT_INFO";

    /**
     * 人员管理界面-新增人员
     */
    String ADD_USER = "ADD_USER";

    /**
     * 人员管理界面-修改人员
     */
    String EDIT_USER = "EDIT_USER";

    /**
     * 人员管理界面-删除人员
     */
    String DELETE_USER = "DELETE_USER";

    /**
     * 人员管理界面-分配角色
     */
    String ASSIGN_USER_ROLE = "ASSIGN_USER_ROLE";

    /**
     * 人员管理界面-重置密码
     */
    String RESET_PASSWORD = "RESET_PASSWORD";

    /**
     * 人员管理界面-修改状态
     */
    String UPDATE_USER_STATUS = "UPDATE_USER_STATUS";

    /**
     * 职位管理界面-新增职位
     */
    String ADD_POSITION = "ADD_POSITION";

    /**
     * 职位管理界面-修改职位
     */
    String EDIT_POSITION = "EDIT_POSITION";

    /**
     * 职位管理界面-删除职位
     */
    String DELETE_POSITION = "DELETE_POSITION";

    /**
     * 组织机构界面-新增机构
     */
    String ADD_ORG = "ADD_ORG";

    /**
     * 组织机构界面-修改机构
     */
    String EDIT_ORG = "EDIT_ORG";

    /**
     * 组织机构界面-删除机构
     */
    String DELETE_ORG = "DELETE_ORG";

    /**
     * 组织机构界面-设置审批人
     */
    String ASSIGN_APPROVER = "ASSIGN_APPROVER";

    /**
     * 应用管理界面-新增应用
     */
    String ADD_APP = "ADD_APP";

    /**
     * 应用管理界面-修改应用
     */
    String EDIT_APP = "EDIT_APP";

    /**
     * 应用管理界面-删除应用
     */
    String DELETE_APP = "DELETE_APP";

    /**
     * 应用管理界面-更新应用状态
     */
    String UPDATE_APP_STATUS = "UPDATE_APP_STATUS";

    /**
     * 角色管理界面-新增角色
     */
    String ADD_ROLE = "ADD_ROLE";

    /**
     * 角色管理界面-删除角色
     */
    String DELETE_ROLE = "DELETE_ROLE";

    /**
     * 角色管理界面-修改角色
     */
    String EDIT_ROLE = "EDIT_ROLE";

    /**
     * 权限管理界面-修改权限
     */
    String CHANGE_ROLE_PERMISSION = "CHANGE_ROLE_PERMISSION";

    /**
     * 权限管理界面-修改数据范围
     */
    String CHANGE_ROLE_DATA_SCOPE = "CHANGE_ROLE_DATA_SCOPE";

    /**
     * 菜单管理整个界面的权限标识
     */
    String AUTH_MENU = "AUTH_MENU";

    /**
     * 资源管理整个界面的权限标识
     */
    String AUTH_RESOURCE = "AUTH_RESOURCE";

    /**
     * 在线用户界面的权限标识
     */
    String ONLINE_USER = "ONLINE_USER";

    /**
     * 修改权限的权限范围权限标识
     */
    String CHANGE_ROLE_BIND_LIMIT = "CHANGE_ROLE_BIND_LIMIT";

    /**
     * 密码策略配置权限标识
     */
    String PASSWORD_STRATEGY_CONFIG = "PASSWORD_STRATEGY_CONFIG";

}
