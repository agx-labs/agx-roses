package cn.stylefeng.roses.kernel.sys.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.SimpleUserDTO;

import java.util.HashMap;

/**
 * 针对用户详情的渲染，增加对用户头像的支持
 *
 * @author fengshuonan
 * @since 2023/4/24 23:53
 */
public class UserAvatarFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {

        if (ObjectUtil.isEmpty(businessId)) {
            return null;
        }

        Long userId = Convert.toLong(businessId);

        SysUserServiceApi userServiceApi = SpringUtil.getBean(SysUserServiceApi.class);
        SimpleUserDTO userInfo = userServiceApi.getUserInfoByUserId(userId);

        // 获取用户姓名
        String realName = userInfo.getRealName();

        // 获取用户头像
        String avatarUrl = userInfo.getAvatarUrl();

        HashMap<String, Object> userInfoResult = new HashMap<>();
        userInfoResult.put("realName", realName);
        userInfoResult.put("avatarUrl", avatarUrl);

        return userInfoResult;
    }

}
