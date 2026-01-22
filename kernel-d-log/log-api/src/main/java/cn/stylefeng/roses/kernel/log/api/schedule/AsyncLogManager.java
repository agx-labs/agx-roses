package cn.stylefeng.roses.kernel.log.api.schedule;

import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.log.api.LoginLogServiceApi;
import cn.stylefeng.roses.kernel.log.api.SecurityLogServiceApi;
import cn.stylefeng.roses.kernel.log.api.pojo.security.LogSecurityRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志执行器，为了解决日志记录的异步问题
 *
 * @author fengshuonan
 * @since 2024/7/10 17:25
 */
@Slf4j
public class AsyncLogManager {

    // 异步操作记录日志的线程池
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);

    private AsyncLogManager() {
    }

    public static AsyncLogManager logManager = new AsyncLogManager();

    public static AsyncLogManager getInstance() {
        return logManager;
    }

    /**
     * 执行日志记录任务
     *
     * @author fengshuonan
     * @since 2024/7/10 17:27
     */
    public void executeLog(TimerTask task) {
        // 日志记录操作延时
        int delayTime = 10;
        executor.schedule(task, delayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 异步记录登录失败的日志
     *
     * @author fengshuonan
     * @since 2024/7/10 17:29
     */
    public void recordLoginLogFail(String account, String ip) {
        this.executeLog(new TimerTask() {
            @Override
            public void run() {
                try {
                    LoginLogServiceApi loginLogServiceApi = SpringUtil.getBean(LoginLogServiceApi.class);
                    loginLogServiceApi.loginFail(account, ip);
                } catch (Exception e) {
                    log.error("记录登录失败日志异常！", e);
                }
            }
        });
    }

    /**
     * 异步记录安全日志
     *
     * @author fengshuonan
     * @since 2024/7/11 18:36
     */
    public void recordSecurityLog(LogSecurityRequest logSecurityRequest) {
        this.executeLog(new TimerTask() {
            @Override
            public void run() {
                try {
                    SecurityLogServiceApi securityLogServiceApi = SpringUtil.getBean(SecurityLogServiceApi.class);
                    securityLogServiceApi.add(logSecurityRequest);
                } catch (Exception e) {
                    log.error("记录安全日志异常！", e);
                }
            }
        });
    }

}
