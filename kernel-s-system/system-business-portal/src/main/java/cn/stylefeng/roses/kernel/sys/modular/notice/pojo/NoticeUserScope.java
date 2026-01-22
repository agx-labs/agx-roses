package cn.stylefeng.roses.kernel.sys.modular.notice.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import lombok.Data;

import java.util.List;

/**
 * 通知的通知人员范围
 *
 * @author fengshuonan
 * @since 2024-01-12 16:32
 */
@Data
public class NoticeUserScope {

    /**
     * 发送给指定的机构
     */
    @ChineseDescription("发送给指定的机构")
    private List<SimpleDict> pointOrgList;

    /**
     * 发送给指定的用户
     */
    @ChineseDescription("发送给指定的用户")
    private List<SimpleDict> pointUserList;

}
