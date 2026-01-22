package cn.stylefeng.roses.kernel.sys.modular.theme.service;

import cn.stylefeng.roses.kernel.sys.modular.theme.entity.SysThemeTemplateRel;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeTemplateRelRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统主题模板属性关系service接口
 *
 * @author xixiaowei
 * @since 2021/12/17 16:13
 */
public interface SysThemeTemplateRelService extends IService<SysThemeTemplateRel> {

    /**
     * 增加系统主题模板属性关系
     *
     * @author xixiaowei
     * @since 2021/12/24 10:56
     */
    void add(SysThemeTemplateRelRequest sysThemeTemplateRelRequest);

    /**
     * 删除系统主题模板属性关系
     *
     * @author xixiaowei
     * @since 2021/12/24 11:18
     */
    void del(SysThemeTemplateRelRequest sysThemeTemplateRelRequest);

    /**
     * 获取主题模板下关联的主题字段code集合
     *
     * @author fengshuonan
     * @since 2023/7/11 17:02
     */
    List<String> getThemeTemplateFieldCodeList(Long templateId);

}
