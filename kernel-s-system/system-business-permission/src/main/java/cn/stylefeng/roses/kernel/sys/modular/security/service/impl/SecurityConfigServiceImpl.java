package cn.stylefeng.roses.kernel.sys.modular.security.service.impl;

import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.expander.LoginConfigExpander;
import cn.stylefeng.roses.kernel.auth.api.password.PasswordStoredEncryptApi;
import cn.stylefeng.roses.kernel.config.api.ConfigServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SecurityConfigService;
import cn.stylefeng.roses.kernel.sys.api.exception.SysException;
import cn.stylefeng.roses.kernel.sys.api.exception.enums.SecurityStrategyExceptionEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.security.SecurityConfig;
import cn.stylefeng.roses.kernel.sys.modular.security.entity.SysUserPasswordRecord;
import cn.stylefeng.roses.kernel.sys.modular.security.service.SysUserPasswordRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 系统配置的业务
 *
 * @author fengshuonan
 * @since 2023/10/4 16:24
 */
@Service
public class SecurityConfigServiceImpl implements SecurityConfigService {

    @Resource
    private ConfigServiceApi configServiceApi;

    @Resource
    private SysUserPasswordRecordService sysUserPasswordRecordService;

    @Resource
    private PasswordStoredEncryptApi passwordStoredEncryptApi;

    @Override
    public SecurityConfig getSecurityConfig() {

        SecurityConfig securityConfig = new SecurityConfig();
        securityConfig.setMaxErrorLoginCount(LoginConfigExpander.getMaxErrorLoginCount());
        securityConfig.setMinPasswordLength(LoginConfigExpander.getMinPasswordLength());

        securityConfig.setPasswordMinSpecialSymbolCount(LoginConfigExpander.getPasswordMinSpecialSymbolCount());
        securityConfig.setGetPasswordMinUpperCaseCount(LoginConfigExpander.getPasswordMinUpperCaseCount());
        securityConfig.setPasswordMinLowerCaseCount(LoginConfigExpander.getPasswordMinLowerCaseCount());
        securityConfig.setPasswordMinNumberCount(LoginConfigExpander.getPasswordMinNumberCount());

        securityConfig.setPasswordMinUpdateDays(LoginConfigExpander.getPasswordMinUpdateDays());
        securityConfig.setPasswordMinCantRepeatTimes(LoginConfigExpander.getPasswordMinCantRepeatTimes());

        return securityConfig;
    }

    @Override
    public void updateSecurityConfig(SecurityConfig securityConfig) {

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_MAX_ERROR_LOGIN_COUNT,
                String.valueOf(securityConfig.getMaxErrorLoginCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_MIN_PASSWORD_LENGTH,
                String.valueOf(securityConfig.getMinPasswordLength()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_SPECIAL_SYMBOL_COUNT,
                String.valueOf(securityConfig.getPasswordMinSpecialSymbolCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_UPPER_CASE_COUNT,
                String.valueOf(securityConfig.getGetPasswordMinUpperCaseCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_LOWER_CASE_COUNT,
                String.valueOf(securityConfig.getPasswordMinLowerCaseCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_NUMBER_COUNT,
                String.valueOf(securityConfig.getPasswordMinNumberCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_UPDATE_DAYS,
                String.valueOf(securityConfig.getPasswordMinUpdateDays()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_CANT_REPEAT_TIMES,
                String.valueOf(securityConfig.getPasswordMinCantRepeatTimes()));
    }

    @Override
    public void validatePasswordSecurityRule(boolean updatePasswordFlag, String password) {

        // 获取现在密码规则
        SecurityConfig securityConfig = this.getSecurityConfig();

        // 1. 校验密码长度是否符合规则
        if (password.length() < securityConfig.getMinPasswordLength()) {
            throw new SysException(SecurityStrategyExceptionEnum.PASSWORD_LENGTH, securityConfig.getMinPasswordLength());
        }

        // 2. 校验密码中特殊字符的数量
        int specialSymbolCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                specialSymbolCount++;
            }
        }
        if (specialSymbolCount < securityConfig.getPasswordMinSpecialSymbolCount()) {
            throw new SysException(SecurityStrategyExceptionEnum.SPECIAL_SYMBOL, securityConfig.getPasswordMinSpecialSymbolCount());
        }

        // 3. 校验密码中大写字母的数量
        int upperCaseCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                upperCaseCount++;
            }
        }
        if (upperCaseCount < securityConfig.getGetPasswordMinUpperCaseCount()) {
            throw new SysException(SecurityStrategyExceptionEnum.UPPER_CASE, securityConfig.getGetPasswordMinUpperCaseCount());
        }

        // 4. 校验密码中小写字母的数量
        int lowerCaseCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLowerCase(c)) {
                lowerCaseCount++;
            }
        }
        if (lowerCaseCount < securityConfig.getPasswordMinLowerCaseCount()) {
            throw new SysException(SecurityStrategyExceptionEnum.LOWER_CASE, securityConfig.getPasswordMinLowerCaseCount());
        }

        // 5. 校验密码中数字的数量
        int numberCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isDigit(c)) {
                numberCount++;
            }
        }
        if (numberCount < securityConfig.getPasswordMinNumberCount()) {
            throw new SysException(SecurityStrategyExceptionEnum.NUMBER_SYMBOL, securityConfig.getPasswordMinNumberCount());
        }

        // 6. 如果是修改密码，则校验密码是否和最近几次的密码相同
        Integer passwordMinCantRepeatTimes = securityConfig.getPasswordMinCantRepeatTimes();

        // 如果为0则不用校验
        if (passwordMinCantRepeatTimes == null || passwordMinCantRepeatTimes.equals(0)) {
            return;
        }
        List<SysUserPasswordRecord> recentRecords = sysUserPasswordRecordService.getRecentRecords(
                LoginContext.me().getLoginUser().getUserId(), passwordMinCantRepeatTimes);
        for (SysUserPasswordRecord recentRecord : recentRecords) {
            Boolean resultTrue = passwordStoredEncryptApi.checkPasswordWithSalt(password, recentRecord.getHistoryPasswordSalt(),
                    recentRecord.getHistoryPassword());
            if (resultTrue) {
                throw new SysException(SecurityStrategyExceptionEnum.PASSWORD_REPEAT, passwordMinCantRepeatTimes);
            }
        }
    }

    @Override
    public void recordPasswordEditLog(Long userId, String md5, String salt) {
        SysUserPasswordRecord sysUserPasswordRecord = new SysUserPasswordRecord();
        sysUserPasswordRecord.setUserId(userId);
        sysUserPasswordRecord.setHistoryPassword(md5);
        sysUserPasswordRecord.setHistoryPasswordSalt(salt);
        sysUserPasswordRecord.setUpdatePasswordTime(new Date());
        this.sysUserPasswordRecordService.save(sysUserPasswordRecord);
    }

}

