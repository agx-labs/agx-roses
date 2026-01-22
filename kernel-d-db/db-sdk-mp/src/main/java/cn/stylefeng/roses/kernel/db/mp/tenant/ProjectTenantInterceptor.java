package cn.stylefeng.roses.kernel.db.mp.tenant;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.db.api.pojo.tenant.TenantTableProperties;
import cn.stylefeng.roses.kernel.db.mp.tenant.holder.TenantIdHolder;
import cn.stylefeng.roses.kernel.db.mp.tenant.holder.TenantSwitchHolder;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;

import java.util.List;

/**
 * 租户隔离插件
 *
 * @author fengshuonan
 * @since 2023/8/30 10:14
 */
public class ProjectTenantInterceptor implements TenantLineHandler {

    /**
     * 租户相关的表的配置
     */
    private TenantTableProperties tenantTableProperties;

    public ProjectTenantInterceptor(TenantTableProperties tenantTableProperties) {
        this.tenantTableProperties = tenantTableProperties;
    }

    @Override
    public Expression getTenantId() {

        // 1. 优先从线程变量中获取，这个优先级最高
        Long tenantId = TenantIdHolder.get();
        if (ObjectUtil.isNotEmpty(tenantId)) {
            return new LongValue(tenantId);
        }

        // 2. 从LoginUser中获取tenantId
        LoginUser loginUserNullable = LoginContext.me().getLoginUserNullable();
        if (loginUserNullable == null) {
            return null;
        }
        if (ObjectUtil.isEmpty(loginUserNullable.getTenantId())) {
            return null;
        }
        return new LongValue(loginUserNullable.getTenantId());
    }

    @Override
    public boolean ignoreTable(String tableName) {

        // 1. 优先从线程变量中获取，这个优先级最高
        Boolean openFlag = TenantSwitchHolder.get();
        if (openFlag != null && !openFlag) {
            return true;
        }

        // 2. 第2步，从系统配置中获取，是否开启此table的开关
        if (tenantTableProperties == null) {
            return true;
        }

        List<String> businessTableList = tenantTableProperties.getBusinessTableList();
        if (ObjectUtil.isEmpty(businessTableList)) {
            return true;
        }

        for (String tenantTable : businessTableList) {
            if (tenantTable.equalsIgnoreCase(tableName)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }

}
