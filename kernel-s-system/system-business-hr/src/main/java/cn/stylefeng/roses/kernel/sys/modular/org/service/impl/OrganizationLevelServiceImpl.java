package cn.stylefeng.roses.kernel.sys.modular.org.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.api.entity.OrganizationLevel;
import cn.stylefeng.roses.kernel.sys.api.exception.SysException;
import cn.stylefeng.roses.kernel.sys.api.exception.enums.OrgExceptionEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.OrganizationLevelRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.enums.OrganizationLevelExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.org.mapper.OrganizationLevelMapper;
import cn.stylefeng.roses.kernel.sys.modular.org.service.OrganizationLevelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织机构层级业务实现层
 *
 * @author fengshuonan
 * @since 2025/01/22 09:44
 */
@Service
public class OrganizationLevelServiceImpl extends ServiceImpl<OrganizationLevelMapper, OrganizationLevel> implements OrganizationLevelService {

    @Override
    public List<OrganizationLevel> findList(OrganizationLevelRequest organizationLevelRequest) {
        LambdaQueryWrapper<OrganizationLevel> wrapper = this.createWrapper(organizationLevelRequest);

        wrapper.select(OrganizationLevel::getOrgLevelId, OrganizationLevel::getLevelCode, OrganizationLevel::getLevelNumber, OrganizationLevel::getLevelName, OrganizationLevel::getLevelColor);

        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTotal(OrganizationLevelRequest organizationLevelRequest) {

        // 删除所有的层级数据
        this.remove(new LambdaQueryWrapper<>());

        // 插入层级数据
        List<OrganizationLevel> paramLevelList = organizationLevelRequest.getLevelList();

        // 去掉不需要的属性并校验参数
        List<OrganizationLevel> newList = new ArrayList<>();
        for (OrganizationLevel organizationLevel : paramLevelList) {
            if (ObjectUtil.isEmpty(organizationLevel.getLevelNumber())) {
                throw new SysException(OrgExceptionEnum.ORG_LEVEL_EMPTY, "levelNumber");
            }
            if (ObjectUtil.isEmpty(organizationLevel.getLevelName())) {
                throw new SysException(OrgExceptionEnum.ORG_LEVEL_EMPTY, "levelName");
            }
            if (ObjectUtil.isEmpty(organizationLevel.getLevelCode())) {
                throw new SysException(OrgExceptionEnum.ORG_LEVEL_EMPTY, "levelCode");
            }

            OrganizationLevel temp = new OrganizationLevel();
            temp.setLevelNumber(organizationLevel.getLevelNumber());
            temp.setLevelName(organizationLevel.getLevelName());
            temp.setLevelCode(organizationLevel.getLevelCode());
            temp.setLevelColor(organizationLevel.getLevelColor());
            newList.add(temp);
        }

        // 层级的编码、number、名称不能重复
        for (int i = 0; i < newList.size(); i++) {
            for (int j = i + 1; j < newList.size(); j++) {
                if (newList.get(i).getLevelCode().equals(newList.get(j).getLevelCode())) {
                    throw new SysException(OrganizationLevelExceptionEnum.CODE_CANT_REPEAT);
                }
                if (newList.get(i).getLevelNumber().equals(newList.get(j).getLevelNumber())) {
                    throw new SysException(OrganizationLevelExceptionEnum.NUMBER_CANT_REPEAT);
                }
                if (newList.get(i).getLevelName().equals(newList.get(j).getLevelName())) {
                    throw new SysException(OrganizationLevelExceptionEnum.NAME_CANT_REPEAT);
                }
            }
        }

        this.saveBatch(newList);
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2025/01/22 09:44
     */
    private LambdaQueryWrapper<OrganizationLevel> createWrapper(OrganizationLevelRequest organizationLevelRequest) {
        LambdaQueryWrapper<OrganizationLevel> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByAsc(OrganizationLevel::getLevelNumber);

        return queryWrapper;
    }

}
