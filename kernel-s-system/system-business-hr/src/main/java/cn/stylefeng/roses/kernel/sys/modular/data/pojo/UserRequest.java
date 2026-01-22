package cn.stylefeng.roses.kernel.sys.modular.data.pojo;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;

/**
 * 用户请求的参数
 *
 * @author fengshuonan
 * @since 2024-01-29 14:36
 */
@Data
public class UserRequest extends BaseRequest {

    /**
     * 组织机构id
     */
    private Long org_id;

    /**
     * 机构id
     */
    private Long dept_id;

}
