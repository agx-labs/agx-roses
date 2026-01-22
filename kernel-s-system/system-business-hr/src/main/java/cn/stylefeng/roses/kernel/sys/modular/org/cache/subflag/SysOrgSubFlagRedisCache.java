package cn.stylefeng.roses.kernel.sys.modular.org.cache.subflag;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.sys.modular.org.constants.OrgConstants;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 组织机构是否包含下级的缓存
 *
 * @author fengshuonan
 * @since 2023/7/14 1:06
 */
public class SysOrgSubFlagRedisCache extends AbstractRedisCacheOperator<Boolean> {

    public SysOrgSubFlagRedisCache(RedisTemplate<String, Boolean> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return OrgConstants.SYS_ORG_SUB_FLAG_CACHE_PREFIX;
    }

}
