package cn.stylefeng.roses.kernel.city.modular.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 行政区域实例类
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
@TableName("sys_area")
@Data
@EqualsAndHashCode(callSuper = true)
public class Area extends BaseEntity {

    /**
     * 区域id
     */
    @TableId(value = "area_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("区域id")
    private Long areaId;

    /**
     * 区域编码
     */
    @TableField("area_code")
    @ChineseDescription("区域编码")
    private String areaCode;

    /**
     * 区域全称
     */
    @TableField("area_name")
    @ChineseDescription("区域全称")
    private String areaName;

    /**
     * 上级区域编码
     */
    @TableField("parent_id")
    @ChineseDescription("上级区域编码")
    private String parentId;

    /**
     * 区域级别
     */
    @TableField("area_level")
    @ChineseDescription("区域级别")
    private Integer areaLevel;

    /**
     * 排序码
     */
    @TableField("area_sort")
    @ChineseDescription("排序码")
    private BigDecimal areaSort;

    /**
     * 是否删除
     */
    @TableField("del_flag")
    @ChineseDescription("是否删除")
    private String delFlag;

    /**
     * 所有的上级区域编码,用逗号分隔
     */
    @TableField("area_pids")
    @ChineseDescription("所有的上级区域编码,用逗号分隔")
    private String areaPids;

}
