package cn.stylefeng.roses.kernel.sys.modular.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.app.enums.SysAppExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.app.mapper.SysAppMapper;
import cn.stylefeng.roses.kernel.sys.modular.app.pojo.request.SysAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.IndexUserAppInfo;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 系统应用业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Service
public class SysAppServiceImpl extends ServiceImpl<SysAppMapper, SysApp> implements SysAppService {

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public void add(SysAppRequest sysAppRequest) {
        SysApp sysApp = new SysApp();
        BeanUtil.copyProperties(sysAppRequest, sysApp);
        this.save(sysApp);
    }

    @Override
    public void del(SysAppRequest sysAppRequest) {
        SysApp sysApp = this.querySysApp(sysAppRequest);

        // 判断应用下是否有绑定菜单
        if (this.sysMenuService.validateMenuBindApp(CollectionUtil.set(false, sysApp.getAppId()))) {
            throw new ServiceException(SysAppExceptionEnum.APP_BIND_MENU);
        }

        this.removeById(sysApp.getAppId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(SysAppRequest sysAppRequest) {

        Set<Long> appIdList = sysAppRequest.getAppIdList();

        // 判断应用下是否有绑定菜单
        if (this.sysMenuService.validateMenuBindApp(appIdList)) {
            throw new ServiceException(SysAppExceptionEnum.APP_BIND_MENU);
        }

        this.removeBatchByIds(appIdList);
    }

    @Override
    public void edit(SysAppRequest sysAppRequest) {
        SysApp sysApp = this.querySysApp(sysAppRequest);

        // 应用编码不允许修改
        if (!sysApp.getAppCode().equals(sysAppRequest.getAppCode())) {
            throw new ServiceException(SysAppExceptionEnum.APP_CODE_CANT_EDIT);
        }

        BeanUtil.copyProperties(sysAppRequest, sysApp);

        this.updateById(sysApp);
    }

    @Override
    public SysApp detail(SysAppRequest sysAppRequest) {
        return this.querySysApp(sysAppRequest);
    }

    @Override
    public PageResult<SysApp> findPage(SysAppRequest sysAppRequest) {
        LambdaQueryWrapper<SysApp> wrapper = createWrapper(sysAppRequest);

        // 只查询有用的列
        wrapper.select(SysApp::getAppId, SysApp::getAppName, SysApp::getAppCode, SysApp::getAppIcon, SysApp::getStatusFlag,
                SysApp::getAppSort, BaseEntity::getCreateTime);

        Page<SysApp> sysAppPage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysAppPage);
    }

    @Override
    public List<AppGroupDetail> getAppList() {

        LambdaQueryWrapper<SysApp> wrapper = this.createWrapper(new SysAppRequest());

        // 只查询启用的
        wrapper.eq(SysApp::getStatusFlag, StatusEnum.ENABLE.getCode());

        // 查询有效字段
        wrapper.select(SysApp::getAppId, SysApp::getAppName, SysApp::getAppIcon, SysApp::getRemark);

        List<SysApp> appList = this.list(wrapper);

        // 结果转化为指定格式
        ArrayList<AppGroupDetail> appGroupDetails = new ArrayList<>();
        for (SysApp sysApp : appList) {
            AppGroupDetail appGroupDetail = new AppGroupDetail(sysApp.getAppId(), sysApp.getAppName(), sysApp.getAppIcon(),
                    sysApp.getRemark());
            appGroupDetails.add(appGroupDetail);
        }

        return appGroupDetails;
    }

    @Override
    public List<IndexUserAppInfo> getIndexUserAppList(Set<Long> appIds) {

        if (ObjectUtil.isEmpty(appIds)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysApp> sysAppLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysAppLambdaQueryWrapper.in(SysApp::getAppId, appIds);
        sysAppLambdaQueryWrapper.select(SysApp::getAppId, SysApp::getAppName, SysApp::getAppIcon, SysApp::getRemark);
        sysAppLambdaQueryWrapper.orderByAsc(SysApp::getAppSort);

        List<SysApp> sysAppList = this.list(sysAppLambdaQueryWrapper);

        // 获取应用详情
        ArrayList<IndexUserAppInfo> indexUserAppInfos = new ArrayList<>();
        for (SysApp sysApp : sysAppList) {
            IndexUserAppInfo indexUserAppInfo = new IndexUserAppInfo();
            indexUserAppInfo.setAppId(sysApp.getAppId());
            indexUserAppInfo.setAppName(sysApp.getAppName());
            indexUserAppInfo.setAppIcon(sysApp.getAppIcon());
            indexUserAppInfo.setRemark(sysApp.getRemark());
            indexUserAppInfo.setCurrentSelectFlag(false);
            indexUserAppInfos.add(indexUserAppInfo);
        }

        return indexUserAppInfos;
    }

    @Override
    public List<SysApp> findList(SysAppRequest sysAppRequest) {
        LambdaQueryWrapper<SysApp> wrapper = this.createWrapper(sysAppRequest);

        // 查询名称、图标和id
        wrapper.select(SysApp::getAppId, SysApp::getAppName, SysApp::getAppIcon);

        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private SysApp querySysApp(SysAppRequest sysAppRequest) {
        SysApp sysApp = this.getById(sysAppRequest.getAppId());
        if (ObjectUtil.isEmpty(sysApp)) {
            throw new ServiceException(SysAppExceptionEnum.SYS_APP_NOT_EXISTED);
        }
        return sysApp;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private LambdaQueryWrapper<SysApp> createWrapper(SysAppRequest sysAppRequest) {
        LambdaQueryWrapper<SysApp> queryWrapper = new LambdaQueryWrapper<>();

        // 根据搜索条件查询
        String searchText = sysAppRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(SysApp::getAppCode, searchText);
            queryWrapper.or().like(SysApp::getAppName, searchText);
            queryWrapper.or().like(SysApp::getRemark, searchText);
        }

        // 根据排序查询
        queryWrapper.orderByAsc(SysApp::getAppSort);

        return queryWrapper;
    }

    @Override
    public void updateStatus(SysAppRequest sysUserRequest) {

        // 更新应用状态
        LambdaUpdateWrapper<SysApp> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysApp::getStatusFlag, sysUserRequest.getStatusFlag());
        updateWrapper.eq(SysApp::getAppId, sysUserRequest.getAppId());
        this.update(updateWrapper);

    }

}