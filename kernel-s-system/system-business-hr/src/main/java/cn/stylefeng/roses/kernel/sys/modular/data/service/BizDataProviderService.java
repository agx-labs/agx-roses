package cn.stylefeng.roses.kernel.sys.modular.data.service;

import cn.stylefeng.roses.kernel.dict.api.DictApi;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;
import cn.stylefeng.roses.kernel.sys.api.PositionServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.HrOrganizationDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 表单组件数据提供
 *
 * @author fengshuonan
 * @since 2024-04-20 11:17
 */
@Service
public class BizDataProviderService {

    @Resource
    private SysUserServiceApi sysUserServiceApi;

    @Resource
    private OrganizationServiceApi organizationServiceApi;

    @Resource
    private SysRoleServiceApi sysRoleServiceApi;

    @Resource
    private PositionServiceApi positionServiceApi;

    @Resource
    private DictApi dictApi;

    /**
     * 获取人员姓名
     *
     * @author fengshuonan
     * @since 2024-04-20 11:18
     */
    public String getUserName(Long userId) {
        return sysUserServiceApi.getUserRealName(userId);
    }

    /**
     * 通过id获取机构的名称
     *
     * @author fengshuonan
     * @since 2024-04-20 11:23
     */
    public String getOrgName(Long orgId) {
        HrOrganizationDTO orgInfo = organizationServiceApi.getOrgInfo(orgId);
        return orgInfo.getOrgName();
    }

    /**
     * 通过id获取角色名称
     *
     * @author fengshuonan
     * @since 2024-04-20 11:25
     */
    public String getRoleName(Long roleId) {
        return sysRoleServiceApi.getRoleNameByRoleId(roleId);
    }

    /**
     * 获取职位名称
     *
     * @author fengshuonan
     * @since 2024-04-20 11:27
     */
    public String getPositionName(Long positionId) {
        return positionServiceApi.getPositionName(positionId);
    }

    /**
     * 通过字典id获取字典值名称·
     *
     * @author fengshuonan
     * @since 2024-04-20 11:28
     */
    public String getDictName(Long dictId) {
        return dictApi.getDictNameByDictId(dictId);
    }

}
