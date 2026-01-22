package cn.stylefeng.roses.kernel.sys.modular.org.mapper;

import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.OrganizationLevelRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.OrganizationLevelVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织机构层级 Mapper 接口
 *
 * @author fengshuonan
 * @since 2025/01/22 09:44
 */
public interface OrganizationLevelMapper extends BaseMapper<OrganizationLevel> {

    /**
     * 获取自定义查询列表
     *
     * @author fengshuonan
     * @since 2025/01/22 09:44
     */
    List<OrganizationLevelVo> customFindList(@Param("page") Page page, @Param("param")OrganizationLevelRequest request);

}
