package cn.stylefeng.roses.kernel.sys.modular.org.cache.orginfo;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.HrOrganizationDTO;
import cn.stylefeng.roses.kernel.sys.modular.org.constants.OrgConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 组织机构详情信息的缓存
 * <p>
 * key是组织机构id，value是组织机构详情
 *
 * @author fengshuonan
 * @since 2023/7/14 1:06
 */
public class SysOrgInfoRedisCache extends AbstractRedisCacheOperator<HrOrganizationDTO> {

    public SysOrgInfoRedisCache(RedisTemplate<String, HrOrganizationDTO> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return OrgConstants.ORG_INFO_CACHE_PREFIX;
    }

}
