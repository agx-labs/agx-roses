package cn.stylefeng.roses.kernel.sys.modular.org.pojo.response;

import lombok.Data;

/**
 * 组织机构信息统计
 *
 * @author fengshuonan
 * @since 2023/6/26 22:52
 */
@Data
public class HomeCompanyInfo {

    /**
     * 所有组织机构数
     */
    private Long organizationNum;

    /**
     * 所有企业人员总数
     */
    private Long enterprisePersonNum;

    /**
     * 所有职位总数
     */
    private Long positionNum;

    /**
     * 当前登录用户，所在公司的部门数量
     */
    private Integer currentDeptNum;

    /**
     * 当前登录用户，所在公司的总人数
     */
    private Long currentCompanyPersonNum;
}
