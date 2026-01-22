package cn.stylefeng.roses.kernel.sys.modular.notice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.sys.api.MessagePublishApi;
import cn.stylefeng.roses.kernel.sys.api.SysUserOrgServiceApi;
import cn.stylefeng.roses.kernel.sys.api.enums.message.MessageBusinessTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.enums.notice.NoticePublishStatusEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageRetractDTO;
import cn.stylefeng.roses.kernel.sys.modular.notice.entity.SysNotice;
import cn.stylefeng.roses.kernel.sys.modular.notice.enums.SysNoticeExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.notice.factory.NoticeFactory;
import cn.stylefeng.roses.kernel.sys.modular.notice.mapper.SysNoticeMapper;
import cn.stylefeng.roses.kernel.sys.modular.notice.pojo.NoticeUserScope;
import cn.stylefeng.roses.kernel.sys.modular.notice.pojo.request.SysNoticeRequest;
import cn.stylefeng.roses.kernel.sys.modular.notice.service.SysNoticeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 通知管理业务实现层
 *
 * @author fengshuonan
 * @since 2024/01/12 16:06
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    @Resource
    private MessagePublishApi messagePublishApi;

    @Resource
    private SysUserOrgServiceApi sysUserOrgServiceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysNoticeRequest sysNoticeRequest) {

        // 1. 校验通知的人员范围是否为空
        this.validateUserScope(sysNoticeRequest);

        // 2. 存储基础信息
        SysNotice sysNotice = new SysNotice();
        BeanUtil.copyProperties(sysNoticeRequest, sysNotice);

        // 新增的时候，默认设置状态为未发布
        sysNotice.setPublishStatus(NoticePublishStatusEnum.NOT_PUBLISH.getCode());

        this.save(sysNotice);
    }

    @Override
    public void del(SysNoticeRequest sysNoticeRequest) {
        SysNotice sysNotice = this.querySysNotice(sysNoticeRequest);
        this.removeById(sysNotice.getNoticeId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(SysNoticeRequest sysNoticeRequest) {
        this.removeByIds(sysNoticeRequest.getBatchDeleteIdList());
    }

    @Override
    public void edit(SysNoticeRequest sysNoticeRequest) {

        // 1. 校验通知的人员范围是否为空
        this.validateUserScope(sysNoticeRequest);

        // 2. 如果当前的通知是已经发布了，则不允许修改
        SysNotice sysNotice = this.querySysNotice(sysNoticeRequest);
        if (sysNotice == null) {
            throw new ServiceException(SysNoticeExceptionEnum.SYS_NOTICE_NOT_EXISTED);
        }
        if (NoticePublishStatusEnum.ALREADY.getCode().equals(sysNotice.getPublishStatus())) {
            throw new ServiceException(SysNoticeExceptionEnum.SYS_NOTICE_CANT_EDIT);
        }

        // 3. 修改通知信息
        BeanUtil.copyProperties(sysNoticeRequest, sysNotice);
        this.updateById(sysNotice);
    }

    @Override
    public SysNotice detail(SysNoticeRequest sysNoticeRequest) {
        return this.querySysNotice(sysNoticeRequest);
    }

    @Override
    public PageResult<SysNotice> findPage(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> wrapper = createWrapper(sysNoticeRequest);

        // 只查询需要的字段
        wrapper.select(SysNotice::getNoticeId, SysNotice::getNoticeTitle, SysNotice::getPublishStatus, SysNotice::getPriorityLevel,
                SysNotice::getNoticeBeginTime,
                SysNotice::getNoticeEndTime,
                BaseEntity::getCreateUser, BaseEntity::getCreateTime);

        Page<SysNotice> pageList = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(pageList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishNotice(SysNoticeRequest sysNoticeRequest) {

        // 1. 修改当前通知的状态改为已发布
        SysNotice sysNotice = this.querySysNotice(sysNoticeRequest);
        sysNotice.setPublishStatus(NoticePublishStatusEnum.ALREADY.getCode());
        this.updateById(sysNotice);

        // 2. 获取发送对象，全部转化为用户id
        NoticeUserScope noticeUserScope = sysNotice.getNoticeUserScope();
        Set<Long> noticeUserList = this.getNoticeUserList(noticeUserScope);

        // 3. 发送通知给接收人
        messagePublishApi.batchSendMessage(NoticeFactory.createMessageSendDTO(noticeUserList, sysNotice));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retractNotice(SysNoticeRequest sysNoticeRequest) {

        // 1. 修改当前通知的状态改为未发布
        SysNotice sysNotice = this.querySysNotice(sysNoticeRequest);
        sysNotice.setPublishStatus(NoticePublishStatusEnum.NOT_PUBLISH.getCode());
        this.updateById(sysNotice);

        // 2. 调用撤回接口，将已发送的消息全都撤回
        MessageRetractDTO messageRetractDTO = new MessageRetractDTO();
        messageRetractDTO.setBusinessType(MessageBusinessTypeEnum.SYS_NOTICE.getCode());
        messageRetractDTO.setBusinessId(String.valueOf(sysNotice.getNoticeId()));
        messagePublishApi.batchRetractMessage(messageRetractDTO);
    }

    @Override
    public List<SysNotice> findList(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> wrapper = this.createWrapper(sysNoticeRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    private SysNotice querySysNotice(SysNoticeRequest sysNoticeRequest) {
        SysNotice sysNotice = this.getById(sysNoticeRequest.getNoticeId());
        if (ObjectUtil.isEmpty(sysNotice)) {
            throw new ServiceException(SysNoticeExceptionEnum.SYS_NOTICE_NOT_EXISTED);
        }
        return sysNotice;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    private LambdaQueryWrapper<SysNotice> createWrapper(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> queryWrapper = new LambdaQueryWrapper<>();

        // 根据查询条件搜索
        String searchText = sysNoticeRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(SysNotice::getNoticeTitle, searchText);
            queryWrapper.or().like(SysNotice::getNoticeContent, searchText);
        }

        // 根据是否发布搜索
        Integer publishStatus = sysNoticeRequest.getPublishStatus();
        if (ObjectUtil.isNotEmpty(publishStatus)) {
            queryWrapper.nested(i -> i.eq(SysNotice::getPublishStatus, publishStatus));
        }

        // 根据优先级搜索
        String priorityLevel = sysNoticeRequest.getPriorityLevel();
        if (ObjectUtil.isNotEmpty(priorityLevel)) {
            queryWrapper.nested(i -> i.eq(SysNotice::getPriorityLevel, priorityLevel));
        }
        return queryWrapper;
    }

    /**
     * 校验通知的人员范围是否为空
     *
     * @author fengshuonan
     * @since 2024-01-12 17:03
     */
    private void validateUserScope(SysNoticeRequest sysNoticeRequest) {
        // 1. 判断通知人员范围不能为空
        NoticeUserScope noticeUserScope = sysNoticeRequest.getNoticeUserScope();
        if (noticeUserScope == null) {
            throw new ServiceException(SysNoticeExceptionEnum.SYS_NOTICE_SCOPE_EMPTY);
        }

        // 2. 通知的人员和部门不能同时为空
        if (ObjectUtil.isEmpty(noticeUserScope.getPointOrgList()) && ObjectUtil.isEmpty(noticeUserScope.getPointUserList())) {
            throw new ServiceException(SysNoticeExceptionEnum.SYS_NOTICE_SCOPE_EMPTY);
        }
    }

    /**
     * 通知的范围，转化为具体的用户id
     *
     * @author fengshuonan
     * @since 2024/1/14 22:46
     */
    private Set<Long> getNoticeUserList(NoticeUserScope noticeUserScope) {

        // 初始化用户集合
        Set<Long> userIdCollection = new LinkedHashSet<>();

        // 添加通知中的用户集合
        List<SimpleDict> pointUserList = noticeUserScope.getPointUserList();
        if (ObjectUtil.isNotEmpty(pointUserList)) {
            userIdCollection.addAll(pointUserList.stream().map(SimpleDict::getId).collect(Collectors.toList()));
        }

        // 组织机构id转化为用户id
        List<SimpleDict> pointOrgList = noticeUserScope.getPointOrgList();
        if (ObjectUtil.isNotEmpty(pointOrgList)) {
            Set<Long> orgUserIdList = sysUserOrgServiceApi.getOrgUserIdList(
                    pointOrgList.stream().map(SimpleDict::getId).collect(Collectors.toSet()));
            if (ObjectUtil.isNotEmpty(orgUserIdList)) {
                userIdCollection.addAll(orgUserIdList);
            }
        }

        return userIdCollection;
    }

}
