package cn.stylefeng.roses.kernel.group.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.group.api.pojo.SysGroupDTO;
import cn.stylefeng.roses.kernel.group.modular.entity.SysGroup;
import cn.stylefeng.roses.kernel.group.modular.pojo.GroupAddRequest;
import cn.stylefeng.roses.kernel.group.modular.pojo.GroupDelRequest;
import cn.stylefeng.roses.kernel.group.modular.pojo.GroupSelectListRequest;
import cn.stylefeng.roses.kernel.group.modular.service.SysGroupService;
import cn.stylefeng.roses.kernel.group.modular.service.SysGroupV2Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务分组V2接口
 *
 * @author fengshuonan
 * @since 2025/10/27 10:57
 */
@Service
public class SysGroupServiceV2Impl implements SysGroupV2Service {

    @Resource
    private SysGroupService sysGroupService;

    @Override
    public List<SysGroupDTO> selectList(GroupSelectListRequest groupSelectListRequest) {

        // 定义返回结果
        List<SysGroupDTO> sysGroupDTOS = new ArrayList<>();

        // 组装查询条件封装
        LambdaQueryWrapper<SysGroup> sysGroupLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysGroupLambdaQueryWrapper.eq(SysGroup::getGroupBizCode, groupSelectListRequest.getGroupBizCode());
        sysGroupLambdaQueryWrapper.eq(SysGroup::getUserId, LoginContext.me().getLoginUser().getUserId());
        sysGroupLambdaQueryWrapper.select(SysGroup::getGroupName, SysGroup::getGroupId, SysGroup::getGroupBizCode, SysGroup::getBusinessId,
                SysGroup::getUserId);
        sysGroupLambdaQueryWrapper.groupBy(SysGroup::getGroupName);
        List<SysGroup> list = this.sysGroupService.list(sysGroupLambdaQueryWrapper);
        if (ObjectUtil.isNotEmpty(list)) {
            sysGroupDTOS = BeanUtil.copyToList(list, SysGroupDTO.class);
        }

        return sysGroupDTOS;
    }

    @Override
    public void addBindGroup(GroupAddRequest groupAddRequest) {
        ArrayList<SysGroup> sysGroups = new ArrayList<>();
        Long userId = LoginContext.me().getLoginUser().getUserId();
        List<Long> businessIdList = groupAddRequest.getBusinessIdList();

        // 移除当前用户在这个bizCode下已经绑定的分组
        LambdaUpdateWrapper<SysGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysGroup::getBusinessId, businessIdList);
        updateWrapper.eq(SysGroup::getGroupBizCode, groupAddRequest.getGroupBizCode());
        updateWrapper.eq(SysGroup::getUserId, userId);
        updateWrapper.in(SysGroup::getGroupName, groupAddRequest.getGroupNameList());
        this.sysGroupService.remove(updateWrapper);

        // 从新增加当前用户在这个bizCode下的所有分组
        for (String groupName : groupAddRequest.getGroupNameList()) {
            for (Long businessId : businessIdList) {
                SysGroup sysGroup = new SysGroup();
                sysGroup.setGroupName(groupName);
                sysGroup.setBusinessId(businessId);
                sysGroup.setUserId(userId);
                sysGroup.setGroupBizCode(groupAddRequest.getGroupBizCode());
                sysGroups.add(sysGroup);
            }
        }

        if (ObjectUtil.isNotEmpty(sysGroups)) {
            this.sysGroupService.saveBatch(sysGroups);
        }
    }

    @Override
    public void del(GroupDelRequest groupDelRequest) {

        Long userId = LoginContext.me().getLoginUser().getUserId();

        LambdaUpdateWrapper<SysGroup> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysGroup::getGroupBizCode, groupDelRequest.getGroupBizCode());
        wrapper.eq(SysGroup::getGroupName, groupDelRequest.getGroupName());
        wrapper.eq(SysGroup::getUserId, userId);
        wrapper.in(SysGroup::getBusinessId, groupDelRequest.getBusinessIdList());

        this.sysGroupService.remove(wrapper);
    }

}
