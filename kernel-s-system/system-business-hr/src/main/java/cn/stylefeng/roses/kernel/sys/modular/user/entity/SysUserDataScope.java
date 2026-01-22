package cn.stylefeng.roses.kernel.sys.modular.user.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户数据范围实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@TableName("sys_user_data_scope")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDataScope extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "user_data_scope_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long userDataScopeId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 机构id
     */
    @TableField("org_id")
    @ChineseDescription("机构id")
    private Long orgId;

}
