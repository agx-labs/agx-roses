package cn.stylefeng.roses.kernel.sys.modular.theme.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.sys.api.exception.SysException;
import cn.stylefeng.roses.kernel.sys.modular.theme.entity.SysThemeTemplate;
import cn.stylefeng.roses.kernel.sys.modular.theme.entity.SysThemeTemplateRel;
import cn.stylefeng.roses.kernel.sys.modular.theme.exceptions.SysThemeTemplateExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.theme.mapper.SysThemeTemplateRelMapper;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeTemplateRelRequest;
import cn.stylefeng.roses.kernel.sys.modular.theme.service.SysThemeTemplateRelService;
import cn.stylefeng.roses.kernel.sys.modular.theme.service.SysThemeTemplateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统主题模板属性关系service接口实现类
 *
 * @author xixiaowei
 * @since 2021/12/17 16:14
 */
@Service
public class SysThemeTemplateRelServiceImpl extends ServiceImpl<SysThemeTemplateRelMapper, SysThemeTemplateRel> implements
        SysThemeTemplateRelService {

    @Resource
    private SysThemeTemplateService sysThemeTemplateService;

    @Override
    public void add(SysThemeTemplateRelRequest sysThemeTemplateRelRequest) {
        // 校验模板状态
        this.checkTemplateStatus(sysThemeTemplateRelRequest);

        // 获取请求中的所有属性编码
        String[] fieldCodes = sysThemeTemplateRelRequest.getFieldCodes();

        List<SysThemeTemplateRel> sysThemeTemplateRels = new ArrayList<>();

        // 填充对象
        for (String fieldCode : fieldCodes) {
            SysThemeTemplateRel sysThemeTemplateRel = new SysThemeTemplateRel();
            sysThemeTemplateRel.setTemplateId(sysThemeTemplateRelRequest.getTemplateId());
            sysThemeTemplateRel.setFieldCode(fieldCode);

            sysThemeTemplateRels.add(sysThemeTemplateRel);
        }

        // 保存关系
        this.saveBatch(sysThemeTemplateRels);
    }

    /**
     * 校验模板使用状态
     *
     * @author xixiaowei
     * @since 2021/12/30 17:28
     */
    private void checkTemplateStatus(SysThemeTemplateRelRequest sysThemeTemplateRelRequest) {
        // 判断当前模板是否被使用
        LambdaQueryWrapper<SysThemeTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysThemeTemplate::getTemplateId, sysThemeTemplateRelRequest.getTemplateId());
        SysThemeTemplate sysThemeTemplate = sysThemeTemplateService.getOne(queryWrapper, true);
        // 判断状态，如果是启用则禁止操作
        if (YesOrNotEnum.Y.getCode().equals(sysThemeTemplate.getStatusFlag().toString())) {
            throw new SysException(SysThemeTemplateExceptionEnum.TEMPLATE_IS_USED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysThemeTemplateRelRequest sysThemeTemplateRelRequest) {
        // 校验模板状态
        this.checkTemplateStatus(sysThemeTemplateRelRequest);

        // 获取请求中的所有属性编码
        String[] fieldCodes = sysThemeTemplateRelRequest.getFieldCodes();

        // 构建删除条件
        LambdaQueryWrapper<SysThemeTemplateRel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysThemeTemplateRel::getFieldCode, fieldCodes);

        this.remove(queryWrapper);
    }

    @Override
    public List<String> getThemeTemplateFieldCodeList(Long templateId) {

        if (ObjectUtil.isEmpty(templateId)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysThemeTemplateRel> queryWrapper = new LambdaQueryWrapper<>();

        // 根据模板id查询
        queryWrapper.eq(SysThemeTemplateRel::getTemplateId, templateId);

        // 只查询字段的编码信息
        queryWrapper.select(SysThemeTemplateRel::getFieldCode);

        List<SysThemeTemplateRel> list = this.list(queryWrapper);

        return list.stream().map(SysThemeTemplateRel::getFieldCode).collect(Collectors.toList());
    }


}
