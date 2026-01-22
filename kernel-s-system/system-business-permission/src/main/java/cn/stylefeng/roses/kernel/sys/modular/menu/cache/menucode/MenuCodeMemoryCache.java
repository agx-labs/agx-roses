package cn.stylefeng.roses.kernel.sys.modular.menu.cache.menucode;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.sys.modular.menu.constants.MenuConstants;

/**
 * 菜单编码的缓存
 * <p>
 * 同时用在，菜单功能编码缓存
 *
 * @author fengshuonan
 * @since 2023/7/15 0:08
 */
public class MenuCodeMemoryCache extends AbstractMemoryCacheOperator<String> {

    public MenuCodeMemoryCache(TimedCache<String, String> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return MenuConstants.MENU_CODE_CACHE_PREFIX;
    }

}