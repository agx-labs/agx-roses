package cn.stylefeng.roses.kernel.rule.pidset;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析pids这种树形结构
 *
 * @author fengshuonan
 * @since 2024/8/30 11:10
 */
public class ParentIdParseUtil {

    /**
     * 将字符串类型的pid结构，解析成Long类型List
     *
     * @author fengshuonan
     * @since 2024/8/30 11:12
     */
    public static List<Long> parseToPidList(String pidListString) {

        List<Long> result = new ArrayList<>();

        if (StrUtil.isBlank(pidListString)) {
            return result;
        }

        // 去掉中括号符号
        pidListString = pidListString.replaceAll("\\[", "");
        pidListString = pidListString.replaceAll("]", "");

        // 解析出来所有的id列表
        String[] pidStringList = pidListString.split(",");

        // 查找到缺失的父级id
        for (String idString : pidStringList) {
            result.add(Convert.toLong(idString));
        }

        return result;
    }

}
