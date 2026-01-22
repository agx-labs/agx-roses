package cn.stylefeng.roses.kernel.favorite.modular.mapper;

import cn.stylefeng.roses.kernel.favorite.modular.entity.SysUserFavorite;
import cn.stylefeng.roses.kernel.favorite.modular.pojo.request.SysUserFavoriteRequest;
import cn.stylefeng.roses.kernel.favorite.modular.pojo.response.SysUserFavoriteVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户收藏信息 Mapper 接口
 *
 * @author fengshuonan
 * @since 2023/11/21 22:09
 */
public interface SysUserFavoriteMapper extends BaseMapper<SysUserFavorite> {

    /**
     * 获取自定义查询列表
     *
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    List<SysUserFavoriteVo> customFindList(@Param("page") Page page, @Param("param")SysUserFavoriteRequest request);

}
