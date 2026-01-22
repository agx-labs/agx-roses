package cn.stylefeng.roses.kernel.sys.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserCertificate;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.validator.api.validators.unique.TableUniqueValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 系统用户封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class, detail.class, updateStatus.class, resetPassword.class})
    @ChineseDescription("主键")
    private Long userId;

    /**
     * 姓名
     */
    @ChineseDescription("姓名")
    @NotBlank(message = "姓名不能为空", groups = {add.class, edit.class, updateInfo.class})
    private String realName;

    /**
     * 昵称
     */
    @ChineseDescription("昵称")
    private String nickName;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = {add.class, edit.class})
    @ChineseDescription("账号")
    @TableUniqueValue(message = "用户账号存在重复", groups = {add.class, edit.class},
            tableName = "sys_user",
            columnName = "account",
            idFieldName = "user_id",
            excludeLogicDeleteItems = true)
    private String account;

    /**
     * 密码，加密方式为BCrypt
     */
    @NotBlank(message = "密码，加密方式为BCrypt不能为空", groups = {add.class, updatePwd.class})
    @ChineseDescription("密码，加密方式为BCrypt")
    private String password;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空", groups = {updatePwd.class})
    @ChineseDescription("新密码")
    private String newPassword;

    /**
     * 头像，存的为文件id
     */
    @NotNull(message = "头像不能为空", groups = {updateAvatar.class})
    @ChineseDescription("头像，存的为文件id")
    private Long avatar;

    /**
     * 生日
     */
    @ChineseDescription("生日")
    private String birthday;

    /**
     * 性别：M-男，F-女
     */
    @NotBlank(message = "性别：M-男，F-女不能为空", groups = {add.class, edit.class, updateInfo.class})
    @ChineseDescription("性别：M-男，F-女")
    private String sex;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误", groups = {updateInfo.class})
    @ChineseDescription("邮箱")
    private String email;

    /**
     * 手机
     */
    @ChineseDescription("手机")
    private String phone;

    /**
     * 电话
     */
    @ChineseDescription("电话")
    private String tel;

    /**
     * 是否是超级管理员：Y-是，N-否
     */
    @NotBlank(message = "是否是超级管理员：Y-是，N-否不能为空", groups = {add.class, edit.class})
    @ChineseDescription("是否是超级管理员：Y-是，N-否")
    private String superAdminFlag;

    /**
     * 状态：1-正常，2-冻结，3-临时冻结
     */
    @ChineseDescription("状态：1-正常，2-冻结，3-临时冻结")
    @NotNull(message = "状态不能为空", groups = {add.class, edit.class, updateStatus.class})
    private Integer statusFlag;

    /**
     * 用户的排序
     */
    @ChineseDescription("用户的排序")
    private BigDecimal userSort = new BigDecimal(1000);

    /**
     * 用户工号
     */
    @ChineseDescription("用户工号")
    private String employeeNumber;

    /**
     * 对接外部主数据的用户id
     */
    @ChineseDescription("对接外部主数据的用户id")
    private String masterUserId;

    //-------------------------------非实体字段-------------------------------
    //-------------------------------非实体字段-------------------------------
    //-------------------------------非实体字段-------------------------------

    /**
     * 组织机构id查询条件
     */
    @ChineseDescription("组织机构id查询条件")
    private Long orgIdCondition;

    /**
     * 用户和组织机构关系集合
     */
    @ChineseDescription("用户和组织机构关系集合")
    @NotEmpty(message = "用户和组织机构关系集合不能为空", groups = {add.class, edit.class})
    private List<SysUserOrg> userOrgList;

    /**
     * 用户证书信息集合
     */
    @ChineseDescription("用户证书信息集合")
    private List<SysUserCertificate> userCertificateList;

    /**
     * 用户id集合，用在批量删除用户的参数
     */
    @ChineseDescription("用户id集合，用在批量删除用户的参数")
    @NotEmpty(message = "用户id集合不能为空", groups = batchDelete.class)
    private Set<Long> userIdList;

    /**
     * 参数校验分组：重置用户密码
     */
    public @interface resetPassword {
    }

    /**
     * 参数校验分组：更新信息
     */
    public @interface updateInfo {
    }

    /**
     * 参数校验分组：修改密码
     */
    public @interface updatePwd {
    }

    /**
     * 参数校验分组：修改头像
     */
    public @interface updateAvatar {
    }
}
