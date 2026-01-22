package cn.stylefeng.roses.kernel.sys.modular.login.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.auth.api.SessionManagerApi;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.exception.enums.UserExceptionEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.OnlineUserItem;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.OnlineUserResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取用户在线登录信息业务
 *
 * @author fengshuonan
 * @since 2023/7/2 11:25
 */
@Service
public class OnlineUserService {

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private SysUserServiceApi sysUserServiceApi;

    /**
     * 获取当前在线用户列表
     * <p>
     * 可以根据用户名称和账号进行查询
     *
     * @author fengshuonan
     * @since 2023/7/2 12:09
     */
    public OnlineUserResult getOnlineUserList(String searchText) {

        OnlineUserResult onlineUserResult = new OnlineUserResult();

        List<LoginUser> loginUsers = sessionManagerApi.onlineUserList();
        if (ObjectUtil.isEmpty(loginUsers)) {
            return new OnlineUserResult();
        }

        // 返回总的在线人数
        onlineUserResult.setTotalUserCount(loginUsers.size());

        // 转化为用户的在线信息
        List<OnlineUserItem> onlineUserInfos = new ArrayList<>();
        for (LoginUser loginUser : loginUsers) {
            OnlineUserItem onlineUserInfo = new OnlineUserItem();
            onlineUserInfo.setUserId(loginUser.getUserId());
            onlineUserInfo.setToken(loginUser.getToken());
            onlineUserInfo.setLoginIp(loginUser.getLoginIp());
            onlineUserInfo.setLoginTime(loginUser.getLoginTime());
            onlineUserInfos.add(onlineUserInfo);
        }

        // 如果没传查询条件，只返回前10条
        if (StrUtil.isBlank(searchText)) {
            if (onlineUserInfos.size() > 10) {
                onlineUserInfos = onlineUserInfos.subList(0, 9);
            }

            // 用户信息补充姓名和账号返回
            for (OnlineUserItem onlineUserInfo : onlineUserInfos) {
                OnlineUserItem userNameAccountInfo = sysUserServiceApi.getUserNameAccountInfo(onlineUserInfo.getUserId());
                onlineUserInfo.setAccount(userNameAccountInfo.getAccount());
                onlineUserInfo.setRealName(userNameAccountInfo.getRealName());
            }

            onlineUserResult.setOnlineUserList(onlineUserInfos);
            return onlineUserResult;
        }

        // 如果传递了查询条件，则从在线用户id和指定查询条件中筛选出来结果
        else {
            List<OnlineUserItem> resultUserList = sysUserServiceApi.getUserNameAccountInfoListByCondition(onlineUserInfos, searchText);
            onlineUserResult.setOnlineUserList(resultUserList);
            return onlineUserResult;
        }
    }

    /**
     * 踢下线某个用户，根据参数中的token
     *
     * @author fengshuonan
     * @since 2023/7/2 12:23
     */
    public void offlineUser(OnlineUserItem onlineUserInfo) throws ServiceException {

        if (!LoginContext.me().getSuperAdminFlag()) {
            throw new ServiceException(UserExceptionEnum.KICK_OFF_ERROR);
        }

        sessionManagerApi.removeSession(onlineUserInfo.getToken());

    }

}
