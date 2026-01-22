package cn.stylefeng.roses.kernel.sys.modular.app.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.validator.api.validators.unique.TableUniqueValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 系统应用封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysAppRequest extends BaseRequest {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = {edit.class, delete.class, updateStatus.class})
    @ChineseDescription("主键id")
    private Long appId;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("应用名称")
    @TableUniqueValue(
            message = "应用名称存在重复",
            groups = {add.class, edit.class},
            tableName = "sys_app",
            columnName = "app_name",
            idFieldName = "app_id",
            excludeLogicDeleteItems = true)
    private String appName;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("编码")
    @TableUniqueValue(
            message = "应用编码存在重复",
            groups = {add.class, edit.class},
            tableName = "sys_app",
            columnName = "app_code",
            idFieldName = "app_id",
            excludeLogicDeleteItems = true)
    private String appCode;

    /**
     * 应用图标，存fileId，上传的图片
     */
    @ChineseDescription("应用图标，存fileId，上传的图片")
    @NotNull(message = "应用图标不能为空", groups = {add.class, edit.class})
    private Long appIcon;

    /**
     * 状态：1-启用，2-禁用
     */
    @NotNull(message = "状态：1-启用，2-禁用不能为空", groups = {add.class, edit.class, updateStatus.class})
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 排序
     */
    @ChineseDescription("排序")
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    private BigDecimal appSort;

    /**
     * 备注
     */
    @ChineseDescription("备注")
    private String remark;

    /**
     * 主键id集合，用在批量删除
     */
    @NotEmpty(message = "主键id集合不能为空", groups = batchDelete.class)
    @ChineseDescription("主键id集合")
    private Set<Long> appIdList;

}
