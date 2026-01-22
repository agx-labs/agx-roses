package cn.stylefeng.roses.kernel.city.modular.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 行政区域封装类
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AreaRequest extends BaseRequest {

    /**
     * 区域id
     */
    @NotNull(message = "区域id不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("区域id")
    private Long areaId;

    /**
     * 区域编码
     */
    @NotBlank(message = "区域编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("区域编码")
    private String areaCode;

    /**
     * 区域全称
     */
    @NotBlank(message = "区域全称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("区域全称")
    private String areaName;

    /**
     * 上级区域编码
     */
    @NotBlank(message = "上级区域编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("上级区域编码")
    private String parentId;

    /**
     * 区域级别
     */
    @ChineseDescription("区域级别")
    private Integer areaLevel;

    /**
     * 排序码
     */
    @ChineseDescription("排序码")
    private BigDecimal areaSort;

    /**
     * 是否删除
     */
    @ChineseDescription("是否删除")
    private String delFlag;

    /**
     * 所有的上级区域编码,用逗号分隔
     */
    @ChineseDescription("所有的上级区域编码,用逗号分隔")
    private String areaPids;

}
