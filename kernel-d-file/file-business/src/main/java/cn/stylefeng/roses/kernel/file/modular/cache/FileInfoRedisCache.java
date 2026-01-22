package cn.stylefeng.roses.kernel.file.modular.cache;

import cn.stylefeng.roses.kernel.cache.redis.AbstractRedisCacheOperator;
import cn.stylefeng.roses.kernel.file.api.constants.FileConstants;
import cn.stylefeng.roses.kernel.file.api.pojo.response.SysFileInfoResponse;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 文件信息的Redis缓存
 * <p>
 * key是文件id，value是SysFileInfoResponse
 *
 * @author fengshuonan
 * @since 2025/1/14 16:58
 */
public class FileInfoRedisCache extends AbstractRedisCacheOperator<SysFileInfoResponse> {

    public FileInfoRedisCache(RedisTemplate<String, SysFileInfoResponse> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public String getCommonKeyPrefix() {
        return FileConstants.FILE_INFO_CACHE_NAME_PREFIX;
    }

}
