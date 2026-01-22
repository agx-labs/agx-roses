package cn.stylefeng.roses.kernel.sys.api;

/**
 * 职位相关的业务api
 *
 * @author fengshuonan
 * @since 2023/7/15 21:59
 */
public interface PositionServiceApi {

    /**
     * 获取职务的名称
     *
     * @author fengshuonan
     * @since 2023/6/12 16:38
     */
    String getPositionName(Long positionId);

}
