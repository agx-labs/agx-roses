package cn.stylefeng.roses.kernel.sys.modular.security.mapper;

import cn.stylefeng.roses.kernel.sys.modular.security.entity.SysUserPasswordRecord;
import cn.stylefeng.roses.kernel.sys.modular.security.pojo.request.SysUserPasswordRecordRequest;
import cn.stylefeng.roses.kernel.sys.modular.security.pojo.response.SysUserPasswordRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户历史密码记录 Mapper 接口
 *
 * @author fengshuonan
 * @date 2023/10/04 23:28
 */
public interface SysUserPasswordRecordMapper extends BaseMapper<SysUserPasswordRecord> {

    /**
     * 获取自定义查询列表
     *
     * @author fengshuonan
     * @date 2023/10/04 23:28
     */
    List<SysUserPasswordRecordVo> customFindList(@Param("page") Page page, @Param("param")SysUserPasswordRecordRequest request);

}
