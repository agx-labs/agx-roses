package cn.stylefeng.roses.kernel.sys.modular.theme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.sys.api.exception.SysException;
import cn.stylefeng.roses.kernel.sys.modular.theme.constants.ThemeConstants;
import cn.stylefeng.roses.kernel.sys.modular.theme.entity.SysThemeTemplateField;
import cn.stylefeng.roses.kernel.sys.modular.theme.entity.SysThemeTemplateRel;
import cn.stylefeng.roses.kernel.sys.modular.theme.enums.ThemeFieldTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.theme.exceptions.SysThemeExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.theme.exceptions.SysThemeTemplateFieldExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.theme.mapper.SysThemeTemplateFieldMapper;
import cn.stylefeng.roses.kernel.sys.modular.theme.pojo.SysThemeTemplateFieldRequest;
import cn.stylefeng.roses.kernel.sys.modular.theme.service.SysThemeTemplateFieldService;
import cn.stylefeng.roses.kernel.sys.modular.theme.service.SysThemeTemplateRelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 系统主题模板属性service接口实现类
 *
 * @author xixiaowei
 * @since 2021/12/17 10:34
 */
@Service
public class SysThemeTemplateFieldServiceImpl extends ServiceImpl<SysThemeTemplateFieldMapper, SysThemeTemplateField> implements
        SysThemeTemplateFieldService {

    @Resource
    private SysThemeTemplateRelService sysThemeTemplateRelService;

    @Override
    public void add(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest) {
        SysThemeTemplateField sysThemeTemplateField = new SysThemeTemplateField();

        // 拷贝属性
        BeanUtil.copyProperties(sysThemeTemplateFieldRequest, sysThemeTemplateField);

        // 设置是否必填：如果请求参数为空，默认设置为非必填
        sysThemeTemplateField.setFieldRequired(StringUtils.isBlank(sysThemeTemplateFieldRequest.getFieldType()) ? YesOrNotEnum.N.getCode()
                .charAt(0) : sysThemeTemplateFieldRequest.getFieldRequired());

        this.save(sysThemeTemplateField);
    }

    @Override
    public void del(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest) {
        SysThemeTemplateField sysThemeTemplateField = this.queryThemeTemplateFieldById(sysThemeTemplateFieldRequest);

        // Guns开头的模板字段不能删除，系统内置
        if (sysThemeTemplateField.getFieldCode().toUpperCase(Locale.ROOT).startsWith(ThemeConstants.THEME_CODE_SYSTEM_PREFIX)) {
            throw new SysException(SysThemeExceptionEnum.THEME_IS_SYSTEM);
        }

        // 校验系统主题模板属性使用
        this.verificationAttributeUsage(sysThemeTemplateField);

        this.removeById(sysThemeTemplateField.getFieldId());
    }

    /**
     * 校验系统主题模板属性使用
     *
     * @author xixiaowei
     * @since 2021/12/24 9:16
     */
    private void verificationAttributeUsage(SysThemeTemplateField sysThemeTemplateField) {
        // 查询当前属性是否被使用
        LambdaQueryWrapper<SysThemeTemplateRel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysThemeTemplateRel::getFieldCode, sysThemeTemplateField.getFieldCode());

        List<SysThemeTemplateRel> themeTemplateRels = sysThemeTemplateRelService.list(queryWrapper);

        // 被使用，抛出异常
        if (themeTemplateRels.size() > 0) {
            throw new SysException(SysThemeTemplateFieldExceptionEnum.FIELD_IS_USED);
        }
    }

    @Override
    public void edit(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest) {
        SysThemeTemplateField sysThemeTemplateField = this.queryThemeTemplateFieldById(sysThemeTemplateFieldRequest);

        // 编号不能修改
        sysThemeTemplateFieldRequest.setFieldCode(null);

        // 更新属性
        BeanUtil.copyProperties(sysThemeTemplateFieldRequest, sysThemeTemplateField);

        this.updateById(sysThemeTemplateField);
    }

    @Override
    public SysThemeTemplateField detail(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest) {
        return this.queryThemeTemplateFieldById(sysThemeTemplateFieldRequest);
    }

    @Override
    public PageResult<SysThemeTemplateField> findPage(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest) {
        LambdaQueryWrapper<SysThemeTemplateField> queryWrapper = new LambdaQueryWrapper<>();
        // 根据属性名称模糊查询
        queryWrapper.like(StrUtil.isNotBlank(sysThemeTemplateFieldRequest.getFieldName()), SysThemeTemplateField::getFieldName,
                sysThemeTemplateFieldRequest.getFieldName());

        Page<SysThemeTemplateField> page = page(PageFactory.defaultPage(), queryWrapper);

        return PageResultFactory.createPageResult(page);
    }

    @Override
    public List<SysThemeTemplateField> findRelList(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest) {
        List<String> fieldCodes = this.getFieldCodes(sysThemeTemplateFieldRequest, sysThemeTemplateRelService);

        // 查询具体属性信息
        LambdaQueryWrapper<SysThemeTemplateField> queryWrapper = new LambdaQueryWrapper<>();

        List<SysThemeTemplateField> sysThemeTemplateFields = null;
        // 如果关联属性非空，拼接查询条件
        if (fieldCodes.size() > 0) {
            queryWrapper.in(SysThemeTemplateField::getFieldCode, fieldCodes);
            sysThemeTemplateFields = this.list(queryWrapper);
        }

        return sysThemeTemplateFields;
    }

    /**
     * 查询所有关联的属性编码
     *
     * @author xixiaowei
     * @since 2021/12/24 14:38
     */
    private List<String> getFieldCodes(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest,
                                       SysThemeTemplateRelService sysThemeTemplateRelService) {
        // 查询有关联的属性
        LambdaQueryWrapper<SysThemeTemplateRel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysThemeTemplateRel::getTemplateId, sysThemeTemplateFieldRequest.getTemplateId());
        List<SysThemeTemplateRel> sysThemeTemplateRels = sysThemeTemplateRelService.list(queryWrapper);

        // 过滤出所有的属性编码
        return sysThemeTemplateRels.stream().map(SysThemeTemplateRel::getFieldCode).collect(Collectors.toList());
    }

    @Override
    public List<SysThemeTemplateField> findNotRelList(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest) {
        // 查询有关联的属性
        List<String> fieldCodes = getFieldCodes(sysThemeTemplateFieldRequest, sysThemeTemplateRelService);

        // 查询没有关联的属性
        LambdaQueryWrapper<SysThemeTemplateField> queryWrapper = new LambdaQueryWrapper<>();

        List<SysThemeTemplateField> sysThemeTemplateFields;
        // 如果关联属性非空，拼接条件；否者查询全部
        if (fieldCodes.size() > 0) {
            queryWrapper.notIn(SysThemeTemplateField::getFieldCode, fieldCodes);
            sysThemeTemplateFields = this.list(queryWrapper);
        } else {
            sysThemeTemplateFields = this.list();
        }

        return sysThemeTemplateFields;
    }

    @Override
    public boolean getKeyFileFlag(String code) {
        LambdaQueryWrapper<SysThemeTemplateField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysThemeTemplateField::getFieldCode, code);
        wrapper.select(SysThemeTemplateField::getFieldType);

        SysThemeTemplateField sysThemeTemplateField = this.getOne(wrapper, false);
        if (sysThemeTemplateField == null) {
            return false;
        }

        return ThemeFieldTypeEnum.FILE.getCode().equals(sysThemeTemplateField.getFieldType());
    }

    @Override
    public List<SysThemeTemplateField> getFieldListByFieldCode(List<String> fieldCodeList) {

        LambdaQueryWrapper<SysThemeTemplateField> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysThemeTemplateField::getFieldCode, fieldCodeList);

        // 只查询关键字段
        wrapper.select(SysThemeTemplateField::getFieldId, SysThemeTemplateField::getFieldCode, SysThemeTemplateField::getFieldName,
                SysThemeTemplateField::getFieldType, SysThemeTemplateField::getFieldLength, SysThemeTemplateField::getFieldRequired,
                SysThemeTemplateField::getFieldDescription);

        return this.list(wrapper);
    }

    /**
     * 获取主题模板属性
     *
     * @param sysThemeTemplateFieldRequest 请求参数
     * @return 主题模板属性
     * @author xixiaowei
     * @since 2021/12/17 11:03
     */
    private SysThemeTemplateField queryThemeTemplateFieldById(SysThemeTemplateFieldRequest sysThemeTemplateFieldRequest) {
        SysThemeTemplateField sysThemeTemplateField = this.getById(sysThemeTemplateFieldRequest.getFieldId());
        if (ObjectUtil.isNull(sysThemeTemplateField)) {
            throw new SysException(SysThemeTemplateFieldExceptionEnum.FIELD_NOT_EXIST);
        }
        return sysThemeTemplateField;
    }
}
