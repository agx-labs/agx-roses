package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.enums.role.RoleTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindItem;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindResponse;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.UserRoleDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.DeleteRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.RoleControlRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.StatusControlRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.SyncBindRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserOrgExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.factory.RoleAssignFactory;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserOrgRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysRoleAssignService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新的用户授权界面业务
 *
 * @author fengshuonan
 * @since 2024/1/17 23:08
 */
@Service
public class SysRoleAssignServiceImpl implements SysRoleAssignService {

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource
    private SysRoleServiceApi sysRoleServiceApi;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource(name = "userRoleCache")
    private CacheOperatorApi<List<SysUserRole>> userRoleCache;

    @Override
    public List<NewUserRoleBindResponse> getUserAssignList(Long userId) {

        // 1. 获取用户的所有绑定的组织机构信息
        List<UserOrgDTO> userOrgList = sysUserOrgService.getUserOrgList(userId, true);

        // 用户没绑定机构，直接返回
        if (ObjectUtil.isEmpty(userOrgList)) {
            return new ArrayList<>();
        }

        // 2. 组装NewUserRoleBindResponse
        List<NewUserRoleBindResponse> baseResponse = RoleAssignFactory.createBaseResponse(userOrgList);

        // 3. 获取系统中，所有的业务角色（type=15），以及所有的公司角色（type=20）
        List<SysRoleDTO> businessRoleAndCompanyRole = sysRoleServiceApi.getBusinessRoleAndCompanyRole(userOrgList.stream().map(UserOrgDTO::getCompanyId).collect(Collectors.toList()));

        // 4. 组装每个机构下的角色信息下拉列表
        RoleAssignFactory.fillRoleSelectList(baseResponse, businessRoleAndCompanyRole);

        // 5. 获取用户已经绑定的关联机构的角色（为了赋值选中状态）
        List<UserRoleDTO> userLinkedOrgRoleList = sysUserRoleService.getUserLinkedOrgRoleList(userId);

        // 6. 填充绑定关系，返回结果
        return RoleAssignFactory.fillRoleBind(baseResponse, userLinkedOrgRoleList);
    }

    @Override
    public void changeUserBindStatus(StatusControlRequest statusControlRequest) {

        // 1. 获取用户所属机构的信息
        SysUserOrg userOrgInfo = sysUserOrgService.getUserOrgInfo(statusControlRequest.getUserId(), statusControlRequest.getOrgId());
        if (userOrgInfo == null) {
            throw new ServiceException(SysUserOrgExceptionEnum.SYS_USER_ORG_NOT_EXISTED);
        }

        // 2. 修改用户所属机构的状态
        if (statusControlRequest.getCheckedFlag()) {
            userOrgInfo.setStatusFlag(StatusEnum.ENABLE.getCode());
        } else {
            userOrgInfo.setStatusFlag(StatusEnum.DISABLE.getCode());
        }

        this.sysUserOrgService.updateById(userOrgInfo);
    }

    @Override
    public void changeRoleSelect(RoleControlRequest roleControlRequest) {

        // 1. 获取用户是否已经绑定了这个机构下的这个角色
        SysUserRole pointUserRole = sysUserRoleService.getPointUserRole(roleControlRequest.getUserId(), roleControlRequest.getRoleId(), roleControlRequest.getOrgId());

        // 针对已经绑定的不需要再次绑定
        if (pointUserRole != null && roleControlRequest.getCheckedFlag()) {
            return;
        }

        // 未绑定过，但是需要绑定，则新增一条记录
        else if (pointUserRole == null && roleControlRequest.getCheckedFlag()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(roleControlRequest.getUserId());
            sysUserRole.setRoleId(roleControlRequest.getRoleId());
            sysUserRole.setRoleType(roleControlRequest.getRoleType());
            sysUserRole.setRoleOrgId(roleControlRequest.getOrgId());
            sysUserRoleService.save(sysUserRole);
        }

        // 不需要绑定，则删除这条记录
        else if (pointUserRole != null) {
            sysUserRoleService.removeById(pointUserRole.getUserRoleId());
        }

        // 清空用户绑定的缓存
        userRoleCache.remove(String.valueOf(roleControlRequest.getUserId()));

        // 记录业务日志
        BusinessLogUtil.setLogTitle("绑定公司或业务角色");
        BusinessLogUtil.addContent("用户id：", roleControlRequest.getUserId(),
                "，角色id：", roleControlRequest.getRoleId(),
                "，角色类型：", roleControlRequest.getRoleType(),
                "，机构id：", roleControlRequest.getOrgId(),
                "，绑定状态：", roleControlRequest.getCheckedFlag());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserOrgBind(DeleteRequest deleteRequest) {

        // 1. 删除用户机构关联信息
        this.sysUserOrgService.removeUserOrgLink(deleteRequest.getUserId(), deleteRequest.getOrgId());

        // 2. 删除用户在机构下的所有角色信息
        this.sysUserRoleService.removeUserOrgRoleLink(deleteRequest.getUserId(), deleteRequest.getOrgId());

        // 3. 清空缓存
        userRoleCache.remove(String.valueOf(deleteRequest.getUserId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearAllOrgAndRoleBind(SysUserOrgRequest sysUserOrgRequest) {

        // 1. 先清空用户的机构绑定
        sysUserOrgService.deleteAllOrgBind(sysUserOrgRequest);

        // 2. 再清空用户的所有的带机构的角色绑定
        sysUserRoleService.deleteUserAllOrgBind(sysUserOrgRequest.getUserId());
    }

    @Override
    public void disableAllOrgStatus(SysUserOrgRequest sysUserOrgRequest) {
        sysUserOrgService.disableAllOrgStatus(sysUserOrgRequest.getUserId());
    }

    @Override
    public void syncOtherOrgStatusAndBusinessRole(SyncBindRequest syncBindRequest) {

        // 1. 获取当前指定公司的同步状态
        Boolean statusFlag = syncBindRequest.getStatusFlag();

        // 2. 更新用户所属其他组织机构的状态
        sysUserOrgService.updateOtherOrgStatus(syncBindRequest.getUserId(), syncBindRequest.getOrgId(), statusFlag ? StatusEnum.ENABLE.getCode() : StatusEnum.DISABLE.getCode());

        // 3. 筛选出当前公司的业务角色
        List<NewUserRoleBindItem> currentOrgBindRoleList = syncBindRequest.getRoleBindItemList().stream().filter(i -> i.getRoleType().equals(RoleTypeEnum.BUSINESS_ROLE.getCode()))
                .filter(NewUserRoleBindItem::getCheckedFlag).collect(Collectors.toList());

        // 4. 更新用户所属其他组织机构的业务角色
        sysUserRoleService.updateOtherOrgBusinessRole(syncBindRequest.getUserId(), syncBindRequest.getOrgId(), currentOrgBindRoleList);
    }

}