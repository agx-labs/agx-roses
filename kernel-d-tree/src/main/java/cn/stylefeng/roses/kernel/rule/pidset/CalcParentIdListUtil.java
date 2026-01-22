package cn.stylefeng.roses.kernel.rule.pidset;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.rule.constants.SymbolConstant;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.pidset.callback.PidGetterService;
import cn.stylefeng.roses.kernel.rule.pidset.callback.PidSettable;
import cn.stylefeng.roses.kernel.rule.pidset.mapper.CommonUpdatePidMapper;
import cn.stylefeng.roses.kernel.rule.pidset.pojo.ParentIdInfoPojo;

import java.util.Date;

/**
 * pid和pids的填充计算
 *
 * @author fengshuonan
 * @since 2024/8/30 13:45
 */
public class CalcParentIdListUtil {

    /**
     * 填充该节点的pIds
     * <p>
     * 如果pid是顶级节点，pids就是 [-1],
     * <p>
     * 如果pid不是顶级节点，pids就是父节点的pids + [pid] + ,
     *
     * @author fengshuonan
     * @since 2024/8/30 13:45
     */
    public static void fillParentIds(PidSettable pidSettable, PidGetterService pidGetterService) {

        // 如果父级是-1，则代表顶级节点
        if (TreeConstants.DEFAULT_PARENT_ID.equals(pidSettable.getParentId())) {
            pidSettable.setParentIdListString(SymbolConstant.LEFT_SQUARE_BRACKETS + TreeConstants.DEFAULT_PARENT_ID + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA);
        }

        // 如果不是顶级节点，则查询到父级的id集合，再拼接上级id即可
        else {
            // 获取父级的节点信息
            ParentIdInfoPojo pointNodePidInfo = pidGetterService.getPointNodePidInfo(pidSettable.getParentId());

            // 设置本节点的父ids为 (上一个节点的pids + (上级节点的id) )
            pidSettable.setParentIdListString(pointNodePidInfo.getParentIdListString() + SymbolConstant.LEFT_SQUARE_BRACKETS + pointNodePidInfo.getId() + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA);
        }

    }

    /**
     * 检测是否变化了parentId，如果变化了parentId则需要更新子节点的子节点的pids
     * <p>
     * 更新节点的所有子节点的pid_list_string集合
     *
     * @author fengshuonan
     * @since 2024/8/30 17:03
     */
    public static void updateParentIdStringList(String tableName, String pidsFieldName,
                                                PidSettable oldItem, PidSettable newItem, PidGetterService pidGetterService) {

        if (oldItem == null || newItem == null) {
            return;
        }

        // 如果新旧pid都一样，则不需要更新
        if (oldItem.getParentId().equals(newItem.getParentId())) {
            return;
        }

        // 计算出被更新的pids
        String oldPids = oldItem.getParentIdListString();
        oldPids = oldPids + SymbolConstant.LEFT_SQUARE_BRACKETS + oldItem.getCurrentId() + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;

        // 将pids更改为新的pids
        String newParentIds = newItem.getParentIdListString() + SymbolConstant.LEFT_SQUARE_BRACKETS + newItem.getCurrentId() + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;

        // 获取用户id
        Long loginUserId = -1L;
        LoginUser loginUserNullable = LoginContext.me().getLoginUserNullable();
        if (loginUserNullable != null) {
            loginUserId = loginUserNullable.getUserId();
        }

        // 更新pids结构
        CommonUpdatePidMapper commonUpdatePidMapper = SpringUtil.getBean(CommonUpdatePidMapper.class);
        commonUpdatePidMapper.updateSubParentIdListString(tableName, pidsFieldName, oldPids, newParentIds, DateUtil.formatDateTime(new Date()), loginUserId);
    }

}
