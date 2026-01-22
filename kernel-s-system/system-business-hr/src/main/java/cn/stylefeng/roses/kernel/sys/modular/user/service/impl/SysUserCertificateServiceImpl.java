package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.dsctn.api.context.DataSourceContext;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserCertificate;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserCertificateMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.response.SysUserCertificateResponse;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserCertificateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户证书业务实现层
 *
 * @author fengshuonan
 * @since 2023/11/09 22:44
 */
@Service
public class SysUserCertificateServiceImpl extends ServiceImpl<SysUserCertificateMapper, SysUserCertificate> implements
        SysUserCertificateService, RemoveUserCallbackApi {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserCertificate(Long userId, List<SysUserCertificate> sysUserCertificateList) {
        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(sysUserCertificateList)) {
            return;
        }

        // 先设置证书的用户id
        sysUserCertificateList.forEach(sysUserCertificate -> sysUserCertificate.setUserId(userId));

        // 先清空当前绑定的证书信息
        LambdaQueryWrapper<SysUserCertificate> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SysUserCertificate::getUserId, userId);
        this.remove(deleteWrapper);

        // 重新绑定用户证书信息
        if (DbTypeEnum.MYSQL.equals(DataSourceContext.me().getCurrentDbType())) {
            this.getBaseMapper().insertBatchSomeColumn(sysUserCertificateList);
        } else {
            this.saveBatch(sysUserCertificateList);
        }
    }

    @Override
    public List<SysUserCertificateResponse> getUserCertificateList(Long userId) {

        LambdaQueryWrapper<SysUserCertificate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserCertificate::getUserId, userId);

        queryWrapper.select(SysUserCertificate::getCertificateType, SysUserCertificate::getCertificateNo, SysUserCertificate::getDateIssued,
                SysUserCertificate::getDateExpires, SysUserCertificate::getIssuingAuthority, SysUserCertificate::getAttachmentId);
        List<SysUserCertificate> sysUserCertificateList = this.list(queryWrapper);
        if (ObjectUtil.isEmpty(sysUserCertificateList)) {
            return new ArrayList<>();
        }

        return BeanUtil.copyToList(sysUserCertificateList, SysUserCertificateResponse.class);
    }

    @Override
    public void validateHaveUserBind(Set<Long> beRemovedUserIdList) {
        // ignore
    }

    @Override
    public void removeUserAction(Set<Long> beRemovedUserIdList) {
        LambdaQueryWrapper<SysUserCertificate> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.in(SysUserCertificate::getUserId, beRemovedUserIdList);
        this.remove(deleteWrapper);
    }

}
