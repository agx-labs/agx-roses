package cn.stylefeng.roses.kernel.sys.modular.org.service;

import cn.stylefeng.roses.kernel.sys.api.OrgLevelServiceApi;
import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.OrganizationLevelRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 组织机构层级服务类
 *
 * @author fengshuonan
 * @since 2025/01/22 09:44
 */
public interface OrganizationLevelService extends IService<OrganizationLevel>, OrgLevelServiceApi {

    /**
     * 新增组织机构层级
     *
     * @param organizationLevelRequest 请求参数
     * @author fengshuonan
     * @since 2025/01/22 09:44
     */
    void updateTotal(OrganizationLevelRequest organizationLevelRequest);

}
