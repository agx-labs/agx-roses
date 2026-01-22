package cn.stylefeng.roses.kernel.sys.modular.tablewidth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.entity.SysTableWidth;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.enums.FieldTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.enums.exceptions.SysTableWidthExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.mapper.SysTableWidthMapper;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.pojo.request.SysTableWidthRequest;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.service.SysTableWidthService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务中表的宽度业务实现层
 *
 * @author fengshuonan
 * @date 2023/02/23 22:21
 */
@Service
public class SysTableWidthServiceImpl extends ServiceImpl<SysTableWidthMapper, SysTableWidth> implements SysTableWidthService {

    @Override
    public SysTableWidth detail(SysTableWidthRequest sysTableWidthRequest) {

        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 获取当前人，在这个业务下，是否有生成过配置
        LambdaQueryWrapper<SysTableWidth> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTableWidth::getUserId, userId);
        wrapper.eq(SysTableWidth::getFieldBusinessCode, sysTableWidthRequest.getFieldBusinessCode());
        wrapper.select(SysTableWidth::getFieldBusinessCode, SysTableWidth::getTableWidthJson, SysTableWidth::getFieldType);
        SysTableWidth sysTableWidth = this.getOne(wrapper, false);

        // 如果存在配置，则直接返回
        if (sysTableWidth != null) {
            return sysTableWidth;
        }

        // 如果不存在配置，则去查找当前项目是否存在全局的配置
        LambdaQueryWrapper<SysTableWidth> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(SysTableWidth::getFieldBusinessCode, sysTableWidthRequest.getFieldBusinessCode());
        wrapper2.eq(SysTableWidth::getFieldType, FieldTypeEnum.TOTAL.getCode());
        wrapper2.select(SysTableWidth::getFieldBusinessCode, SysTableWidth::getTableWidthJson, SysTableWidth::getFieldType);
        SysTableWidth totalTableWidth = this.getOne(wrapper2, false);

        // 存在全局配置，直接返回
        if (totalTableWidth != null) {
            return totalTableWidth;
        }

        // 如果没有个人配置，也没有全局配置，返回空
        return new SysTableWidth();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setTableWidth(SysTableWidthRequest sysTableWidthRequest) {

        // 如果设置全局的，需要超级管理员身份
        if (FieldTypeEnum.TOTAL.getCode().equals(sysTableWidthRequest.getFieldType())) {
            if (!LoginContext.me().getSuperAdminFlag()) {
                throw new ServiceException(SysTableWidthExceptionEnum.PERMISSION_NOT_ALLOW);
            }
        }

        SysTableWidth sysTableWidth = new SysTableWidth();
        BeanUtil.copyProperties(sysTableWidthRequest, sysTableWidth);

        // 当前用户id
        Long currentUserId = LoginContext.me().getLoginUser().getUserId();

        // 如果是保存个人的信息，设置上用户id
        if (FieldTypeEnum.USER.getCode().equals(sysTableWidthRequest.getFieldType())) {
            sysTableWidth.setUserId(currentUserId);
        }

        // 如果设置的是全局的，则把个人配置的全都删掉
        else {
            LambdaQueryWrapper<SysTableWidth> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysTableWidth::getFieldBusinessCode, sysTableWidthRequest.getFieldBusinessCode());
            this.remove(queryWrapper);
        }

        // 先删除用户的配置再保存
        String fieldBusinessCode = sysTableWidthRequest.getFieldBusinessCode();
        Integer fieldType = sysTableWidthRequest.getFieldType();

        LambdaQueryWrapper<SysTableWidth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTableWidth::getFieldBusinessCode, fieldBusinessCode);
        queryWrapper.eq(SysTableWidth::getFieldType, fieldType);
        queryWrapper.eq(SysTableWidth::getUserId, currentUserId);
        this.remove(queryWrapper);

        this.save(sysTableWidth);
    }

}