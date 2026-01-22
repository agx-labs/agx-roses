package cn.stylefeng.roses.kernel.sys.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.annotation.ApiLog;
import cn.stylefeng.roses.kernel.rule.annotation.BizLog;
import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.SysBizLogConstants;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.response.PersonalInfo;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人信息控制器
 *
 * @author fengshuonan
 * @date 2021/3/17 22:05
 */
@RestController
@ApiResource(name = "个人信息", resBizType = ResBizTypeEnum.SYSTEM)
public class PersonalInfoController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 获取个人信息详情
     *
     * @author fengshuonan
     * @since 2023/6/26 22:27
     */
    @GetResource(name = "获取个人信息详情", path = "/personalInfo/getUserInfo")
    public ResponseData<PersonalInfo> getUserInfo() {
        PersonalInfo personalInfo = sysUserService.getPersonalInfo();
        return new SuccessResponseData<>(personalInfo);
    }

    /**
     * 更新个人信息
     *
     * @author fengshuonan
     * @since 2023/6/26 22:24
     */
    @PostResource(name = "更新个人信息", path = "/personalInfo/updateInfo")
    @ApiLog
    public ResponseData<?> updateInfo(@RequestBody @Validated(SysUserRequest.updateInfo.class) SysUserRequest sysUserRequest) {
        sysUserService.editInfo(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改个人头像
     *
     * @author fengshuonan
     * @since 2023/6/26 22:24
     */
    @PostResource(name = "修改个人头像", path = "/personalInfo/updateAvatar")
    @ApiLog
    public ResponseData<?> updateAvatar(@RequestBody @Validated(SysUserRequest.updateAvatar.class) SysUserRequest sysUserRequest) {
        sysUserService.editAvatar(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改个人密码
     *
     * @author fengshuonan
     * @since 2023/6/26 22:24
     */
    @PostResource(name = "修改个人密码", path = "/personalInfo/updatePassword")
    @BizLog(logTypeCode = SysBizLogConstants.LOG_UPDATE_USER_PASSWORD)
    public ResponseData<?> updatePwd(@RequestBody @Validated(SysUserRequest.updatePwd.class) SysUserRequest sysUserRequest) {
        sysUserService.editPassword(sysUserRequest);
        return new SuccessResponseData<>();
    }

}
