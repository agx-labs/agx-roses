package cn.stylefeng.roses.kernel.security.blackwhite.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.util.List;

/**
 * 黑白名单列表
 *
 * @author fengshuonan
 * @since 2024/7/10 21:30
 */
@Data
public class BlackWhiteList {

    /**
     * 黑名单列表
     */
    @ChineseDescription("黑名单列表")
    private List<String> blackList;

    /**
     * 白名单列表
     */
    @ChineseDescription("白名单列表")
    private List<String> whiteList;

}
