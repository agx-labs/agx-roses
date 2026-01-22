package cn.stylefeng.roses.kernel.file.modular.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.file.api.FileBusinessApi;
import cn.stylefeng.roses.kernel.file.modular.entity.SysFileBusiness;
import cn.stylefeng.roses.kernel.file.modular.pojo.request.SysFileBusinessRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 业务关联的文件 服务类
 *
 * @author fengshuonan
 * @since 2023/03/31 13:30
 */
public interface SysFileBusinessService extends IService<SysFileBusiness>, FileBusinessApi {

    /**
     * 新增
     *
     * @param sysFileBusinessRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/31 13:30
     */
    void add(SysFileBusinessRequest sysFileBusinessRequest);

    /**
     * 删除
     *
     * @param sysFileBusinessRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/31 13:30
     */
    void del(SysFileBusinessRequest sysFileBusinessRequest);

    /**
     * 编辑
     *
     * @param sysFileBusinessRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/31 13:30
     */
    void edit(SysFileBusinessRequest sysFileBusinessRequest);

    /**
     * 查询详情
     *
     * @param sysFileBusinessRequest 请求参数
     * @author fengshuonan
     * @since 2023/03/31 13:30
     */
    SysFileBusiness detail(SysFileBusinessRequest sysFileBusinessRequest);

    /**
     * 获取列表
     *
     * @param sysFileBusinessRequest 请求参数
     * @return List<SysFileBusiness>   返回结果
     * @author fengshuonan
     * @since 2023/03/31 13:30
     */
    List<SysFileBusiness> findList(SysFileBusinessRequest sysFileBusinessRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysFileBusinessRequest 请求参数
     * @return PageResult<SysFileBusiness>   返回结果
     * @author fengshuonan
     * @since 2023/03/31 13:30
     */
    PageResult<SysFileBusiness> findPage(SysFileBusinessRequest sysFileBusinessRequest);

}