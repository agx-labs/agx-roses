package cn.stylefeng.roses.kernel.sys.api.pojo.org;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 组织机构层级封装类
 *
 * @author fengshuonan
 * @since 2025/01/22 09:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationLevelRequest extends BaseRequest {

    /**
     * 组织机构层级不能为空
     */
    @NotEmpty(message = "组织机构层级不能为空", groups = edit.class)
    @ChineseDescription("组织机构层级")
    private List<OrganizationLevel> levelList;

}