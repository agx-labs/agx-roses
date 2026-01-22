package cn.stylefeng.roses.kernel.log.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.pojo.security.LogSecurityRequest;
import cn.stylefeng.roses.kernel.log.security.entity.LogSecurity;
import cn.stylefeng.roses.kernel.log.security.enums.LogSecurityExceptionEnum;
import cn.stylefeng.roses.kernel.log.security.mapper.LogSecurityMapper;
import cn.stylefeng.roses.kernel.log.security.service.LogSecurityService;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.security.api.CountValidatorApi;
import cn.stylefeng.roses.kernel.security.api.exception.CountValidateException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.stylefeng.roses.kernel.log.api.constants.LogConstants.VALIDATE_COUNT_PREFIX;

/**
 * 安全日志业务实现层
 *
 * @author fengshuonan
 * @since 2024/07/11 15:56
 */
@Service
public class LogSecurityServiceImpl extends ServiceImpl<LogSecurityMapper, LogSecurity> implements LogSecurityService {

    @Resource
    private CountValidatorApi countValidatorApi;

    @Override
    public void add(LogSecurityRequest logSecurityRequest) {
        try {
            // 同一个IP一个小时内不能超过100条，防止刷接口
            countValidatorApi.countAndValidate(VALIDATE_COUNT_PREFIX + logSecurityRequest.getClientIp(),
                    3600L, 100L);

            LogSecurity logSecurity = new LogSecurity();
            BeanUtil.copyProperties(logSecurityRequest, logSecurity);
            this.save(logSecurity);

        } catch (CountValidateException e) {
            // none
        }
    }

    @Override
    public void del(LogSecurityRequest logSecurityRequest) {
        LogSecurity logSecurity = this.queryLogSecurity(logSecurityRequest);
        this.removeById(logSecurity.getSecurityLogId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(LogSecurityRequest logSecurityRequest) {
        this.removeByIds(logSecurityRequest.getBatchDeleteIdList());
    }

    @Override
    public void edit(LogSecurityRequest logSecurityRequest) {
        LogSecurity logSecurity = this.queryLogSecurity(logSecurityRequest);
        BeanUtil.copyProperties(logSecurityRequest, logSecurity);
        this.updateById(logSecurity);
    }

    @Override
    public LogSecurity detail(LogSecurityRequest logSecurityRequest) {
        return this.queryLogSecurity(logSecurityRequest);
    }

    @Override
    public PageResult<LogSecurity> findPage(LogSecurityRequest logSecurityRequest) {
        LambdaQueryWrapper<LogSecurity> wrapper = createWrapper(logSecurityRequest);
        Page<LogSecurity> pageList = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(pageList);
    }

    @Override
    public List<LogSecurity> findList(LogSecurityRequest logSecurityRequest) {
        LambdaQueryWrapper<LogSecurity> wrapper = this.createWrapper(logSecurityRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    private LogSecurity queryLogSecurity(LogSecurityRequest logSecurityRequest) {
        LogSecurity logSecurity = this.getById(logSecurityRequest.getSecurityLogId());
        if (ObjectUtil.isEmpty(logSecurity)) {
            throw new ServiceException(LogSecurityExceptionEnum.LOG_SECURITY_NOT_EXISTED);
        }
        return logSecurity;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    private LambdaQueryWrapper<LogSecurity> createWrapper(LogSecurityRequest logSecurityRequest) {
        LambdaQueryWrapper<LogSecurity> queryWrapper = new LambdaQueryWrapper<>();

        // 根据请求URL查询
        String requestUrl = logSecurityRequest.getRequestUrl();
        queryWrapper.like(ObjectUtil.isNotEmpty(requestUrl), LogSecurity::getRequestUrl, requestUrl);

        // 根据客户端IP查询
        String clientIp = logSecurityRequest.getClientIp();
        queryWrapper.like(ObjectUtil.isNotEmpty(clientIp), LogSecurity::getClientIp, clientIp);

        // 根据HTTP请求方式查询
        String httpMethod = logSecurityRequest.getHttpMethod();
        queryWrapper.like(ObjectUtil.isNotEmpty(httpMethod), LogSecurity::getHttpMethod, httpMethod);

        // 根据请求内容查询
        String logContent = logSecurityRequest.getLogContent();
        queryWrapper.like(ObjectUtil.isNotEmpty(logContent), LogSecurity::getLogContent, logContent);

        // 根据时间范围
        String searchBeginTime = logSecurityRequest.getSearchBeginTime();
        String searchEndTime = logSecurityRequest.getSearchEndTime();
        if (StrUtil.isNotBlank(searchBeginTime) && StrUtil.isNotBlank(searchEndTime)) {
            queryWrapper.between(BaseEntity::getCreateTime, searchBeginTime + " 00:00:00", searchEndTime + " 23:59:59");
        }

        // 请求时间倒序排列
        queryWrapper.orderByDesc(BaseEntity::getCreateTime);

        return queryWrapper;
    }

}
