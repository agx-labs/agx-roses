package cn.stylefeng.roses.kernel.db.mp.tenant.defaultimpl;

import cn.stylefeng.roses.kernel.db.mp.tenant.SaasServiceApi;
import cn.stylefeng.roses.kernel.db.mp.tenant.TenantSwitchApi;
import cn.stylefeng.roses.kernel.db.mp.tenant.holder.TenantIdHolder;
import cn.stylefeng.roses.kernel.db.mp.tenant.holder.TenantSwitchHolder;
import cn.stylefeng.roses.kernel.db.mp.tenant.pojo.TenantSwitchInfo;
import jakarta.annotation.Resource;

import java.util.function.Supplier;

/**
 * 某人的租户切换的实现
 *
 * @author fengshuonan
 * @since 2025/5/5 21:06
 */
public class DefaultTenantSwitchImpl implements TenantSwitchApi {

    @Resource
    private SaasServiceApi saasServiceApi;

    @Override
    public <T> T changeTenant(String tenantCode, Supplier<T> action) {

        // 获取租户的切换信息
        TenantSwitchInfo tenantSwitchInfo = saasServiceApi.getTenantSwitchInfo(tenantCode);
        if (tenantSwitchInfo == null) {
            return action.get();
        }

        return this.commonChangeTenant(action, tenantSwitchInfo);
    }

    @Override
    public <T> T changeTenant(Long tenantId, Supplier<T> action) {

        // 获取租户的切换信息
        TenantSwitchInfo tenantSwitchInfo = saasServiceApi.getTenantSwitchInfo(tenantId);
        if (tenantSwitchInfo == null) {
            return action.get();
        }

        return this.commonChangeTenant(action, tenantSwitchInfo);
    }

    @Override
    public <T> T changeTenant(TenantSwitchInfo tenantSwitchInfo, Supplier<T> action) {
        return this.commonChangeTenant(action, tenantSwitchInfo);
    }

    @Override
    public <T> T doNoTenantChange(Supplier<T> action) {
        try {
            // 去掉租户切换的逻辑，这个用在按租户id切割的情况下
            TenantSwitchHolder.set(false);

            // 执行传入的逻辑
            return action.get();

        } finally {
            // 恢复租户切换逻辑
            TenantSwitchHolder.remove();
        }
    }

    /**
     * 通用切换租户的信息
     *
     * @author fengshuonan
     * @since 2025/5/5 11:45
     */
    private <T> T commonChangeTenant(Supplier<T> action, TenantSwitchInfo tenantSwitchInfo) {
        // 保存当前租户的id
        Long currentTenantId = TenantIdHolder.get();

        try {
            // 切换租户
            TenantIdHolder.set(tenantSwitchInfo.getTenantId());

            // 执行传入的逻辑
            return action.get();

        } finally {
            // 恢复当前租户
            TenantIdHolder.set(currentTenantId);
        }
    }

}
