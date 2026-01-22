package cn.stylefeng.roses.kernel.db.mp.tenant.holder;

import cn.stylefeng.roses.kernel.rule.threadlocal.RemoveThreadLocalApi;
import org.springframework.stereotype.Component;

/**
 * 清除参数缓存相关的ThreadLocal
 *
 * @author fengshuonan
 * @since 2023/8/31 17:06
 */
@Component
public class TenantRemoveThreadLocalHolder implements RemoveThreadLocalApi {

    @Override
    public void removeThreadLocalAction() {
        TenantIdHolder.remove();
        TenantSwitchHolder.remove();
    }

}
