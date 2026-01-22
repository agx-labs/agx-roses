package cn.stylefeng.roses.kernel.stat.modular.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户点击数量统计实例类
 *
 * @author fengshuonan
 * @since 2023/03/28 14:52
 */
@TableName("sys_click_count")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClickCount extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "click_count_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long clickCountId;

    /**
     * 业务的分类标识
     */
    @TableField("business_type")
    @ChineseDescription("业务的分类标识")
    private String businessType;

    /**
     * 业务的主键id
     */
    @TableField("business_key_id")
    @ChineseDescription("业务的主键id")
    private Long businessKeyId;

    /**
     * 点击次数
     */
    @TableField("click_count")
    @ChineseDescription("点击次数")
    private Long clickCount;

}
