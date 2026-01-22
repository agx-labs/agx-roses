package cn.stylefeng.roses.kernel.event.sdk.container;

import cn.stylefeng.roses.kernel.event.sdk.pojo.BusinessListenerDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件的容器
 *
 * @author fengshuonan
 * @since 2023/7/14 15:08
 */
public class EventContainer {

    /**
     * key是业务的标识，例如：ORG_ADD
     * <p>
     * value是对应监听这个事件的所有方法信息
     */
    private static final Map<String, List<BusinessListenerDetail>> CONTEXT = new HashMap<>();

    /**
     * 获取业务对应的所有监听器
     *
     * @author fengshuonan
     * @since 2023/7/14 15:21
     */
    public static List<BusinessListenerDetail> getListener(String businessCode) {
        return CONTEXT.get(businessCode);
    }

    /**
     * 添加到容器中业务监听器信息
     *
     * @author fengshuonan
     * @since 2023/7/14 15:22
     */
    public static void addListenerList(String businessCode, List<BusinessListenerDetail> businessListenerItemList) {
        CONTEXT.put(businessCode, businessListenerItemList);
    }

}
