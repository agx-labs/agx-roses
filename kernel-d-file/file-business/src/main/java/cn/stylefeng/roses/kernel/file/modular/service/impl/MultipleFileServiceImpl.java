package cn.stylefeng.roses.kernel.file.modular.service.impl;

import cn.stylefeng.roses.kernel.file.api.pojo.request.SysFileInfoRequest;
import cn.stylefeng.roses.kernel.file.api.pojo.response.SysFileInfoResponse;
import cn.stylefeng.roses.kernel.file.modular.entity.SysFileInfo;
import cn.stylefeng.roses.kernel.file.modular.factory.FileInfoFactory;
import cn.stylefeng.roses.kernel.file.modular.service.MultipleFileService;
import cn.stylefeng.roses.kernel.file.modular.service.SysFileInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 多文件上传和下载的业务
 *
 * @author fengshuonan
 * @since 2024/8/20 9:03
 */
@Service
public class MultipleFileServiceImpl implements MultipleFileService {

    @Resource
    private SysFileInfoService sysFileInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysFileInfoResponse> batchUploadFile(MultipartFile[] fileList, SysFileInfoRequest sysFileInfoRequest) {

        // 1. 创建文件基本信息
        Map<MultipartFile, SysFileInfo> multipartFileSysFileInfoMap = FileInfoFactory.batchCreateFileInfo(fileList, sysFileInfoRequest);

        // 2. 进行批量保存文件
        for (Map.Entry<MultipartFile, SysFileInfo> entry : multipartFileSysFileInfoMap.entrySet()) {
            MultipartFile file = entry.getKey();
            SysFileInfo fileInfo = entry.getValue();
            this.sysFileInfoService.storageFile(file, fileInfo);
        }

        // 2.1 批量保存文件信息
        Collection<SysFileInfo> sysFileInfos = multipartFileSysFileInfoMap.values();
        this.sysFileInfoService.saveBatch(sysFileInfos);

        // 3. 获取文件信息
        List<SysFileInfoResponse> fileInfoResponses = new ArrayList<>();
        for (SysFileInfo sysFileInfo : sysFileInfos) {
            SysFileInfoResponse fileInfoResponse = sysFileInfoService.getFileInfoResponse(sysFileInfo);
            fileInfoResponses.add(fileInfoResponse);
        }
        return fileInfoResponses;
    }

}