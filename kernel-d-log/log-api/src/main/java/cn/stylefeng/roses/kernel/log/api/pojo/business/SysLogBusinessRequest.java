package cn.stylefeng.roses.kernel.log.api.pojo.business;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务日志记录封装类
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysLogBusinessRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long businessLogId;

    /**
     * 日志的业务分类的编码
     */
    @ChineseDescription("日志的业务分类的编码")
    private String logTypeCode;

    /**
     * 日志的标题，摘要信息
     */
    @ChineseDescription("日志的标题，摘要信息")
    private String logTitle;

    /**
     * 当前用户请求的url
     */
    @ChineseDescription("当前用户请求的url")
    private String requestUrl;

    /**
     * 请求http方法
     */
    @ChineseDescription("请求http方法")
    private String httpMethod;

    /**
     * 客户端的ip
     */
    @ChineseDescription("客户端的ip")
    private String clientIp;

    /**
     * 业务操作的用户id
     */
    @ChineseDescription("业务操作的用户id")
    private Long userId;

}
