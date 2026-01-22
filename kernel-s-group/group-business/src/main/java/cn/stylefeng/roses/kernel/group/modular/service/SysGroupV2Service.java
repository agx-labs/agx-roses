package cn.stylefeng.roses.kernel.group.modular.service;

import cn.stylefeng.roses.kernel.group.api.pojo.SysGroupDTO;
import cn.stylefeng.roses.kernel.group.modular.pojo.GroupAddRequest;
import cn.stylefeng.roses.kernel.group.modular.pojo.GroupDelRequest;
import cn.stylefeng.roses.kernel.group.modular.pojo.GroupSelectListRequest;

import java.util.List;

/**
 * 业务分组的业务V2
 *
 * @author fengshuonan
 * @since 2025/10/27 10:55
 */
public interface SysGroupV2Service {

    /**
     * 添加分组的时候的下拉选择
     *
     * @author fengshuonan
     * @since 2025/10/27 11:00
     */
    List<SysGroupDTO> selectList(GroupSelectListRequest groupSelectListRequest);

    /**
     * 添加绑定分组
     *
     * @author fengshuonan
     * @since 2025/10/27 11:05
     */
    void addBindGroup(GroupAddRequest groupAddRequest);

    /**
     * 删除业务分组的绑定
     *
     * @author fengshuonan
     * @since 2025/10/27 11:45
     */
    void del(GroupDelRequest groupDelRequest);

}
