package cn.stylefeng.roses.kernel.rule.tree.buildpids;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.constants.PidBuildConstants;
import cn.stylefeng.roses.kernel.rule.constants.SymbolConstant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 构建pids结构的工具，自动填充到参数中pids
 *
 * @author fengshuonan
 * @since 2023/6/15 15:02
 */
public class PidStructureBuildUtil {

    /**
     * 构造pids结构
     *
     * @author fengshuonan
     * @since 2023/6/15 15:03
     */
    public static <T extends BasePidBuildModel> void createPidStructure(List<T> list) {

        // 保存每一个数据的pids路径（成品），key是节点id，value是pids路径字符串
        Map<String, String> routeMap = new HashMap<>();

        // 暂时未找到父级的pids的对象，key是节点id，value是实体对象
        Map<String, T> waitMap = new HashMap<>();

        // 一次遍历
        for (T itemModel : list) {

            String pid = itemModel.pidBuildParentId();

            String pids = "";

            // pid不存在或者pid是-1，直接设置pids为[-1],
            if (ObjectUtil.isEmpty(pid) || PidBuildConstants.TOP_FLAG.equals(pid)) {
                pids = PidBuildConstants.TOP_PIDS;
                itemModel.setPidBuildPidStructure(pids);
                routeMap.put(itemModel.pidBuildNodeId(), pids);
            }

            // pid存在，去routeMap找它的父级的pids，【父级的pids,[父级的id],】
            else {
                String parentPids = routeMap.get(pid);

                //如果此时还没处理到它的父级，放入等待队列中，处理完一轮后再回溯
                if (ObjectUtil.isEmpty(parentPids)) {
                    waitMap.put(itemModel.pidBuildNodeId(), itemModel);
                } else {
                    pids = parentPids + SymbolConstant.LEFT_SQUARE_BRACKETS + pid + SymbolConstant.RIGHT_SQUARE_BRACKETS + PidBuildConstants.SEPARATOR;
                    itemModel.setPidBuildPidStructure(pids);
                    routeMap.put(itemModel.pidBuildNodeId(), pids);
                }
            }
        }

        // 处理暂时未找到父级的pids的对象集合
        int size = waitMap.size();
        while (size != 0) {
            // 处理waitMap中没拿到结果的对象
            Iterator<Map.Entry<String, T>> iterator = waitMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, T> entry = iterator.next();
                T waitProcessItem = entry.getValue();

                // 再次寻找父级的pids
                String parentPids = routeMap.get(waitProcessItem.pidBuildParentId());
                if (ObjectUtil.isEmpty(parentPids)) {
                    //如果还是没有，等待下一次回溯
                    continue;
                }

                // 父级的pids有了，组装自己的pids
                String pids = parentPids + SymbolConstant.LEFT_SQUARE_BRACKETS + waitProcessItem.pidBuildParentId() + SymbolConstant.RIGHT_SQUARE_BRACKETS + PidBuildConstants.SEPARATOR;
                waitProcessItem.setPidBuildPidStructure(pids);

                // 放入map，以便它的下级在回溯的时候能够找到它
                routeMap.put(waitProcessItem.pidBuildNodeId(), pids);

                // 每找到一个，就删除一个
                size--;
                iterator.remove();
            }
        }
    }
}
