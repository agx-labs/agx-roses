package cn.stylefeng.roses.kernel.sys.modular.menu.factory;

import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuOptionsExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuOptionsRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 参数功能的校验
 *
 * @author fengshuonan
 * @since 2023/6/15 23:21
 */
public class MenuOptionsValidateFactory {

    /**
     * 校验同菜单下功能名称和编码不能重复
     *
     * @author fengshuonan
     * @since 2023/6/15 23:21
     */
    public static void validateMenuOptionsParam(SysMenuOptionsRequest sysMenuOptionsRequest) {

        SysMenuOptionsService sysMenuOptionsService = SpringUtil.getBean(SysMenuOptionsService.class);

        // 1. 校验功能编码不能重复，全局唯一校验，编码涉及到权限分配，所以必须全局唯一
        Long menuId = sysMenuOptionsRequest.getMenuId();

        LambdaQueryWrapper<SysMenuOptions> codeWrapper = new LambdaQueryWrapper<>();
        codeWrapper.eq(SysMenuOptions::getOptionCode, sysMenuOptionsRequest.getOptionCode());

        // 如果是编辑菜单，则排除当前这个菜单的查询
        if (sysMenuOptionsRequest.getMenuOptionId() != null) {
            codeWrapper.ne(SysMenuOptions::getMenuOptionId, sysMenuOptionsRequest.getMenuOptionId());
        }

        long alreadyCodeCount = sysMenuOptionsService.count(codeWrapper);
        if (alreadyCodeCount > 0) {
            throw new ServiceException(SysMenuOptionsExceptionEnum.OPTIONS_CODE_REPEAT);
        }

        // 2. 校验同菜单下名称不能重复
        LambdaQueryWrapper<SysMenuOptions> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(SysMenuOptions::getMenuId, menuId);
        nameWrapper.eq(SysMenuOptions::getOptionName, sysMenuOptionsRequest.getOptionName());

        // 如果是编辑菜单，则排除当前这个菜单的查询
        if (sysMenuOptionsRequest.getMenuOptionId() != null) {
            nameWrapper.ne(SysMenuOptions::getMenuOptionId, sysMenuOptionsRequest.getMenuOptionId());
        }

        long alreadyNameCount = sysMenuOptionsService.count(nameWrapper);
        if (alreadyNameCount > 0) {
            throw new ServiceException(SysMenuOptionsExceptionEnum.OPTIONS_NAME_REPEAT);
        }
    }

}
