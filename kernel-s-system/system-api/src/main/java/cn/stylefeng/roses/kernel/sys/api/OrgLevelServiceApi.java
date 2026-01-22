package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.OrganizationLevelRequest;

import java.util.List;

/**
 * 单独编写用户和组织机构关系的Api
 *
 * @author fengshuonan
 * @since 2023/6/18 23:14
 */
public interface OrgLevelServiceApi {

    /**
     * 获取机构层级列表
     *
     * @param organizationLevelRequest 请求参数
     * @return List<OrganizationLevel>  返回结果
     * @author fengshuonan
     * @since 2025/01/22 09:44
     */
    List<OrganizationLevel> findList(OrganizationLevelRequest organizationLevelRequest);

}
