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
 * 用户点击状态实例类
 *
 * @author fengshuonan
 * @since 2023/03/28 14:52
 */
@TableName("sys_click_status")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClickStatus extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "click_status_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long clickStatusId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 业务的主键id
     */
    @TableField("business_key_id")
    @ChineseDescription("业务的主键id")
    private Long businessKeyId;

    /**
     * 业务的分类标识
     */
    @TableField("business_type")
    @ChineseDescription("业务的分类标识")
    private String businessType;

}
