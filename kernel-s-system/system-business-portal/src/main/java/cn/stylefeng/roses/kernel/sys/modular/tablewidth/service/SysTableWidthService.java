package cn.stylefeng.roses.kernel.sys.modular.tablewidth.service;

import cn.stylefeng.roses.kernel.sys.modular.tablewidth.entity.SysTableWidth;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.pojo.request.SysTableWidthRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 业务中表的宽度 服务类
 *
 * @author fengshuonan
 * @date 2023/02/23 22:21
 */
public interface SysTableWidthService extends IService<SysTableWidth> {

    /**
     * 获取用户针对某个业务的table的列宽配置
     *
     * @author fengshuonan
     * @since 2023/6/27 23:23
     */
    SysTableWidth detail(SysTableWidthRequest sysTableWidthRequest);

    /**
     * 添加用户针对某个table的列属性配置
     *
     * @author fengshuonan
     * @since 2023/6/27 23:24
     */
    void setTableWidth(SysTableWidthRequest sysTableWidthRequest);

}