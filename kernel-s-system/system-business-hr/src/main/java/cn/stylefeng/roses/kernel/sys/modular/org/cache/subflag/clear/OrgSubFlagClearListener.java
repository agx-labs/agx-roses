package cn.stylefeng.roses.kernel.sys.modular.org.cache.subflag.clear;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.api.annotation.BusinessListener;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.HrOrganizationDTO;
import cn.stylefeng.roses.kernel.sys.modular.org.constants.OrgConstants;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * 监听组织机构相关的事件，从而进行清空子级标识，保障缓存同步
 *
 * @author fengshuonan
 * @since 2023/7/14 21:45
 */
@Service
public class OrgSubFlagClearListener {

    @Resource(name = "sysOrgSubFlagCache")
    private CacheOperatorApi<Boolean> sysOrgSubFlagCache;

    @Resource(name = "sysOrgInfoCache")
    private CacheOperatorApi<HrOrganizationDTO> sysOrgInfoCache;

    /**
     * 监听添加组织机构
     * <p>
     * 清空组织机构下级标识
     *
     * @author fengshuonan
     * @since 2023/7/14 18:38
     */
    @BusinessListener(businessCode = OrgConstants.ADD_ORG_EVENT)
    public void addOrgCallback(HrOrganization businessObject) {
        if (ObjectUtil.isNotEmpty(businessObject.getOrgId())) {
            sysOrgSubFlagCache.remove(String.valueOf(businessObject.getOrgId()));
        }
        if (ObjectUtil.isNotEmpty(businessObject.getOrgParentId())) {
            sysOrgSubFlagCache.remove(String.valueOf(businessObject.getOrgParentId()));
        }
    }

    /**
     * 监听编辑组织机构
     *
     * @author fengshuonan
     * @since 2023/7/14 18:40
     */
    @BusinessListener(businessCode = OrgConstants.EDIT_ORG_EVENT)
    public void editOrgCallback(Long orgId) {
        // 获取所有主键
        Collection<String> allKeys = sysOrgSubFlagCache.getAllKeys();

        // 删除所有子集标识
        sysOrgSubFlagCache.remove(allKeys);

        // 删除组织机构详情的缓存
        sysOrgInfoCache.remove(orgId.toString());
    }

    /**
     * 监听删除组织机构的事件
     *
     * @author fengshuonan
     * @since 2023/7/14 18:40
     */
    @BusinessListener(businessCode = OrgConstants.DELETE_ORG_EVENT)
    public void deleteOrgCallback(Set<Long> orgIdList) {

        // 获取所有主键
        Collection<String> allKeys = sysOrgSubFlagCache.getAllKeys();

        // 删除所有子集标识
        sysOrgSubFlagCache.remove(allKeys);

        // 删除组织机构详情的缓存
        // 机构id列表转化为String列表
        Collection<String> orgIdListStr = orgIdList.stream().map(String::valueOf).toList();
        sysOrgInfoCache.remove(orgIdListStr);
    }

}
