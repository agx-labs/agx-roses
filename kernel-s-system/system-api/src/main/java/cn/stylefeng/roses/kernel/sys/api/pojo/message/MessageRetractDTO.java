package cn.stylefeng.roses.kernel.sys.api.pojo.message;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.util.Set;

/**
 * 消息发送的信息包装
 *
 * @author fengshuonan
 * @since 2024/1/14 22:15
 */
@Data
public class MessageRetractDTO {

    /**
     * 被撤回的用户id集合
     */
    @ChineseDescription("被撤回的用户id集合")
    private Set<Long> userIdList;

    /**
     * 业务类型，根据业务类型进行撤回
     */
    @ChineseDescription("业务类型")
    private String businessType;

    /**
     * 业务id，根据业务类型进行撤回
     */
    @ChineseDescription("业务id")
    private String businessId;

}
