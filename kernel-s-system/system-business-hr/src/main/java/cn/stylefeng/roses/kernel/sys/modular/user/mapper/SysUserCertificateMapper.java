package cn.stylefeng.roses.kernel.sys.modular.user.mapper;

import cn.stylefeng.roses.kernel.db.mp.injector.CustomBaseMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserCertificate;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserCertificateRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.response.SysUserCertificateVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户证书 Mapper 接口
 *
 * @author fengshuonan
 * @since 2023/11/09 22:44
 */
public interface SysUserCertificateMapper extends CustomBaseMapper<SysUserCertificate> {

    /**
     * 获取自定义查询列表
     *
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    List<SysUserCertificateVo> customFindList(@Param("page") Page page, @Param("param")SysUserCertificateRequest request);

}
