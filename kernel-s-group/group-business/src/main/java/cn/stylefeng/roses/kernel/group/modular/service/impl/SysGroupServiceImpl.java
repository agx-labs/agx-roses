package cn.stylefeng.roses.kernel.group.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.group.api.callback.GroupNameCallbackApi;
import cn.stylefeng.roses.kernel.group.api.constants.GroupConstants;
import cn.stylefeng.roses.kernel.group.api.pojo.SysGroupDTO;
import cn.stylefeng.roses.kernel.group.api.pojo.SysGroupRequest;
import cn.stylefeng.roses.kernel.group.modular.entity.SysGroup;
import cn.stylefeng.roses.kernel.group.modular.mapper.SysGroupMapper;
import cn.stylefeng.roses.kernel.group.modular.service.SysGroupService;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务分组业务实现层
 *
 * @author fengshuonan
 * @since 2022/05/11 12:54
 */
@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements SysGroupService {

    @Override
    public List<SysGroupDTO> findGroupList(SysGroupRequest sysGroupRequest, boolean getTotal) {

        // 定义返回结果
        List<SysGroupDTO> sysGroupDTOS = new ArrayList<>();

        String groupBizCode = sysGroupRequest.getGroupBizCode();
        Long userId = LoginContext.me().getLoginUser().getUserId();

        LambdaQueryWrapper<SysGroup> wrapper = this.createWrapper(groupBizCode, userId, getTotal);
        List<SysGroup> list = this.list(wrapper);
        if (ObjectUtil.isNotEmpty(list)) {
            sysGroupDTOS = BeanUtil.copyToList(list, SysGroupDTO.class);
        }

        // 增加两个固定的选中和取消选项
        addAllGroup(groupBizCode, sysGroupDTOS);

        return sysGroupDTOS;
    }

    @Override
    public List<SysGroupDTO> addSelect(SysGroupRequest sysGroupRequest) {

        // 定义返回结果
        List<SysGroupDTO> sysGroupDTOS = new ArrayList<>();

        String groupBizCode = sysGroupRequest.getGroupBizCode();
        Long userId = LoginContext.me().getLoginUser().getUserId();

        LambdaQueryWrapper<SysGroup> wrapper = this.createWrapper(groupBizCode, userId, false);
        List<SysGroup> list = this.list(wrapper);
        if (ObjectUtil.isNotEmpty(list)) {
            sysGroupDTOS = BeanUtil.copyToList(list, SysGroupDTO.class);
        }

        // 增加两个固定的选中和取消选项
        addCommonGroup(groupBizCode, sysGroupDTOS);

        return sysGroupDTOS;
    }

    @Override
    public void add(SysGroupRequest sysGroupRequest) {
        ArrayList<SysGroup> sysGroups = new ArrayList<>();
        Long userId = LoginContext.me().getLoginUser().getUserId();
        List<Long> businessIdList = sysGroupRequest.getBusinessIdList();

        // 移除当前用户在这个bizCode下已经绑定的分组
        LambdaUpdateWrapper<SysGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysGroup::getGroupBizCode, sysGroupRequest.getGroupBizCode());
        updateWrapper.in(SysGroup::getBusinessId, businessIdList);
        updateWrapper.eq(SysGroup::getUserId, userId);
        this.remove(updateWrapper);

        // 如果分组名称是未分组，则删除分组
        if (!GroupConstants.GROUP_DELETE_NAME.equals(sysGroupRequest.getGroupName())) {
            for (Long bizId : businessIdList) {
                SysGroup sysGroup = new SysGroup();
                sysGroup.setGroupName(sysGroupRequest.getGroupName());
                sysGroup.setGroupBizCode(sysGroupRequest.getGroupBizCode());
                sysGroup.setBusinessId(bizId);
                sysGroup.setUserId(userId);
                sysGroups.add(sysGroup);
            }

            this.saveBatch(sysGroups);
        }
    }

    @Override
    public void del(SysGroupRequest sysGroupRequest) {

        Long userId = LoginContext.me().getLoginUser().getUserId();

        LambdaUpdateWrapper<SysGroup> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysGroup::getUserId, userId);
        wrapper.eq(SysGroup::getGroupBizCode, sysGroupRequest.getGroupBizCode());
        wrapper.in(SysGroup::getBusinessId, sysGroupRequest.getBusinessIdList());

        this.remove(wrapper);
    }

    @Override
    public List<Long> findUserGroupDataList(SysGroupRequest sysGroupRequest) {

        Long userId = LoginContext.me().getLoginUser().getUserId();

        LambdaQueryWrapper<SysGroup> sysGroupLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysGroupLambdaQueryWrapper.eq(SysGroup::getUserId, userId);
        sysGroupLambdaQueryWrapper.eq(SysGroup::getGroupBizCode, sysGroupRequest.getGroupBizCode());
        sysGroupLambdaQueryWrapper.eq(StrUtil.isNotBlank(sysGroupRequest.getGroupName()), SysGroup::getGroupName,
                sysGroupRequest.getGroupName());

        List<SysGroup> list = this.list(sysGroupLambdaQueryWrapper);
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        } else {
            return list.stream().map(SysGroup::getBusinessId).collect(Collectors.toList());
        }
    }

    @Override
    public void removeGroup(String bizCode, Long bizId) {
        Long userId = LoginContext.me().getLoginUser().getUserId();

        LambdaUpdateWrapper<SysGroup> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysGroup::getUserId, userId);
        wrapper.eq(SysGroup::getGroupBizCode, bizCode);
        wrapper.eq(SysGroup::getBusinessId, bizId);

        this.remove(wrapper);
    }

    @Override
    public Boolean getRequestUnGroupedFlag(BaseRequest baseRequest) {
        // 获取前端请求的分组名称
        String conditionGroupName = baseRequest.getConditionGroupName();
        return GroupConstants.GROUP_DELETE_NAME.equals(conditionGroupName);
    }

    @Override
    public List<Long> getUserGroupBizIds(String groupBizCode, BaseRequest baseRequest) {
        // 拼接分组相关的查询条件
        String conditionGroupName = baseRequest.getConditionGroupName();
        List<Long> userBizIds = new ArrayList<>();

        // 如果查询的是所有分组，就是查询所有的数据，不需要拼接条件
        if (ObjectUtil.isNotEmpty(conditionGroupName) && !conditionGroupName.equals(GroupConstants.ALL_GROUP_NAME)) {

            SysGroupRequest sysGroupRequest = new SysGroupRequest();
            sysGroupRequest.setGroupBizCode(groupBizCode);

            // 查詢的不是未分组，则需要拼接一个groupName
            if (!getRequestUnGroupedFlag(baseRequest)) {
                sysGroupRequest.setGroupName(conditionGroupName);
            }

            // 获取到筛选条件的结果
            userBizIds = this.findUserGroupDataList(sysGroupRequest);
        }

        return userBizIds;
    }

    @Override
    public void renderBizListGroupName(String groupBizCode, List<? extends GroupNameCallbackApi> businessList) {

        if (ObjectUtil.isEmpty(businessList)) {
            return;
        }

        // 查询结果中有没有用户挂标签的，有的话就返回中文分组名称
        SysGroupRequest sysGroupRequest = new SysGroupRequest();
        sysGroupRequest.setGroupBizCode(groupBizCode);
        List<SysGroupDTO> list = this.findGroupList(sysGroupRequest, true);

        // 增加返回结果的分组名称
        for (SysGroupDTO sysGroupDTO : list) {
            for (GroupNameCallbackApi item : businessList) {
                if (item.getRenderBusinessId().equals(sysGroupDTO.getBusinessId())) {
                    item.renderGroupName(sysGroupDTO.getGroupName());
                }
            }
        }
    }

    /**
     * 返回结果增加通用的两个分组名称
     *
     * @author fengshuonan
     * @since 2022/6/28 10:50
     */
    private void addCommonGroup(String groupBizCode, List<SysGroupDTO> result) {

        // 添加分组
        SysGroupDTO addGroup = new SysGroupDTO();
        addGroup.setGroupBizCode(groupBizCode);
        addGroup.setGroupName(GroupConstants.GROUP_ADD_NAME);

        // 未分组
        SysGroupDTO noneGroup = new SysGroupDTO();
        noneGroup.setGroupBizCode(groupBizCode);
        noneGroup.setGroupName(GroupConstants.GROUP_DELETE_NAME);

        result.add(0, noneGroup);
        result.add(0, addGroup);
    }

    /**
     * 返回所有分组和未分组的查询
     *
     * @author fengshuonan
     * @since 2022/6/28 10:50
     */
    private void addAllGroup(String groupBizCode, List<SysGroupDTO> result) {

        // 添加分组
        SysGroupDTO addGroup = new SysGroupDTO();
        addGroup.setGroupBizCode(groupBizCode);
        addGroup.setGroupName(GroupConstants.ALL_GROUP_NAME);

        // 未分组
        SysGroupDTO noneGroup = new SysGroupDTO();
        noneGroup.setGroupBizCode(groupBizCode);
        noneGroup.setGroupName(GroupConstants.GROUP_DELETE_NAME);

        result.add(0, noneGroup);
        result.add(0, addGroup);
    }

    /**
     * 创建通用wrapper
     *
     * @author fengshuonan
     * @since 2023/7/11 16:23
     */
    private LambdaQueryWrapper<SysGroup> createWrapper(String groupBizCode, Long userId, Boolean getTotal) {

        LambdaQueryWrapper<SysGroup> sysGroupLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysGroupLambdaQueryWrapper.eq(SysGroup::getGroupBizCode, groupBizCode);
        sysGroupLambdaQueryWrapper.eq(SysGroup::getUserId, userId);

        if (getTotal != null && !getTotal) {
            sysGroupLambdaQueryWrapper.groupBy(SysGroup::getGroupName);
        }

        sysGroupLambdaQueryWrapper.select(SysGroup::getGroupName, SysGroup::getGroupId, SysGroup::getGroupBizCode, SysGroup::getBusinessId,
                SysGroup::getUserId);

        return sysGroupLambdaQueryWrapper;
    }

}
