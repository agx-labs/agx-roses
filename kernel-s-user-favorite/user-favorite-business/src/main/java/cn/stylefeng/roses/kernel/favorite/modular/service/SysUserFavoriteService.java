package cn.stylefeng.roses.kernel.favorite.modular.service;

import cn.stylefeng.roses.kernel.favorite.modular.api.UserFavoriteApi;
import cn.stylefeng.roses.kernel.favorite.modular.entity.SysUserFavorite;
import cn.stylefeng.roses.kernel.favorite.modular.pojo.request.SysUserFavoriteRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户收藏信息服务类
 *
 * @author fengshuonan
 * @since 2023/11/21 22:09
 */
public interface SysUserFavoriteService extends IService<SysUserFavorite>, UserFavoriteApi {

    /**
     * 绑定用户收藏信息
     *
     * @author fengshuonan
     * @since 2023/11/21 22:48
     */
    void bind(SysUserFavoriteRequest sysUserFavoriteRequest);

    /**
     * 取消用户绑定的收藏
     *
     * @author fengshuonan
     * @since 2023/11/21 22:54
     */
    void unBind(SysUserFavoriteRequest sysUserFavoriteRequest);

}
