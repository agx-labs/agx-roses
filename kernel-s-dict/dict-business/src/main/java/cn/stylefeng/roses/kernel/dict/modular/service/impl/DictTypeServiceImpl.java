/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.dict.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.dict.api.enums.DictTypeClassEnum;
import cn.stylefeng.roses.kernel.dict.api.exception.DictException;
import cn.stylefeng.roses.kernel.dict.api.exception.enums.DictExceptionEnum;
import cn.stylefeng.roses.kernel.dict.modular.entity.SysDict;
import cn.stylefeng.roses.kernel.dict.modular.entity.SysDictType;
import cn.stylefeng.roses.kernel.dict.modular.mapper.DictTypeMapper;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.DictRequest;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.DictTypeRequest;
import cn.stylefeng.roses.kernel.dict.modular.service.DictService;
import cn.stylefeng.roses.kernel.dict.modular.service.DictTypeService;
import cn.stylefeng.roses.kernel.pinyin.api.PinYinApi;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典类型表 服务实现类
 *
 * @author fengshuonan
 * @since 2020/12/26 22:36
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, SysDictType> implements DictTypeService {

    @Resource
    private PinYinApi pinYinApi;

    @Resource
    private DictService dictService;

    @Override
    public void add(DictTypeRequest dictTypeRequest) {

        // 如果是系统级字典，只允许管理员操作
        validateSystemTypeClassOperate(dictTypeRequest);

        SysDictType sysDictType = new SysDictType();
        BeanUtil.copyProperties(dictTypeRequest, sysDictType);
        sysDictType.setStatusFlag(StatusEnum.ENABLE.getCode());
        sysDictType.setDictTypeNamePinyin(pinYinApi.parseEveryPinyinFirstLetter(sysDictType.getDictTypeName()));
        this.save(sysDictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(DictTypeRequest dictTypeRequest) {

        // 如果是系统级字典，只允许管理员操作
        validateSystemTypeClassOperate(dictTypeRequest);

        // 删除字典类型
        this.removeById(dictTypeRequest.getDictTypeId());

        // 删除字典类型下的所有字典
        dictService.removeByDictTypeId(dictTypeRequest.getDictTypeId());

        // 删除字典相关缓存
        this.deleteDictCaches(dictTypeRequest.getDictTypeId());
    }

    @Override
    public void edit(DictTypeRequest dictTypeRequest) {

        // 如果是系统级字典，只允许管理员操作
        validateSystemTypeClassOperate(dictTypeRequest);

        // 获取旧的字典类型信息
        SysDictType sysDictType = this.querySysDictType(dictTypeRequest);

        // 删除旧的字典类型的缓存
        this.deleteDictCaches(dictTypeRequest.getDictTypeId());

        // 修改属性
        BeanUtil.copyProperties(dictTypeRequest, sysDictType);

        // 字典类型编码不能修改
        sysDictType.setDictTypeCode(null);

        // 设置首字母拼音
        sysDictType.setDictTypeNamePinyin(pinYinApi.parseEveryPinyinFirstLetter(sysDictType.getDictTypeName()));

        this.updateById(sysDictType);
    }

    @Override
    public SysDictType detail(DictTypeRequest dictTypeRequest) {
        return this.querySysDictType(dictTypeRequest);
    }

    @Override
    public List<SysDictType> findList(DictTypeRequest dictTypeRequest) {

        LambdaQueryWrapper<SysDictType> wrapper = this.createWrapper(dictTypeRequest);

        // 只查询字典类型id和字典类型名称
        wrapper.select(SysDictType::getDictTypeName, SysDictType::getDictTypeId);

        return this.list(wrapper);
    }

    @Override
    public Long getDictTypeIdByDictTypeCode(String dictTypeCode) {

        if (ObjectUtil.isEmpty(dictTypeCode)) {
            return null;
        }

        LambdaQueryWrapper<SysDictType> sysDictTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictTypeLambdaQueryWrapper.eq(SysDictType::getDictTypeCode, dictTypeCode);
        sysDictTypeLambdaQueryWrapper.select(SysDictType::getDictTypeId);
        SysDictType sysDictType = this.getOne(sysDictTypeLambdaQueryWrapper);

        if (sysDictType == null) {
            return null;
        }

        return sysDictType.getDictTypeId();
    }

    /**
     * 校验dictTypeClass是否是系统字典，如果是系统字典只能超级管理员操作
     *
     * @author fengshuonan
     * @since 2020/12/25 15:57
     */
    private void validateSystemTypeClassOperate(DictTypeRequest dictTypeRequest) {
        if (DictTypeClassEnum.SYSTEM_TYPE.getCode().equals(dictTypeRequest.getDictTypeClass())) {
            if (!LoginContext.me().getSuperAdminFlag()) {
                throw new DictException(DictExceptionEnum.SYSTEM_DICT_NOT_ALLOW_OPERATION);
            }
        }
    }

    /**
     * 根据主键id获取对象
     *
     * @author chenjinlong
     * @since 2021/1/26 13:28
     */
    private SysDictType querySysDictType(DictTypeRequest dictTypeRequest) {
        SysDictType sysDictType = this.getById(dictTypeRequest.getDictTypeId());
        if (ObjectUtil.isEmpty(sysDictType)) {
            throw new DictException(DictExceptionEnum.DICT_TYPE_NOT_EXISTED, dictTypeRequest.getDictTypeId());
        }
        return sysDictType;
    }

    /**
     * 实体构建queryWrapper
     *
     * @author fengshuonan
     * @since 2021/1/24 22:03
     */
    private LambdaQueryWrapper<SysDictType> createWrapper(DictTypeRequest dictTypeRequest) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();

        // 根据搜索的名称进行查询字典分类
        String searchText = dictTypeRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(SysDictType::getDictTypeName, searchText);
            queryWrapper.or().like(SysDictType::getDictTypeCode, searchText);
            queryWrapper.or().like(SysDictType::getDictTypeNamePinyin, searchText);
        }

        return queryWrapper;
    }

    /**
     * 删除针对字典类型下的所有字典的缓存
     *
     * @author fengshuonan
     * @since 2025/1/10 18:12
     */
    private void deleteDictCaches(Long dictTypeId) {
        if (dictTypeId == null) {
            return;
        }

        // 获取字典类型下的所有字典
        DictRequest dictRequest = new DictRequest();
        dictRequest.setDictTypeId(dictTypeId);
        List<SysDict> list = dictService.findList(dictRequest);

        // 删除掉这些字典的所有缓存
        for (SysDict sysDict : list) {
            this.dictService.deleteDictIdCaches(sysDict);
        }
    }

}
