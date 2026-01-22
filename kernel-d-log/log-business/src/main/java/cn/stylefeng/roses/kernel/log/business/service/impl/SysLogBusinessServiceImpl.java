package cn.stylefeng.roses.kernel.log.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.BizLogServiceApi;
import cn.stylefeng.roses.kernel.log.api.pojo.business.SysLogBusinessRequest;
import cn.stylefeng.roses.kernel.log.api.pojo.entity.SysLogBusiness;
import cn.stylefeng.roses.kernel.log.api.pojo.entity.SysLogBusinessContent;
import cn.stylefeng.roses.kernel.log.business.enums.SysLogBusinessExceptionEnum;
import cn.stylefeng.roses.kernel.log.business.mapper.SysLogBusinessMapper;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessContentService;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessService;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务日志记录业务实现层
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
@Service
public class SysLogBusinessServiceImpl extends ServiceImpl<SysLogBusinessMapper, SysLogBusiness> implements SysLogBusinessService,
        BizLogServiceApi {

    @Resource
    private SysLogBusinessContentService sysLogBusinessContentService;

    @Override
    public void add(SysLogBusinessRequest sysLogBusinessRequest) {
        SysLogBusiness sysLogBusiness = new SysLogBusiness();
        BeanUtil.copyProperties(sysLogBusinessRequest, sysLogBusiness);
        this.save(sysLogBusiness);
    }

    @Override
    public void del(SysLogBusinessRequest sysLogBusinessRequest) {
        SysLogBusiness sysLogBusiness = this.querySysLogBusiness(sysLogBusinessRequest);
        this.removeById(sysLogBusiness.getBusinessLogId());
    }

    @Override
    public void edit(SysLogBusinessRequest sysLogBusinessRequest) {
        SysLogBusiness sysLogBusiness = this.querySysLogBusiness(sysLogBusinessRequest);
        BeanUtil.copyProperties(sysLogBusinessRequest, sysLogBusiness);
        this.updateById(sysLogBusiness);
    }

    @Override
    public SysLogBusiness detail(SysLogBusinessRequest sysLogBusinessRequest) {
        return this.querySysLogBusiness(sysLogBusinessRequest);
    }

    @Override
    public PageResult<SysLogBusiness> findPage(SysLogBusinessRequest sysLogBusinessRequest) {
        LambdaQueryWrapper<SysLogBusiness> wrapper = createWrapper(sysLogBusinessRequest);
        Page<SysLogBusiness> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public void saveBatchLogs(SysLogBusiness context, List<String> batchContentList) {

        if (ObjectUtil.isEmpty(batchContentList)) {
            return;
        }

        // 先保存基础的日志信息
        this.save(context);

        // 再保存详细的日志信息
        List<SysLogBusinessContent> sysLogBusinessContentList = new ArrayList<>();
        for (String content : batchContentList) {
            SysLogBusinessContent sysLogBusinessContent = new SysLogBusinessContent();
            sysLogBusinessContent.setBusinessLogId(context.getBusinessLogId());
            sysLogBusinessContent.setLogContent(content);
            sysLogBusinessContentList.add(sysLogBusinessContent);
        }
        this.sysLogBusinessContentService.batchSaveContent(sysLogBusinessContentList);
    }

    @Override
    public List<SysLogBusiness> findList(SysLogBusinessRequest sysLogBusinessRequest) {
        LambdaQueryWrapper<SysLogBusiness> wrapper = this.createWrapper(sysLogBusinessRequest);
        return this.list(wrapper);
    }

    @Override
    public PageResult<SysLogBusiness> getOperateLogByLogType(List<String> logTypeCodeList, SysLogBusinessRequest sysLogBusinessRequest) {
        LambdaQueryWrapper<SysLogBusiness> queryWrapper = new LambdaQueryWrapper<>();

        // 指定范围为空，则直接返回空结果
        if (ObjectUtil.isEmpty(logTypeCodeList)) {
            Page<SysLogBusiness> page = PageFactory.defaultPage();
            return PageResultFactory.createPageResult(page);
        }

        // 如果request参数有code，则需要判断这个code是否在指定参数范围内，如果在范围内则查询，如果不在范围内，则直接返回空
        String logTypeCode = sysLogBusinessRequest.getLogTypeCode();
        if (StrUtil.isNotBlank(logTypeCode)) {
            if (logTypeCodeList.contains(logTypeCode)) {
                queryWrapper.eq(SysLogBusiness::getLogTypeCode, logTypeCode);
            } else {
                Page<SysLogBusiness> page = PageFactory.defaultPage();
                return PageResultFactory.createPageResult(page);
            }
        } else {
            queryWrapper.in(SysLogBusiness::getLogTypeCode, logTypeCodeList);
        }

        // 填充搜索名称和排序
        this.buildCommonWrapper(sysLogBusinessRequest, queryWrapper);

        Page<SysLogBusiness> sysRolePage = this.page(PageFactory.defaultPage(), queryWrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    private SysLogBusiness querySysLogBusiness(SysLogBusinessRequest sysLogBusinessRequest) {
        SysLogBusiness sysLogBusiness = this.getById(sysLogBusinessRequest.getBusinessLogId());
        if (ObjectUtil.isEmpty(sysLogBusiness)) {
            throw new ServiceException(SysLogBusinessExceptionEnum.SYS_LOG_BUSINESS_NOT_EXISTED);
        }
        return sysLogBusiness;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/07/21 15:00
     */
    private LambdaQueryWrapper<SysLogBusiness> createWrapper(SysLogBusinessRequest sysLogBusinessRequest) {
        LambdaQueryWrapper<SysLogBusiness> queryWrapper = new LambdaQueryWrapper<>();

        // 1. 根据日志类型编码查询
        String logTypeCode = sysLogBusinessRequest.getLogTypeCode();
        if (StrUtil.isNotBlank(logTypeCode)) {
            queryWrapper.like(SysLogBusiness::getLogTypeCode, logTypeCode);
        }

        // 2. 根据HTTP方法查询
        String httpMethod = sysLogBusinessRequest.getHttpMethod();
        if (StrUtil.isNotBlank(httpMethod)) {
            queryWrapper.eq(SysLogBusiness::getHttpMethod, httpMethod);
        }

        // 3. 根据客户端IP检索
        String clientIp = sysLogBusinessRequest.getClientIp();
        if (StrUtil.isNotBlank(clientIp)) {
            queryWrapper.likeRight(SysLogBusiness::getClientIp, clientIp);
        }

        // 4. 根据操作用户检索
        Long userId = sysLogBusinessRequest.getUserId();
        if (ObjectUtil.isNotEmpty(userId)) {
            queryWrapper.eq(SysLogBusiness::getUserId, userId);
        }

        // 5. 根据日志的时间段进行检索
        String searchBeginTime = sysLogBusinessRequest.getSearchBeginTime();
        String searchEndTime = sysLogBusinessRequest.getSearchEndTime();
        if (StrUtil.isNotBlank(searchBeginTime) && StrUtil.isNotBlank(searchEndTime)) {
            queryWrapper.between(SysLogBusiness::getCreateTime, searchBeginTime + " 00:00:00", searchEndTime + " 23:59:59");
        }

        // 填充搜索名称和排序
        this.buildCommonWrapper(sysLogBusinessRequest, queryWrapper);

        return queryWrapper;
    }

    /**
     * 填充搜索名称和排序的条件
     *
     * @author fengshuonan
     * @since 2023/11/13 18:41
     */
    private void buildCommonWrapper(SysLogBusinessRequest sysLogBusinessRequest, LambdaQueryWrapper<SysLogBusiness> queryWrapper) {
        // 根据文本检索内容查询
        String searchText = sysLogBusinessRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.nested(wrap -> {
                wrap.like(SysLogBusiness::getLogTitle, searchText).or().like(SysLogBusiness::getRequestUrl, searchText);
            });
        }

        // 根据请求参数的顺序排列，或者根据创建时间倒序排列
        if (ObjectUtil.isNotEmpty(sysLogBusinessRequest.getOrderBy()) && ObjectUtil.isNotEmpty(sysLogBusinessRequest.getSortBy())) {
            queryWrapper.last(sysLogBusinessRequest.getOrderByLastSql());
        } else {
            queryWrapper.orderByDesc(SysLogBusiness::getCreateTime);
        }
    }

}