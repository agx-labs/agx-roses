package cn.stylefeng.roses.kernel.favorite.modular.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户收藏信息封装类
 *
 * @author fengshuonan
 * @since 2023/11/21 22:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserFavoriteRequest extends BaseRequest {

    /**
     * 主键id
     */
    @ChineseDescription("主键id")
    private Long favoriteId;

    /**
     * 收藏业务的类型，存业务编码
     */
    @ChineseDescription("收藏业务的类型，存业务编码")
    @NotBlank(message = "收藏业务类型不能为空", groups = {add.class, delete.class})
    private String favType;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 具体业务id
     */
    @ChineseDescription("具体业务id")
    @NotNull(message = "具体业务id不能为空", groups = {add.class, delete.class})
    private Long businessId;

    /**
     * 批量删除用的id集合
     */
    @ChineseDescription("批量删除用的id集合")
    private List<Long> batchDeleteIdList;

}
