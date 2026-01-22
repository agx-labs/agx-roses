package cn.stylefeng.roses.kernel.favorite.modular.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.favorite.modular.entity.SysUserFavorite;
import cn.stylefeng.roses.kernel.favorite.modular.mapper.SysUserFavoriteMapper;
import cn.stylefeng.roses.kernel.favorite.modular.pojo.request.SysUserFavoriteRequest;
import cn.stylefeng.roses.kernel.favorite.modular.service.SysUserFavoriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户收藏信息业务实现层
 *
 * @author fengshuonan
 * @since 2023/11/21 22:09
 */
@Service
public class SysUserFavoriteServiceImpl extends ServiceImpl<SysUserFavoriteMapper, SysUserFavorite> implements SysUserFavoriteService {

    @Override
    public List<Long> getFavoriteBusinessId(Long userId, String favType) {

        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(favType)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysUserFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserFavorite::getUserId, userId);
        queryWrapper.eq(SysUserFavorite::getFavType, favType);

        // 只查询业务id
        queryWrapper.select(SysUserFavorite::getBusinessId);

        List<SysUserFavorite> sysUserFavoriteList = this.list(queryWrapper);
        if (ObjectUtil.isEmpty(sysUserFavoriteList)) {
            return new ArrayList<>();
        }

        return sysUserFavoriteList.stream().map(SysUserFavorite::getBusinessId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getCurrentUserFavoriteBusinessId(String favType) {
        if (ObjectUtil.isEmpty(favType)) {
            return new ArrayList<>();
        }

        LoginUser loginUserNullable = LoginContext.me().getLoginUserNullable();
        if (loginUserNullable == null) {
            return new ArrayList<>();
        }

        return this.getFavoriteBusinessId(loginUserNullable.getUserId(), favType);
    }

    @Override
    public void bind(SysUserFavoriteRequest sysUserFavoriteRequest) {

        String favType = sysUserFavoriteRequest.getFavType();
        Long businessId = sysUserFavoriteRequest.getBusinessId();
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 查询是否用户已经绑定了收藏
        LambdaQueryWrapper<SysUserFavorite> sysUserFavoriteLambdaQueryWrapper = createCommonUserBindWrapper(
                userId, businessId, favType);
        long count = this.count(sysUserFavoriteLambdaQueryWrapper);
        if (count > 0) {
            return;
        }

        // 新绑定用户收藏
        SysUserFavorite sysUserFavorite = new SysUserFavorite();
        sysUserFavorite.setFavType(favType);
        sysUserFavorite.setUserId(userId);
        sysUserFavorite.setBusinessId(businessId);
        sysUserFavorite.setFavTime(new Date());
        this.save(sysUserFavorite);
    }

    @Override
    public void unBind(SysUserFavoriteRequest sysUserFavoriteRequest) {

        String favType = sysUserFavoriteRequest.getFavType();
        Long businessId = sysUserFavoriteRequest.getBusinessId();
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 直接取消绑定
        LambdaQueryWrapper<SysUserFavorite> sysUserFavoriteLambdaQueryWrapper = createCommonUserBindWrapper(
                userId, businessId, favType);
        this.remove(sysUserFavoriteLambdaQueryWrapper);
    }

    /**
     * 创建通用wrapper
     *
     * @author fengshuonan
     * @since 2023/11/21 22:55
     */
    private static LambdaQueryWrapper<SysUserFavorite> createCommonUserBindWrapper(Long userId, Long businessId, String favType) {
        LambdaQueryWrapper<SysUserFavorite> sysUserFavoriteLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserFavoriteLambdaQueryWrapper.eq(SysUserFavorite::getUserId, userId);
        sysUserFavoriteLambdaQueryWrapper.eq(SysUserFavorite::getBusinessId, businessId);
        sysUserFavoriteLambdaQueryWrapper.eq(SysUserFavorite::getFavType, favType);
        return sysUserFavoriteLambdaQueryWrapper;
    }

}
