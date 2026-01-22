package cn.stylefeng.roses.kernel.sys.modular.user.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import cn.stylefeng.roses.kernel.file.api.format.FileUrlFormatProcess;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.annotation.EnumFieldFormat;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import cn.stylefeng.roses.kernel.rule.enums.SexEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.util.sort.GetSortKey;
import cn.stylefeng.roses.kernel.sys.api.enums.user.UserStatusEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.response.SysUserCertificateResponse;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 系统用户实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@TableName(value = "sys_user", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseExpandFieldEntity implements GetSortKey {

    /**
     * 主键
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long userId;

    /**
     * 姓名
     */
    @TableField("real_name")
    @ChineseDescription("姓名")
    private String realName;

    /**
     * 昵称
     */
    @TableField("nick_name")
    @ChineseDescription("昵称")
    private String nickName;

    /**
     * 账号
     */
    @TableField("account")
    @ChineseDescription("账号")
    private String account;

    /**
     * 密码，加密方式：md5+盐
     */
    @TableField("password")
    @ChineseDescription("密码，加密方式：md5+盐")
    private String password;

    /**
     * 密码盐，加密方式：md5+盐
     */
    @TableField("password_salt")
    @ChineseDescription("密码盐，加密方式：md5+盐")
    private String passwordSalt;

    /**
     * 头像，存的为文件id
     */
    @TableField("avatar")
    @ChineseDescription("头像，存的为文件id")
    @SimpleFieldFormat(processClass = FileUrlFormatProcess.class)
    private Long avatar;

    /**
     * 生日
     */
    @TableField("birthday")
    @ChineseDescription("生日")
    private Date birthday;

    /**
     * 性别：M-男，F-女
     */
    @TableField("sex")
    @ChineseDescription("性别：M-男，F-女")
    @EnumFieldFormat(processEnum = SexEnum.class)
    private String sex;

    /**
     * 邮箱
     */
    @TableField("email")
    @ChineseDescription("邮箱")
    private String email;

    /**
     * 手机
     */
    @TableField("phone")
    @ChineseDescription("手机")
    private String phone;

    /**
     * 电话
     */
    @TableField("tel")
    @ChineseDescription("电话")
    private String tel;

    /**
     * 是否是超级管理员：Y-是，N-否
     */
    @TableField("super_admin_flag")
    @ChineseDescription("是否是超级管理员：Y-是，N-否")
    @EnumFieldFormat(processEnum = YesOrNotEnum.class)
    private String superAdminFlag;

    /**
     * 状态：1-正常，2-冻结，3-临时冻结
     */
    @TableField("status_flag")
    @ChineseDescription("状态：1-正常，2-冻结，3-临时冻结")
    @EnumFieldFormat(processEnum = UserStatusEnum.class)
    private Integer statusFlag;

    /**
     * 冻结截止时间
     */
    @TableField(value = "freeze_deadline_time", updateStrategy = FieldStrategy.IGNORED, insertStrategy = FieldStrategy.IGNORED)
    @ChineseDescription("冻结截止时间")
    private Date freezeDeadlineTime;

    /**
     * 登录次数
     */
    @TableField("login_count")
    @ChineseDescription("登录次数")
    private Integer loginCount;

    /**
     * 最后登陆IP
     */
    @TableField("last_login_ip")
    @ChineseDescription("最后登陆IP")
    private String lastLoginIp;

    /**
     * 最后登陆时间
     */
    @TableField("last_login_time")
    @ChineseDescription("最后登陆时间")
    private Date lastLoginTime;

    /**
     * 用户的排序
     */
    @TableField("user_sort")
    @ChineseDescription("用户的排序")
    private BigDecimal userSort;

    /**
     * 用户工号
     */
    @TableField("employee_number")
    @ChineseDescription("用户工号")
    private String employeeNumber;

    /**
     * 对接外部主数据的用户id
     */
    @TableField("master_user_id")
    @ChineseDescription("对接外部主数据的用户id")
    private String masterUserId;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    @ChineseDescription("租户id")
    private Long tenantId;

    /**
     * 是否隐藏：Y-隐藏，N-不隐藏，用在系统内置账号
     */
    @TableField(value = "hidden_flag")
    @ChineseDescription("是否隐藏：Y-隐藏，N-不隐藏，用在系统内置账号")
    private String hiddenFlag;

    //-------------------------------非实体字段-------------------------------
    //-------------------------------非实体字段-------------------------------
    //-------------------------------非实体字段-------------------------------

    /**
     * 用户组织机构详情【只返回主部门】
     * <p>
     * 用在用户分页列表的响应
     */
    @TableField(exist = false)
    @ChineseDescription("用户组织机构详情【只返回主部门】")
    private UserOrgDTO userOrgDTO;

    /**
     * 用户组织机构详情【所有部门都显示】
     * <p>
     * 用在获取用户详情信息的响应
     */
    @TableField(exist = false)
    @ChineseDescription("用户组织机构详情【所有部门都显示】")
    private List<UserOrgDTO> userOrgDTOList;

    /**
     * 获取用户角色id列表
     * <p>
     * 用在获取用户详情信息的响应
     */
    @TableField(exist = false)
    @ChineseDescription("获取用户角色id列表")
    private List<Long> roleIdList;

    /**
     * 用户证书列表
     * <p>
     * 用在获取用户详情信息的响应
     */
    @TableField(exist = false)
    @ChineseDescription("用户证书列表")
    private List<SysUserCertificateResponse> userCertificateList;

    @Override
    public Object getSortKey() {
        return userId;
    }

}
