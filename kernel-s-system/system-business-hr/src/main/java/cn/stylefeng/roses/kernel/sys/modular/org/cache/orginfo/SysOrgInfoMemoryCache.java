package cn.stylefeng.roses.kernel.sys.modular.org.cache.orginfo;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.HrOrganizationDTO;
import cn.stylefeng.roses.kernel.sys.modular.org.constants.OrgConstants;

/**
 * 组织机构详情信息的缓存
 * <p>
 * key是组织机构id，value是组织机构详情
 *
 * @author fengshuonan
 * @since 2025/1/10 11:47
 */
public class SysOrgInfoMemoryCache extends AbstractMemoryCacheOperator<HrOrganizationDTO> {

    public SysOrgInfoMemoryCache(TimedCache<String, HrOrganizationDTO> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return OrgConstants.ORG_INFO_CACHE_PREFIX;
    }

}