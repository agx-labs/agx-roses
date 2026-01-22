package cn.stylefeng.roses.kernel.sys.modular.org.service;

import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.sys.api.OrgApproverServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrgApprover;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrgApproverRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 组织机构审批人 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
public interface HrOrgApproverService extends IService<HrOrgApprover>, OrgApproverServiceApi {

    /**
     * 删除
     *
     * @param hrOrgApproverRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void del(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 获取所有审批人类型列表
     *
     * @return 审批人类型列表
     * @author fengshuonan
     * @since 2022/9/26 10:18
     */
    List<SimpleDict> getApproverTypeList();

    /**
     * 获取组织机构绑定的审批人列表
     *
     * @author fengshuonan
     * @since 2023/6/11 15:23
     */
    List<HrOrgApprover> getOrgApproverList(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 组织机构审批人，绑定用户（可以绑定多个）
     *
     * @author fengshuonan
     * @since 2023/6/11 15:51
     */
    void bindUserList(HrOrgApproverRequest hrOrgApproverRequest);

}