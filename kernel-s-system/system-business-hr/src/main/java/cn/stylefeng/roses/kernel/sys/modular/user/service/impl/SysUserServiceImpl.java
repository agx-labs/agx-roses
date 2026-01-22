package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.TenantCodeGetApi;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.password.PasswordStoredEncryptApi;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.auth.api.pojo.password.SaltedEncryptResult;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.db.mp.tenant.context.TenantSwitchContext;
import cn.stylefeng.roses.kernel.db.mp.tenant.holder.TenantSwitchHolder;
import cn.stylefeng.roses.kernel.dsctn.api.context.DataSourceContext;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.file.api.FileInfoApi;
import cn.stylefeng.roses.kernel.file.api.constants.FileConstants;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.util.SortUtils;
import cn.stylefeng.roses.kernel.sys.api.SecurityConfigService;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.enums.user.UserStatusEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.*;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.factory.SysUserCreateFactory;
import cn.stylefeng.roses.kernel.sys.modular.user.factory.UserDataScopeFactory;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.response.PersonalInfo;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.response.SysUserCertificateResponse;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserCertificateService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.stylefeng.roses.kernel.sys.modular.user.constants.UserConstants.UPDATE_USER_INFO_EVENT;

/**
 * 系统用户业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private PasswordStoredEncryptApi passwordStoredEncryptApi;

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private FileInfoApi fileInfoApi;

    @Resource
    private TenantCodeGetApi tenantCodeGetApi;

    @Resource(name = "loginErrorCountCacheApi")
    private CacheOperatorApi<Integer> loginErrorCountCacheApi;

    @Resource
    private SecurityConfigService securityConfigService;

    @Resource
    private SysUserCertificateService sysUserCertificateService;

    @Resource(name = "userInfoCache")
    private CacheOperatorApi<UserInfoDetailDTO> userInfoCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserRequest sysUserRequest) {
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(sysUserRequest, sysUser);

        // 校验密码是否符合规则
        String password = sysUserRequest.getPassword().trim();
        securityConfigService.validatePasswordSecurityRule(false, password);

        // 将密码加密存储到库中
        SaltedEncryptResult saltedEncryptResult = passwordStoredEncryptApi.encryptWithSalt(password);
        sysUser.setPassword(saltedEncryptResult.getEncryptPassword());
        sysUser.setPasswordSalt(saltedEncryptResult.getPasswordSalt());

        // 设置用户默认头像
        sysUser.setAvatar(FileConstants.DEFAULT_AVATAR_FILE_ID);

        // 默认不隐藏
        sysUser.setHiddenFlag(YesOrNotEnum.N.getCode());

        this.save(sysUser);

        // 记录一个密码修改记录
        securityConfigService.recordPasswordEditLog(sysUser.getUserId(), saltedEncryptResult.getEncryptPassword(), saltedEncryptResult.getPasswordSalt());

        // 更新用户的任职信息
        sysUserOrgService.updateUserOrg(sysUser.getUserId(), sysUserRequest.getUserOrgList());

        // 添加用户一个默认角色
        sysUserRoleService.bindUserDefaultRole(sysUser.getUserId());

        // 增加用户证书的信息
        sysUserCertificateService.updateUserCertificate(sysUser.getUserId(), sysUserRequest.getUserCertificateList());

        // 记录日志
        BusinessLogUtil.setLogTitle("新增用户，用户账号：" + sysUser.getAccount());
        BusinessLogUtil.addContent("新增用户账号信息：", sysUser.getAccount());
        BusinessLogUtil.addContent("用户详细信息如下：\n", sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // 不能删除超级管理员
        if (YesOrNotEnum.Y.getCode().equals(sysUser.getSuperAdminFlag())) {
            throw new ServiceException(SysUserExceptionEnum.USER_CAN_NOT_DELETE_ADMIN);
        }

        // 删除用户的业务操作
        this.baseRemoveUser(CollectionUtil.set(false, sysUser.getUserId()));

        // 发布更新缓存事件，清空用户名称缓存
        BusinessEventPublisher.publishEvent(UPDATE_USER_INFO_EVENT, sysUser.getUserId());

        // 记录日志
        BusinessLogUtil.setLogTitle("删除用户，用户账号：" + sysUser.getAccount());
        BusinessLogUtil.addContent("用户账号：", sysUser.getAccount());
        BusinessLogUtil.addContent("用户详细信息：\n", sysUser);
    }

    @Override
    public void batchDel(SysUserRequest sysUserRequest) {

        // 判断被删除用户是否有管理员
        Set<Long> userIdList = sysUserRequest.getUserIdList();

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.in(SysUser::getUserId, userIdList);
        sysUserLambdaQueryWrapper.eq(SysUser::getSuperAdminFlag, YesOrNotEnum.Y.getCode());
        long adminCount = this.count(sysUserLambdaQueryWrapper);
        if (adminCount > 0) {
            throw new ServiceException(SysUserExceptionEnum.USER_CAN_NOT_DELETE_ADMIN);
        }

        // 删除用户的业务操作
        this.baseRemoveUser(userIdList);

        // 发布更新缓存事件，清空用户名称缓存
        for (Long userId : userIdList) {
            BusinessEventPublisher.publishEvent(UPDATE_USER_INFO_EVENT, userId.toString());
        }

        // 记录日志
        BusinessLogUtil.setLogTitle("批量删除用户");
        BusinessLogUtil.addContent("用户id集合为：\n", userIdList);
    }

    @Override
    public void edit(SysUserRequest sysUserRequest) {

        SysUser sysUser = this.querySysUser(sysUserRequest);

        BusinessLogUtil.setLogTitle("更新用户信息，用户账号：" + sysUser.getAccount());
        BusinessLogUtil.addContent("原始用户信息如下：\n", sysUser);

        // 不能修改admin账号的超级管理员标识和账号
        if (SysConstants.ADMIN_USER_ACCOUNT.equals(sysUser.getAccount())) {
            if (!sysUser.getAccount().equals(sysUserRequest.getAccount())) {
                throw new ServiceException(SysUserExceptionEnum.CANT_CHANGE_ADMIN_ACCOUNT);
            }
            if (YesOrNotEnum.N.getCode().equals(sysUserRequest.getSuperAdminFlag())) {
                throw new ServiceException(SysUserExceptionEnum.CANT_CHANGE_ADMIN_FLAG);
            }
        }

        // copy属性
        BeanUtil.copyProperties(sysUserRequest, sysUser);

        // 编辑不能修改密码
        sysUser.setPassword(null);

        // 更新用户详情信息
        this.updateById(sysUser);

        // 更新用户的任职信息
        sysUserOrgService.updateUserOrg(sysUser.getUserId(), sysUserRequest.getUserOrgList());

        // 更新用户证书信息
        this.sysUserCertificateService.updateUserCertificate(sysUser.getUserId(), sysUserRequest.getUserCertificateList());

        // 记录日志
        BusinessLogUtil.addContent("更新后用户信息如下：\n", sysUser);

        // 发布修改的事件
        BusinessEventPublisher.publishEvent(UPDATE_USER_INFO_EVENT, sysUser.getUserId());
    }

    @Override
    public SysUser detail(SysUserRequest sysUserRequest) {

        // 查询用户个人信息
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, sysUserRequest.getUserId());
        sysUserLambdaQueryWrapper.select(SysUser::getUserId, SysUser::getAvatar, SysUser::getAccount, SysUser::getUserSort, SysUser::getSuperAdminFlag, SysUser::getRealName, SysUser::getSex,
                SysUser::getBirthday, SysUser::getEmail, SysUser::getPhone, SysUser::getLastLoginIp, SysUser::getLoginCount, SysUser::getLastLoginTime, SysUser::getStatusFlag,
                BaseEntity::getCreateTime, BaseEntity::getUpdateTime, SysUser::getEmployeeNumber);
        SysUser sysUser = this.getOne(sysUserLambdaQueryWrapper, false);

        // 获取用户的组织机构信息
        List<UserOrgDTO> userOrgList = sysUserOrgService.getUserOrgList(sysUser.getUserId(), true);
        sysUser.setUserOrgDTOList(userOrgList);

        // 获取用户的角色信息
        Set<Long> userRoleIdList = sysUserRoleService.getUserSystemRoleIdList(sysUser.getUserId());
        sysUser.setRoleIdList(new ArrayList<>(userRoleIdList));

        // 获取用户证书信息
        List<SysUserCertificateResponse> userCertificateList = sysUserCertificateService.getUserCertificateList(sysUser.getUserId());
        sysUser.setUserCertificateList(userCertificateList);

        return sysUser;
    }

    @Override
    public List<SysUser> findList(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> wrapper = this.createWrapper(sysUserRequest);
        return this.list(wrapper);
    }

    @Override
    public PageResult<SysUser> findPage(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> wrapper = createWrapper(sysUserRequest);

        // 只查询需要的字段
        wrapper.select(SysUser::getUserId, SysUser::getRealName, SysUser::getPhone, SysUser::getAccount, SysUser::getSex, SysUser::getStatusFlag, BaseEntity::getCreateTime, SysUser::getEmployeeNumber);

        // 分页查询
        Page<SysUser> sysUserPage = this.page(PageFactory.defaultPage(), wrapper);

        // 遍历查询结果，增加对用户部门信息的返回
        for (SysUser record : sysUserPage.getRecords()) {
            record.setUserOrgDTO(sysUserOrgService.getUserMainOrgInfo(record.getUserId()));
        }

        return PageResultFactory.createPageResult(sysUserPage);
    }

    @Override
    public void updateStatus(SysUserRequest sysUserRequest) {

        // 如果是将用户禁用，检测判断不能禁用超级管理员用户
        boolean userSuperAdminFlag = this.getUserSuperAdminFlag(sysUserRequest.getUserId());
        if (userSuperAdminFlag) {
            throw new ServiceException(SysUserExceptionEnum.CANT_UPDATE_STATUS);
        }

        // 校验状态传值是否正确
        Integer statusFlag = sysUserRequest.getStatusFlag();
        UserStatusEnum.validateUserStatus(statusFlag);

        // 更新用户状态
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysUser::getStatusFlag, sysUserRequest.getStatusFlag());
        updateWrapper.eq(SysUser::getUserId, sysUserRequest.getUserId());
        this.update(updateWrapper);

        // 如果是启用用户，则清除掉用户密码错误次数的缓存
        if (statusFlag.equals(UserStatusEnum.ENABLE.getCode())) {
            // 获取用户id对应的账号
            Long userId = sysUserRequest.getUserId();
            LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
            sysUserLambdaQueryWrapper.select(SysUser::getAccount);
            SysUser sysUser = this.getOne(sysUserLambdaQueryWrapper);
            if (sysUser != null) {
                loginErrorCountCacheApi.remove(sysUser.getAccount());
            }
        }

        // 记录日志
        BusinessLogUtil.setLogTitle("更新用户状态，用户账号：" + this.getUserRealName(sysUserRequest.getUserId()));
        BusinessLogUtil.addContent("用户id：", sysUserRequest.getUserId());
        BusinessLogUtil.addContent("修改状态为：", sysUserRequest.getStatusFlag());
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {

        SysUser sysUser = this.getById(userId);

        // 密码加密后，存储到数据库中
        SaltedEncryptResult saltedEncryptResult = passwordStoredEncryptApi.encryptWithSalt(newPassword);
        sysUser.setPassword(saltedEncryptResult.getEncryptPassword());
        sysUser.setPasswordSalt(saltedEncryptResult.getPasswordSalt());

        this.updateById(sysUser);

        // 记录日志
        BusinessLogUtil.setLogTitle("重置用户密码，用户账号：" + sysUser.getRealName());
        BusinessLogUtil.addContent("用户信息如下：\n", sysUser);
    }

    @Override
    public PersonalInfo getPersonalInfo() {

        // 获取当前登录用户id
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 查询用户的详细信息
        UserInfoDetailDTO userDetail = this.getUserDetail(userId);
        if (userDetail == null || userDetail.getUserId() == null) {
            return new PersonalInfo();
        }

        PersonalInfo personalInfo = new PersonalInfo();
        BeanUtil.copyProperties(userDetail, personalInfo);
        return personalInfo;
    }

    @Override
    public void editInfo(SysUserRequest sysUserRequest) {

        // 获取当前登录用户的id
        sysUserRequest.setUserId(LoginContext.me().getLoginUser().getUserId());
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // 填充更新用户的信息
        SysUserCreateFactory.fillUpdateInfo(sysUserRequest, sysUser);

        this.updateById(sysUser);

        // 发布修改用户的事件
        BusinessEventPublisher.publishEvent(UPDATE_USER_INFO_EVENT, sysUserRequest.getUserId());
    }

    @Override
    public void editPassword(SysUserRequest sysUserRequest) {

        // 新密码与原密码相同
        if (sysUserRequest.getNewPassword().equals(sysUserRequest.getPassword())) {
            throw new ServiceException(SysUserExceptionEnum.USER_PWD_REPEAT);
        }

        // 获取当前用户的userId
        LoginUser loginUser = LoginContext.me().getLoginUser();
        sysUserRequest.setUserId(loginUser.getUserId());
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // 原密码错误
        if (!passwordStoredEncryptApi.checkPasswordWithSalt(sysUserRequest.getPassword(), sysUser.getPasswordSalt(), sysUser.getPassword())) {
            throw new ServiceException(SysUserExceptionEnum.USER_PWD_ERROR);
        }

        // 将密码进行空字符串处理
        String password = sysUserRequest.getNewPassword().trim();

        // 校验新密码规则，根据密码策略
        securityConfigService.validatePasswordSecurityRule(true, password);

        // 设置新的加密后密码和盐
        SaltedEncryptResult saltedEncryptResult = passwordStoredEncryptApi.encryptWithSalt(password);
        sysUser.setPassword(saltedEncryptResult.getEncryptPassword());
        sysUser.setPasswordSalt(saltedEncryptResult.getPasswordSalt());

        this.updateById(sysUser);

        // 记录一个密码修改记录
        securityConfigService.recordPasswordEditLog(sysUser.getUserId(), saltedEncryptResult.getEncryptPassword(), saltedEncryptResult.getPasswordSalt());

        // 记录业务日志
        BusinessLogUtil.setLogTitle("修改密码，账号：" + loginUser.getAccount());
        BusinessLogUtil.addContent("用户账号：" + loginUser.getAccount() + "修改了密码");
    }

    @Override
    public List<SimpleDict> batchGetName(SysUserRequest sysUserRequest) {

        List<SimpleDict> dictList = new ArrayList<>();

        if (ObjectUtil.isEmpty(sysUserRequest) || ObjectUtil.isEmpty(sysUserRequest.getUserIdList())) {
            return dictList;
        }

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getUserId, sysUserRequest.getUserIdList());
        wrapper.select(SysUser::getRealName, SysUser::getUserId);
        List<SysUser> list = this.list(wrapper);

        if (ObjectUtil.isEmpty(list)) {
            return dictList;
        }

        // 排序
        List<SysUser> sortUsers = SortUtils.sortListByObjectKey(list, new LinkedList<>(sysUserRequest.getUserIdList()));

        for (SysUser sysUser : sortUsers) {
            dictList.add(new SimpleDict(sysUser.getUserId(), sysUser.getRealName(), null));
        }

        return dictList;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void quickBatchSaveUser(List<SysUser> batchUser) {
        if (DbTypeEnum.MYSQL.equals(DataSourceContext.me().getCurrentDbType())) {
            this.getBaseMapper().insertBatchSomeColumn(batchUser);
        } else {
            this.saveBatch(batchUser);
        }
    }

    @Override
    public void editAvatar(SysUserRequest sysUserRequest) {
        // 新头像文件id
        Long fileId = sysUserRequest.getAvatar();

        // 从当前用户获取用户id
        LoginUser loginUser = LoginContext.me().getLoginUser();

        // 更新用户头像
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getUserId, loginUser.getUserId());
        wrapper.set(SysUser::getAvatar, fileId);
        this.update(wrapper);

        // 发布修改用户的事件
        BusinessEventPublisher.publishEvent(UPDATE_USER_INFO_EVENT, loginUser.getUserId());
    }

    @Override
    public SimpleUserDTO getUserInfoByUserId(Long userId) {

        if (ObjectUtil.isEmpty(userId)) {
            return null;
        }

        UserInfoDetailDTO userDetail = this.getUserDetail(userId);
        if (userDetail == null || userDetail.getUserId() == null) {
            return null;
        }

        SimpleUserDTO simpleUserDTO = new SimpleUserDTO();
        simpleUserDTO.setUserId(userId);
        simpleUserDTO.setRealName(userDetail.getRealName());
        simpleUserDTO.setAccount(userDetail.getAccount());

        // 获取头像文件id信息，转化为头像URL
        Long avatarFileId = userDetail.getAvatar();
        if (avatarFileId != null) {
            // 获取头像的访问地址
            String fileAuthUrl = fileInfoApi.getFileAuthUrl(avatarFileId);
            simpleUserDTO.setAvatarUrl(fileAuthUrl);
        }

        return simpleUserDTO;
    }

    @Override
    public String getUserRealName(Long userId) {
        if (userId == null) {
            return "";
        }
        UserInfoDetailDTO userDetail = this.getUserDetail(userId);
        return userDetail.getRealName();
    }

    @Override
    public UserValidateDTO getUserLoginValidateDTO(Long tenantId, String account) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getAccount, account);
        sysUserLambdaQueryWrapper.select(SysUser::getPassword, SysUser::getPasswordSalt, SysUser::getStatusFlag, SysUser::getFreezeDeadlineTime, SysUser::getUserId);

        // 单独填充租户id
        SysUser sysUserServiceOne = TenantSwitchContext.me().changeTenant(tenantId, () -> {

            // 获取用户信息
            SysUser user = this.getOne(sysUserLambdaQueryWrapper, false);

            // 用户查询不到，提示账号密码错误
            if (user == null) {
                throw new ServiceException(SysUserExceptionEnum.ACCOUNT_NOT_EXIST);
            }

            // 更新用户的冻结状态
            this.updateSysUserFreezeStatus(account, user);

            return user;
        });

        return new UserValidateDTO(sysUserServiceOne.getUserId(), sysUserServiceOne.getPassword(), sysUserServiceOne.getPasswordSalt(), sysUserServiceOne.getStatusFlag(), tenantId, account);
    }

    @Override
    public UserValidateDTO getUserLoginValidateDTO(Long userId) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(SysUser::getPassword, SysUser::getAccount, SysUser::getPasswordSalt, SysUser::getStatusFlag, SysUser::getUserId, SysUser::getTenantId);

        // 单独填充租户id
        SysUser sysUser;
        try {
            TenantSwitchHolder.set(false);
            sysUser = this.getOne(sysUserLambdaQueryWrapper, false);
        } finally {
            TenantSwitchHolder.remove();
        }

        if (sysUser == null) {
            throw new ServiceException(SysUserExceptionEnum.ACCOUNT_NOT_EXIST);
        }

        return new UserValidateDTO(sysUser.getUserId(), sysUser.getPassword(), sysUser.getPasswordSalt(), sysUser.getStatusFlag(), sysUser.getTenantId(), sysUser.getAccount());
    }

    @Override
    public void updateUserLoginInfo(Long userId, String ip, Long tenantId) {

        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(ip)) {
            return;
        }

        // 获取原来的登录次数
        long loginCount = 0;
        SysUser originUser = TenantSwitchContext.me().changeTenant(tenantId, () -> {
            LambdaQueryWrapper<SysUser> sysUserOriginLoginCountWrapper = new LambdaQueryWrapper<>();
            sysUserOriginLoginCountWrapper.eq(SysUser::getUserId, userId);
            sysUserOriginLoginCountWrapper.select(SysUser::getLoginCount);
            return this.getOne(sysUserOriginLoginCountWrapper, false);
        });

        // 获取原有的登录次数，准备加1
        if (originUser == null || originUser.getLoginCount() == null) {
            loginCount = 1;
        } else {
            loginCount = originUser.getLoginCount() + 1;
        }

        // 更新登录次数，登录ip，登录时间
        LambdaUpdateWrapper<SysUser> sysUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysUserLambdaUpdateWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaUpdateWrapper.set(SysUser::getLastLoginTime, new Date());
        sysUserLambdaUpdateWrapper.set(SysUser::getLastLoginIp, ip);
        sysUserLambdaUpdateWrapper.set(SysUser::getLoginCount, loginCount);
        TenantSwitchContext.me().changeTenant(tenantId, () -> {
            this.update(sysUserLambdaUpdateWrapper);
            return 0;
        });
    }

    @Override
    public boolean getUserSuperAdminFlag(Long userId) {

        if (ObjectUtil.isEmpty(userId)) {
            return false;
        }

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(SysUser::getSuperAdminFlag);
        SysUser result = this.getOne(sysUserLambdaQueryWrapper, false);

        if (result == null) {
            return false;
        }

        return YesOrNotEnum.Y.getCode().equals(result.getSuperAdminFlag());
    }

    @Override
    public List<Long> queryAllUserIdList() {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.select(SysUser::getUserId);
        List<SysUser> list = this.list(sysUserLambdaQueryWrapper);
        return list.stream().map(SysUser::getUserId).collect(Collectors.toList());
    }

    @Override
    public Boolean userExist(Long userId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserId, userId);
        long count = this.count(wrapper);
        return count > 0;
    }

    @Override
    public OnlineUserItem getUserNameAccountInfo(Long userId) {

        if (userId == null) {
            return new OnlineUserItem();
        }

        UserInfoDetailDTO userDetail = this.getUserDetail(userId);
        if (userDetail == null || userDetail.getUserId() == null) {
            return new OnlineUserItem();
        }

        return new OnlineUserItem(userId, userDetail.getRealName(), userDetail.getAccount());
    }

    @Override
    public List<OnlineUserItem> getUserNameAccountInfoListByCondition(List<OnlineUserItem> onlineUserItems, String searchText) {

        if (ObjectUtil.isEmpty(onlineUserItems) || ObjectUtil.isEmpty(searchText)) {
            return new ArrayList<>();
        }

        // 在线用户列表的id集合
        Set<Long> userIdList = onlineUserItems.stream().map(OnlineUserItem::getUserId).collect(Collectors.toSet());

        // 在这些id集合和查询条件中筛选符合条件的用户，并组装上他们的姓名和账号
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getUserId, userIdList);
        wrapper.nested(wrap -> {
            wrap.like(SysUser::getRealName, searchText).or().like(SysUser::getAccount, searchText);
        });
        wrapper.select(SysUser::getUserId, SysUser::getRealName, SysUser::getAccount);
        List<SysUser> sysUserList = this.list(wrapper);

        List<OnlineUserItem> resultList = new ArrayList<>();
        if (ObjectUtil.isEmpty(sysUserList)) {
            return resultList;
        }

        Map<Long, SysUser> userMap = sysUserList.stream().collect(Collectors.toMap(SysUser::getUserId, item -> item));

        // 从在线用户中查找包含这些key的元素
        for (OnlineUserItem onlineUserItem : onlineUserItems) {
            SysUser sysUser = userMap.get(onlineUserItem.getUserId());
            if (sysUser != null) {
                onlineUserItem.setRealName(sysUser.getRealName());
                onlineUserItem.setAccount(sysUser.getAccount());
                resultList.add(onlineUserItem);
            }
        }

        return resultList;
    }

    @Override
    public UserInfoDetailDTO getUserDetail(Long userId) {

        if (ObjectUtil.isEmpty(userId)) {
            return new UserInfoDetailDTO();
        }

        // 先获取缓存中是否有记录
        UserInfoDetailDTO result = userInfoCache.get(String.valueOf(userId));
        if (result != null) {
            return result;
        }

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(SysUser::getUserId, SysUser::getRealName, SysUser::getNickName, SysUser::getAccount, SysUser::getAvatar,
                SysUser::getBirthday, SysUser::getSex, SysUser::getEmail, SysUser::getPhone, SysUser::getTel,
                SysUser::getSuperAdminFlag, SysUser::getStatusFlag, SysUser::getUserSort, SysUser::getEmployeeNumber, SysUser::getMasterUserId);
        SysUser userInfo = this.getOne(sysUserLambdaQueryWrapper, false);
        if (userInfo == null) {
            return new UserInfoDetailDTO();
        }

        result = new UserInfoDetailDTO();
        BeanUtil.copyProperties(userInfo, result);

        // 缓存结果
        this.userInfoCache.put(String.valueOf(userInfo.getUserId()), result, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        return result;
    }

    @Override
    public void lockUserStatus(String tenantCode, String account) {

        LambdaUpdateWrapper<SysUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysUser::getAccount, account);
        lambdaUpdateWrapper.set(SysUser::getStatusFlag, UserStatusEnum.TEMP_FREEZE.getKey());
        lambdaUpdateWrapper.set(SysUser::getFreezeDeadlineTime, DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, 1));

        TenantSwitchContext.me().changeTenant(tenantCode, () -> {
            this.update(lambdaUpdateWrapper);
            return 0;
        });
    }

    @Override
    public TempLoginUserInfo createTempUserInfo(Long userId) {

        if (ObjectUtil.isEmpty(userId)) {
            return null;
        }

        // 查询用户id对应的用户信息
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserId, userId);
        wrapper.select(SysUser::getUserId, SysUser::getAccount, SysUser::getTenantId, SysUser::getLastLoginIp, SysUser::getLastLoginTime);
        SysUser sysUser = null;
        try {
            TenantSwitchHolder.set(false);
            sysUser = this.getOne(wrapper);
        } finally {
            TenantSwitchHolder.remove();
        }

        if (sysUser == null) {
            return null;
        }

        TempLoginUserInfo tempLoginUserInfo = new TempLoginUserInfo();
        tempLoginUserInfo.setUserId(sysUser.getUserId());
        tempLoginUserInfo.setAccount(sysUser.getAccount());
        tempLoginUserInfo.setTenantId(sysUser.getTenantId());
        tempLoginUserInfo.setLoginTime(sysUser.getLastLoginTime());
        tempLoginUserInfo.setLoginIp(sysUser.getLastLoginIp());

        return tempLoginUserInfo;
    }

    @Override
    public List<SimpleDict> batchGetNameByUserIdList(Set<Long> userIdList) {
        SysUserRequest sysUserRequest = new SysUserRequest();
        sysUserRequest.setUserIdList(userIdList);
        return this.batchGetName(sysUserRequest);
    }

    @Override
    public Set<Long> getUserComprehensiveIdentitySummary() {

        LoginUser loginUser = LoginContext.me().getLoginUserNullable();
        if (loginUser == null) {
            return new HashSet<>();
        }

        // 获取当前登录用户id
        Long userId = loginUser.getUserId();

        // 获取当前用户登录的机构id
        Long currentOrgId = loginUser.getCurrentOrgId();

        // 获取当前用户的角色信息列表
        List<Long> userRoleIdList = sysUserRoleService.getUserRoleIdList(userId);

        HashSet<Long> comprehensiveIdentity = new HashSet<>();
        if (ObjectUtil.isNotEmpty(userId)) {
            comprehensiveIdentity.add(userId);
        }
        if (ObjectUtil.isNotEmpty(currentOrgId)) {
            comprehensiveIdentity.add(currentOrgId);
        }
        if (ObjectUtil.isNotEmpty(userRoleIdList)) {
            comprehensiveIdentity.addAll(userRoleIdList);
        }

        return comprehensiveIdentity;
    }

    @Override
    public List<SimpleUserDTO> batchGetUserSimpleInfoList(Set<Long> userIdList) {

        if (ObjectUtil.isEmpty(userIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getUserId, userIdList);
        wrapper.select(SysUser::getRealName, SysUser::getAccount, SysUser::getAvatar, SysUser::getUserId);
        List<SysUser> list = this.list(wrapper);

        if (ObjectUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        // 排序
        List<SysUser> sortUsers = SortUtils.sortListByObjectKey(list, new LinkedList<>(userIdList));

        // 初始化返回结果
        List<SimpleUserDTO> simpleUserDTOS = new ArrayList<>();
        for (SysUser sysUser : sortUsers) {
            SimpleUserDTO simpleUserDTO = new SimpleUserDTO();
            simpleUserDTO.setAccount(sysUser.getAccount());
            simpleUserDTO.setRealName(sysUser.getRealName());
            simpleUserDTO.setUserId(sysUser.getUserId());

            // 获取头像的访问地址
            Long avatar = sysUser.getAvatar();
            String fileAuthUrl = null;
            try {
                fileAuthUrl = fileInfoApi.getFileAuthUrl(avatar);
            } catch (Exception e) {
                log.warn("获取不到对应用户的头像，头像id为：{}", avatar);
            }
            simpleUserDTO.setAvatarUrl(fileAuthUrl);
            simpleUserDTOS.add(simpleUserDTO);
        }

        return simpleUserDTOS;
    }

    @Override
    public Long simpleAddUser(AddUserDTO addUserDTO, Long orgId, Long positionId) {

        SysUser simpleUser = SysUserCreateFactory.createSimpleAddUser(addUserDTO);

        this.save(simpleUser);

        // 记录一个密码修改记录
        securityConfigService.recordPasswordEditLog(simpleUser.getUserId(), simpleUser.getPassword(), simpleUser.getPasswordSalt());

        // 更新用户的任职信息，用户有且只有一个所属机构
        sysUserOrgService.updateUserOrg(simpleUser.getUserId(),
                ListUtil.of(
                        SysUserCreateFactory.createOneUserOrg(simpleUser.getUserId(), orgId, positionId)));

        // 添加用户一个默认角色
        sysUserRoleService.bindUserDefaultRole(simpleUser.getUserId());

        // 记录日志
        BusinessLogUtil.setLogTitle("新增用户，用户账号：" + simpleUser.getAccount());
        BusinessLogUtil.addContent("新增用户账号信息：", simpleUser.getAccount());
        BusinessLogUtil.addContent("用户详细信息如下：\n", simpleUser);

        return simpleUser.getUserId();
    }

    @Override
    public UserInfoDetailDTO getUserDetailByEmail(String email) {

        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getEmail, email);
        lambdaQueryWrapper.select(SysUser::getUserId, SysUser::getRealName, SysUser::getNickName, SysUser::getAccount, SysUser::getBirthday, SysUser::getSex, SysUser::getEmail, SysUser::getPhone,
                SysUser::getTel, SysUser::getSuperAdminFlag, SysUser::getStatusFlag, SysUser::getUserSort, SysUser::getMasterUserId);
        List<SysUser> list = this.list(lambdaQueryWrapper);
        if (ObjectUtil.isEmpty(list)) {
            return null;
        }

        if (list.size() > 1) {
            throw new ServiceException(SysUserExceptionEnum.EMAIL_REPEAT);
        }

        SysUser sysUser = list.get(0);

        UserInfoDetailDTO userInfoDetailDTO = new UserInfoDetailDTO();
        BeanUtil.copyProperties(sysUser, userInfoDetailDTO);
        return userInfoDetailDTO;
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private SysUser querySysUser(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.getById(sysUserRequest.getUserId());
        if (ObjectUtil.isEmpty(sysUser)) {
            throw new ServiceException(SysUserExceptionEnum.SYS_USER_NOT_EXISTED);
        }
        return sysUser;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private LambdaQueryWrapper<SysUser> createWrapper(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();

        // 根据输入内容进行查询
        String searchText = sysUserRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.nested(wrap -> {
                wrap.like(SysUser::getRealName, searchText);
                wrap.or().like(SysUser::getAccount, searchText);
                wrap.or().like(SysUser::getPhone, searchText);
                wrap.or().like(SysUser::getTel, searchText);
                wrap.or().like(SysUser::getEmployeeNumber, searchText);
            });
        }

        // 根据状态进行查询
        Integer statusFlag = sysUserRequest.getStatusFlag();
        queryWrapper.eq(ObjectUtil.isNotEmpty(statusFlag), SysUser::getStatusFlag, statusFlag);

        // 如果传递了组织机构id查询条件，则查询对应机构id下有哪些用户，再拼接用户查询条件
        if (ObjectUtil.isNotEmpty(sysUserRequest.getOrgIdCondition())) {
            List<Long> orgUserIdList = this.sysUserOrgService.getOrgUserIdList(sysUserRequest.getOrgIdCondition(), true);

            // 指定部门下没人，则直接返回一个不成立条件
            if (ObjectUtil.isEmpty(orgUserIdList)) {
                queryWrapper.in(SysUser::getUserId, -1L);
            } else {
                queryWrapper.in(SysUser::getUserId, orgUserIdList);
            }
        }

        // 数据权限范围控制
        UserDataScopeFactory.getUserDataScopeCondition(queryWrapper);

        // 查询非隐藏的用户
        queryWrapper.ne(SysUser::getHiddenFlag, YesOrNotEnum.Y.getCode());

        // 按用户排序字段排序
        queryWrapper.orderByAsc(SysUser::getUserSort);

        return queryWrapper;
    }

    /**
     * 删除用户操作的基础业务
     *
     * @author fengshuonan
     * @since 2023/6/12 10:44
     */
    private void baseRemoveUser(Set<Long> userIdList) {
        // 校验是否有其他业务绑定了用户信息
        Map<String, RemoveUserCallbackApi> removeUserCallbackApiMap = SpringUtil.getBeansOfType(RemoveUserCallbackApi.class);
        for (RemoveUserCallbackApi removeUserCallbackApi : removeUserCallbackApiMap.values()) {
            removeUserCallbackApi.validateHaveUserBind(userIdList);
        }

        // 执行删除用户操作
        this.removeBatchByIds(userIdList);

        // 执行删除用户关联业务的操作
        for (RemoveUserCallbackApi removeUserCallbackApi : removeUserCallbackApiMap.values()) {
            removeUserCallbackApi.removeUserAction(userIdList);
        }
    }

    /**
     * 更新用户冻结状态，如果用户被冻结了
     *
     * @author fengshuonan
     * @since 2024/7/12 15:11
     */
    private void updateSysUserFreezeStatus(String account, SysUser sysUserServiceOne) {
        // 判断用户状态是否被临时冻结，如果超过了临时冻结时间，将用户状态改为正常态
        if (UserStatusEnum.TEMP_FREEZE.getCode().equals(sysUserServiceOne.getStatusFlag())) {
            Date freezeDeadlineTime = sysUserServiceOne.getFreezeDeadlineTime();
            if (freezeDeadlineTime != null && freezeDeadlineTime.before(new Date())) {

                // 修改用户状态为启用
                sysUserServiceOne.setStatusFlag(UserStatusEnum.ENABLE.getCode());

                // 修改库中的状态，并设置截止冻结日期为空
                LambdaUpdateWrapper<SysUser> objectLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                objectLambdaUpdateWrapper.set(SysUser::getStatusFlag, UserStatusEnum.ENABLE.getCode());
                objectLambdaUpdateWrapper.set(SysUser::getFreezeDeadlineTime, null);
                objectLambdaUpdateWrapper.eq(SysUser::getAccount, account);
                this.update(objectLambdaUpdateWrapper);
            }
        }
    }

}