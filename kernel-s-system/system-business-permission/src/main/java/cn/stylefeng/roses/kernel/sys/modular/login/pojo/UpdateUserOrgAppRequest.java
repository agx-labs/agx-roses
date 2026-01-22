package cn.stylefeng.roses.kernel.sys.modular.login.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

/**
 * 切换当前用户激活的组织机构id或者应用id
 *
 * @author fengshuonan
 * @since 2023/6/21 16:05
 */
@Data
public class UpdateUserOrgAppRequest {

    /**
     * 切换后的组织机构id
     */
    @ChineseDescription("切换后的组织机构id")
    private Long newOrgId;

    /**
     * 切换后的应用id
     */
    @ChineseDescription("切换后的应用id")
    private Long newAppId;

}
