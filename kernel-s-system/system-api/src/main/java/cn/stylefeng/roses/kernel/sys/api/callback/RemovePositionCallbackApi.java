package cn.stylefeng.roses.kernel.sys.api.callback;

import java.util.Set;

/**
 * 删掉职位的回调方法
 *
 * @author fengshuonan
 * @since 2023/6/11 17:33
 */
public interface RemovePositionCallbackApi {

    /**
     * 校验是否绑定了职位相关的信息，如果绑定了则报错
     *
     * @param beRemovedPositionIdList 被删除的职位id集合
     * @author fengshuonan
     * @since 2023/6/11 17:34
     */
    void validateHavePositionBind(Set<Long> beRemovedPositionIdList);

    /**
     * 删除职位后的回调
     *
     * @param beRemovedPositionIdList 被删除的职位id集合
     * @author fengshuonan
     * @since 2023/6/11 17:34
     */
    void removePositionAction(Set<Long> beRemovedPositionIdList);

}
