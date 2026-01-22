package cn.stylefeng.roses.kernel.sys.modular.position.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.sys.api.callback.RemovePositionCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.modular.position.entity.HrPosition;
import cn.stylefeng.roses.kernel.sys.modular.position.enums.HrPositionExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.position.mapper.HrPositionMapper;
import cn.stylefeng.roses.kernel.sys.modular.position.pojo.request.HrPositionRequest;
import cn.stylefeng.roses.kernel.sys.modular.position.service.HrPositionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 职位信息业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:25
 */
@Service
public class HrPositionServiceImpl extends ServiceImpl<HrPositionMapper, HrPosition> implements HrPositionService {

    @Resource(name = "positionNameCache")
    private CacheOperatorApi<String> positionNameCache;

    @Override
    public void add(HrPositionRequest hrPositionRequest) {
        HrPosition hrPosition = new HrPosition();
        BeanUtil.copyProperties(hrPositionRequest, hrPosition);

        // 设置状态为启用
        hrPosition.setStatusFlag(StatusEnum.ENABLE.getCode());

        this.save(hrPosition);

        // 记录日志
        BusinessLogUtil.setLogTitle("新增职务，职务名称：" + hrPositionRequest.getPositionName());
        BusinessLogUtil.addContent("职务详细信息如下：\n", hrPosition);
    }

    @Override
    public void del(HrPositionRequest hrPositionRequest) {

        // 基础的删除逻辑
        this.baseDelete(CollectionUtil.set(false, hrPositionRequest.getPositionId()));

        HrPosition hrPosition = this.queryHrPosition(hrPositionRequest);
        this.removeById(hrPosition.getPositionId());

        // 删除缓存
        positionNameCache.remove(hrPosition.getPositionId().toString());

        // 记录日志
        BusinessLogUtil.setLogTitle("删除职务，职务名称：" + hrPosition.getPositionName());
        BusinessLogUtil.addContent("职务详细信息如下：\n", hrPosition);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(HrPositionRequest hrPositionRequest) {

        Set<Long> positionIdList = hrPositionRequest.getPositionIdList();

        // 先执行基础的删除逻辑
        this.baseDelete(positionIdList);

        // 批量删除职位
        this.removeBatchByIds(positionIdList);

        // 删除缓存
        Set<String> positionIdString = positionIdList.stream().map(String::valueOf).collect(Collectors.toSet());
        positionNameCache.remove(positionIdString);

        // 记录日志
        BusinessLogUtil.setLogTitle("批量删除职务");
        BusinessLogUtil.addContent("被删除职务信息列表：\n", positionIdList);
    }

    @Override
    public void edit(HrPositionRequest hrPositionRequest) {
        HrPosition hrPosition = this.queryHrPosition(hrPositionRequest);

        BusinessLogUtil.setLogTitle("编辑职务信息，职务名称：", hrPosition.getPositionName());
        BusinessLogUtil.addContent("原始职务信息如下：\n", hrPosition);

        BeanUtil.copyProperties(hrPositionRequest, hrPosition);
        this.updateById(hrPosition);

        BusinessLogUtil.addContent("新职务信息如下：\n", hrPosition);

        // 清空这个id的缓存
        this.positionNameCache.remove(hrPosition.getPositionId().toString());
    }

    @Override
    public HrPosition detail(HrPositionRequest hrPositionRequest) {
        return this.queryHrPosition(hrPositionRequest);
    }

    @Override
    public PageResult<HrPosition> findPage(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> wrapper = this.createWrapper(hrPositionRequest);

        // 筛选主要属性
        wrapper.select(HrPosition::getPositionId, HrPosition::getPositionName, HrPosition::getPositionCode, HrPosition::getRemark,
                HrPosition::getPositionSort, BaseEntity::getCreateTime);

        Page<HrPosition> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SimpleDict> batchGetName(HrPositionRequest hrPositionRequest) {

        List<SimpleDict> dictList = new ArrayList<>();

        if (ObjectUtil.isEmpty(hrPositionRequest) || ObjectUtil.isEmpty(hrPositionRequest.getPositionIdList())) {
            return dictList;
        }

        LambdaQueryWrapper<HrPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(HrPosition::getPositionId, hrPositionRequest.getPositionIdList());
        wrapper.select(HrPosition::getPositionName, HrPosition::getPositionId);
        List<HrPosition> list = this.list(wrapper);

        if (ObjectUtil.isEmpty(list)) {
            return dictList;
        }

        for (HrPosition hrPosition : list) {
            dictList.add(new SimpleDict(hrPosition.getPositionId(), hrPosition.getPositionName(), null));
        }

        return dictList;
    }

    @Override
    public String getPositionName(Long positionId) {
        if (positionId == null) {
            return null;
        }

        // 先从缓存中获取
        String positionName = positionNameCache.get(positionId.toString());
        if (positionName != null) {
            return positionName;
        }

        LambdaQueryWrapper<HrPosition> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HrPosition::getPositionId, positionId);
        queryWrapper.select(HrPosition::getPositionName);
        HrPosition hrPosition = this.getOne(queryWrapper);
        if (hrPosition == null) {
            return null;
        } else {
            positionNameCache.put(positionId.toString(), hrPosition.getPositionName(), SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            return hrPosition.getPositionName();
        }
    }

    @Override
    public List<HrPosition> findList(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> wrapper = this.createWrapper(hrPositionRequest);

        // 筛选主要属性
        wrapper.select(HrPosition::getPositionId, HrPosition::getPositionName, HrPosition::getPositionCode, HrPosition::getRemark,
                BaseEntity::getCreateTime);

        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    private HrPosition queryHrPosition(HrPositionRequest hrPositionRequest) {
        HrPosition hrPosition = this.getById(hrPositionRequest.getPositionId());
        if (ObjectUtil.isEmpty(hrPosition)) {
            throw new ServiceException(HrPositionExceptionEnum.HR_POSITION_NOT_EXISTED);
        }
        return hrPosition;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    private LambdaQueryWrapper<HrPosition> createWrapper(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> queryWrapper = new LambdaQueryWrapper<>();

        // 根据输入的搜索内容查询
        String searchText = hrPositionRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(HrPosition::getPositionName, searchText);
            queryWrapper.or().like(HrPosition::getPositionCode, searchText);
        }

        // 根据排序升序排列
        queryWrapper.orderByAsc(HrPosition::getPositionSort);

        return queryWrapper;
    }

    /**
     * 删除职务的操作
     *
     * @author fengshuonan
     * @since 2023/6/11 17:37
     */
    private void baseDelete(Set<Long> positionIdList) {
        // 删除前的业务绑定校验
        Map<String, RemovePositionCallbackApi> callbackApiMap = SpringUtil.getBeansOfType(RemovePositionCallbackApi.class);
        for (RemovePositionCallbackApi callbackApi : callbackApiMap.values()) {
            callbackApi.validateHavePositionBind(positionIdList);
        }

        // 执行删除关联业务的操作
        for (RemovePositionCallbackApi callbackApi : callbackApiMap.values()) {
            callbackApi.removePositionAction(positionIdList);
        }
    }

}