package cn.stylefeng.roses.kernel.log.security.mapper;

import cn.stylefeng.roses.kernel.log.api.pojo.security.LogSecurityRequest;
import cn.stylefeng.roses.kernel.log.security.entity.LogSecurity;
import cn.stylefeng.roses.kernel.log.security.pojo.response.LogSecurityVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 安全日志 Mapper 接口
 *
 * @author fengshuonan
 * @since 2024/07/11 15:56
 */
public interface LogSecurityMapper extends BaseMapper<LogSecurity> {

    /**
     * 获取自定义查询列表
     *
     * @author fengshuonan
     * @since 2024/07/11 15:56
     */
    List<LogSecurityVo> customFindList(@Param("page") Page page, @Param("param")LogSecurityRequest request);

}
