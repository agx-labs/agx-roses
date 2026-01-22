package cn.stylefeng.roses.kernel.sys.modular.security.service;

import cn.stylefeng.roses.kernel.sys.modular.security.entity.SysUserPasswordRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户历史密码记录服务类
 *
 * @author fengshuonan
 * @date 2023/10/04 23:28
 */
public interface SysUserPasswordRecordService extends IService<SysUserPasswordRecord> {

    /**
     * 获取最近几次的密码记录
     *
     * @author fengshuonan
     * @since 2023/10/5 20:01
     */
    List<SysUserPasswordRecord> getRecentRecords(Long userId, Integer times);

}
