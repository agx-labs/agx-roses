package cn.stylefeng.roses.kernel.sys.modular.user.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 用户组织机构关联实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@TableName(value = "sys_user_org", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserOrg extends BaseEntity {

    /**
     * 企业员工主键id
     */
    @TableId(value = "user_org_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("企业员工主键id")
    private Long userOrgId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 对接外部主数据的用户id
     */
    @TableField("master_user_id")
    @ChineseDescription("对接外部主数据的用户id")
    private String masterUserId;

    /**
     * 所属机构id
     */
    @TableField("org_id")
    @ChineseDescription("所属机构id")
    private Long orgId;

    /**
     * 对接外部组织机构id
     */
    @TableField("master_org_id")
    @ChineseDescription("对接外部组织机构id")
    private String masterOrgId;

    /**
     * 职位id
     */
    @TableField("position_id")
    @ChineseDescription("职位id")
    private Long positionId;

    /**
     * 是否是主部门：Y-是，N-不是
     */
    @TableField("main_flag")
    @ChineseDescription("是否是主部门：Y-是，N-不是")
    private String mainFlag;

    /**
     * 是否启用：1-启用，2-禁用
     */
    @TableField("status_flag")
    @ChineseDescription("是否启用：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 拓展字段
     */
    @TableField(value = "expand_field", typeHandler = JacksonTypeHandler.class)
    @ChineseDescription("拓展字段")
    private Map<String, Object> expandField;

}
