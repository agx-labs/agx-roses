package cn.stylefeng.roses.kernel.sys.modular.userapp.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户常用功能封装类
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PortalUserAppRequest extends BaseRequest {

    /**
     * 用户选择的关联的菜单id集合
     */
    @ChineseDescription("用户选择的关联的菜单id集合")
    @NotEmpty(message = "菜单id集合不能为空", groups = edit.class)
    private List<Long> menuIdList;

}
