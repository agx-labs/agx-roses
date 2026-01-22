package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.sys.modular.role.entity.RoleCategory;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleCategoryRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色分类服务类
 *
 * @author fengshuonan
 * @since 2025/01/22 17:40
 */
public interface RoleCategoryService extends IService<RoleCategory> {

    /**
     * 获取角色分类树
     *
     * @param roleCategoryRequest 请求参数
     * @return List<RoleCategory>  返回结果
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    List<RoleCategory> treeList(RoleCategoryRequest roleCategoryRequest);

    /**
     * 新增角色分类
     *
     * @param roleCategoryRequest 请求参数
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    void add(RoleCategoryRequest roleCategoryRequest);

    /**
     * 删除角色分类
     *
     * @param roleCategoryRequest 请求参数
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    void del(RoleCategoryRequest roleCategoryRequest);

    /**
     * 编辑角色分类
     *
     * @param roleCategoryRequest 请求参数
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    void edit(RoleCategoryRequest roleCategoryRequest);

    /**
     * 查询详情角色分类
     *
     * @param roleCategoryRequest 请求参数
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    RoleCategory detail(RoleCategoryRequest roleCategoryRequest);

}
