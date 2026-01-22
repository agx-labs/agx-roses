package cn.stylefeng.roses.kernel.sys.modular.theme.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.annotation.ApiLog;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.theme.constants.ThemeConstants;
import cn.stylefeng.roses.kernel.sys.modular.theme.entity.SysThemeTemplate;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeTemplateDataDTO;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeTemplateRequest;
import cn.stylefeng.roses.kernel.sys.modular.theme.service.SysThemeTemplateService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统主题模板控制器
 *
 * @author xixiaowei
 * @since 2021/12/17 13:53
 */
@RestController
@ApiResource(name = "系统主题模板管理", requiredPermission = true, requirePermissionCode = ThemeConstants.THEME_MANAGER)
public class SysThemeTemplateController {

    @Resource
    private SysThemeTemplateService sysThemeTemplateService;

    /**
     * 增加系统主题模板
     *
     * @author xixiaowei
     * @since 2021/12/17 14:16
     */
    @PostResource(name = "增加系统主题模板", path = "/sysThemeTemplate/add")
    @ApiLog
    public ResponseData<?> add(@RequestBody @Validated(SysThemeTemplateRequest.add.class) SysThemeTemplateRequest sysThemeTemplateParam) {
        sysThemeTemplateService.add(sysThemeTemplateParam);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑系统主题模板
     *
     * @author xixiaowei
     * @since 2021/12/17 14:36
     */
    @PostResource(name = "编辑系统主题模板", path = "/sysThemeTemplate/edit")
    @ApiLog
    public ResponseData<?> edit(@RequestBody @Validated(SysThemeTemplateRequest.edit.class) SysThemeTemplateRequest sysThemeTemplateParam) {
        sysThemeTemplateService.edit(sysThemeTemplateParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统主题模板
     *
     * @author xixiaowei
     * @since 2021/12/17 14:47
     */
    @PostResource(name = "删除系统主题模板", path = "/sysThemeTemplate/del")
    @ApiLog
    public ResponseData<?> del(
            @RequestBody @Validated(SysThemeTemplateRequest.delete.class) SysThemeTemplateRequest sysThemeTemplateParam) {
        sysThemeTemplateService.del(sysThemeTemplateParam);
        return new SuccessResponseData<>();
    }

    /**
     * 查询系统主题模板
     *
     * @author xixiaowei
     * @since 2021/12/17 15:00
     */
    @GetResource(name = "查询系统主题模板", path = "/sysThemeTemplate/findPage")
    public ResponseData<PageResult<SysThemeTemplate>> findPage(SysThemeTemplateRequest sysThemeTemplateParam) {
        return new SuccessResponseData<>(sysThemeTemplateService.findPage(sysThemeTemplateParam));
    }

    /**
     * 查询系统主题模板列表
     *
     * @author xixiaowei
     * @since 2021/12/29 9:12
     */
    @GetResource(name = "查询系统主题模板列表", path = "/sysThemeTemplate/findList")
    public ResponseData<List<SysThemeTemplate>> findList(SysThemeTemplateRequest sysThemeTemplateParam) {
        return new SuccessResponseData<>(sysThemeTemplateService.findList(sysThemeTemplateParam));
    }

    /**
     * 修改系统主题模板状态
     *
     * @author xixiaowei
     * @since 2021/12/17 15:09
     */
    @PostResource(name = "修改系统主题模板状态", path = "/sysThemeTemplate/updateStatus")
    @ApiLog
    public ResponseData<?> updateTemplateStatus(
            @RequestBody @Validated(SysThemeTemplateRequest.updateStatus.class) SysThemeTemplateRequest sysThemeTemplateParam) {
        sysThemeTemplateService.updateTemplateStatus(sysThemeTemplateParam);
        return new SuccessResponseData<>();
    }

    /**
     * 查询系统主题模板详情
     *
     * @author xixiaowei
     * @since 2021/12/17 16:09
     */
    @GetResource(name = "查询系统主题模板详情", path = "/sysThemeTemplate/detail")
    public ResponseData<List<SysThemeTemplateDataDTO>> detail(
            @Validated(BaseRequest.detail.class) SysThemeTemplateRequest sysThemeTemplateParam) {
        return new SuccessResponseData<>(sysThemeTemplateService.detail(sysThemeTemplateParam));
    }
}
