package cn.stylefeng.roses.kernel.stat.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.stat.api.callback.ClickStatusCallback;
import cn.stylefeng.roses.kernel.stat.modular.entity.ClickStatus;
import cn.stylefeng.roses.kernel.stat.modular.enums.ClickStatusExceptionEnum;
import cn.stylefeng.roses.kernel.stat.modular.mapper.ClickStatusMapper;
import cn.stylefeng.roses.kernel.stat.modular.request.ClickStatusRequest;
import cn.stylefeng.roses.kernel.stat.modular.service.ClickStatusService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户点击状态业务实现层
 *
 * @author fengshuonan
 * @since 2023/03/28 14:52
 */
@Service
public class ClickStatusServiceImpl extends ServiceImpl<ClickStatusMapper, ClickStatus> implements ClickStatusService {

    @Override
    public void add(ClickStatusRequest portalClickStatusRequest) {
        ClickStatus portalClickStatus = new ClickStatus();
        BeanUtil.copyProperties(portalClickStatusRequest, portalClickStatus);
        this.save(portalClickStatus);
    }

    @Override
    public void del(ClickStatusRequest portalClickStatusRequest) {
        ClickStatus portalClickStatus = this.queryPortalClickStatus(portalClickStatusRequest);
        this.removeById(portalClickStatus.getClickStatusId());
    }

    @Override
    public void edit(ClickStatusRequest portalClickStatusRequest) {
        ClickStatus portalClickStatus = this.queryPortalClickStatus(portalClickStatusRequest);
        BeanUtil.copyProperties(portalClickStatusRequest, portalClickStatus);
        this.updateById(portalClickStatus);
    }

    @Override
    public ClickStatus detail(ClickStatusRequest portalClickStatusRequest) {
        return this.queryPortalClickStatus(portalClickStatusRequest);
    }

    @Override
    public PageResult<ClickStatus> findPage(ClickStatusRequest portalClickStatusRequest) {
        LambdaQueryWrapper<ClickStatus> wrapper = createWrapper(portalClickStatusRequest);
        Page<ClickStatus> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public boolean getUserBusinessClickFlag(Long userId, Long businessId) {
        LambdaQueryWrapper<ClickStatus> portalClickStatusLambdaQueryWrapper = new LambdaQueryWrapper<>();
        portalClickStatusLambdaQueryWrapper.eq(ClickStatus::getUserId, userId);
        portalClickStatusLambdaQueryWrapper.eq(ClickStatus::getBusinessKeyId, businessId);
        long count = this.count(portalClickStatusLambdaQueryWrapper);
        return count > 0;
    }

    @Override
    public List<ClickStatus> findList(ClickStatusRequest portalClickStatusRequest) {
        LambdaQueryWrapper<ClickStatus> wrapper = this.createWrapper(portalClickStatusRequest);
        return this.list(wrapper);
    }

    @Override
    public <T extends ClickStatusCallback> void calcClickStatus(List<T> clickStatusCallbackList) {

        if (ObjectUtil.isEmpty(clickStatusCallbackList)) {
            return;
        }

        for (T clickStatusCallback : clickStatusCallbackList) {

            // 获取这条记录的用户id
            Long userId = clickStatusCallback.getUserId();

            // 获取这条记录的业务id
            Long businessId = clickStatusCallback.getBusinessId();

            // 查询这个用户有没有点击过这个记录
            boolean userBusinessClickFlag = this.getUserBusinessClickFlag(userId, businessId);
            if (userBusinessClickFlag) {
                clickStatusCallback.addClickStatus();
            }
        }
    }

    @Override
    public void addClickStatus(ClickStatusCallback clickStatusCallback) {

        Long userId = clickStatusCallback.getUserId();
        Long businessId = clickStatusCallback.getBusinessId();

        // 获取是否已经点击过，点击过就不记录了
        boolean userBusinessClickFlag = this.getUserBusinessClickFlag(userId, businessId);
        if (userBusinessClickFlag) {
            return;
        }

        // 添加点击录
        ClickStatus portalClickStatus = new ClickStatus();
        portalClickStatus.setUserId(userId);
        portalClickStatus.setBusinessType(clickStatusCallback.getBusinessTypeCode());
        portalClickStatus.setBusinessKeyId(businessId);
        this.save(portalClickStatus);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    private ClickStatus queryPortalClickStatus(ClickStatusRequest portalClickStatusRequest) {
        ClickStatus portalClickStatus = this.getById(portalClickStatusRequest.getClickStatusId());
        if (ObjectUtil.isEmpty(portalClickStatus)) {
            throw new ServiceException(ClickStatusExceptionEnum.PORTAL_CLICK_STATUS_NOT_EXISTED);
        }
        return portalClickStatus;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    private LambdaQueryWrapper<ClickStatus> createWrapper(ClickStatusRequest portalClickStatusRequest) {
        LambdaQueryWrapper<ClickStatus> queryWrapper = new LambdaQueryWrapper<>();

        Long clickStatusId = portalClickStatusRequest.getClickStatusId();
        Long userId = portalClickStatusRequest.getUserId();
        Long businessKeyId = portalClickStatusRequest.getBusinessKeyId();
        String businessType = portalClickStatusRequest.getBusinessType();

        queryWrapper.eq(ObjectUtil.isNotNull(clickStatusId), ClickStatus::getClickStatusId, clickStatusId);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), ClickStatus::getUserId, userId);
        queryWrapper.eq(ObjectUtil.isNotNull(businessKeyId), ClickStatus::getBusinessKeyId, businessKeyId);
        queryWrapper.like(ObjectUtil.isNotEmpty(businessType), ClickStatus::getBusinessType, businessType);

        return queryWrapper;
    }
}