package cn.stylefeng.roses.kernel.log.security.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安全日志实例类
 *
 * @author fengshuonan
 * @since 2024/07/11 15:56
 */
@TableName("sys_log_security")
@Data
@EqualsAndHashCode(callSuper = true)
public class LogSecurity extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "security_log_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long securityLogId;

    /**
     * 当前用户请求的url
     */
    @TableField("request_url")
    @ChineseDescription("当前用户请求的url")
    private String requestUrl;

    /**
     * http或方法的请求参数体
     */
    @TableField("request_params")
    @ChineseDescription("http或方法的请求参数体")
    private String requestParams;

    /**
     * 当前服务器的ip
     */
    @TableField("server_ip")
    @ChineseDescription("当前服务器的ip")
    private String serverIp;

    /**
     * 客户端的ip
     */
    @TableField("client_ip")
    @ChineseDescription("客户端的ip")
    private String clientIp;

    /**
     * 请求http方法
     */
    @TableField("http_method")
    @ChineseDescription("请求http方法")
    private String httpMethod;

    /**
     * 客户浏览器标识
     */
    @TableField("client_browser")
    @ChineseDescription("客户浏览器标识")
    private String clientBrowser;

    /**
     * 客户操作系统
     */
    @TableField("client_os")
    @ChineseDescription("客户操作系统")
    private String clientOs;

    /**
     * 安全日志内容
     */
    @TableField("log_content")
    @ChineseDescription("安全日志内容")
    private String logContent;

}