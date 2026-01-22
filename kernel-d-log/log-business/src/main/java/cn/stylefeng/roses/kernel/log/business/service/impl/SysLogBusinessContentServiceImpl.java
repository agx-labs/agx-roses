package cn.stylefeng.roses.kernel.log.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.dsctn.api.context.DataSourceContext;
import cn.stylefeng.roses.kernel.log.api.pojo.business.SysLogBusinessContentRequest;
import cn.stylefeng.roses.kernel.log.api.pojo.entity.SysLogBusinessContent;
import cn.stylefeng.roses.kernel.log.business.mapper.SysLogBusinessContentMapper;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessContentService;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务日志记录业务实现层
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
@Service
public class SysLogBusinessContentServiceImpl extends ServiceImpl<SysLogBusinessContentMapper, SysLogBusinessContent> implements
        SysLogBusinessContentService {

    @Override
    public PageResult<SysLogBusinessContent> findPage(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        LambdaQueryWrapper<SysLogBusinessContent> wrapper = createWrapper(sysLogBusinessContentRequest);

        // 只查询详情内容
        wrapper.select(SysLogBusinessContent::getLogContent);

        Page<SysLogBusinessContent> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public void batchSaveContent(List<SysLogBusinessContent> sysLogBusinessContentList) {
        if (DbTypeEnum.MYSQL.equals(DataSourceContext.me().getCurrentDbType())) {
            this.getBaseMapper().insertBatchSomeColumn(sysLogBusinessContentList);
        } else {
            this.saveBatch(sysLogBusinessContentList);
        }
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    private LambdaQueryWrapper<SysLogBusinessContent> createWrapper(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        LambdaQueryWrapper<SysLogBusinessContent> queryWrapper = new LambdaQueryWrapper<>();

        // 根据业务日志主键id查询
        Long businessLogId = sysLogBusinessContentRequest.getBusinessLogId();
        queryWrapper.eq(ObjectUtil.isNotNull(businessLogId), SysLogBusinessContent::getBusinessLogId, businessLogId);

        // 根据搜索条件查询
        String searchText = sysLogBusinessContentRequest.getSearchText();
        queryWrapper.like(ObjectUtil.isNotEmpty(searchText), SysLogBusinessContent::getLogContent, searchText);

        // 排序根据日志id排序
        queryWrapper.orderByAsc(SysLogBusinessContent::getContentId);

        return queryWrapper;
    }

}