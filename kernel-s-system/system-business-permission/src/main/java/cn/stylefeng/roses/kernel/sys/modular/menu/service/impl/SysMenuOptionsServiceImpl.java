package cn.stylefeng.roses.kernel.sys.modular.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveMenuCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.constants.MenuConstants;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuOptionsExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.factory.MenuOptionsValidateFactory;
import cn.stylefeng.roses.kernel.sys.modular.menu.mapper.SysMenuOptionsMapper;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuOptionsRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单下的功能操作业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Service
public class SysMenuOptionsServiceImpl extends ServiceImpl<SysMenuOptionsMapper, SysMenuOptions> implements SysMenuOptionsService,
        RemoveMenuCallbackApi {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource(name = "menuCodeCache")
    private CacheOperatorApi<String> menuCodeCache;

    @Override
    public void add(SysMenuOptionsRequest sysMenuOptionsRequest) {

        // 同菜单下功能名称和编码不能重复
        MenuOptionsValidateFactory.validateMenuOptionsParam(sysMenuOptionsRequest);

        SysMenuOptions sysMenuOptions = new SysMenuOptions();
        BeanUtil.copyProperties(sysMenuOptionsRequest, sysMenuOptions);

        // 获取菜单的应用id
        Map<Long, Long> menuAppId = sysMenuService.getMenuAppId(ListUtil.list(false, sysMenuOptionsRequest.getMenuId()));
        Long appId = menuAppId.get(sysMenuOptionsRequest.getMenuId());
        sysMenuOptions.setAppId(appId);

        this.save(sysMenuOptions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysMenuOptionsRequest sysMenuOptionsRequest) {

        SysMenuOptions sysMenuOptions = this.querySysMenuOptions(sysMenuOptionsRequest);
        this.removeById(sysMenuOptions.getMenuOptionId());

        // 删除角色绑定的功能关联
        sysRoleMenuOptionsService.removeRoleBindOptions(sysMenuOptionsRequest.getMenuOptionId());

        // 发布菜单功能的更新事件
        BusinessEventPublisher.publishEvent(MenuConstants.MENU_OPTIONS_UPDATE_EVENT, sysMenuOptions.getMenuOptionId());

    }

    @Override
    public void edit(SysMenuOptionsRequest sysMenuOptionsRequest) {

        // 同菜单下功能名称和编码不能重复
        MenuOptionsValidateFactory.validateMenuOptionsParam(sysMenuOptionsRequest);

        SysMenuOptions sysMenuOptions = this.querySysMenuOptions(sysMenuOptionsRequest);
        BeanUtil.copyProperties(sysMenuOptionsRequest, sysMenuOptions);
        this.updateById(sysMenuOptions);

        // 发布菜单功能的更新事件
        BusinessEventPublisher.publishEvent(MenuConstants.MENU_OPTIONS_UPDATE_EVENT, sysMenuOptions.getMenuOptionId());

    }

    @Override
    public SysMenuOptions detail(SysMenuOptionsRequest sysMenuOptionsRequest) {
        return this.querySysMenuOptions(sysMenuOptionsRequest);
    }

    @Override
    public PageResult<SysMenuOptions> findPage(SysMenuOptionsRequest sysMenuOptionsRequest) {
        LambdaQueryWrapper<SysMenuOptions> wrapper = createWrapper(sysMenuOptionsRequest);

        // 只查询有用字段
        wrapper.select(SysMenuOptions::getOptionName, SysMenuOptions::getOptionCode, SysMenuOptions::getMenuId,
                SysMenuOptions::getMenuOptionId);

        Page<SysMenuOptions> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysMenuOptions> getTotalMenuOptionsList() {
        return this.getTotalMenuOptionsList(null);
    }

    @Override
    public List<SysMenuOptions> getTotalMenuOptionsList(Set<Long> roleLimitMenuIdsAndOptionIds) {
        LambdaQueryWrapper<SysMenuOptions> sysMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuOptionsLambdaQueryWrapper.select(SysMenuOptions::getMenuOptionId, SysMenuOptions::getMenuId, SysMenuOptions::getAppId,
                SysMenuOptions::getOptionName);
        if (ObjectUtil.isNotEmpty(roleLimitMenuIdsAndOptionIds)) {
            sysMenuOptionsLambdaQueryWrapper.in(SysMenuOptions::getMenuOptionId, roleLimitMenuIdsAndOptionIds);
        }
        return this.list(sysMenuOptionsLambdaQueryWrapper);
    }

    @Override
    public List<String> getOptionsCodeList(List<Long> optionsIdList) {

        List<String> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(optionsIdList)) {
            return result;
        }

        for (Long optionsId : optionsIdList) {

            String optionsIdKey = optionsId.toString();

            // 先从缓存获取是否有对应的编码
            String cachedCode = menuCodeCache.get(optionsIdKey);

            if (ObjectUtil.isNotEmpty(cachedCode)) {
                result.add(cachedCode);
                continue;
            }

            // 缓存没有，从数据库查询功能的编码
            LambdaQueryWrapper<SysMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenuOptions::getMenuOptionId, optionsId);
            queryWrapper.select(SysMenuOptions::getOptionCode);
            SysMenuOptions sysMenuOptions = this.getOne(queryWrapper, false);

            if (sysMenuOptions != null) {
                String optionCodeQueryResult = sysMenuOptions.getOptionCode();
                result.add(optionCodeQueryResult);

                // 添加到缓存中一份
                menuCodeCache.put(optionsIdKey, optionCodeQueryResult, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            }
        }

        return result;
    }

    @Override
    public List<SysMenuOptions> findList(SysMenuOptionsRequest sysMenuOptionsRequest) {
        LambdaQueryWrapper<SysMenuOptions> wrapper = this.createWrapper(sysMenuOptionsRequest);

        // 只查询有用字段
        wrapper.select(SysMenuOptions::getOptionName, SysMenuOptions::getOptionCode, SysMenuOptions::getMenuId,
                SysMenuOptions::getMenuOptionId);

        return this.list(wrapper);
    }

    @Override
    public void removeMenuAction(Set<Long> beRemovedMenuIdList) {
        LambdaQueryWrapper<SysMenuOptions> menuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuOptionsLambdaQueryWrapper.in(SysMenuOptions::getMenuId, beRemovedMenuIdList);
        this.remove(menuOptionsLambdaQueryWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private SysMenuOptions querySysMenuOptions(SysMenuOptionsRequest sysMenuOptionsRequest) {
        SysMenuOptions sysMenuOptions = this.getById(sysMenuOptionsRequest.getMenuOptionId());
        if (ObjectUtil.isEmpty(sysMenuOptions)) {
            throw new ServiceException(SysMenuOptionsExceptionEnum.SYS_MENU_OPTIONS_NOT_EXISTED);
        }
        return sysMenuOptions;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private LambdaQueryWrapper<SysMenuOptions> createWrapper(SysMenuOptionsRequest sysMenuOptionsRequest) {
        LambdaQueryWrapper<SysMenuOptions> queryWrapper = new LambdaQueryWrapper<>();

        // 根据菜单id查询
        Long menuId = sysMenuOptionsRequest.getMenuId();
        queryWrapper.eq(ObjectUtil.isNotNull(menuId), SysMenuOptions::getMenuId, menuId);

        // 根据查询文本查询
        String searchText = sysMenuOptionsRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.nested(wrap -> {
                wrap.like(SysMenuOptions::getOptionCode, searchText);
                wrap.or().like(SysMenuOptions::getOptionName, searchText);
            });
        }

        return queryWrapper;
    }

}