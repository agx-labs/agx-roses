package cn.stylefeng.roses.kernel.favorite.modular.controller;

import cn.stylefeng.roses.kernel.favorite.modular.pojo.request.SysUserFavoriteRequest;
import cn.stylefeng.roses.kernel.favorite.modular.service.SysUserFavoriteService;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用收藏控制器
 *
 * @author fengshuonan
 * @since 2023/11/21 22:36
 */
@RestController
@ApiResource(name = "通用收藏控制器")
public class FavoriteController {

    @Resource
    private SysUserFavoriteService sysUserFavoriteService;

    /**
     * 绑定用户收藏
     *
     * @author fengshuonan
     * @since 2023/11/21 22:46
     */
    @PostResource(name = "绑定用户收藏", path = "/favorite/bind")
    public ResponseData<?> bind(@RequestBody @Validated(BaseRequest.add.class) SysUserFavoriteRequest sysUserFavoriteRequest) {
        sysUserFavoriteService.bind(sysUserFavoriteRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 取消用户绑定的收藏
     *
     * @author fengshuonan
     * @since 2023/11/21 22:46
     */
    @PostResource(name = "取消用户绑定的收藏", path = "/favorite/unBind")
    public ResponseData<?> unBind(@RequestBody @Validated(BaseRequest.delete.class) SysUserFavoriteRequest sysUserFavoriteRequest) {
        sysUserFavoriteService.unBind(sysUserFavoriteRequest);
        return new SuccessResponseData<>();
    }

}
