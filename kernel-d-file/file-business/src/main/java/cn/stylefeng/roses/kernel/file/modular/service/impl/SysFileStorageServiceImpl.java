package cn.stylefeng.roses.kernel.file.modular.service.impl;

import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.file.api.constants.FileConstants;
import cn.stylefeng.roses.kernel.file.api.expander.FileConfigExpander;
import cn.stylefeng.roses.kernel.file.modular.entity.SysFileStorage;
import cn.stylefeng.roses.kernel.file.modular.mapper.SysFileStorageMapper;
import cn.stylefeng.roses.kernel.file.modular.service.SysFileStorageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 文件存储信息业务实现层
 *
 * @author fengshuonan
 * @since 2022/01/08 15:53
 */
@Service
public class SysFileStorageServiceImpl extends ServiceImpl<SysFileStorageMapper, SysFileStorage> implements SysFileStorageService {

    @Override
    public void saveFile(Long fileId, byte[] bytes) {
        SysFileStorage sysFileStorage = new SysFileStorage();
        sysFileStorage.setFileId(fileId);
        sysFileStorage.setFileBytes(bytes);
        this.save(sysFileStorage);
    }

    @Override
    public String getFileAuthUrl(String fileId) {
        // 获取登录用户的token
        String token = LoginContext.me().getToken();
        return FileConfigExpander.getServerDeployHost() + FileConstants.FILE_PRIVATE_PREVIEW_URL + "?fileId=" + fileId + "&token=" + token;
    }

    @Override
    public String getFileUnAuthUrl(String fileId) {
        return FileConfigExpander.getServerDeployHost() + FileConstants.FILE_PUBLIC_PREVIEW_URL + "?fileId=" + fileId;
    }

}
