package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.RoleCategory;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleCategoryRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.RoleCategoryService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色分类控制器
 *
 * @author fengshuonan
 * @since 2025/01/22 17:40
 */
@RestController
@ApiResource(name = "角色分类")
public class RoleCategoryController {

    @Resource
    private RoleCategoryService roleCategoryService;

    /**
     * 获取角色分类树
     *
     * @author fengshuonan
     * @since 2025/1/22 17:45
     */
    @GetResource(name = "获取角色分类树", path = "/roleCategory/treeList")
    public ResponseData<List<RoleCategory>> treeList(@Validated(BaseRequest.list.class) RoleCategoryRequest roleCategoryRequest) {
        return new SuccessResponseData<>(roleCategoryService.treeList(roleCategoryRequest));
    }

    /**
     * 添加角色分类
     *
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    @PostResource(name = "添加角色分类", path = "/roleCategory/add")
    public ResponseData<RoleCategory> add(@RequestBody @Validated(RoleCategoryRequest.add.class) RoleCategoryRequest roleCategoryRequest) {
        roleCategoryService.add(roleCategoryRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除角色分类
     *
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    @PostResource(name = "删除角色分类", path = "/roleCategory/delete")
    public ResponseData<?> delete(@RequestBody @Validated(RoleCategoryRequest.delete.class) RoleCategoryRequest roleCategoryRequest) {
        roleCategoryService.del(roleCategoryRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑角色分类
     *
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    @PostResource(name = "编辑角色分类", path = "/roleCategory/edit")
    public ResponseData<?> edit(@RequestBody @Validated(RoleCategoryRequest.edit.class) RoleCategoryRequest roleCategoryRequest) {
        roleCategoryService.edit(roleCategoryRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看角色分类详情
     *
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    @GetResource(name = "查看角色分类详情", path = "/roleCategory/detail")
    public ResponseData<RoleCategory> detail(@Validated(RoleCategoryRequest.detail.class) RoleCategoryRequest roleCategoryRequest) {
        return new SuccessResponseData<>(roleCategoryService.detail(roleCategoryRequest));
    }

}
