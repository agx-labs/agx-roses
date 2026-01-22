package cn.stylefeng.roses.kernel.file.modular.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务关联的文件实例类
 *
 * @author fengshuonan
 * @since 2023/03/31 13:30
 */
@TableName("sys_file_business")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysFileBusiness extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "file_business_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long fileBusinessId;

    /**
     * 业务的编码，业务自定义
     */
    @TableField("business_code")
    @ChineseDescription("业务的编码，业务自定义")
    private String businessCode;

    /**
     * 业务主键id
     */
    @TableField("business_id")
    @ChineseDescription("业务主键id")
    private Long businessId;

    /**
     * 关联文件表的id
     */
    @TableField("file_id")
    @ChineseDescription("关联文件表的id")
    private Long fileId;

    /**
     * 下载次数
     */
    @TableField("download_count")
    @ChineseDescription("下载次数")
    private Integer downloadCount;

}
