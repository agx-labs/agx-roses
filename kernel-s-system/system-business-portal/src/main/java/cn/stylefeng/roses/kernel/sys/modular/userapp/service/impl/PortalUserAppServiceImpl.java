package cn.stylefeng.roses.kernel.sys.modular.userapp.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.dsctn.api.context.DataSourceContext;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.SysMenuServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.menu.UserAppMenuInfo;
import cn.stylefeng.roses.kernel.sys.modular.userapp.entity.PortalUserApp;
import cn.stylefeng.roses.kernel.sys.modular.userapp.mapper.PortalUserAppMapper;
import cn.stylefeng.roses.kernel.sys.modular.userapp.pojo.request.PortalUserAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.userapp.service.PortalUserAppService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户常用功能业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
@Service
public class PortalUserAppServiceImpl extends ServiceImpl<PortalUserAppMapper, PortalUserApp> implements PortalUserAppService {

    @Resource
    private SysMenuServiceApi sysMenuServiceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAppList(PortalUserAppRequest portalUserAppRequest) {

        // 获取当前登录用户id
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 删除掉用户绑定的菜单集合
        LambdaQueryWrapper<PortalUserApp> removeWrapper = new LambdaQueryWrapper<>();
        removeWrapper.eq(PortalUserApp::getUserId, userId);
        this.remove(removeWrapper);

        // 获取菜单id集合对应的应用id集合，为了冗余字段，删除应用时，联动删除
        Map<Long, Long> menuIdAppIdMap = sysMenuServiceApi.getMenuAppId(portalUserAppRequest.getMenuIdList());

        List<PortalUserApp> portalUserApps = new ArrayList<>();
        for (Long menuId : portalUserAppRequest.getMenuIdList()) {
            PortalUserApp portalUserApp = new PortalUserApp();
            portalUserApp.setUserId(userId);
            portalUserApp.setAppId(menuIdAppIdMap.get(menuId));
            portalUserApp.setMenuId(menuId);
            portalUserApps.add(portalUserApp);
        }

        if (DbTypeEnum.MYSQL.equals(DataSourceContext.me().getCurrentDbType())) {
            this.getBaseMapper().insertBatchSomeColumn(portalUserApps);
        } else {
            this.saveBatch(portalUserApps);
        }
    }

    @Override
    public List<UserAppMenuInfo> getUserAppList() {

        // 获取当前登录用户id
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 获取所有的用户常用功能（功能其实对应的就是菜单）
        LambdaQueryWrapper<PortalUserApp> portalUserAppLambdaQueryWrapper = new LambdaQueryWrapper<>();
        portalUserAppLambdaQueryWrapper.eq(PortalUserApp::getUserId, userId);
        portalUserAppLambdaQueryWrapper.select(PortalUserApp::getMenuId);
        List<PortalUserApp> userAppList = this.list(portalUserAppLambdaQueryWrapper);

        if (ObjectUtil.isEmpty(userAppList)) {
            return new ArrayList<>();
        }

        // 获取这些功能的菜单id集合
        Set<Long> totalMenuIdList = userAppList.stream().map(PortalUserApp::getMenuId).collect(Collectors.toSet());

        // 通过这些菜单id查询到菜单的名称、图标、路由返回给前端
        return sysMenuServiceApi.getUserAppMenuDetailList(totalMenuIdList);
    }

}