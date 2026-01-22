package cn.stylefeng.roses.kernel.sys.modular.role.mapper;

import cn.stylefeng.roses.kernel.sys.modular.role.entity.RoleCategory;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleCategoryRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleCategoryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色分类 Mapper 接口
 *
 * @author fengshuonan
 * @since 2025/01/22 17:40
 */
public interface RoleCategoryMapper extends BaseMapper<RoleCategory> {

    /**
     * 获取自定义查询列表
     *
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    List<RoleCategoryVo> customFindList(@Param("page") Page page, @Param("param")RoleCategoryRequest request);

}
