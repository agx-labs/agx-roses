package cn.stylefeng.roses.kernel.sys.modular.app.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import cn.stylefeng.roses.kernel.file.api.format.FileUrlFormatProcess;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 系统应用实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@TableName(value = "sys_app", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysApp extends BaseExpandFieldEntity {

    /**
     * 主键id
     */
    @TableId(value = "app_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long appId;

    /**
     * 应用名称
     */
    @TableField("app_name")
    @ChineseDescription("应用名称")
    private String appName;

    /**
     * 编码
     */
    @TableField("app_code")
    @ChineseDescription("编码")
    private String appCode;

    /**
     * 应用图标，存fileId，上传的图片
     */
    @TableField("app_icon")
    @ChineseDescription("应用图标，存fileId，上传的图片")
    @SimpleFieldFormat(processClass = FileUrlFormatProcess.class)
    private Long appIcon;

    /**
     * 状态：1-启用，2-禁用
     */
    @TableField("status_flag")
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 排序
     */
    @TableField("app_sort")
    @ChineseDescription("排序")
    private BigDecimal appSort;

    /**
     * 备注
     */
    @TableField("remark")
    @ChineseDescription("备注")
    private String remark;

}
