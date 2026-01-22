package cn.stylefeng.roses.kernel.sys.modular.theme.clean;

import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.config.api.ConfigInitCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.theme.constants.ThemeConstants;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.DefaultTheme;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 系统初始化之后的主题资源刷新
 *
 * @author fengshuonan
 * @since 2022/2/26 12:55
 */
@Component
public class ThemeConfigRefresh implements ConfigInitCallbackApi {

    @Resource(name = "themeCacheApi")
    private CacheOperatorApi<DefaultTheme> themeCacheApi;

    @Override
    public void initBefore() {

    }

    @Override
    public void initAfter() {
        themeCacheApi.remove(ThemeConstants.THEME_GUNS_PLATFORM);
    }

}
