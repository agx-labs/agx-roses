package cn.stylefeng.roses.kernel.sys.modular.menu.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.api.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuOptionsRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单下的功能操作接口
 *
 * @author fengshuonan
 * @date 2023/06/15 23:04
 */
@RestController
@ApiResource(name = "菜单下的功能操作接口", requirePermissionCode = PermissionCodeConstants.AUTH_MENU)
public class SysMenuOptionsController {

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    /**
     * 添加菜单功能
     *
     * @author fengshuonan
     * @date 2023/06/15 23:04
     */
    @PostResource(name = "添加菜单功能", path = "/sysMenuOptions/add")
    public ResponseData<SysMenuOptions> add(
            @RequestBody @Validated(SysMenuOptionsRequest.add.class) SysMenuOptionsRequest sysMenuOptionsRequest) {
        sysMenuOptionsService.add(sysMenuOptionsRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除菜单功能
     *
     * @author fengshuonan
     * @date 2023/06/15 23:04
     */
    @PostResource(name = "删除菜单功能", path = "/sysMenuOptions/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysMenuOptionsRequest.delete.class) SysMenuOptionsRequest sysMenuOptionsRequest) {
        sysMenuOptionsService.del(sysMenuOptionsRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑菜单功能
     *
     * @author fengshuonan
     * @date 2023/06/15 23:04
     */
    @PostResource(name = "编辑菜单功能", path = "/sysMenuOptions/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysMenuOptionsRequest.edit.class) SysMenuOptionsRequest sysMenuOptionsRequest) {
        sysMenuOptionsService.edit(sysMenuOptionsRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 分页查询-菜单功能
     *
     * @author fengshuonan
     * @date 2023/06/15 23:04
     */
    @GetResource(name = "分页查询-菜单功能", path = "/sysMenuOptions/page")
    public ResponseData<PageResult<SysMenuOptions>> page(SysMenuOptionsRequest sysMenuOptionsRequest) {
        return new SuccessResponseData<>(sysMenuOptionsService.findPage(sysMenuOptionsRequest));
    }

    /**
     * 获取所有功能列表
     *
     * @author liyanjun
     * @since 2023/7/01 21:23
     */
    @GetResource(name = "获取菜单的功能列表", path = "/sysMenuOptions/list")
    public ResponseData<List<SysMenuOptions>> getRoleList(SysMenuOptionsRequest sysMenuOptionsRequest) {
        return new SuccessResponseData<>(sysMenuOptionsService.findList(sysMenuOptionsRequest));
    }
}
