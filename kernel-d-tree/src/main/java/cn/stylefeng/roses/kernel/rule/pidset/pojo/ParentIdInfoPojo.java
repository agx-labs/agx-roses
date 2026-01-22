package cn.stylefeng.roses.kernel.rule.pidset.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 父级id信息的获取
 *
 * @author fengshuonan
 * @since 2024/8/30 13:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentIdInfoPojo {

    /**
     * 指定节点的id
     */
    private Long id;

    /**
     * 指定节点的父级id集合
     */
    private String parentIdListString;

}
