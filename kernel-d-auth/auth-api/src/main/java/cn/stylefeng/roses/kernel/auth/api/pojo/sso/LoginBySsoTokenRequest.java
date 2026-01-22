package cn.stylefeng.roses.kernel.auth.api.pojo.sso;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 单点获取到的token
 *
 * @author fengshuonan
 * @since 2021/5/25 22:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginBySsoTokenRequest extends BaseRequest {

    /**
     * 从单点服务获取到的token
     */
    @NotBlank(message = "CA Token不能为空")
    private String token;

}
