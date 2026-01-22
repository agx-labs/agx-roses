package cn.stylefeng.roses.kernel.sys.modular.login.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.OnlineUserItem;
import lombok.Data;

import java.util.List;

/**
 * 在线用户的返回结果
 *
 * @author fengshuonan
 * @since 2023/7/2 12:29
 */
@Data
public class OnlineUserResult {

    /**
     * 总的在线用户数
     */
    private Integer totalUserCount = 0;

    /**
     * 用户在线列表人数
     */
    @ChineseDescription("用户在线列表人数")
    private List<OnlineUserItem> onlineUserList;

}
