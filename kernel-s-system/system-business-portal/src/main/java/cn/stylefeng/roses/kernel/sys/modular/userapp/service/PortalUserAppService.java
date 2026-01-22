package cn.stylefeng.roses.kernel.sys.modular.userapp.service;

import cn.stylefeng.roses.kernel.sys.api.pojo.menu.UserAppMenuInfo;
import cn.stylefeng.roses.kernel.sys.modular.userapp.entity.PortalUserApp;
import cn.stylefeng.roses.kernel.sys.modular.userapp.pojo.request.PortalUserAppRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户常用功能 服务类
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
public interface PortalUserAppService extends IService<PortalUserApp> {

    /**
     * 更新用户的常用功能
     *
     * @param portalUserAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    void updateUserAppList(PortalUserAppRequest portalUserAppRequest);

    /**
     * 获取列表
     *
     * @return List<PortalUserApp>   返回结果
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    List<UserAppMenuInfo> getUserAppList();

}