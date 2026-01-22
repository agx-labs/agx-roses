package cn.stylefeng.roses.kernel.db.mp.datascope;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.db.mp.datascope.holder.DataScopeHolder;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.stream.Collectors;

/**
 * 项目数据权限的处理器
 *
 * @author fengshuonan
 * @since 2024/2/28 21:16
 */
public class ProjectDataScopeHandler implements MultiDataPermissionHandler {

    /**
     * 不存在的业务id值
     */
    public static final Long NONE_ID_VALUE = -1L;

    /**
     * 用在数据范围筛选的用户id字段的默认字段名称
     */
    public static final String DEFAULT_USER_ID_FIELD_NAME = "user_id";

    /**
     * 用在数据范围筛选的组织id字段的默认字段名称
     */
    public static final String DEFAULT_ORG_ID_FIELD_NAME = "org_id";

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {

        // 获取数据范围上下文的配置，如果没有则直接略过
        DataScopeConfig dataScopeConfig = DataScopeHolder.get();
        if (ObjectUtil.isEmpty(dataScopeConfig)) {
            return null;
        }

        // 如果是全部权限，则不校验
        if (dataScopeConfig.isTotalDataScope()) {
            return null;
        }

        Expression dataScopeExpression = null;

        // 如果是需要校验仅创建人数据
        if (dataScopeConfig.isDoCreateUserValidate()) {

            // 获取创建人ID
            Long currentUserId = dataScopeConfig.getUserId();

            // 生成创建人条件表达式
            dataScopeExpression = getEqualsTo(dataScopeConfig.getUserIdFieldName(), currentUserId);
        }

        // 如果是需要校验指定部门数据
        if (dataScopeConfig.isDoOrgScopeValidate()) {

            // 获取组织范围条件表达式
            InExpression orgScopeCondition = getInExpression(dataScopeConfig);

            // 如果已经有创建人条件，需要合并
            if (dataScopeExpression != null) {
                // 使用 OrExpression 合并条件
                dataScopeExpression = new AndExpression(dataScopeExpression, orgScopeCondition);
            } else {
                // 否则仅使用组织范围条件
                dataScopeExpression = orgScopeCondition;
            }
        }

        return dataScopeExpression;
    }

    /**
     * 获取equals语法的表达式
     *
     * @author fengshuonan
     * @since 2024-02-29 20:36
     */
    private static EqualsTo getEqualsTo(String fieldName, Long value) {
        Column orgIdColumn = new Column(fieldName);
        LongValue longValue = new LongValue(value);

        // 创建代表等式的 EqualsTo 对象
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(orgIdColumn);
        equalsTo.setRightExpression(longValue);
        return equalsTo;
    }

    /**
     * 获取in的表达式配置
     *
     * @author fengshuonan
     * @since 2024-02-29 20:35
     */
    private static InExpression getInExpression(DataScopeConfig dataScopeConfig) {
        // 创建 org_id 列
        Column orgIdColumn = new Column(dataScopeConfig.getOrgIdFieldName());

        // 创建 IN 表达式的值列表
        ExpressionList expressionList = new ExpressionList();
        expressionList.setExpressions(dataScopeConfig.getUserOrgIdList().stream().map(LongValue::new).collect(Collectors.toList()));

        // 创建 IN 表达式
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(orgIdColumn);
        inExpression.setRightExpression(new Parenthesis(expressionList));
        return inExpression;
    }

}
