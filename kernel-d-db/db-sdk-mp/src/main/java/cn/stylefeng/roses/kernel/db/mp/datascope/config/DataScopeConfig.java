package cn.stylefeng.roses.kernel.db.mp.datascope.config;

import cn.stylefeng.roses.kernel.db.mp.datascope.ProjectDataScopeHandler;
import lombok.Data;

import java.util.Set;

/**
 * 数据范围权限配置
 *
 * @author fengshuonan
 * @since 2024-02-29 10:04
 */
@Data
public class DataScopeConfig {

    /**
     * 是否是全部的数据范围
     * <p>
     * 不限制数据范围的查询
     */
    private boolean totalDataScope = false;

    //-------------------------------针对限制的用户的数据范围进行校验-------------------------------

    /**
     * 是否对创建人进行校验
     */
    private boolean doCreateUserValidate = false;

    /**
     * 用户拥有权限的用户id
     * <p>
     * 一般为用户自己的id
     */
    private Long userId;

    /**
     * 用来限制只查询自己数据的字段名称
     */
    private String userIdFieldName = ProjectDataScopeHandler.DEFAULT_USER_ID_FIELD_NAME;

    //-------------------------------针对限制的部门集合数据范围进行校验-------------------------------

    /**
     * 是否对机构的数据范围进行校验
     */
    private boolean doOrgScopeValidate = false;

    /**
     * 用户拥有权限的组织机构id集合
     * <p>
     * 通过角色权限表，计算出来的最终结果
     */
    private Set<Long> userOrgIdList;

    /**
     * 限制组织机构范围的字段名称
     */
    private String orgIdFieldName = ProjectDataScopeHandler.DEFAULT_ORG_ID_FIELD_NAME;

}
