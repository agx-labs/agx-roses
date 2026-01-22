package cn.stylefeng.roses.kernel.sys.modular.login.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.OnlineUserItem;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.OnlineUserResult;
import cn.stylefeng.roses.kernel.sys.modular.login.service.OnlineUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取用户在线信息的接口
 *
 * @author fengshuonan
 * @since 2023/7/2 11:25
 */
@RestController
@Slf4j
@ApiResource(name = "获取用户在线信息的接口", requiredPermission = true, requirePermissionCode = PermissionCodeConstants.ONLINE_USER)
public class OnlineUserController {

    @Resource
    private OnlineUserService onlineUserService;

    /**
     * 获取在线用户列表
     *
     * @param searchText 查询条件，可以根据用户姓名和账号进行查询
     * @author fengshuonan
     * @since 2023/7/2 11:26
     */
    @GetResource(name = "获取在线用户列表", path = "/getOnlineUserList")
    public ResponseData<OnlineUserResult> getOnlineUserList(@RequestParam(value = "searchText", required = false) String searchText) {
        OnlineUserResult result = onlineUserService.getOnlineUserList(searchText);
        return new SuccessResponseData<>(result);
    }

    /**
     * 踢掉在线的某个人token
     *
     * @param onlineUserInfo 请求参数
     * @author fengshuonan
     * @since 2023/7/2 11:26
     */
    @PostResource(name = "踢下线某个用户", path = "/offlineUser")
    public ResponseData<?> offlineUser(@RequestBody @Validated(OnlineUserItem.offlineUser.class) OnlineUserItem onlineUserInfo) {
        onlineUserService.offlineUser(onlineUserInfo);
        return new SuccessResponseData<>();
    }

}
