package cn.stylefeng.roses.kernel.db.mp.tenant.context;

import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.db.mp.tenant.TenantSwitchApi;

/**
 * 租户切换的上下文，快捷工具
 *
 * @author fengshuonan
 * @since 2025/5/5 11:21
 */
public class TenantSwitchContext {

    public TenantSwitchContext() {
    }

    public static TenantSwitchApi me() {
        return SpringUtil.getBean(TenantSwitchApi.class);
    }

}
