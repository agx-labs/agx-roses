package cn.stylefeng.roses.kernel.config.api;

/**
 * 系统配置的操作API
 *
 * @author fengshuonan
 * @since 2023/10/4 22:05
 */
public interface ConfigServiceApi {

    /**
     * 更新系统配置的值，通过配置的编码
     * <p>
     * 同时更新数据库的和缓存的配置
     *
     * @param code  配置编码
     * @param value 配置值
     * @author fengshuonan
     * @since 2023/10/4 22:05
     */
    void updateConfigByCode(String code, String value);

}
