package cn.stylefeng.roses.kernel.sys.api.pojo.user;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用在响应获取在线用户列表
 *
 * @author fengshuonan
 * @since 2023/7/2 11:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OnlineUserItem extends BaseRequest {

    /**
     * 用户主键id
     */
    @ChineseDescription("用户主键id")
    private Long userId;

    /**
     * 用户token
     */
    @ChineseDescription("用户token")
    @NotBlank(message = "用户token不能为空", groups = offlineUser.class)
    private String token;

    /**
     * 真实姓名
     */
    @ChineseDescription("真实姓名")
    private String realName;

    /**
     * 账号
     */
    @ChineseDescription("账号")
    private String account;

    /**
     * 登录时的ip
     */
    @ChineseDescription("登录时的ip")
    private String loginIp;

    /**
     * 登录时间
     */
    @ChineseDescription("登录时间")
    private Date loginTime;

    /**
     * 参数校验分组：踢下线某个token
     */
    public @interface offlineUser {
    }

    public OnlineUserItem() {
    }

    public OnlineUserItem(Long userId, String realName, String account) {
        this.userId = userId;
        this.realName = realName;
        this.account = account;
    }
}
