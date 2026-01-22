package cn.stylefeng.roses.kernel.favorite.modular.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户收藏信息实例类
 *
 * @author fengshuonan
 * @since 2023/11/21 22:09
 */
@TableName("sys_user_favorite")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserFavorite extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(value = "favorite_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long favoriteId;

    /**
     * 收藏业务的类型，存业务编码
     */
    @TableField("fav_type")
    @ChineseDescription("收藏业务的类型，存业务编码")
    private String favType;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 具体业务id
     */
    @TableField("business_id")
    @ChineseDescription("具体业务id")
    private Long businessId;

    /**
     * 收藏时间
     */
    @TableField("fav_time")
    @ChineseDescription("收藏时间")
    private Date favTime;

}
