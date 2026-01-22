package cn.stylefeng.roses.kernel.sys.modular.org.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.dict.api.DictApi;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysUserOrgServiceApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveOrgCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.ApproverConstants;
import cn.stylefeng.roses.kernel.sys.api.enums.org.DetectModeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrgApprover;
import cn.stylefeng.roses.kernel.sys.modular.org.enums.HrOrgApproverExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.org.factory.OrgApproverFactory;
import cn.stylefeng.roses.kernel.sys.modular.org.mapper.HrOrgApproverMapper;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrgApproverRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.ApproverBindUserItem;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrgApproverService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织机构审批人业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@Service
public class HrOrgApproverServiceImpl extends ServiceImpl<HrOrgApproverMapper, HrOrgApprover> implements HrOrgApproverService,
        RemoveOrgCallbackApi {

    @Resource
    private DictApi dictApi;

    @Resource
    private OrganizationServiceApi organizationServiceApi;

    @Resource
    private SysUserOrgServiceApi sysUserOrgServiceApi;

    @Override
    public void del(HrOrgApproverRequest hrOrgApproverRequest) {
        LambdaQueryWrapper<HrOrgApprover> wrapper = this.createWrapper(hrOrgApproverRequest);
        this.remove(wrapper);
    }

    @Override
    public List<SimpleDict> getApproverTypeList() {
        return dictApi.getDictDetailsByDictTypeCode(ApproverConstants.APPROVER_TYPE_DICT_TYPE_CODE, null);
    }

    @Override
    public List<HrOrgApprover> getOrgApproverList(HrOrgApproverRequest hrOrgApproverRequest) {

        // 获取当前系统的审批人类型列表
        List<SimpleDict> approverTypeList = this.getApproverTypeList();

        // 获取指定机构的绑定情况
        LambdaQueryWrapper<HrOrgApprover> hrOrgApproverLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrgApproverLambdaQueryWrapper.eq(HrOrgApprover::getOrgId, hrOrgApproverRequest.getOrgId());
        List<HrOrgApprover> orgTotalBindingList = this.list(hrOrgApproverLambdaQueryWrapper);

        // 将每个类型的用户分组，key是审批组类型，value是该组下的用户
        Map<Integer, List<HrOrgApprover>> groupingByUsers = new HashMap<>();
        if (ObjectUtil.isNotEmpty(orgTotalBindingList)) {
            groupingByUsers = orgTotalBindingList.stream().collect(Collectors.groupingBy(HrOrgApprover::getOrgApproverType));
        }


        // 先初始化空的绑定情况列表
        ArrayList<HrOrgApprover> resultList = new ArrayList<>();
        for (SimpleDict orgApproverType : approverTypeList) {
            HrOrgApprover hrOrgApprover = new HrOrgApprover();

            // 设置类型
            hrOrgApprover.setOrgApproverType(Convert.toInt(orgApproverType.getCode()));

            if (ObjectUtil.isNotEmpty(orgTotalBindingList)) {
                // 设置该类型下的审批人列表
                List<HrOrgApprover> userList = groupingByUsers.get(hrOrgApprover.getOrgApproverType());
                if (ObjectUtil.isNotEmpty(userList)) {
                    List<ApproverBindUserItem> bindUserItems = OrgApproverFactory.convertUserItem(userList);
                    hrOrgApprover.setBindUserItemList(bindUserItems);
                }
            }

            // 如果没设置上审批人列表，返回空数组
            if (ObjectUtil.isEmpty(hrOrgApprover.getBindUserItemList())) {
                hrOrgApprover.setBindUserItemList(new ArrayList<>());
            }

            resultList.add(hrOrgApprover);
        }

        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindUserList(HrOrgApproverRequest hrOrgApproverRequest) {

        // 获取改组织下，该种类型已经绑定的人
        LambdaQueryWrapper<HrOrgApprover> wrapper = this.createWrapper(hrOrgApproverRequest);
        List<HrOrgApprover> alreadyBindUsers = this.list(wrapper);

        // 如果已经绑定的人是空的，则直接绑定
        if (ObjectUtil.isEmpty(alreadyBindUsers)) {
            ArrayList<HrOrgApprover> tempApproverList = new ArrayList<>();
            for (Long userId : hrOrgApproverRequest.getUserIdList()) {
                HrOrgApprover hrOrgApprover = new HrOrgApprover();
                hrOrgApprover.setOrgId(hrOrgApproverRequest.getOrgId());
                hrOrgApprover.setOrgApproverType(hrOrgApproverRequest.getOrgApproverType());
                hrOrgApprover.setUserId(userId);
                tempApproverList.add(hrOrgApprover);
            }
            this.saveBatch(tempApproverList);
            return;
        }

        // 如果有已经绑定的人，则需要判断请求参数中的人是否已经包含在内，包含在内则不用从新绑定
        List<Long> alreadyBindUserIdList = alreadyBindUsers.stream().map(HrOrgApprover::getUserId).collect(Collectors.toList());
        ArrayList<HrOrgApprover> tempApprovers = new ArrayList<>();
        for (Long needToBindUserId : hrOrgApproverRequest.getUserIdList()) {
            boolean needToAdd = true;
            for (Long tempUserId : alreadyBindUserIdList) {
                if (tempUserId.equals(needToBindUserId)) {
                    needToAdd = false;
                    break;
                }
            }
            if (needToAdd) {
                HrOrgApprover hrOrgApprover = new HrOrgApprover();
                hrOrgApprover.setOrgId(hrOrgApproverRequest.getOrgId());
                hrOrgApprover.setOrgApproverType(hrOrgApproverRequest.getOrgApproverType());
                hrOrgApprover.setUserId(needToBindUserId);
                tempApprovers.add(hrOrgApprover);
            }
            this.saveBatch(tempApprovers);
        }
    }

    @Override
    public List<Long> getUserMainOrgApprover(Long userId, Integer orgApproverType, Integer parentLevel, DetectModeEnum detectModeEnum) {

        // 获取用户的所属机构id
        UserOrgDTO userMainOrgInfo = sysUserOrgServiceApi.getUserMainOrgInfo(userId);
        Long orgId = userMainOrgInfo.getDeptId();

        if (ObjectUtil.isEmpty(orgId)) {
            orgId = userMainOrgInfo.getCompanyId();
        }

        // 用户没有机构，则返回空
        if (ObjectUtil.isEmpty(orgId)) {
            return new ArrayList<>();
        }

        // 获取指定部门的负责人id信息
        return getDeptOrgApprover(orgId, orgApproverType, parentLevel, detectModeEnum);
    }

    @Override
    public List<Long> getDeptOrgApprover(Long deptId, Integer orgApproverType, Integer parentLevel, DetectModeEnum detectModeEnum) {

        // 根据参数的级别，获取组织机构的父级机构
        Long parentLevelOrgId = this.organizationServiceApi.getParentLevelOrgId(deptId, parentLevel, detectModeEnum);

        // 如果没有上级组织机构，则直接返回为空
        if (ObjectUtil.isEmpty(parentLevelOrgId)) {
            return new ArrayList<>();
        }

        // 获取该组织机构的指定类型的负责人
        LambdaQueryWrapper<HrOrgApprover> hrOrgApproverLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrgApproverLambdaQueryWrapper.eq(HrOrgApprover::getOrgId, parentLevelOrgId);
        hrOrgApproverLambdaQueryWrapper.eq(HrOrgApprover::getOrgApproverType, orgApproverType);
        List<HrOrgApprover> userList = this.list(hrOrgApproverLambdaQueryWrapper);

        return userList.stream().map(HrOrgApprover::getUserId).collect(Collectors.toList());
    }

    @Override
    public void validateHaveOrgBind(Set<Long> beRemovedOrgIdList) {
        // none
    }

    @Override
    public void removeOrgAction(Set<Long> beRemovedOrgIdList) {
        LambdaUpdateWrapper<HrOrgApprover> hrOrgApproverLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        hrOrgApproverLambdaUpdateWrapper.in(HrOrgApprover::getOrgId, beRemovedOrgIdList);
        this.remove(hrOrgApproverLambdaUpdateWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    private HrOrgApprover queryHrOrgApprover(HrOrgApproverRequest hrOrgApproverRequest) {
        HrOrgApprover hrOrgApprover = this.getById(hrOrgApproverRequest.getOrgApproverId());
        if (ObjectUtil.isEmpty(hrOrgApprover)) {
            throw new ServiceException(HrOrgApproverExceptionEnum.HR_ORG_APPROVER_NOT_EXISTED);
        }
        return hrOrgApprover;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    private LambdaQueryWrapper<HrOrgApprover> createWrapper(HrOrgApproverRequest hrOrgApproverRequest) {
        LambdaQueryWrapper<HrOrgApprover> queryWrapper = new LambdaQueryWrapper<>();

        // 根据审批人类型查询
        Integer orgApproverType = hrOrgApproverRequest.getOrgApproverType();
        queryWrapper.eq(ObjectUtil.isNotNull(orgApproverType), HrOrgApprover::getOrgApproverType, orgApproverType);

        // 根据组织机构id查询
        Long orgId = hrOrgApproverRequest.getOrgId();
        queryWrapper.eq(ObjectUtil.isNotNull(orgId), HrOrgApprover::getOrgId, orgId);

        // 根据用户id查询
        Long userId = hrOrgApproverRequest.getUserId();
        queryWrapper.eq(ObjectUtil.isNotNull(userId), HrOrgApprover::getUserId, userId);

        return queryWrapper;
    }

}