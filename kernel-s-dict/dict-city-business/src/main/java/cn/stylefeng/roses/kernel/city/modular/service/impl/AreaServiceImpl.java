package cn.stylefeng.roses.kernel.city.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.city.modular.entity.Area;
import cn.stylefeng.roses.kernel.city.modular.enums.AreaExceptionEnum;
import cn.stylefeng.roses.kernel.city.modular.mapper.AreaMapper;
import cn.stylefeng.roses.kernel.city.modular.pojo.request.AreaRequest;
import cn.stylefeng.roses.kernel.city.modular.service.AreaService;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行政区域业务实现层
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    @Override
    public void add(AreaRequest areaRequest) {
        Area area = new Area();
        BeanUtil.copyProperties(areaRequest, area);
        this.save(area);
    }

    @Override
    public void del(AreaRequest areaRequest) {
        Area area = this.queryArea(areaRequest);
        this.removeById(area.getAreaId());
    }

    @Override
    public void edit(AreaRequest areaRequest) {
        Area area = this.queryArea(areaRequest);
        BeanUtil.copyProperties(areaRequest, area);
        this.updateById(area);
    }

    @Override
    public Area detail(AreaRequest areaRequest) {
        return this.queryArea(areaRequest);
    }

    @Override
    public PageResult<Area> findPage(AreaRequest areaRequest) {
        LambdaQueryWrapper<Area> wrapper = this.createWrapper(areaRequest);
        Page<Area> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    public List<Area> findList(AreaRequest areaRequest) {

        String parentId = areaRequest.getParentId();
        if (ObjectUtil.isEmpty(parentId)) {
            areaRequest.setParentId(TreeConstants.DEFAULT_PARENT_ID.toString());
        }

        LambdaQueryWrapper<Area> wrapper = this.createWrapper(areaRequest);

        wrapper.select(Area::getAreaId, Area::getAreaName, Area::getAreaCode);

        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    private Area queryArea(AreaRequest areaRequest) {
        Area area = this.getById(areaRequest.getAreaId());
        if (ObjectUtil.isEmpty(area)) {
            throw new ServiceException(AreaExceptionEnum.AREA_NOT_EXISTED);
        }
        return area;
    }

    /**
     * 创建查询wrapper
     *
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    private LambdaQueryWrapper<Area> createWrapper(AreaRequest areaRequest) {
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();

        String parentId = areaRequest.getParentId();
        queryWrapper.eq(ObjectUtil.isNotEmpty(parentId), Area::getParentId, parentId);

        // 排序字段排序
        queryWrapper.orderByAsc(Area::getAreaSort);

        return queryWrapper;
    }

}