package cn.stylefeng.roses.kernel.sys.modular.login.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 首页需要的用户信息，包括【个人基本信息】、【菜单信息】、【当前登录的应用】、【当前登录的组织机构身份】
 *
 * @author fengshuonan
 * @since 2023/6/18 22:03
 */
@Data
public class UserIndexInfo {

    /**
     * 用户主键id
     */
    @ChineseDescription("用户主键id")
    private Long userId;

    /**
     * 真实姓名
     */
    @ChineseDescription("真实姓名")
    private String realName;

    /**
     * 头像地址
     */
    @ChineseDescription("头像地址")
    private String avatarUrl;

    /**
     * 当前用户的部门和任职信息
     */
    @ChineseDescription("当前用户的部门和任职信息")
    private List<IndexUserOrgInfo> userOrgInfoList;

    /**
     * 当前用户的权限编码集合，包括【菜单编码】和【菜单功能编码】
     * <p>
     * 一般用在前端用户鉴权，或者按钮的权限判断
     */
    @ChineseDescription("当前用户的权限编码集合，包括【菜单编码】和【菜单功能编码】")
    private Set<String> permissionCodeList;

    /**
     * 当前用户拥有的应用信息
     */
    @ChineseDescription("当前用户拥有的应用信息")
    private List<IndexUserAppInfo> userAppInfoList;

    /**
     * 登录人的websocketUrl，用来获取右上角的实时消息
     */
    @ChineseDescription("登录人的websocketUrl，用来获取右上角的实时消息")
    private String websocketUrl;

    /**
     * 管理员用户表示
     */
    @ChineseDescription("管理员用户表示")
    private Boolean superAdminFlag;

}
