package cn.stylefeng.roses.kernel.sys.modular.menu.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.enums.menu.MenuTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 菜单参数校验
 *
 * @author fengshuonan
 * @since 2023/6/14 23:09
 */
public class MenuValidateFactory {

    /**
     * 校验新增菜单时候的参数合法性
     *
     * @author fengshuonan
     * @since 2023/6/14 23:10
     */
    public static void validateAddMenuParam(SysMenuRequest sysMenuRequest) {

        SysMenuService sysMenuService = SpringUtil.getBean(SysMenuService.class);

        // 1. 校验菜单编码不能重复，全局唯一，因为菜单编码涉及到权限分配，如果不唯一则会权限分配错乱
        Long menuId = sysMenuRequest.getMenuId();
        String menuCode = sysMenuRequest.getMenuCode();

        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.eq(SysMenu::getMenuCode, menuCode);

        // 如果是编辑菜单，则排除当前这个菜单的查询
        if (menuId != null) {
            sysMenuLambdaQueryWrapper.ne(SysMenu::getMenuId, menuId);
        }

        long alreadyCount = sysMenuService.count(sysMenuLambdaQueryWrapper);
        if (alreadyCount > 0) {
            throw new ServiceException(SysMenuExceptionEnum.MENU_CODE_REPEAT);
        }

        // 2. 校验vue组件相关的配置是否必填
        Integer menuType = sysMenuRequest.getMenuType();

        // 2.1 如果是后台菜单，校验路由地址、组件代码路径、是否隐藏参数
        if (MenuTypeEnum.BACKEND_MENU.getKey().equals(menuType)) {
            if (ObjectUtil.isEmpty(sysMenuRequest.getAntdvRouter())) {
                throw new ServiceException(SysMenuExceptionEnum.URL_CANT_EMPTY);
            }
            if (ObjectUtil.isEmpty(sysMenuRequest.getAntdvVisible())) {
                throw new ServiceException(SysMenuExceptionEnum.HIDDEN_FLAG_CANT_EMPTY);
            }
        }

        // 2.2 如果是纯前端路由，则判断路由地址和组件代码路径
        else if (MenuTypeEnum.FRONT_VUE.getKey().equals(menuType)) {
            if (ObjectUtil.isEmpty(sysMenuRequest.getAntdvRouter())) {
                throw new ServiceException(SysMenuExceptionEnum.URL_CANT_EMPTY);
            }
            if (ObjectUtil.isEmpty(sysMenuRequest.getAntdvComponent())) {
                throw new ServiceException(SysMenuExceptionEnum.COMPONENT_PATH_CANT_EMPTY);
            }
        }

        // 2.3 如果是内部链接，判断路由地址和连接地址
        else if (MenuTypeEnum.INNER_URL.getKey().equals(menuType)) {
            if (ObjectUtil.isEmpty(sysMenuRequest.getAntdvRouter())) {
                throw new ServiceException(SysMenuExceptionEnum.URL_CANT_EMPTY);
            }
            if (ObjectUtil.isEmpty(sysMenuRequest.getAntdvComponent())) {
                throw new ServiceException(SysMenuExceptionEnum.COMPONENT_PATH_CANT_EMPTY);
            }
        }

        // 2.4 如果是外部链接，则判断外部链接地址
        else if (MenuTypeEnum.OUT_URL.getKey().equals(menuType)) {
            if (ObjectUtil.isEmpty(sysMenuRequest.getAntdvRouter())) {
                throw new ServiceException(SysMenuExceptionEnum.URL_CANT_EMPTY);
            }
        }
    }

}
