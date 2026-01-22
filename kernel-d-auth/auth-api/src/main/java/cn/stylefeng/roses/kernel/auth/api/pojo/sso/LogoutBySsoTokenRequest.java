package cn.stylefeng.roses.kernel.auth.api.pojo.sso;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通过单点的token退出系统
 *
 * @author fengshuonan
 * @since 2023/11/7 16:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogoutBySsoTokenRequest extends BaseRequest {

    /**
     * 从单点服务获取到的token
     */
    @NotBlank(message = "CA Token不能为空")
    private String caToken;

}
