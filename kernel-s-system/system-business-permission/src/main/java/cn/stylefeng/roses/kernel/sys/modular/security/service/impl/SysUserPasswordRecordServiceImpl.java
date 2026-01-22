package cn.stylefeng.roses.kernel.sys.modular.security.service.impl;

import cn.stylefeng.roses.kernel.sys.modular.security.entity.SysUserPasswordRecord;
import cn.stylefeng.roses.kernel.sys.modular.security.mapper.SysUserPasswordRecordMapper;
import cn.stylefeng.roses.kernel.sys.modular.security.service.SysUserPasswordRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户历史密码记录业务实现层
 *
 * @author fengshuonan
 * @date 2023/10/04 23:28
 */
@Service
public class SysUserPasswordRecordServiceImpl extends ServiceImpl<SysUserPasswordRecordMapper, SysUserPasswordRecord> implements
        SysUserPasswordRecordService {

    @Override
    public List<SysUserPasswordRecord> getRecentRecords(Long userId, Integer times) {

        if (times == null || times.equals(0)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysUserPasswordRecord> sysUserPasswordRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserPasswordRecordLambdaQueryWrapper.eq(SysUserPasswordRecord::getUserId, userId);

        Page<SysUserPasswordRecord> recordPage = new Page<>(1, times);
        Page<SysUserPasswordRecord> page = this.page(recordPage, sysUserPasswordRecordLambdaQueryWrapper);

        List<SysUserPasswordRecord> records = page.getRecords();
        if (records != null && records.size() > 0) {
            return records;
        }

        return new ArrayList<>();
    }

}
