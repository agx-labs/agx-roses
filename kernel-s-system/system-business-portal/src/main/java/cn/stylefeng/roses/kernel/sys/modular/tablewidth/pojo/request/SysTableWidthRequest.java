package cn.stylefeng.roses.kernel.sys.modular.tablewidth.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务中表的宽度封装类
 *
 * @author fengshuonan
 * @date 2023/02/23 22:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysTableWidthRequest extends BaseRequest {

    /**
     * 主键id
     */
    @ChineseDescription("主键id")
    private Long tableWidthId;

    /**
     * 业务标识的编码，可以前端自定义，例如：PROJECT_TABLE
     */
    @NotBlank(message = "业务标识的编码不能为空", groups = {detail.class, add.class})
    @ChineseDescription("业务标识的编码，可以前端自定义，例如：PROJECT_TABLE")
    private String fieldBusinessCode;

    /**
     * 宽度记录的类型：1-全体员工，2-个人独有
     */
    @NotNull(message = "宽度记录的类型不能为空", groups = {add.class})
    @ChineseDescription("宽度记录的类型：1-全体员工，2-个人独有")
    private Integer fieldType;

    /**
     * 所属用户id
     */
    @ChineseDescription("所属用户id")
    private Long userId;

    /**
     * 自定义列是否显示、宽度、顺序和列的锁定，一段json
     */
    @NotBlank(message = "详情json不能为空", groups = {add.class})
    @ChineseDescription("自定义列是否显示、宽度、顺序和列的锁定，一段json")
    private String tableWidthJson;

}
