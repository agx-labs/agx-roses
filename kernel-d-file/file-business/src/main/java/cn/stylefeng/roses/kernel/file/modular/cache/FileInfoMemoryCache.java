package cn.stylefeng.roses.kernel.file.modular.cache;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.file.api.constants.FileConstants;
import cn.stylefeng.roses.kernel.file.api.pojo.response.SysFileInfoResponse;

/**
 * 文件信息的内存缓存
 * <p>
 * key是文件id，value是SysFileInfoResponse
 *
 * @author fengshuonan
 * @since 2025/1/14 16:58
 */
public class FileInfoMemoryCache extends AbstractMemoryCacheOperator<SysFileInfoResponse> {

    public FileInfoMemoryCache(TimedCache<String, SysFileInfoResponse> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return FileConstants.FILE_INFO_CACHE_NAME_PREFIX;
    }

}