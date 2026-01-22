package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.pidset.CalcParentIdListUtil;
import cn.stylefeng.roses.kernel.rule.pidset.callback.PidGetterService;
import cn.stylefeng.roses.kernel.rule.pidset.pojo.ParentIdInfoPojo;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.RoleCategory;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.RoleCategoryExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.RoleCategoryMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleCategoryRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.RoleCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 角色分类业务实现层
 *
 * @author fengshuonan
 * @since 2025/01/22 17:40
 */
@Service
public class RoleCategoryServiceImpl extends ServiceImpl<RoleCategoryMapper, RoleCategory> implements RoleCategoryService, PidGetterService {

    @Resource
    private DbOperatorApi dbOperatorApi;

    @Override
    public List<RoleCategory> treeList(RoleCategoryRequest roleCategoryRequest) {
        LambdaQueryWrapper<RoleCategory> wrapper = this.createWrapper(roleCategoryRequest);

        // 设置忽略查询的当条记录id
        if (roleCategoryRequest.getIgnoreCategoryId() != null) {
            wrapper.ne(RoleCategory::getId, roleCategoryRequest.getIgnoreCategoryId());
        }

        List<RoleCategory> list = this.list(wrapper);
        if (ObjectUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        // 构建角色类型树
        return new DefaultTreeBuildFactory<RoleCategory>().doTreeBuild(list);
    }

    @Override
    public void add(RoleCategoryRequest roleCategoryRequest) {
        RoleCategory roleCategory = new RoleCategory();
        BeanUtil.copyProperties(roleCategoryRequest, roleCategory);

        // 设置pids
        CalcParentIdListUtil.fillParentIds(roleCategory, this);

        this.save(roleCategory);
    }

    @Override
    public void del(RoleCategoryRequest roleCategoryRequest) {
        Long treeId = roleCategoryRequest.getId();

        // 获取所有的子级节点id
        Set<Long> childIdList = this.dbOperatorApi.findSubListByParentId("sys_role_category", "category_pids", "id", treeId);
        childIdList.add(treeId);

        // 删除本节点 + 子节点
        LambdaUpdateWrapper<RoleCategory> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(RoleCategory::getId, childIdList);
        this.remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(RoleCategoryRequest roleCategoryRequest) {
        RoleCategory oldCategory = this.queryRoleCategory(roleCategoryRequest);

        RoleCategory newRoleCategory = new RoleCategory();
        BeanUtil.copyProperties(oldCategory, newRoleCategory);
        BeanUtil.copyProperties(roleCategoryRequest, newRoleCategory);

        // 如果改了层级结构，则递归更新子结构
        if (!oldCategory.getCategoryParentId().equals(newRoleCategory.getCategoryParentId())) {

            // 设置文件夹的pids集合
            CalcParentIdListUtil.fillParentIds(newRoleCategory, this);

            // 更新文件夹的上下级结构
            CalcParentIdListUtil.updateParentIdStringList("sys_role_category", "category_pids", oldCategory, newRoleCategory, this);
        }

        this.updateById(newRoleCategory);
    }

    @Override
    public RoleCategory detail(RoleCategoryRequest roleCategoryRequest) {
        LambdaQueryWrapper<RoleCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleCategory::getId, roleCategoryRequest.getId());
        queryWrapper.select(RoleCategory::getId, RoleCategory::getCategoryParentId, RoleCategory::getCategoryPids, RoleCategory::getCategoryType, RoleCategory::getRoleCategoryName, RoleCategory::getFldSort);
        return this.getOne(queryWrapper, false);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    private RoleCategory queryRoleCategory(RoleCategoryRequest roleCategoryRequest) {
        RoleCategory roleCategory = this.getById(roleCategoryRequest.getId());
        if (ObjectUtil.isEmpty(roleCategory)) {
            throw new ServiceException(RoleCategoryExceptionEnum.ROLE_CATEGORY_NOT_EXISTED);
        }
        return roleCategory;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2025/01/22 17:40
     */
    private LambdaQueryWrapper<RoleCategory> createWrapper(RoleCategoryRequest roleCategoryRequest) {
        LambdaQueryWrapper<RoleCategory> queryWrapper = new LambdaQueryWrapper<>();

        // 根据名称查询
        String searchText = roleCategoryRequest.getSearchText();
        queryWrapper.like(ObjectUtil.isNotEmpty(searchText), RoleCategory::getRoleCategoryName, searchText);

        // 根据角色分类查询
        Integer categoryType = roleCategoryRequest.getCategoryType();
        queryWrapper.eq(ObjectUtil.isNotEmpty(categoryType), RoleCategory::getCategoryType, roleCategoryRequest.getCategoryType());

        // 根据指定公司查询
        Long companyId = roleCategoryRequest.getCompanyId();
        queryWrapper.eq(ObjectUtil.isNotEmpty(companyId), RoleCategory::getCompanyId, roleCategoryRequest.getCompanyId());

        queryWrapper.orderByAsc(RoleCategory::getFldSort);

        return queryWrapper;
    }

    @Override
    public ParentIdInfoPojo getPointNodePidInfo(Long itemId) {
        if (ObjectUtil.isEmpty(itemId)) {
            return null;
        }
        LambdaQueryWrapper<RoleCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleCategory::getId, itemId);
        queryWrapper.select(RoleCategory::getId, RoleCategory::getCategoryPids);
        RoleCategory roleCategory = this.getOne(queryWrapper, false);
        if (roleCategory == null) {
            return null;
        }
        return new ParentIdInfoPojo(roleCategory.getId(), roleCategory.getCategoryPids());
    }

}
