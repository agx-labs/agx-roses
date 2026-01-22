package cn.stylefeng.roses.kernel.log.api.context;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.log.api.pojo.entity.SysLogBusiness;

import java.util.LinkedList;
import java.util.List;

/**
 * 业务日志暂存
 *
 * @author fengshuonan
 * @since 2023/7/21 16:06
 */
public class BusinessLogHolder {

    /**
     * 暂存当前请求的日志信息
     */
    private static final ThreadLocal<SysLogBusiness> BUSINESS_LOG_CONTEXT = new ThreadLocal<>();

    /**
     * 暂存本次请求的所有日志详情
     */
    private static final ThreadLocal<List<String>> CONTENT_LIST = new ThreadLocal<>();

    /**
     * 本次业务日志记录上下文信息存储到日志中
     *
     * @author fengshuonan
     * @since 2023/7/21 16:23
     */
    public static void setContext(SysLogBusiness sysLogBusiness) {
        BUSINESS_LOG_CONTEXT.set(sysLogBusiness);
    }

    /**
     * 获取本次业务日志记录上下文信息
     *
     * @author fengshuonan
     * @since 2021/3/23 17:41
     */
    public static SysLogBusiness getContext() {
        return BUSINESS_LOG_CONTEXT.get();
    }

    /**
     * 清除所有的日志记录上下文
     *
     * @author fengshuonan
     * @since 2021/3/23 17:42
     */
    public static void clearContext() {
        BUSINESS_LOG_CONTEXT.remove();
        CONTENT_LIST.remove();
    }

    /**
     * 添加日志记录
     *
     * @author fengshuonan
     * @since 2023/7/21 16:53
     */
    public static void addContent(String contentStr) {
        List<String> contentList = CONTENT_LIST.get();
        if (ObjectUtil.isEmpty(contentList)) {
            contentList = new LinkedList<>();
        }
        contentList.add(contentStr);
        CONTENT_LIST.set(contentList);
    }

    /**
     * 获取所有的日志详情记录
     *
     * @author fengshuonan
     * @since 2023/7/21 16:53
     */
    public static List<String> getContent() {
        return CONTENT_LIST.get();
    }

    /**
     * 设置日志的摘要信息，便于后台搜索
     *
     * @author fengshuonan
     * @since 2023/7/21 17:30
     */
    public static void setLogTitle(String logTitle) {
        SysLogBusiness sysLogBusiness = BUSINESS_LOG_CONTEXT.get();
        if (sysLogBusiness == null) {
            return;
        }
        sysLogBusiness.setLogTitle(logTitle);
        BUSINESS_LOG_CONTEXT.set(sysLogBusiness);
    }

}
