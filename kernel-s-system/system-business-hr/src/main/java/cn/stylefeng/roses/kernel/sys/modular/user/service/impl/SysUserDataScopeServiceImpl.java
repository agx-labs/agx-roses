package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserDataScope;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserDataScopeExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserDataScopeMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserDataScopeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 用户数据范围业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
public class SysUserDataScopeServiceImpl extends ServiceImpl<SysUserDataScopeMapper, SysUserDataScope> implements SysUserDataScopeService, RemoveUserCallbackApi {

    @Override
    public void add(SysUserDataScopeRequest sysUserDataScopeRequest) {
        SysUserDataScope sysUserDataScope = new SysUserDataScope();
        BeanUtil.copyProperties(sysUserDataScopeRequest, sysUserDataScope);
        this.save(sysUserDataScope);
    }

    @Override
    public void del(SysUserDataScopeRequest sysUserDataScopeRequest) {
        SysUserDataScope sysUserDataScope = this.querySysUserDataScope(sysUserDataScopeRequest);
        this.removeById(sysUserDataScope.getUserDataScopeId());
    }

    @Override
    public void edit(SysUserDataScopeRequest sysUserDataScopeRequest) {
        SysUserDataScope sysUserDataScope = this.querySysUserDataScope(sysUserDataScopeRequest);
        BeanUtil.copyProperties(sysUserDataScopeRequest, sysUserDataScope);
        this.updateById(sysUserDataScope);
    }

    @Override
    public SysUserDataScope detail(SysUserDataScopeRequest sysUserDataScopeRequest) {
        return this.querySysUserDataScope(sysUserDataScopeRequest);
    }

    @Override
    public PageResult<SysUserDataScope> findPage(SysUserDataScopeRequest sysUserDataScopeRequest) {
        LambdaQueryWrapper<SysUserDataScope> wrapper = createWrapper(sysUserDataScopeRequest);
        Page<SysUserDataScope> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysUserDataScope> findList(SysUserDataScopeRequest sysUserDataScopeRequest) {
        LambdaQueryWrapper<SysUserDataScope> wrapper = this.createWrapper(sysUserDataScopeRequest);
        return this.list(wrapper);
    }

    @Override
    public void validateHaveUserBind(Set<Long> beRemovedUserIdList) {

    }

    @Override
    public void removeUserAction(Set<Long> beRemovedUserIdList) {
        LambdaQueryWrapper<SysUserDataScope> sysUserDataScopeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserDataScopeLambdaQueryWrapper.in(SysUserDataScope::getUserId, beRemovedUserIdList);
        this.remove(sysUserDataScopeLambdaQueryWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private SysUserDataScope querySysUserDataScope(SysUserDataScopeRequest sysUserDataScopeRequest) {
        SysUserDataScope sysUserDataScope = this.getById(sysUserDataScopeRequest.getUserDataScopeId());
        if (ObjectUtil.isEmpty(sysUserDataScope)) {
            throw new ServiceException(SysUserDataScopeExceptionEnum.SYS_USER_DATA_SCOPE_NOT_EXISTED);
        }
        return sysUserDataScope;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private LambdaQueryWrapper<SysUserDataScope> createWrapper(SysUserDataScopeRequest sysUserDataScopeRequest) {
        LambdaQueryWrapper<SysUserDataScope> queryWrapper = new LambdaQueryWrapper<>();

        Long userDataScopeId = sysUserDataScopeRequest.getUserDataScopeId();
        Long userId = sysUserDataScopeRequest.getUserId();
        Long orgId = sysUserDataScopeRequest.getOrgId();

        queryWrapper.eq(ObjectUtil.isNotNull(userDataScopeId), SysUserDataScope::getUserDataScopeId, userDataScopeId);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), SysUserDataScope::getUserId, userId);
        queryWrapper.eq(ObjectUtil.isNotNull(orgId), SysUserDataScope::getOrgId, orgId);

        return queryWrapper;
    }

}