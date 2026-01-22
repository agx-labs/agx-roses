package cn.stylefeng.roses.kernel.log.api.pojo.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务日志记录实例类
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
@TableName("sys_log_business_content")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLogBusinessContent extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "content_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long contentId;

    /**
     * 对应主表主键id
     */
    @TableField("business_log_id")
    @ChineseDescription("对应主表主键id")
    private Long businessLogId;

    /**
     * 日志记录的内容
     */
    @TableField("log_content")
    @ChineseDescription("日志记录的内容")
    private String logContent;

}
