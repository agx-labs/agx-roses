package cn.stylefeng.roses.kernel.sys.modular.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.context.DbOperatorContext;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.db.mp.datascope.annotations.DataScope;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.db.mp.datascope.holder.DataScopeHolder;
import cn.stylefeng.roses.kernel.dsctn.api.context.DataSourceContext;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.pidset.ParentIdParseUtil;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.tree.factory.SortedTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveOrgCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import cn.stylefeng.roses.kernel.sys.api.enums.org.DetectModeEnum;
import cn.stylefeng.roses.kernel.sys.api.enums.org.OrgTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.exception.enums.OrgExceptionEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.HrOrganizationDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.OrganizationLevelRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.constants.OrgConstants;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.factory.OrgConditionFactory;
import cn.stylefeng.roses.kernel.sys.modular.org.factory.OrganizationFactory;
import cn.stylefeng.roses.kernel.sys.modular.org.mapper.HrOrganizationMapper;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.CommonOrgTreeRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.CommonOrgTreeResponse;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.HomeCompanyInfo;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;
import cn.stylefeng.roses.kernel.sys.modular.org.service.OrganizationLevelService;
import cn.stylefeng.roses.kernel.sys.modular.position.service.HrPositionService;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.stylefeng.roses.kernel.rule.constants.SymbolConstant.LEFT_SQUARE_BRACKETS;
import static cn.stylefeng.roses.kernel.rule.constants.SymbolConstant.RIGHT_SQUARE_BRACKETS;

/**
 * 组织机构信息业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@Service
public class HrOrganizationServiceImpl extends ServiceImpl<HrOrganizationMapper, HrOrganization> implements HrOrganizationService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private HrPositionService hrPositionService;

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource(name = "sysOrgSubFlagCache")
    private CacheOperatorApi<Boolean> sysOrgSubFlagCache;

    @Resource(name = "sysOrgInfoCache")
    private CacheOperatorApi<HrOrganizationDTO> sysOrgInfoCache;

    @Resource
    private OrganizationLevelService organizationLevelService;

    @Override
    public void add(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = new HrOrganization();
        BeanUtil.copyProperties(hrOrganizationRequest, hrOrganization);

        // 填充父级parentIds
        OrganizationFactory.fillParentIds(hrOrganization);

        this.save(hrOrganization);

        // 发布一个新增组织机构的事件
        BusinessEventPublisher.publishEvent(OrgConstants.ADD_ORG_EVENT, hrOrganization);

        // 记录日志
        BusinessLogUtil.setLogTitle("添加机构，机构名称：" + hrOrganizationRequest.getOrgName());
        BusinessLogUtil.addContent("新增的机构详情如下：\n", hrOrganization);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(HrOrganizationRequest hrOrganizationRequest) {

        // 查询被删除组织机构的所有子级节点
        Set<Long> totalOrgIdSet = DbOperatorContext.me()
                .findSubListByParentId("sys_hr_organization", "org_pids", "org_id", hrOrganizationRequest.getOrgId());
        totalOrgIdSet.add(hrOrganizationRequest.getOrgId());

        // 执行删除操作
        this.baseDelete(totalOrgIdSet);

        // 发布删除机构的事件
        BusinessEventPublisher.publishEvent(OrgConstants.DELETE_ORG_EVENT, totalOrgIdSet);

        // 记录日志
        BusinessLogUtil.setLogTitle("删除机构，机构ID：" + hrOrganizationRequest.getOrgId());
        BusinessLogUtil.addContent("删除机构，机构ID：", hrOrganizationRequest.getOrgId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(HrOrganizationRequest hrOrganizationRequest) {

        Set<Long> orgIdList = hrOrganizationRequest.getOrgIdList();

        // 批量查询组织机构下的下属机构
        for (Long orgId : orgIdList) {
            // 查询被删除组织机构的所有子级节点
            Set<Long> tempSubOrgIdList = DbOperatorContext.me().findSubListByParentId("sys_hr_organization", "org_pids", "org_id", orgId);
            orgIdList.addAll(tempSubOrgIdList);
        }

        // 执行删除操作
        this.baseDelete(orgIdList);

        // 发布删除机构的事件
        BusinessEventPublisher.publishEvent(OrgConstants.DELETE_ORG_EVENT, orgIdList);

        // 记录日志
        BusinessLogUtil.setLogTitle("批量删除机构");
        BusinessLogUtil.addContent("批量删除机构，id集合为：", orgIdList);
    }

    @Override
    public void edit(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = this.queryHrOrganization(hrOrganizationRequest);

        BusinessLogUtil.setLogTitle("更新机构信息，机构名称为：", hrOrganization.getOrgName());
        BusinessLogUtil.addContent("更新前的机构信息为：\n", hrOrganization);

        BeanUtil.copyProperties(hrOrganizationRequest, hrOrganization);

        // 填充父级parentIds
        OrganizationFactory.fillParentIds(hrOrganization);

        this.updateById(hrOrganization);

        // 发布编辑机构事件
        BusinessEventPublisher.publishEvent(OrgConstants.EDIT_ORG_EVENT, hrOrganization.getOrgId());

        // 记录日志
        BusinessLogUtil.addContent("更新后的机构信息为：\n", hrOrganization);

    }

    @Override
    public HrOrganization detail(HrOrganizationRequest hrOrganizationRequest) {

        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(HrOrganization::getOrgId, hrOrganizationRequest.getOrgId());
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName, HrOrganization::getOrgShortName, HrOrganization::getOrgCode,
                HrOrganization::getOrgParentId, HrOrganization::getOrgSort, HrOrganization::getOrgType, HrOrganization::getStatusFlag,
                HrOrganization::getTaxNo, HrOrganization::getRemark, HrOrganization::getOrgPids, HrOrganization::getLevelCode);

        HrOrganization hrOrganization = this.getOne(wrapper, false);
        if (ObjectUtil.isEmpty(hrOrganization)) {
            throw new ServiceException(OrgExceptionEnum.HR_ORGANIZATION_NOT_EXISTED);
        }

        // 获取机构的上级机构名称
        String parentOrgName = this.getOrgNameById(hrOrganization.getOrgParentId());
        hrOrganization.setParentOrgName(parentOrgName);

        return hrOrganization;
    }

    @Override
    public List<HrOrganization> findList(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> wrapper = OrgConditionFactory.createWrapper(hrOrganizationRequest);
        return this.list(wrapper);
    }

    @Override
    @DataScope(userIdFieldName = "create_user")
    public PageResult<HrOrganization> findPage(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> wrapper = OrgConditionFactory.createWrapper(hrOrganizationRequest);

        // 只查询需要的字段
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName, HrOrganization::getOrgCode, HrOrganization::getStatusFlag,
                HrOrganization::getOrgType, HrOrganization::getOrgSort, BaseEntity::getCreateTime, HrOrganization::getLevelCode);

        Page<HrOrganization> hrOrganizationPage = this.page(PageFactory.defaultPage(), wrapper);
        List<HrOrganization> records = hrOrganizationPage.getRecords();

        // 填充组织机构层级的详情
        List<OrganizationLevel> list;
        DataScopeConfig tempDataScopeConfig = DataScopeHolder.get();
        try {
            DataScopeHolder.set(null);
            list = organizationLevelService.findList(new OrganizationLevelRequest());
        } finally {
            DataScopeHolder.set(tempDataScopeConfig);
        }
        for (HrOrganization record : records) {
            record.setOrganizationLevel(list.stream().filter(item -> item.getLevelCode().equals(record.getLevelCode())).findFirst().orElse(null));
        }

        return PageResultFactory.createPageResult(hrOrganizationPage);
    }

    @Override
    @DataScope(userIdFieldName = "create_user")
    public PageResult<HrOrganization> commonOrgPage(HrOrganizationRequest hrOrganizationRequest) {

        // 只查询未禁用的组织机构
        hrOrganizationRequest.setStatusFlag(StatusEnum.ENABLE.getCode());
        LambdaQueryWrapper<HrOrganization> wrapper = OrgConditionFactory.createWrapper(hrOrganizationRequest);

        // 只查询需要的字段
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName, HrOrganization::getOrgCode, HrOrganization::getOrgType,
                HrOrganization::getStatusFlag, HrOrganization::getOrgParentId);

        Page<HrOrganization> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);

        // 将每个机构的公司名称返回
        for (HrOrganization hrOrganization : sysRolePage.getRecords()) {
            CompanyDeptDTO companyInfo = this.getOrgCompanyInfo(hrOrganization);
            if (companyInfo == null) {
                continue;
            }
            hrOrganization.setCompanyName(companyInfo.getCompanyName());
        }

        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    @DataScope(userIdFieldName = "create_user")
    public CommonOrgTreeResponse commonOrgTree(CommonOrgTreeRequest commonOrgTreeRequest) {

        // 预填充一些查询参数
        OrgConditionFactory.prepareRequest(commonOrgTreeRequest);

        // 根据条件查询组织机构列表
        LambdaQueryWrapper<HrOrganization> wrapper = OrgConditionFactory.createCommonTreeWrapper(commonOrgTreeRequest, this);
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgPids, HrOrganization::getOrgParentId, HrOrganization::getOrgName,
                HrOrganization::getOrgSort, HrOrganization::getOrgType);
        List<HrOrganization> hrOrganizationList = this.list(wrapper);
        if (ObjectUtil.isEmpty(hrOrganizationList)) {
            return new CommonOrgTreeResponse(hrOrganizationList, new ArrayList<>());
        }

        // 构建树形结构
        hrOrganizationList = new SortedTreeBuildFactory<HrOrganization>().doTreeBuild(hrOrganizationList);

        // 遍历所有节点，查询这些节点有没有子级，填充haveSubOrgFlag
        this.fillHaveSubFlag(hrOrganizationList);

        // 遍历这些节点，如果有children的，都展开，并搜集到数组里
        List<Long> expandOrgIds = new ArrayList<>();
        this.fillExpandFlag(hrOrganizationList, expandOrgIds);

        return new CommonOrgTreeResponse(hrOrganizationList, expandOrgIds);
    }

    @Override
    public CompanyDeptDTO getCompanyDeptInfo(Long orgId) {

        if (orgId == null) {
            return null;
        }

        HrOrganization hrOrganization = this.getById(orgId);
        if (hrOrganization == null) {
            return null;
        }

        // 获取当前组织机构id是公司还是部门，如果是公司，则直接返回结果
        if (OrgTypeEnum.COMPANY.getCode().equals(hrOrganization.getOrgType())) {
            return new CompanyDeptDTO(hrOrganization.getOrgId(), hrOrganization.getOrgName());
        }

        // 如果是部门，则递归向上查询到部门所属的公司id
        CompanyDeptDTO orgCompanyInfo = this.getOrgCompanyInfo(hrOrganization);

        // 查到公司id之后，设置部门id则为参数orgId
        if (orgCompanyInfo != null) {
            orgCompanyInfo.setDeptId(hrOrganization.getOrgId());
            orgCompanyInfo.setDeptName(hrOrganization.getOrgName());
        }

        return orgCompanyInfo;
    }

    @Override
    public CompanyDeptDTO getOrgCompanyInfo(HrOrganization hrOrganization) {

        if (hrOrganization == null) {
            return null;
        }

        // 如果是到了根节点，则直接返回当前根节点信息
        if (TreeConstants.DEFAULT_PARENT_ID.equals(hrOrganization.getOrgParentId())) {
            return new CompanyDeptDTO(hrOrganization.getOrgId(), hrOrganization.getOrgName());
        }

        // 如果当前已经是公司类型，则直接返回
        if (OrgTypeEnum.COMPANY.getCode().equals(hrOrganization.getOrgType())) {
            return new CompanyDeptDTO(hrOrganization.getOrgId(), hrOrganization.getOrgName());
        }

        // 查询父级是否是公司
        Long orgParentId = hrOrganization.getOrgParentId();
        HrOrganizationDTO parentOrgInfo = this.getOrgInfo(orgParentId);

        HrOrganization parentEntity = new HrOrganization();
        BeanUtil.copyProperties(parentOrgInfo, parentEntity);
        return this.getOrgCompanyInfo(parentEntity);
    }

    @Override
    public CompanyDeptDTO getOrgCompanyInfo(Long orgId) {

        if (orgId == null) {
            return null;
        }

        // 查询组织机构对应的信息
        HrOrganizationDTO orgInfo = this.getOrgInfo(orgId);
        if (orgInfo == null) {
            return null;
        }

        // 查询机构对应的公司部门信息
        HrOrganization hrOrganization = new HrOrganization();
        BeanUtil.copyProperties(orgInfo, hrOrganization);
        return this.getOrgCompanyInfo(hrOrganization);
    }

    @Override
    public HrOrganizationDTO getOrgInfo(Long orgId) {
        if (orgId == null) {
            return new HrOrganizationDTO();
        }

        // 先从缓存中获取
        HrOrganizationDTO hrOrganizationDTO = sysOrgInfoCache.get(String.valueOf(orgId));
        if (hrOrganizationDTO != null) {
            return hrOrganizationDTO;
        }

        // 查询组织机构对应的信息
        LambdaQueryWrapper<HrOrganization> hrOrganizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrganizationLambdaQueryWrapper.eq(HrOrganization::getOrgId, orgId);
        hrOrganizationLambdaQueryWrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgType,
                HrOrganization::getOrgParentId, HrOrganization::getOrgPids, HrOrganization::getOrgName,
                HrOrganization::getOrgShortName, HrOrganization::getOrgSort, HrOrganization::getOrgCode);
        HrOrganization hrOrganization = this.getOne(hrOrganizationLambdaQueryWrapper, false);

        if (ObjectUtil.isEmpty(hrOrganization)) {
            sysOrgInfoCache.put(String.valueOf(orgId), new HrOrganizationDTO(), SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            return new HrOrganizationDTO();
        }

        hrOrganizationDTO = new HrOrganizationDTO();
        BeanUtil.copyProperties(hrOrganization, hrOrganizationDTO);
        sysOrgInfoCache.put(String.valueOf(orgId), hrOrganizationDTO, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);

        return hrOrganizationDTO;
    }

    @Override
    public List<HrOrganizationDTO> getOrgNameList(Collection<Long> orgIdList) {
        LambdaQueryWrapper<HrOrganization> hrOrganizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrganizationLambdaQueryWrapper.in(HrOrganization::getOrgId, orgIdList);
        hrOrganizationLambdaQueryWrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName);
        List<HrOrganization> list = this.list(hrOrganizationLambdaQueryWrapper);

        if (ObjectUtil.isNotEmpty(list)) {
            return BeanUtil.copyToList(list, HrOrganizationDTO.class);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public String getOrgTotalPathName(Long orgId) {

        // 获取机构的详情
        HrOrganizationDTO orgInfo = this.getOrgInfo(orgId);
        if (orgInfo == null || orgInfo.getOrgId() == null) {
            return "";
        }

        // 解析pids
        List<Long> orgIdList = ParentIdParseUtil.parseToPidList(orgInfo.getOrgPids());

        // 去掉-1根节点
        orgIdList.remove(TreeConstants.DEFAULT_PARENT_ID);

        // 用于存储组织名称的列表
        List<String> orgNameList = new ArrayList<>();

        // 遍历每个父节点ID，获取组织名称
        for (Long parentId : orgIdList) {
            HrOrganizationDTO parentOrgInfo = this.getOrgInfo(parentId);
            if (parentOrgInfo != null && parentOrgInfo.getOrgName() != null) {
                orgNameList.add(parentOrgInfo.getOrgName());
            }
        }

        // 添加当前组织的名称
        orgNameList.add(orgInfo.getOrgName());

        // 用斜杠连接所有名称
        return String.join("/", orgNameList);
    }

    @Override
    public Long getParentOrgLevel(Long orgId, String orgLevelCode) {
        if (orgId == null || StrUtil.isEmpty(orgLevelCode)) {
            return null;
        }

        // 获取当前机构的父级id集合
        LambdaQueryWrapper<HrOrganization> hrOrganizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrganizationLambdaQueryWrapper.eq(HrOrganization::getOrgId, orgId);
        hrOrganizationLambdaQueryWrapper.select(HrOrganization::getOrgPids);
        HrOrganization thisOrg = this.getOne(hrOrganizationLambdaQueryWrapper, false);
        if (thisOrg == null) {
            return null;
        }
        String orgPids = thisOrg.getOrgPids();
        if (ObjectUtil.isEmpty(orgPids)) {
            return null;
        }

        // 获取父级id集合，移除掉为-1的id
        List<Long> parentIdList = ParentIdParseUtil.parseToPidList(orgPids);
        parentIdList.remove(TreeConstants.DEFAULT_PARENT_ID);

        // 获取这些机构的详细信息
        List<HrOrganization> parentOrgList = this.listByIds(parentIdList);
        parentOrgList = CollectionUtil.reverse(parentOrgList);

        // 开始遍历机构，找到指定参数层级的机构
        for (HrOrganization parentOrg : parentOrgList) {
            // 获取到当前机构的层级编码
            String orgLevelCodeTemp = parentOrg.getLevelCode();

            // 如果层级编码和参数的层级编码相同，则返回当前机构的层级编码
            if (orgLevelCodeTemp.equals(orgLevelCode)) {
                return parentOrg.getOrgId();
            }
        }

        return null;
    }

    @Override
    public HrOrganizationDTO getParentOrgByDataScopeType(HrOrganizationDTO hrOrganizationDTO, String levelCode) {
        if (hrOrganizationDTO == null) {
            return null;
        }
        // 查询父级单位
        String orgPids = hrOrganizationDTO.getOrgPids();
        if (StrUtil.isBlank(orgPids)) {
            return null;
        }
        List<Long> parentIdList = ParentIdParseUtil.parseToPidList(orgPids);
        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(HrOrganization::getOrgId, parentIdList);
        List<HrOrganization> list = this.list(wrapper);

        // 遍历父级单位，找到指定参数层级对应的机构
        for (HrOrganization hrOrganization : list) {
            String orgLevelCode = hrOrganization.getLevelCode();
            if (orgLevelCode.equals(levelCode)) {
                return BeanUtil.copyProperties(hrOrganization, HrOrganizationDTO.class);
            }
        }
        return null;
    }

    @Override
    public HomeCompanyInfo orgStatInfo() {

        // todo 加缓存

        HomeCompanyInfo homeCompanyInfo = new HomeCompanyInfo();

        // 1. 总机构数量
        long totalOrgCount = this.count();
        homeCompanyInfo.setOrganizationNum(totalOrgCount);

        // 2. 总人员数量
        long totalUserCount = sysUserService.count();
        homeCompanyInfo.setEnterprisePersonNum(totalUserCount);

        // 3. 总职位信息
        long totalPositionCount = hrPositionService.count();
        homeCompanyInfo.setPositionNum(totalPositionCount);

        // 4. 当前公司下的机构数量
        Long currentOrgId = LoginContext.me().getLoginUser().getCurrentOrgId();
        CompanyDeptDTO orgCompanyInfo = this.getOrgCompanyInfo(currentOrgId);

        // 当前用户没公司，则直接设置为0
        if (currentOrgId == null || orgCompanyInfo == null) {
            homeCompanyInfo.setCurrentCompanyPersonNum(0L);
            homeCompanyInfo.setCurrentCompanyPersonNum(0L);
            return homeCompanyInfo;
        }

        Long companyId = orgCompanyInfo.getCompanyId();

        // 获取当前公司的所有子公司数量(含当前公司)
        LambdaQueryWrapper<HrOrganization> wrapper = Wrappers.lambdaQuery(HrOrganization.class)
                .like(HrOrganization::getOrgPids, LEFT_SQUARE_BRACKETS + companyId + RIGHT_SQUARE_BRACKETS).or()
                .eq(HrOrganization::getOrgId, companyId).select(HrOrganization::getOrgId);
        List<HrOrganization> organizations = this.list(wrapper);
        homeCompanyInfo.setCurrentDeptNum(organizations.size());

        // 5. 当前机构下的人员数量
        if (ObjectUtil.isEmpty(organizations)) {
            homeCompanyInfo.setCurrentCompanyPersonNum(0L);
        } else {
            List<Long> orgIdList = organizations.stream().map(HrOrganization::getOrgId).collect(Collectors.toList());
            LambdaQueryWrapper<SysUserOrg> userWrapper = new LambdaQueryWrapper<SysUserOrg>().in(SysUserOrg::getOrgId, orgIdList);
            userWrapper.select(SysUserOrg::getUserId);
            List<SysUserOrg> list = sysUserOrgService.list(userWrapper);
            Set<Long> currentOrgUserSize = list.stream().map(SysUserOrg::getUserId).collect(Collectors.toSet());
            homeCompanyInfo.setCurrentCompanyPersonNum(Convert.toLong(currentOrgUserSize.size()));
        }

        return homeCompanyInfo;
    }

    @Override
    public Set<Long> queryOrgIdParentIdList(Set<Long> orgIdList) {

        Set<Long> parentIdListTotal = new HashSet<>();

        if (ObjectUtil.isEmpty(orgIdList)) {
            return parentIdListTotal;
        }

        // 首先加上参数的机构集合
        parentIdListTotal.addAll(orgIdList);

        LambdaQueryWrapper<HrOrganization> hrOrganizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrganizationLambdaQueryWrapper.in(HrOrganization::getOrgId, orgIdList);
        hrOrganizationLambdaQueryWrapper.select(HrOrganization::getOrgPids);
        List<HrOrganization> hrOrganizationList = this.list(hrOrganizationLambdaQueryWrapper);

        for (HrOrganization hrOrganization : hrOrganizationList) {
            String orgPids = hrOrganization.getOrgPids();
            if (ObjectUtil.isEmpty(orgPids)) {
                continue;
            }

            orgPids = orgPids.replaceAll("\\[", "");
            orgPids = orgPids.replaceAll("]", "");

            String[] split = orgPids.split(",");
            for (String pidString : split) {
                parentIdListTotal.add(Convert.toLong(pidString));
            }
        }

        return parentIdListTotal;
    }

    @Override
    public Boolean getOrgHaveSubFlag(Long orgId) {

        if (ObjectUtil.isEmpty(orgId)) {
            return false;
        }

        Boolean cacheResult = sysOrgSubFlagCache.get(orgId.toString());
        if (cacheResult != null) {
            return cacheResult;
        }

        // 查询库中是否有上级包含了本orgId
        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HrOrganization::getOrgParentId, orgId);
        wrapper.select(HrOrganization::getOrgId);
        List<HrOrganization> hrOrganizationList = this.list(wrapper);

        // 查询结果加到缓存中
        if (hrOrganizationList.size() > 0) {
            // 过期时间3600秒
            sysOrgSubFlagCache.put(orgId.toString(), true, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            return true;
        } else {
            // 过期时间3600秒
            sysOrgSubFlagCache.put(orgId.toString(), false, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            return false;
        }
    }

    @Override
    public List<SimpleDict> getOrgListName(HrOrganizationRequest hrOrganizationRequest) {

        List<SimpleDict> dictList = new ArrayList<>();

        if (ObjectUtil.isEmpty(hrOrganizationRequest) || ObjectUtil.isEmpty(hrOrganizationRequest.getOrgIdList())) {
            return dictList;
        }

        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(HrOrganization::getOrgId, hrOrganizationRequest.getOrgIdList());
        wrapper.select(HrOrganization::getOrgName, HrOrganization::getOrgId, HrOrganization::getOrgCode);
        List<HrOrganization> list = this.list(wrapper);

        if (ObjectUtil.isEmpty(list)) {
            return dictList;
        }

        for (HrOrganization hrOrganization : list) {
            dictList.add(new SimpleDict(hrOrganization.getOrgId(), hrOrganization.getOrgName(), hrOrganization.getOrgCode()));
        }

        return dictList;
    }

    @Override
    public void quickBatchSaveOrg(List<HrOrganization> batchOrgList) {
        if (DbTypeEnum.MYSQL.equals(DataSourceContext.me().getCurrentDbType())) {
            this.getBaseMapper().insertBatchSomeColumn(batchOrgList);
        } else {
            this.saveBatch(batchOrgList);
        }
    }

    @Override
    public String getOrgNameById(Long orgId) {

        if (TreeConstants.DEFAULT_PARENT_ID.equals(orgId)) {
            return OrgConstants.NONE_PARENT_ORG;
        }

        if (ObjectUtil.isEmpty(orgId)) {
            return OrgConstants.NONE_PARENT_ORG;
        }

        // 获取组织机构详情
        HrOrganizationDTO orgInfo = this.getOrgInfo(orgId);
        return orgInfo.getOrgName();
    }

    @Override
    public Long getParentLevelOrgId(Long orgId, Integer parentLevelNum, DetectModeEnum detectModeEnum) {
        if (DetectModeEnum.TO_TOP.equals(detectModeEnum)) {
            return calcParentOrgId(orgId, parentLevelNum, true);
        } else {
            return calcParentOrgId(orgId, parentLevelNum, false);
        }
    }

    @Override
    public CompanyDeptDTO remoteGetOrgCompanyDept(Long orgId) {
        return this.getOrgCompanyInfo(orgId);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    private HrOrganization queryHrOrganization(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = this.getById(hrOrganizationRequest.getOrgId());
        if (ObjectUtil.isEmpty(hrOrganization)) {
            throw new ServiceException(OrgExceptionEnum.HR_ORGANIZATION_NOT_EXISTED);
        }
        return hrOrganization;
    }

    /**
     * 批量删除组织机构
     *
     * @author fengshuonan
     * @since 2023/6/11 17:00
     */
    private void baseDelete(Set<Long> totalOrgIdSet) {
        // 判断业务是否和组织机构有绑定关系
        Map<String, RemoveOrgCallbackApi> callbackApiMap = SpringUtil.getBeansOfType(RemoveOrgCallbackApi.class);
        for (RemoveOrgCallbackApi removeOrgCallbackApi : callbackApiMap.values()) {
            removeOrgCallbackApi.validateHaveOrgBind(totalOrgIdSet);
        }

        // 联动删除所有和本组织机构相关其他业务数据
        for (RemoveOrgCallbackApi removeOrgCallbackApi : callbackApiMap.values()) {
            removeOrgCallbackApi.removeOrgAction(totalOrgIdSet);
        }

        // 批量删除所有相关节点
        this.removeBatchByIds(totalOrgIdSet);
    }

    /**
     * 填充是否含有下级的标识
     *
     * @author fengshuonan
     * @since 2023/7/13 21:30
     */
    private void fillHaveSubFlag(List<HrOrganization> organizations) {

        if (ObjectUtil.isEmpty(organizations)) {
            return;
        }

        for (HrOrganization organization : organizations) {

            Long orgId = organization.getOrgId();

            // 查询是否包含下级，并设置标识
            Boolean orgHaveSubFlag = this.getOrgHaveSubFlag(orgId);
            organization.setHaveSubOrgFlag(orgHaveSubFlag);

            // 如果有children则将展开标识填充，并继续向下递归填充
            if (ObjectUtil.isNotEmpty(organization.getChildren())) {
                fillHaveSubFlag(organization.getChildren());
            }

        }

    }

    /**
     * 填充是否展开的标识
     * <p>
     * 判定是否展开，如果有children则展开
     *
     * @author fengshuonan
     * @since 2023/7/17 11:11
     */
    private void fillExpandFlag(List<HrOrganization> organizations, List<Long> expandOrgIds) {

        if (ObjectUtil.isEmpty(organizations)) {
            return;
        }

        for (HrOrganization organization : organizations) {

            Long orgId = organization.getOrgId();

            // 如果有children则将展开标识填充，并继续向下递归填充
            if (ObjectUtil.isNotEmpty(organization.getChildren())) {
                expandOrgIds.add(orgId);

                // 搜集子集的children的展开标识
                fillExpandFlag(organization.getChildren(), expandOrgIds);
            }
        }
    }

    /**
     * 计算获取上级组织机构id
     *
     * @param orgId          指定机构id
     * @param parentLevelNum 上级机构的层级数，从0开始，0代表不计算直接返回本身
     * @param reverse        是否反转，true-代表自下而上计算，false-代表自上而下计算
     * @author fengshuonan
     * @since 2022/10/1 11:45
     */
    private Long calcParentOrgId(Long orgId, Integer parentLevelNum, boolean reverse) {

        if (ObjectUtil.isEmpty(orgId) || ObjectUtil.isEmpty(parentLevelNum)) {
            return null;
        }

        // 如果上级层数为0，则直接返回参数的orgId，代表同级别组织机构
        if (parentLevelNum == 0) {
            return orgId;
        }

        // 获取当前部门的所有父级id
        HrOrganization hrOrganization = this.getById(orgId);
        if (hrOrganization == null || StrUtil.isEmpty(hrOrganization.getOrgPids())) {
            return null;
        }
        String orgParentIdListStr = hrOrganization.getOrgPids();

        // 去掉中括号符号
        orgParentIdListStr = orgParentIdListStr.replaceAll("\\[", "");
        orgParentIdListStr = orgParentIdListStr.replaceAll("]", "");

        // 获取所有上级id列表
        String[] orgParentIdList = orgParentIdListStr.split(",");
        if (reverse) {
            orgParentIdList = ArrayUtil.reverse(orgParentIdList);
        }

        // 先删掉id为-1的机构，因为-1是不存在的
        ArrayList<String> parentOrgIdList = new ArrayList<>();
        for (String orgIdItem : orgParentIdList) {
            if (!TreeConstants.DEFAULT_PARENT_ID.toString().equals(orgIdItem)) {
                parentOrgIdList.add(orgIdItem);
            }
        }

        // 根据请求参数，需要从parentOrgIdList获取的下标
        int needGetArrayIndex = parentLevelNum - 1;

        // parentOrgIdList最大能提供的下标
        int maxCanGetIndex = parentOrgIdList.size() - 1;

        // 如果没有最顶级的上级，则他本身就是最顶级上级
        if (maxCanGetIndex < 0) {
            return orgId;
        }

        // 根据参数传参，进行获取上级的操作
        String orgIdString;
        if (needGetArrayIndex <= (maxCanGetIndex)) {
            orgIdString = parentOrgIdList.get(needGetArrayIndex);
        } else {
            // 如果需要获取的下标，大于了最大下标
            if (reverse) {
                orgIdString = parentOrgIdList.get(maxCanGetIndex);
            } else {
                return orgId;
            }
        }
        return Long.valueOf(orgIdString);
    }

}