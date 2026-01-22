package cn.stylefeng.roses.kernel.db.mp.tenant.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

/**
 * 租户切换信息
 *
 * @author fengshuonan
 * @since 2025/5/4 20:38
 */
@Data
public class TenantSwitchInfo {

    /**
     * 租户id
     */
    @ChineseDescription("租户id")
    private Long tenantId;

    /**
     * 租户唯一标识
     */
    @ChineseDescription("租户唯一标识")
    private String tenantCode;

    /**
     * 数据隔离方式：1-租户id隔离，2-数据库分离
     */
    @ChineseDescription("数据隔离方式：1-租户id隔离，2-数据库分离")
    private Integer dataMode;

    public TenantSwitchInfo() {
    }

    public TenantSwitchInfo(Long tenantId, String tenantCode, Integer dataMode) {
        this.tenantId = tenantId;
        this.tenantCode = tenantCode;
        this.dataMode = dataMode;
    }

}
