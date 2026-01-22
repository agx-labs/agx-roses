package cn.stylefeng.roses.kernel.auth.api.pojo.sso;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 生成SSO token时候的格式
 *
 * @author fengshuonan
 * @since 2023/11/5 12:31
 */
@Data
@AllArgsConstructor
public class DecryptCaTokenInfo {

    /**
     * 统一认证中心中，用户的token
     */
    private String caToken;

    /**
     * 用户id
     */
    private DecryptCaLoginUser caLoginUser;

    /**
     * 生成token时的时间
     * <p>
     * 格式是：yyyyMMddHHmmss
     */
    private String generationDateTime;

}
