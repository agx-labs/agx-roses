package cn.stylefeng.roses.kernel.sys.modular.theme.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.annotation.ApiLog;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.theme.constants.ThemeConstants;
import cn.stylefeng.roses.kernel.sys.modular.theme.entity.SysThemeTemplateField;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeTemplateFieldRequest;
import cn.stylefeng.roses.kernel.sys.modular.theme.service.SysThemeTemplateFieldService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统主题模板属性控制器
 *
 * @author xixiaowei
 * @since 2021/12/17 10:28
 */
@RestController
@ApiResource(name = "系统主题模板属性管理", requiredPermission = true, requirePermissionCode = ThemeConstants.THEME_MANAGER)
public class SysThemeTemplateFieldController {

    @Resource
    private SysThemeTemplateFieldService sysThemeTemplateFieldService;

    /**
     * 增加系统主题模板属性
     *
     * @author xixiaowei
     * @since 2021/12/17 11:22
     */
    @PostResource(name = "增加系统主题模板属性", path = "/sysThemeTemplateField/add")
    @ApiLog
    public ResponseData<?> add(
            @RequestBody @Validated(SysThemeTemplateFieldRequest.add.class) SysThemeTemplateFieldRequest sysThemeTemplateFieldParam) {
        sysThemeTemplateFieldService.add(sysThemeTemplateFieldParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统主题模板属性
     *
     * @author xixiaowei
     * @since 2021/12/17 11:25
     */
    @PostResource(name = "删除系统主题模板属性", path = "/sysThemeTemplateField/del")
    @ApiLog
    public ResponseData<?> del(
            @RequestBody @Validated(SysThemeTemplateFieldRequest.delete.class) SysThemeTemplateFieldRequest sysThemeTemplateFieldParam) {
        sysThemeTemplateFieldService.del(sysThemeTemplateFieldParam);
        return new SuccessResponseData<>();
    }

    /**
     * 修改系统主题模板属性
     *
     * @author xixiaowei
     * @since 2021/12/17 11:38
     */
    @PostResource(name = "修改系统模板属性", path = "/sysThemeTemplateField/edit")
    @ApiLog
    public ResponseData<?> edit(
            @RequestBody @Validated(SysThemeTemplateFieldRequest.edit.class) SysThemeTemplateFieldRequest sysThemeTemplateFieldParam) {
        sysThemeTemplateFieldService.edit(sysThemeTemplateFieldParam);
        return new SuccessResponseData<>();
    }

    /**
     * 查询系统主题模板属性详情
     *
     * @author xixiaowei
     * @since 2021/12/17 11:49
     */
    @GetResource(name = "查询系统主题模板属性详情", path = "/sysThemeTemplateField/detail")
    public ResponseData<SysThemeTemplateField> detail(
            @Validated(SysThemeTemplateFieldRequest.detail.class) SysThemeTemplateFieldRequest sysThemeTemplateFieldParam) {
        return new SuccessResponseData<>(sysThemeTemplateFieldService.detail(sysThemeTemplateFieldParam));
    }

    /**
     * 查询系统主题模板属性列表
     *
     * @author xixiaowei
     * @since 2021/12/24 9:47
     */
    @GetResource(name = "查询系统主题模板属性列表", path = "/sysThemeTemplateField/findPage")
    public ResponseData<PageResult<SysThemeTemplateField>> findPage(SysThemeTemplateFieldRequest sysThemeTemplateFieldParam) {
        return new SuccessResponseData<>(sysThemeTemplateFieldService.findPage(sysThemeTemplateFieldParam));
    }

    /**
     * 查询系统主题模板属性已有关系列表
     *
     * @author xixiaowei
     * @since 2021/12/24 14:42
     */
    @GetResource(name = "查询系统主题模板属性已有关系列表", path = "/sysThemeTemplateField/findRelList")
    public ResponseData<List<SysThemeTemplateField>> findRelPage(SysThemeTemplateFieldRequest sysThemeTemplateFieldParam) {
        return new SuccessResponseData<>(sysThemeTemplateFieldService.findRelList(sysThemeTemplateFieldParam));
    }

    /**
     * 查询系统主题模板属性未有关系列表
     *
     * @author xixiaowei
     * @since 2021/12/24 14:44
     */
    @GetResource(name = "查询系统主题模板属性未有关系列表", path = "/sysThemeTemplateField/findNotRelList")
    public ResponseData<List<SysThemeTemplateField>> findNotRelPage(SysThemeTemplateFieldRequest sysThemeTemplateFieldParam) {
        return new SuccessResponseData<>(sysThemeTemplateFieldService.findNotRelList(sysThemeTemplateFieldParam));
    }
}
