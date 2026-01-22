package cn.stylefeng.roses.kernel.auth.auth;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.stylefeng.roses.kernel.auth.api.SessionManagerApi;
import cn.stylefeng.roses.kernel.auth.api.TempSecretApi;
import cn.stylefeng.roses.kernel.auth.api.TenantCodeGetApi;
import cn.stylefeng.roses.kernel.auth.api.constants.AuthConstants;
import cn.stylefeng.roses.kernel.auth.api.context.AuthJwtContext;
import cn.stylefeng.roses.kernel.auth.api.exception.AuthException;
import cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum;
import cn.stylefeng.roses.kernel.auth.api.expander.AuthConfigExpander;
import cn.stylefeng.roses.kernel.auth.api.expander.LoginConfigExpander;
import cn.stylefeng.roses.kernel.auth.api.password.PasswordStoredEncryptApi;
import cn.stylefeng.roses.kernel.auth.api.password.PasswordTransferEncryptApi;
import cn.stylefeng.roses.kernel.auth.api.pojo.auth.LoginRequest;
import cn.stylefeng.roses.kernel.auth.api.pojo.auth.LoginResponse;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.auth.api.pojo.payload.DefaultJwtPayload;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.demo.expander.DemoConfigExpander;
import cn.stylefeng.roses.kernel.log.api.LoginLogServiceApi;
import cn.stylefeng.roses.kernel.log.api.schedule.AsyncLogManager;
import cn.stylefeng.roses.kernel.rule.util.HttpServletUtil;
import cn.stylefeng.roses.kernel.scanner.api.exception.ScannerException;
import cn.stylefeng.roses.kernel.scanner.api.exception.enums.ScannerExceptionEnum;
import cn.stylefeng.roses.kernel.scanner.api.holder.InitScanFlagHolder;
import cn.stylefeng.roses.kernel.security.api.DragCaptchaApi;
import cn.stylefeng.roses.kernel.security.api.ImageCaptchaApi;
import cn.stylefeng.roses.kernel.security.api.expander.SecurityConfigExpander;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.enums.user.UserStatusEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserValidateDTO;
import cn.stylefeng.roses.kernel.validator.api.exception.enums.ValidatorExceptionEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 登录相关的逻辑封装
 *
 * @author fengshuonan
 * @since 2023/6/17 23:45
 */
@Service
public class LoginService {

    @Resource
    private SysUserServiceApi sysUserServiceApi;

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private PasswordTransferEncryptApi passwordTransferEncryptApi;

    @Resource
    private ImageCaptchaApi imageCaptchaApi;

    @Resource
    private DragCaptchaApi dragCaptchaApi;

    @Resource
    private LoginLogServiceApi loginLogServiceApi;

    @Resource(name = "loginErrorCountCacheApi")
    private CacheOperatorApi<Integer> loginErrorCountCacheApi;

    @Resource
    private PasswordStoredEncryptApi passwordStoredEncryptApi;

    @Resource
    private TenantCodeGetApi tenantCodeGetApi;

    /**
     * 登录的真正业务逻辑
     *
     * @param loginRequest     登录参数
     * @param validatePassword 是否校验密码，true-校验密码，false-不会校验密码
     * @param caToken          单点登录后服务端的token，一般为32位uuid
     * @author fengshuonan
     * @since 2020/10/21 16:59
     */
    public LoginResponse loginAction(LoginRequest loginRequest, Boolean validatePassword, String caToken) {

        // 校验当前系统是否初始化资源完成，如果资源还没有初始化，提示用户请等一下再登录
        if (!InitScanFlagHolder.getFlag()) {
            throw new ScannerException(ScannerExceptionEnum.SYSTEM_RESOURCE_URL_NOT_INIT);
        }

        // 1.参数为空校验
        validateEmptyParams(loginRequest, validatePassword);

        // 1.2 判断账号是否密码重试次数过多被冻结
        Integer loginErrorCount = validatePasswordRetryTimes(loginRequest);

        // 2. 如果开启了验证码校验，则验证当前请求的验证码是否正确
        if (validatePassword) {
            validateCaptcha(loginRequest);
        }

        // 3. 解密密码的密文，需要sys_config相关配置打开
        decryptRequestPassword(loginRequest);

        // 4. 通过租户编码获取租户id，如果租户参数没传，则默认填充根租户的id
        String tenantCode = loginRequest.getTenantCode();
        Long tenantId = tenantCodeGetApi.getTenantIdByCode(tenantCode);

        // 5. 获取用户密码的加密值和用户的状态
        UserValidateDTO userValidateInfo = sysUserServiceApi.getUserLoginValidateDTO(tenantId, loginRequest.getAccount());

        // 6. 校验用户密码是否正确
        validateUserPassword(validatePassword, loginErrorCount, loginRequest, userValidateInfo);

        // 7. 校验用户是否异常（不是正常状态）
        if (!UserStatusEnum.ENABLE.getCode().equals(userValidateInfo.getUserStatus())) {
            throw new AuthException(AuthExceptionEnum.USER_STATUS_ERROR, UserStatusEnum.getCodeMessage(userValidateInfo.getUserStatus()));
        }

        // 8. 生成用户的token
        DefaultJwtPayload defaultJwtPayload = new DefaultJwtPayload(userValidateInfo.getUserId(), loginRequest.getAccount(),
                loginRequest.getRememberMe(), caToken);
        String userLoginToken = AuthJwtContext.me().generateTokenDefaultPayload(defaultJwtPayload);

        // 9. 创建loginUser对象
        LoginUser loginUser = new LoginUser(userValidateInfo.getUserId(), loginRequest.getAccount(), userLoginToken, tenantId);

        // 9.1 记录用户登录时间和ip
        String ip = HttpServletUtil.getRequestClientIp(HttpServletUtil.getRequest());
        loginUser.setLoginIp(ip);
        loginUser.setLoginTime(new Date());

        synchronized (loginRequest.getAccount().intern()) {

            // 10. 缓存用户信息，创建会话
            sessionManagerApi.createSession(userLoginToken, loginUser);

            // 11. 如果开启了单账号单端在线，则踢掉已经上线的该用户
            if (LoginConfigExpander.getSingleAccountLoginFlag()) {
                sessionManagerApi.removeSessionExcludeToken(userLoginToken);
            }
        }

        // 演示环境，跳过记录日志，非演示环境则记录登录日志
        if (!DemoConfigExpander.getDemoEnvFlag()) {
            // 12. 更新用户登录时间和ip
            sysUserServiceApi.updateUserLoginInfo(loginUser.getUserId(), ip, tenantId);

            // 13.登录成功日志
            loginLogServiceApi.loginSuccess(loginUser.getUserId(), loginUser.getAccount());
        }

        // 13.1 登录成功，清空用户的错误登录次数
        loginErrorCountCacheApi.remove(loginRequest.getAccount());

        // 14. 组装返回结果
        return new LoginResponse(loginUser.getUserId(), userLoginToken);
    }

    /**
     * 如果开启了密码加密配置，则需要进行密码的密文解密，再进行密码校验
     *
     * @author fengshuonan
     * @since 2023/6/20 23:15
     */
    private void decryptRequestPassword(LoginRequest loginRequest) {
        if (loginRequest.getPassword() != null && LoginConfigExpander.getPasswordRsaValidateFlag()) {
            String decryptPassword = passwordTransferEncryptApi.decrypt(loginRequest.getPassword());
            loginRequest.setPassword(decryptPassword);
        }
    }

    /**
     * 校验用户的图形验证码，或者拖拽验证码是否正确
     *
     * @author fengshuonan
     * @since 2023/6/20 23:13
     */
    private void validateCaptcha(LoginRequest loginRequest) {

        // 1. 验证图形验证码
        if (SecurityConfigExpander.getCaptchaOpen()) {
            String verKey = loginRequest.getVerKey();
            String verCode = loginRequest.getVerCode();

            if (StrUtil.isEmpty(verKey) || StrUtil.isEmpty(verCode)) {
                throw new AuthException(ValidatorExceptionEnum.CAPTCHA_EMPTY);
            }
            if (!imageCaptchaApi.validateCaptcha(verKey, verCode)) {
                throw new AuthException(ValidatorExceptionEnum.CAPTCHA_ERROR);
            }
        }

        // 2. 验证拖拽验证码
        if (SecurityConfigExpander.getDragCaptchaOpen()) {
            String verKey = loginRequest.getDragVerKey();
            String verXLocationValue = loginRequest.getDragVerCode();

            if (StrUtil.isEmpty(verKey) || StrUtil.isEmpty(verXLocationValue)) {
                throw new AuthException(ValidatorExceptionEnum.CAPTCHA_EMPTY);
            }
            if (!dragCaptchaApi.validateCaptcha(verKey, Convert.toInt(verXLocationValue))) {
                throw new AuthException(ValidatorExceptionEnum.DRAG_CAPTCHA_ERROR);
            }
        }
    }

    /**
     * 校验密码重试次数是否过多，默认不能超过5次
     *
     * @author fengshuonan
     * @since 2023/6/20 23:13
     */
    private Integer validatePasswordRetryTimes(LoginRequest loginRequest) {
        Integer loginErrorCount = loginErrorCountCacheApi.get(loginRequest.getAccount());
        if (loginErrorCount != null && loginErrorCount >= LoginConfigExpander.getMaxErrorLoginCount()) {
            // 修改用户状态为锁定
            // 【2024年7月12日新增，默认锁定1天】
            sysUserServiceApi.lockUserStatus(loginRequest.getTenantCode(), loginRequest.getAccount());
            throw new AuthException(AuthExceptionEnum.LOGIN_LOCKED);
        }
        return loginErrorCount;
    }

    /**
     * 登录接口，校验登录参数是否为空
     *
     * @author fengshuonan
     * @since 2023/6/20 23:10
     */
    private static void validateEmptyParams(LoginRequest loginRequest, Boolean validatePassword) {
        if (validatePassword) {
            if (loginRequest == null || StrUtil.hasBlank(loginRequest.getAccount(), loginRequest.getPassword())) {
                throw new AuthException(AuthExceptionEnum.PARAM_EMPTY);
            }
        } else {
            if (loginRequest == null || StrUtil.hasBlank(loginRequest.getAccount())) {
                throw new AuthException(AuthExceptionEnum.ACCOUNT_IS_BLANK);
            }
        }
    }

    /**
     * 调用远程接口获取loginCode
     *
     * @author fengshuonan
     * @since 2021/2/26 15:15
     */
    private String getRemoteLoginCode(LoginRequest loginRequest) {

        // 获取sso的地址
        String ssoUrl = AuthConfigExpander.getSsoUrl();

        // 请求sso服务获取loginCode
        HttpRequest httpRequest = HttpRequest.post(ssoUrl + AuthConstants.SYS_AUTH_SSO_GET_LOGIN_CODE);
        httpRequest.body(JSON.toJSONString(loginRequest));
        HttpResponse httpResponse = httpRequest.execute();

        // 获取返回结果的message
        String body = httpResponse.body();
        JSONObject jsonObject = new JSONObject();
        if (StrUtil.isNotBlank(body)) {
            jsonObject = JSON.parseObject(body);
        }

        // 如果返回结果是失败的
        if (httpResponse.getStatus() != 200) {
            String message = jsonObject.getString("message");
            throw new AuthException(AuthExceptionEnum.SSO_LOGIN_CODE_GET_ERROR, message);
        }

        // 从body中获取loginCode
        String loginCode = jsonObject.getString("data");

        // loginCode为空
        if (loginCode == null) {
            throw new AuthException(AuthExceptionEnum.SSO_LOGIN_CODE_GET_ERROR, "loginCode为空");
        }

        return loginCode;
    }

    /**
     * 用户密码校验，校验失败会报异常
     *
     * @author fengshuonan
     * @since 2022/3/26 14:16
     */
    private void validateUserPassword(Boolean validatePassword, Integer loginErrorCount, LoginRequest loginRequest,
                                      UserValidateDTO userValidateInfo) {

        // 如果不需要校验登录密码，则直接返回
        if (!validatePassword) {
            return;
        }

        // 如果本次登录需要校验密码
        Boolean checkResult = passwordStoredEncryptApi.checkPasswordWithSalt(loginRequest.getPassword(),
                userValidateInfo.getUserPasswordSalt(), userValidateInfo.getUserPasswordHexed());

        // 校验用户表密码是否正确，如果正确则直接返回
        if (checkResult) {
            return;
        }

        // 如果密码不正确则校验用户是否有临时秘钥
        TempSecretApi tempSecretApi = null;
        try {
            tempSecretApi = SpringUtil.getBean(TempSecretApi.class);
            if (tempSecretApi != null) {
                // 如果用户有临时秘钥并且校验成功，则直接返回
                boolean validateResult = tempSecretApi.validateUserTempSecretKey(userValidateInfo.getUserId(), loginRequest.getPassword());
                if (validateResult) {
                    return;
                }
            }
        } catch (Exception ignored) {
        }

        // 临时秘钥校验完成，返回前端用户密码错误
        if (loginErrorCount == null) {
            loginErrorCount = 0;
        }

        // 演示环境，不记录error次数
        if (!DemoConfigExpander.getDemoEnvFlag()) {
            loginErrorCountCacheApi.put(loginRequest.getAccount(), loginErrorCount + 1);

            // 记录登录失败日志
            AsyncLogManager.getInstance().recordLoginLogFail(loginRequest.getAccount(), HttpServletUtil.getRequestClientIp(HttpServletUtil.getRequest()));
        }

        throw new AuthException(AuthExceptionEnum.USERNAME_PASSWORD_ERROR);
    }

}
