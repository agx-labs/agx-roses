package cn.stylefeng.roses.kernel.file.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.file.api.FileInfoApi;
import cn.stylefeng.roses.kernel.file.api.pojo.response.SysFileInfoResponse;
import cn.stylefeng.roses.kernel.file.modular.entity.SysFileBusiness;
import cn.stylefeng.roses.kernel.file.modular.enums.SysFileBusinessExceptionEnum;
import cn.stylefeng.roses.kernel.file.modular.mapper.SysFileBusinessMapper;
import cn.stylefeng.roses.kernel.file.modular.pojo.request.SysFileBusinessRequest;
import cn.stylefeng.roses.kernel.file.modular.service.SysFileBusinessService;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务关联的文件业务实现层
 *
 * @author fengshuonan
 * @since 2023/03/31 13:30
 */
@Service
public class SysFileBusinessServiceImpl extends ServiceImpl<SysFileBusinessMapper, SysFileBusiness> implements SysFileBusinessService {

    @Resource
    private FileInfoApi fileInfoApi;

    @Override
    public void add(SysFileBusinessRequest sysFileBusinessRequest) {
        SysFileBusiness sysFileBusiness = new SysFileBusiness();
        BeanUtil.copyProperties(sysFileBusinessRequest, sysFileBusiness);
        this.save(sysFileBusiness);
    }

    @Override
    public void del(SysFileBusinessRequest sysFileBusinessRequest) {
        SysFileBusiness sysFileBusiness = this.querySysFileBusiness(sysFileBusinessRequest);
        this.removeById(sysFileBusiness.getFileBusinessId());
    }

    @Override
    public void edit(SysFileBusinessRequest sysFileBusinessRequest) {
        SysFileBusiness sysFileBusiness = this.querySysFileBusiness(sysFileBusinessRequest);
        BeanUtil.copyProperties(sysFileBusinessRequest, sysFileBusiness);
        this.updateById(sysFileBusiness);
    }

    @Override
    public SysFileBusiness detail(SysFileBusinessRequest sysFileBusinessRequest) {
        return this.querySysFileBusiness(sysFileBusinessRequest);
    }

    @Override
    public PageResult<SysFileBusiness> findPage(SysFileBusinessRequest sysFileBusinessRequest) {
        LambdaQueryWrapper<SysFileBusiness> wrapper = createWrapper(sysFileBusinessRequest);
        Page<SysFileBusiness> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysFileBusiness> findList(SysFileBusinessRequest sysFileBusinessRequest) {
        LambdaQueryWrapper<SysFileBusiness> wrapper = this.createWrapper(sysFileBusinessRequest);
        return this.list(wrapper);
    }

    @Override
    public void addFileBusinessBind(String businessCode, Long businessId, List<Long> fileIdList) {

        if (ObjectUtil.isEmpty(fileIdList) || ObjectUtil.isEmpty(businessCode) || ObjectUtil.isEmpty(businessId)) {
            return;
        }

        // 删除业务id下的所有文件
        this.removeBusinessIdFileList(businessCode, businessId);

        ArrayList<SysFileBusiness> sysFileBusinesses = new ArrayList<>();
        for (Long fileId : fileIdList) {
            SysFileBusiness sysFileBusiness = new SysFileBusiness();
            sysFileBusiness.setBusinessCode(businessCode);
            sysFileBusiness.setBusinessId(businessId);
            sysFileBusiness.setFileId(fileId);
            sysFileBusinesses.add(sysFileBusiness);
        }

        this.saveBatch(sysFileBusinesses);
    }

    @Override
    public List<SysFileInfoResponse> getBusinessFileInfoList(Long businessId) {

        // 获取业务下绑定的文件列表
        LambdaQueryWrapper<SysFileBusiness> sysFileBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysFileBusinessLambdaQueryWrapper.eq(SysFileBusiness::getBusinessId, businessId);
        List<SysFileBusiness> list = this.list(sysFileBusinessLambdaQueryWrapper);

        if (ObjectUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        // 填充文件的详细信息
        ArrayList<SysFileInfoResponse> sysFileInfoResponses = new ArrayList<>();
        for (SysFileBusiness sysFileBusiness : list) {

            // 获取每个文件的详情
            Long fileId = sysFileBusiness.getFileId();
            SysFileInfoResponse fileInfoWithoutContent = fileInfoApi.getFileInfoWithoutContent(fileId);
            if (fileInfoWithoutContent != null) {
                // 设置下载次数
                fileInfoWithoutContent.setDownloadCount(sysFileBusiness.getDownloadCount());

                // 设置上传人id
                fileInfoWithoutContent.setUploadUserId(sysFileBusiness.getCreateUser());

                // 设置上传时间
                fileInfoWithoutContent.setUploadTime(sysFileBusiness.getCreateTime());
                sysFileInfoResponses.add(fileInfoWithoutContent);
            }
        }

        return sysFileInfoResponses;
    }

    @Override
    public void addFileDownloadCount(Long businessId, Long fileId) {

        LambdaQueryWrapper<SysFileBusiness> sysFileBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysFileBusinessLambdaQueryWrapper.eq(SysFileBusiness::getBusinessId, businessId);
        sysFileBusinessLambdaQueryWrapper.eq(SysFileBusiness::getFileId, fileId);
        SysFileBusiness sysFileBusiness = this.getOne(sysFileBusinessLambdaQueryWrapper, false);

        if (sysFileBusiness != null) {
            sysFileBusiness.setDownloadCount(sysFileBusiness.getDownloadCount() + 1);
            this.updateById(sysFileBusiness);
        }

    }

    @Override
    public void removeBusinessIdFileList(String businessCode, Long businessId) {

        if (ObjectUtil.isEmpty(businessCode) || ObjectUtil.isEmpty(businessId)) {
            return;
        }

        LambdaQueryWrapper<SysFileBusiness> sysFileBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysFileBusinessLambdaQueryWrapper.eq(SysFileBusiness::getBusinessCode, businessCode);
        sysFileBusinessLambdaQueryWrapper.eq(SysFileBusiness::getBusinessId, businessId);
        this.remove(sysFileBusinessLambdaQueryWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2023/03/31 13:30
     */
    private SysFileBusiness querySysFileBusiness(SysFileBusinessRequest sysFileBusinessRequest) {
        SysFileBusiness sysFileBusiness = this.getById(sysFileBusinessRequest.getFileBusinessId());
        if (ObjectUtil.isEmpty(sysFileBusiness)) {
            throw new ServiceException(SysFileBusinessExceptionEnum.SYS_FILE_BUSINESS_NOT_EXISTED);
        }
        return sysFileBusiness;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2023/03/31 13:30
     */
    private LambdaQueryWrapper<SysFileBusiness> createWrapper(SysFileBusinessRequest sysFileBusinessRequest) {
        LambdaQueryWrapper<SysFileBusiness> queryWrapper = new LambdaQueryWrapper<>();

        Long businessId = sysFileBusinessRequest.getBusinessId();
        queryWrapper.eq(ObjectUtil.isNotNull(businessId), SysFileBusiness::getBusinessId, businessId);

        return queryWrapper;
    }

}