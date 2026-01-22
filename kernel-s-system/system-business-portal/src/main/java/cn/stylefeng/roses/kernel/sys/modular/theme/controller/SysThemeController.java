package cn.stylefeng.roses.kernel.sys.modular.theme.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.annotation.ApiLog;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.theme.constants.ThemeConstants;
import cn.stylefeng.roses.kernel.sys.modular.theme.entity.SysTheme;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeDTO;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeRequest;
import cn.stylefeng.roses.kernel.sys.modular.theme.service.SysThemeService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统主题控制器
 *
 * @author xixiaowei
 * @since 2021/12/17 16:40
 */
@RestController
@ApiResource(name = "系统主题管理", requiredPermission = true, requirePermissionCode = ThemeConstants.THEME_MANAGER)
public class SysThemeController {

    @Resource
    private SysThemeService sysThemeService;

    /**
     * 增加系统主题
     *
     * @author xixiaowei
     * @since 2021/12/17 16:43
     */
    @PostResource(name = "增加系统主题", path = "/sysTheme/add")
    @ApiLog
    public ResponseData<?> add(@RequestBody @Validated(SysThemeRequest.add.class) SysThemeRequest sysThemeParam) {
        sysThemeService.add(sysThemeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统主题
     *
     * @author xixiaowei
     * @since 2021/12/17 16:45
     */
    @PostResource(name = "删除系统主题", path = "/sysTheme/del")
    @ApiLog
    public ResponseData<?> del(@RequestBody @Validated(SysThemeRequest.delete.class) SysThemeRequest sysThemeParam) {
        sysThemeService.del(sysThemeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 修改系统主题
     *
     * @author xixiaowei
     * @since 2021/12/17 16:50
     */
    @PostResource(name = "修改系统主题", path = "/sysTheme/edit")
    @ApiLog
    public ResponseData<?> edit(@RequestBody @Validated(SysThemeRequest.edit.class) SysThemeRequest sysThemeParam) {
        sysThemeService.edit(sysThemeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 查询系统主题
     *
     * @author xixiaowei
     * @since 2021/12/17 16:58
     */
    @GetResource(name = "查询系统主题", path = "/sysTheme/findPage")
    public ResponseData<PageResult<SysThemeDTO>> findPage(SysThemeRequest sysThemeParam) {
        return new SuccessResponseData<>(sysThemeService.findPage(sysThemeParam));
    }

    /**
     * 查询系统主题详情
     *
     * @author xixiaowei
     * @since 2021/12/17 17:04
     */
    @GetResource(name = "查询系统主题详情", path = "/sysTheme/detail")
    public ResponseData<SysTheme> detail(@Validated(SysThemeRequest.updateStatus.class) SysThemeRequest sysThemeParam) {
        return new SuccessResponseData<>(sysThemeService.detail(sysThemeParam));
    }

    /**
     * 修改系统主题启用状态
     *
     * @author xixiaowei
     * @since 2021/12/17 17:32
     */
    @PostResource(name = "修改系统主题启用状态", path = "/sysTheme/updateStatus")
    @ApiLog
    public ResponseData<?> updateThemeStatus(@RequestBody @Validated(SysThemeRequest.updateStatus.class) SysThemeRequest sysThemeParam) {
        sysThemeService.updateThemeStatus(sysThemeParam);
        return new SuccessResponseData<>();
    }
}
