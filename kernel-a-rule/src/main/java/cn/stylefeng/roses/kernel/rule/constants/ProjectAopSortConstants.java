package cn.stylefeng.roses.kernel.rule.constants;

/**
 * 数据范围
 * <p>
 * 默认事务的AOP顺序是Integer.MAX_VALUE，最大值，AOP最后执行
 *
 * @author fengshuonan
 * @since 2024/6/21 0:05
 */
public interface ProjectAopSortConstants {

    /**
     * 多数据源切换的aop的顺序
     */
    int MULTI_DATA_SOURCE_EXCHANGE_AOP = 1;

    /**
     * 不进行租户切换的单独的AOP
     */
    int TENANT_DO_NO_TENANT_CHANGE_AOP = 10;

    /**
     * 数据范围控制的AOP顺序
     */
    int DATA_SCOPE_AOP_ORDER = 100;

    /**
     * 事务的AOP顺序
     */
    int TRANSACTION_AOP_ORDER = 200;

    /**
     * 默认业务日志记录的aop的顺序
     */
    Integer DEFAULT_BUSINESS_LOG_AOP_SORT = 400;

    /**
     * 默认api日志记录的aop的顺序
     */
    Integer DEFAULT_API_LOG_AOP_SORT = 500;

}
