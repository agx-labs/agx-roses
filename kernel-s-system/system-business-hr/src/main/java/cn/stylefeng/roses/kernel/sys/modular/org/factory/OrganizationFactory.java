package cn.stylefeng.roses.kernel.sys.modular.org.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.constants.SymbolConstant;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 组织机构创建工厂
 *
 * @author fengshuonan
 * @since 2023/6/11 11:22
 */
public class OrganizationFactory {

    /**
     * 通过组织机构列表，获取这个组织机构列表的所有父级id集合
     *
     * @author fengshuonan
     * @since 2023/6/11 11:22
     */
    public static Set<Long> getOrgParentIdList(List<HrOrganization> organizationList) {

        Set<Long> orgIdList = new HashSet<>();

        for (HrOrganization hrOrganization : organizationList) {
            String orgParentIdListStr = hrOrganization.getOrgPids();

            // 去掉中括号符号
            orgParentIdListStr = orgParentIdListStr.replaceAll("\\[", "");
            orgParentIdListStr = orgParentIdListStr.replaceAll("]", "");

            // 获取所有上级id列表
            String[] orgParentIdList = orgParentIdListStr.split(",");

            for (String orgIdStr : orgParentIdList) {
                Long orgId = Convert.toLong(orgIdStr);
                orgIdList.add(orgId);
            }
        }

        return orgIdList;
    }

    /**
     * 填充该节点的pIds
     * <p>
     * 如果pid是顶级节点，pids就是 [-1],
     * <p>
     * 如果pid不是顶级节点，pids就是父节点的pids + [pid] + ,
     *
     * @author fengshuonan
     * @since 2020/11/5 13:45
     */
    public static void fillParentIds(HrOrganization hrOrganization) {

        // 如果父级是-1，则代表顶级节点
        if (TreeConstants.DEFAULT_PARENT_ID.equals(hrOrganization.getOrgParentId())) {
            hrOrganization.setOrgPids(SymbolConstant.LEFT_SQUARE_BRACKETS + TreeConstants.DEFAULT_PARENT_ID + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA);
        }

        // 如果不是顶级节点，则查询到父级的id集合，再拼接上级id即可
        else {

            HrOrganizationService hrOrganizationService = SpringUtil.getBean(HrOrganizationService.class);

            // 获取父组织机构
            HrOrganizationRequest hrOrganizationRequest = new HrOrganizationRequest();
            hrOrganizationRequest.setOrgId(hrOrganization.getOrgParentId());
            HrOrganization parentOrganization = hrOrganizationService.detail(hrOrganizationRequest);

            // 设置本节点的父ids为 (上一个节点的pids + (上级节点的id) )
            hrOrganization.setOrgPids(parentOrganization.getOrgPids() + SymbolConstant.LEFT_SQUARE_BRACKETS + parentOrganization.getOrgId() + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA);
        }
    }

}
