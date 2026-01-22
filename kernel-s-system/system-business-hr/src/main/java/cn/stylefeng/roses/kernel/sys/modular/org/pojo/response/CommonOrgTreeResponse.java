package cn.stylefeng.roses.kernel.sys.modular.org.pojo.response;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织机构树响应结果
 *
 * @author fengshuonan
 * @since 2023/7/17 11:04
 */
@Data
public class CommonOrgTreeResponse {

    /**
     * 组织机构树列表
     */
    @ChineseDescription("组织机构树列表")
    private List<HrOrganization> orgTreeList;

    /**
     * 展开显示组织机构id的列表
     */
    @ChineseDescription("展开显示组织机构id的列表")
    private List<Long> expandOrgIdList = new ArrayList<>();

    public CommonOrgTreeResponse() {

    }

    public CommonOrgTreeResponse(List<HrOrganization> orgTreeList, List<Long> expandOrgIdList) {
        this.orgTreeList = orgTreeList;
        this.expandOrgIdList = expandOrgIdList;
    }

}
