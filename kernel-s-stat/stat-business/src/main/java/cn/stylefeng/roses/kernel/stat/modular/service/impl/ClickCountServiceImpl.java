package cn.stylefeng.roses.kernel.stat.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.stat.api.callback.ClickCountCallback;
import cn.stylefeng.roses.kernel.stat.modular.entity.ClickCount;
import cn.stylefeng.roses.kernel.stat.modular.enums.ClickCountExceptionEnum;
import cn.stylefeng.roses.kernel.stat.modular.mapper.ClickCountMapper;
import cn.stylefeng.roses.kernel.stat.modular.request.ClickCountRequest;
import cn.stylefeng.roses.kernel.stat.modular.service.ClickCountService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户点击数量统计业务实现层
 *
 * @author fengshuonan
 * @since 2023/03/28 14:52
 */
@Service
public class ClickCountServiceImpl extends ServiceImpl<ClickCountMapper, ClickCount> implements ClickCountService {

    @Override
    public void add(ClickCountRequest portalClickCountRequest) {
        ClickCount portalClickCount = new ClickCount();
        BeanUtil.copyProperties(portalClickCountRequest, portalClickCount);
        this.save(portalClickCount);
    }

    @Override
    public void del(ClickCountRequest portalClickCountRequest) {
        ClickCount portalClickCount = this.queryPortalClickCount(portalClickCountRequest);
        this.removeById(portalClickCount.getClickCountId());
    }

    @Override
    public void edit(ClickCountRequest portalClickCountRequest) {
        ClickCount portalClickCount = this.queryPortalClickCount(portalClickCountRequest);
        BeanUtil.copyProperties(portalClickCountRequest, portalClickCount);
        this.updateById(portalClickCount);
    }

    @Override
    public ClickCount detail(ClickCountRequest portalClickCountRequest) {
        return this.queryPortalClickCount(portalClickCountRequest);
    }

    @Override
    public PageResult<ClickCount> findPage(ClickCountRequest portalClickCountRequest) {
        LambdaQueryWrapper<ClickCount> wrapper = createWrapper(portalClickCountRequest);
        Page<ClickCount> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public Long getBusinessCount(Long businessId) {
        LambdaQueryWrapper<ClickCount> portalClickCountLambdaQueryWrapper = new LambdaQueryWrapper<>();
        portalClickCountLambdaQueryWrapper.eq(ClickCount::getBusinessKeyId, businessId);
        portalClickCountLambdaQueryWrapper.select(ClickCount::getClickCount);
        ClickCount one = this.getOne(portalClickCountLambdaQueryWrapper, false);
        if (one != null) {
            return one.getClickCount();
        } else {
            return 0L;
        }
    }

    @Override
    public List<ClickCount> findList(ClickCountRequest portalClickCountRequest) {
        LambdaQueryWrapper<ClickCount> wrapper = this.createWrapper(portalClickCountRequest);
        return this.list(wrapper);
    }

    @Override
    public <T extends ClickCountCallback> void calcClickCount(List<T> clickCountCallbackList) {

        if (ObjectUtil.isEmpty(clickCountCallbackList)) {
            return;
        }

        for (T clickCountCallback : clickCountCallbackList) {
            Long businessId = clickCountCallback.getBusinessId();
            Long clickCount = this.getBusinessCount(businessId);
            clickCountCallback.addClickCount(clickCount);
        }

    }

    @Override
    public Long addOneClickCount(ClickCountCallback clickCountCallback) {
        LambdaQueryWrapper<ClickCount> portalClickCountLambdaQueryWrapper = new LambdaQueryWrapper<>();
        portalClickCountLambdaQueryWrapper.eq(ClickCount::getBusinessKeyId, clickCountCallback.getBusinessId());
        ClickCount portalClickCount = this.getOne(portalClickCountLambdaQueryWrapper, false);

        // 记录不存在，则直接新增一条记录
        if (portalClickCount == null) {
            portalClickCount = new ClickCount();
            portalClickCount.setBusinessType(clickCountCallback.getBusinessTypeCode());
            portalClickCount.setBusinessKeyId(clickCountCallback.getBusinessId());
            portalClickCount.setClickCount(1L);
            this.save(portalClickCount);
        } else {
            // 如果记录存在则直接加1
            portalClickCount.setClickCount(portalClickCount.getClickCount() + 1);
            this.updateById(portalClickCount);
        }

        return portalClickCount.getClickCount();
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    private ClickCount queryPortalClickCount(ClickCountRequest portalClickCountRequest) {
        ClickCount portalClickCount = this.getById(portalClickCountRequest.getClickCountId());
        if (ObjectUtil.isEmpty(portalClickCount)) {
            throw new ServiceException(ClickCountExceptionEnum.PORTAL_CLICK_COUNT_NOT_EXISTED);
        }
        return portalClickCount;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2023/03/28 14:52
     */
    private LambdaQueryWrapper<ClickCount> createWrapper(ClickCountRequest portalClickCountRequest) {
        LambdaQueryWrapper<ClickCount> queryWrapper = new LambdaQueryWrapper<>();

        Long clickCountId = portalClickCountRequest.getClickCountId();
        String businessType = portalClickCountRequest.getBusinessType();
        Long businessKeyId = portalClickCountRequest.getBusinessKeyId();
        Long clickCount = portalClickCountRequest.getClickCount();

        queryWrapper.eq(ObjectUtil.isNotNull(clickCountId), ClickCount::getClickCountId, clickCountId);
        queryWrapper.like(ObjectUtil.isNotEmpty(businessType), ClickCount::getBusinessType, businessType);
        queryWrapper.eq(ObjectUtil.isNotNull(businessKeyId), ClickCount::getBusinessKeyId, businessKeyId);
        queryWrapper.eq(ObjectUtil.isNotNull(clickCount), ClickCount::getClickCount, clickCount);

        return queryWrapper;
    }
}