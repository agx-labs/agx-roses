package cn.stylefeng.roses.kernel.sys.modular.theme.controller;

import cn.stylefeng.roses.kernel.rule.annotation.ApiLog;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.theme.constants.ThemeConstants;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeTemplateRelRequest;
import cn.stylefeng.roses.kernel.sys.modular.theme.service.SysThemeTemplateRelService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统主题模板属性关系控制器
 *
 * @author xixiaowei
 * @since 2021/12/24 10:55
 */
@RestController
@ApiResource(name = "系统主题模板属性关系管理", requiredPermission = true, requirePermissionCode = ThemeConstants.THEME_MANAGER)
public class SysThemeTemplateRelController {

    @Resource
    private SysThemeTemplateRelService sysThemeTemplateRelService;

    /**
     * 增加系统主题模板属性关系
     *
     * @author xixiaowei
     * @since 2021/12/24 11:17
     */
    @PostResource(name = "增加系统主题模板属性关系", path = "/sysThemeTemplateRel/add")
    @ApiLog
    public ResponseData<?> add(
            @RequestBody @Validated(SysThemeTemplateRelRequest.add.class) SysThemeTemplateRelRequest sysThemeTemplateParam) {
        sysThemeTemplateRelService.add(sysThemeTemplateParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统主题模板属性关系
     *
     * @author xixiaowei
     * @since 2021/12/24 11:23
     */
    @PostResource(name = "删除系统主题模板属性关系", path = "/sysThemeTemplateRel/del")
    @ApiLog
    public ResponseData<?> del(@RequestBody SysThemeTemplateRelRequest sysThemeTemplateRelParam) {
        sysThemeTemplateRelService.del(sysThemeTemplateRelParam);
        return new SuccessResponseData<>();
    }

}
