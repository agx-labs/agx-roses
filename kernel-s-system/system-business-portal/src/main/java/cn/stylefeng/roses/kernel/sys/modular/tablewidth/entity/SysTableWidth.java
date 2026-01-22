package cn.stylefeng.roses.kernel.sys.modular.tablewidth.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务中表的宽度实例类
 *
 * @author fengshuonan
 * @date 2023/02/23 22:21
 */
@TableName("sys_table_width")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysTableWidth extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "table_width_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long tableWidthId;

    /**
     * 业务标识的编码，例如：PROJECT_TABLE
     */
    @TableField("field_business_code")
    @ChineseDescription("业务标识的编码，例如：PROJECT_TABLE")
    private String fieldBusinessCode;

    /**
     * 宽度记录的类型：1-全体员工，2-个人独有
     */
    @TableField("field_type")
    @ChineseDescription("宽度记录的类型：1-全体员工，2-个人独有")
    private Integer fieldType;

    /**
     * 所属用户id
     */
    @TableField("user_id")
    @ChineseDescription("所属用户id")
    private Long userId;

    /**
     * 自定义列是否显示、宽度、顺序和列的锁定，一段json
     */
    @TableField("table_width_json")
    @ChineseDescription("自定义列是否显示、宽度、顺序和列的锁定，一段json")
    private String tableWidthJson;

}
