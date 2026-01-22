package cn.stylefeng.roses.kernel.sys.modular.message.mapper;

import cn.stylefeng.roses.kernel.db.mp.injector.CustomBaseMapper;
import cn.stylefeng.roses.kernel.sys.modular.message.entity.SysMessage;
import cn.stylefeng.roses.kernel.sys.modular.message.pojo.request.SysMessageRequest;
import cn.stylefeng.roses.kernel.sys.modular.message.pojo.response.SysMessageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统消息 Mapper 接口
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
public interface SysMessageMapper extends CustomBaseMapper<SysMessage> {

    /**
     * 获取自定义查询列表
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    List<SysMessageVo> customFindList(@Param("page") Page page, @Param("param") SysMessageRequest request);

}
