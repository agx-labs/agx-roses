package cn.stylefeng.roses.kernel.sys.modular.org.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.SimpleUserDTO;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrgApprover;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.ApproverBindUserItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织机构审批人创建工厂
 *
 * @author fengshuonan
 * @since 2023/6/11 15:35
 */
public class OrgApproverFactory {

    /**
     * Approver信息转化为用户详细信息
     *
     * @author fengshuonan
     * @since 2023/6/11 15:35
     */
    public static List<ApproverBindUserItem> convertUserItem(List<HrOrgApprover> userList) {

        // 获取所有用户id
        List<Long> userIdList = userList.stream().map(HrOrgApprover::getUserId).collect(Collectors.toList());

        if (ObjectUtil.isEmpty(userIdList)) {
            return new ArrayList<>();
        }

        List<ApproverBindUserItem> users = new ArrayList<>();
        for (Long userId : userIdList) {
            ApproverBindUserItem bindUserItem = new ApproverBindUserItem();
            bindUserItem.setUserId(userId);

            // 获取用户详情信息
            SysUserServiceApi sysUserServiceApi = SpringUtil.getBean(SysUserServiceApi.class);
            SimpleUserDTO sysUserDTO = sysUserServiceApi.getUserInfoByUserId(userId);

            bindUserItem.setName(sysUserDTO.getRealName());
            bindUserItem.setAvatarUrl(sysUserDTO.getAvatarUrl());

            users.add(bindUserItem);
        }

        return users;
    }

}
