package cn.stylefeng.roses.kernel.auth.api.loginuser;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.auth.api.constants.AuthConstants;
import cn.stylefeng.roses.kernel.auth.api.exception.AuthException;
import cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum;
import cn.stylefeng.roses.kernel.rule.util.HttpServletUtil;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 获取当前登录用户的相关方法
 *
 * @author fengshuonan
 * @since 2021/9/28 17:46
 */
public class CommonLoginUserUtil {

    /**
     * 获取当前登录用户Token
     *
     * @author fengshuonan
     * @since 2021/9/28 17:46
     */
    public static String getToken() {

        // 获取当前http请求
        HttpServletRequest request = HttpServletUtil.getRequest();

        // 1. 优先从param参数中获取token
        String parameterToken = request.getParameter(AuthConstants.DEFAULT_AUTH_PARAM_NAME);

        // 不为空则直接返回param的token
        if (StrUtil.isNotBlank(parameterToken)) {
            return parameterToken;
        }

        // 2. 从header中获取token
        String authToken = request.getHeader(AuthConstants.DEFAULT_AUTH_HEADER_NAME);
        if (StrUtil.isNotBlank(authToken)) {
            return authToken;
        }

        // 获取不到token，直接告诉用户
        throw new AuthException(AuthExceptionEnum.TOKEN_GET_ERROR);
    }

}
