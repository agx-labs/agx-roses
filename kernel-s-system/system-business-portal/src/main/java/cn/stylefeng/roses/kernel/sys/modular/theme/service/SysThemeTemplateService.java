package cn.stylefeng.roses.kernel.sys.modular.theme.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.theme.entity.SysThemeTemplate;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeTemplateDataDTO;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeTemplateRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统主题模板service接口
 *
 * @author xixiaowei
 * @since 2021/12/17 13:55
 */
public interface SysThemeTemplateService extends IService<SysThemeTemplate> {

    /**
     * 增加系统主题模板
     *
     * @author xixiaowei
     * @since 2021/12/17 14:04
     */
    void add(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 编辑系统主题模板
     *
     * @author xixiaowei
     * @since 2021/12/17 14:21
     */
    void edit(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 删除系统主题模板
     *
     * @author xixiaowei
     * @since 2021/12/17 14:38
     */
    void del(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 查询系统主题模板列表
     *
     * @return 分页结果
     * @author xixiaowei
     * @since 2021/12/17 14:52
     */
    PageResult<SysThemeTemplate> findPage(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 查询系统诸如提模板列表
     *
     * @author xixiaowei
     * @since 2021/12/29 9:10
     */
    List<SysThemeTemplate> findList(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 修改系统主题模板状态
     *
     * @author xixiaowei
     * @since 2021/12/17 15:03
     */
    void updateTemplateStatus(SysThemeTemplateRequest sysThemeTemplateRequest);

    /**
     * 查询系统主题模板详情
     *
     * @author xixiaowei
     * @since 2021/12/17 16:00
     */
    List<SysThemeTemplateDataDTO> detail(SysThemeTemplateRequest sysThemeTemplateRequest);
}
