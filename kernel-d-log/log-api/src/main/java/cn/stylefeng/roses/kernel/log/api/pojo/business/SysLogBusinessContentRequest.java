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
public class SysLogBusinessContentRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long contentId;

    /**
     * 对应主表主键id
     */
    @ChineseDescription("对应主表主键id")
    private Long businessLogId;

    /**
     * 日志记录的内容
     */
    @ChineseDescription("日志记录的内容")
    private String logContent;

}
