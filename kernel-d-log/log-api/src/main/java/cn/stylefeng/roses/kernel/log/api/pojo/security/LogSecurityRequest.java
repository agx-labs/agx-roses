package cn.stylefeng.roses.kernel.log.api.pojo.security;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 安全日志封装类
 *
 * @author fengshuonan
 * @since 2024/07/11 15:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogSecurityRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long securityLogId;

    /**
     * 当前用户请求的url
     */
    @ChineseDescription("当前用户请求的url")
    private String requestUrl;

    /**
     * http或方法的请求参数体
     */
    @ChineseDescription("http或方法的请求参数体")
    private String requestParams;

    /**
     * 当前服务器的ip
     */
    @ChineseDescription("当前服务器的ip")
    private String serverIp;

    /**
     * 客户端的ip
     */
    @ChineseDescription("客户端的ip")
    private String clientIp;

    /**
     * 请求http方法
     */
    @ChineseDescription("请求http方法")
    private String httpMethod;

    /**
     * 客户浏览器标识
     */
    @ChineseDescription("客户浏览器标识")
    private String clientBrowser;

    /**
     * 客户操作系统
     */
    @ChineseDescription("客户操作系统")
    private String clientOs;

    /**
     * 安全日志内容
     */
    @ChineseDescription("安全日志内容")
    private String logContent;

    /**
     * 批量删除用的id集合
     */
    @NotNull(message = "批量删除id集合不能为空", groups = batchDelete.class)
    @ChineseDescription("批量删除用的id集合")
    private List<Long> batchDeleteIdList;

}