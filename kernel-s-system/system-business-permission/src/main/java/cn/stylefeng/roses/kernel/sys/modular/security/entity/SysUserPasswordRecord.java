package cn.stylefeng.roses.kernel.sys.modular.security.entity;

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
 * 用户历史密码记录实例类
 *
 * @author fengshuonan
 * @date 2023/10/04 23:28
 */
@TableName("sys_user_password_record")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserPasswordRecord extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "record_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long recordId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 历史密码记录
     */
    @TableField("history_password")
    @ChineseDescription("历史密码记录")
    private String historyPassword;

    /**
     * 历史密码记录盐值
     */
    @TableField("history_password_salt")
    @ChineseDescription("历史密码记录盐值")
    private String historyPasswordSalt;

    /**
     * 修改密码时间
     */
    @TableField("update_password_time")
    @ChineseDescription("修改密码时间")
    private Date updatePasswordTime;

}
