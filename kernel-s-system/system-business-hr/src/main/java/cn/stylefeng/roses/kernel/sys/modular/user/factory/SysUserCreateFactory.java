package cn.stylefeng.roses.kernel.sys.modular.user.factory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.file.api.constants.FileConstants;
import cn.stylefeng.roses.kernel.rule.enums.SexEnum;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.AddUserDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * 用户信息填充，用于创建和修改用户时，添加一些基础信息
 *
 * @author fengshuonan
 * @date 2020/11/21 12:55
 */
public class SysUserCreateFactory {

    /**
     * 编辑用户时候的用户信息填充
     *
     * @author fengshuonan
     * @date 2020/11/21 12:56
     */
    public static void fillUpdateInfo(SysUserRequest sysUserRequest, SysUser sysUser) {

        // 姓名
        sysUser.setRealName(sysUserRequest.getRealName());

        // 性别（M-男，F-女）
        sysUser.setSex(sysUserRequest.getSex());

        // 邮箱
        sysUser.setEmail(sysUserRequest.getEmail());

        // 生日
        if (ObjectUtil.isNotEmpty(sysUserRequest.getBirthday())) {
            sysUser.setBirthday(DateUtil.parse(sysUserRequest.getBirthday()));
        }

        // 手机
        if (ObjectUtil.isNotEmpty(sysUserRequest.getPhone())) {
            sysUser.setPhone(sysUserRequest.getPhone());
        }

        // 头像id
        if (ObjectUtil.isNotEmpty(sysUserRequest.getAvatar())) {
            sysUser.setAvatar(sysUserRequest.getAvatar());
        }
    }

    /**
     * 通过添加用户的dto创建用户
     *
     * @author fengshuonan
     * @since 2024/5/23 23:51
     */
    public static SysUser createSimpleAddUser(AddUserDTO addUserDTO) {
        SysUser sysUser = new SysUser();

        // 设置主键
        sysUser.setUserId(IdWorker.getId());

        // 设置账号
        sysUser.setAccount(addUserDTO.getAccount());

        // 设置昵称
        sysUser.setNickName(addUserDTO.getNickName());

        // 设置邮箱
        sysUser.setEmail(addUserDTO.getEmail());

        // 设置密码
        sysUser.setPassword(addUserDTO.getPassword());

        // 设置密码盐
        sysUser.setPasswordSalt(addUserDTO.getPasswordSalt());

        // 设置性别
        sysUser.setSex(SexEnum.M.getCode());

        // 设置默认非管理员
        sysUser.setSuperAdminFlag(YesOrNotEnum.N.getCode());

        // 设置状态开启
        sysUser.setStatusFlag(StatusEnum.ENABLE.getCode());

        // 设置用户默认头像
        sysUser.setAvatar(FileConstants.DEFAULT_AVATAR_FILE_ID);

        // 设置真是姓名为昵称一样
        sysUser.setRealName(addUserDTO.getNickName());

        return sysUser;
    }

    /**
     * 创建一个用户的所属机构，假设用户只有一个机构信息
     *
     * @author fengshuonan
     * @since 2024/5/24 0:02
     */
    public static SysUserOrg createOneUserOrg(Long userId, Long orgId, Long positionId) {
        SysUserOrg sysUserOrg = new SysUserOrg();
        sysUserOrg.setUserId(userId);
        sysUserOrg.setOrgId(orgId);
        sysUserOrg.setPositionId(positionId);
        sysUserOrg.setMainFlag(YesOrNotEnum.Y.getCode());
        sysUserOrg.setStatusFlag(StatusEnum.ENABLE.getCode());
        return sysUserOrg;
    }

}
