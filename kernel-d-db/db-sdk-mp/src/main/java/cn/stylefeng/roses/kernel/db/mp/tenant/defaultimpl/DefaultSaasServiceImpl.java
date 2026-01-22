package cn.stylefeng.roses.kernel.db.mp.tenant.defaultimpl;

import cn.stylefeng.roses.kernel.db.mp.tenant.SaasServiceApi;
import cn.stylefeng.roses.kernel.db.mp.tenant.pojo.TenantSwitchInfo;
import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;

/**
 * 默认的租户业务的实现
 *
 * @author fengshuonan
 * @since 2025/5/5 21:06
 */
public class DefaultSaasServiceImpl implements SaasServiceApi {

    @Override
    public TenantSwitchInfo getTenantSwitchInfo(Long tenantId) {
        return new TenantSwitchInfo(RuleConstants.DEFAULT_ROOT_TENANT_ID, null, null);
    }

    @Override
    public TenantSwitchInfo getTenantSwitchInfo(String tenantCode) {
        return new TenantSwitchInfo(RuleConstants.DEFAULT_ROOT_TENANT_ID, tenantCode, null);
    }

}
