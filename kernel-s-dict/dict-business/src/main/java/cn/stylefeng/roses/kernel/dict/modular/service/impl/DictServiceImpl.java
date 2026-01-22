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
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.cache.api.constants.CacheConstants;
import cn.stylefeng.roses.kernel.dict.api.constants.DictConstants;
import cn.stylefeng.roses.kernel.dict.api.exception.DictException;
import cn.stylefeng.roses.kernel.dict.api.exception.enums.DictExceptionEnum;
import cn.stylefeng.roses.kernel.dict.api.pojo.DictDetail;
import cn.stylefeng.roses.kernel.dict.api.pojo.DictTreeDto;
import cn.stylefeng.roses.kernel.dict.api.pojo.SimpleDictUpdateParam;
import cn.stylefeng.roses.kernel.dict.modular.entity.SysDict;
import cn.stylefeng.roses.kernel.dict.modular.entity.SysDictType;
import cn.stylefeng.roses.kernel.dict.modular.factory.DictFactory;
import cn.stylefeng.roses.kernel.dict.modular.mapper.DictMapper;
import cn.stylefeng.roses.kernel.dict.modular.pojo.TreeDictInfo;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.DictRequest;
import cn.stylefeng.roses.kernel.dict.modular.service.DictService;
import cn.stylefeng.roses.kernel.dict.modular.service.DictTypeService;
import cn.stylefeng.roses.kernel.pinyin.api.PinYinApi;
import cn.stylefeng.roses.kernel.rule.constants.SymbolConstant;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.tree.buildpids.PidStructureBuildUtil;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 基础字典 服务实现类
 *
 * @author fengshuonan
 * @since 2020/12/26 22:36
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, SysDict> implements DictService {

    @Resource
    private PinYinApi pinYinApi;

    @Resource
    private DictTypeService dictTypeService;

    @Resource(name = "dictInfoCache")
    private CacheOperatorApi<DictDetail> dictInfoCache;

    @Resource(name = "dictNameMixedCache")
    private CacheOperatorApi<String> dictNameMixedCache;

    @Override
    public List<TreeDictInfo> getTreeDictList(DictRequest dictRequest) {

        // 获取字典类型下所有的字典
        List<SysDict> sysDictList = this.findList(dictRequest);
        if (ObjectUtil.isEmpty(sysDictList)) {
            return new ArrayList<>();
        }

        // 构造树节点信息
        ArrayList<TreeDictInfo> treeDictInfos = new ArrayList<>();
        for (SysDict sysDict : sysDictList) {
            TreeDictInfo treeDictInfo = new TreeDictInfo();
            treeDictInfo.setDictId(sysDict.getDictId());
            treeDictInfo.setDictParentId(sysDict.getDictParentId());
            treeDictInfo.setDictCode(sysDict.getDictCode());
            treeDictInfo.setDictName(sysDict.getDictName());
            treeDictInfo.setDictSort(sysDict.getDictSort());
            treeDictInfos.add(treeDictInfo);
        }

        // 构建菜单树
        return new DefaultTreeBuildFactory<TreeDictInfo>().doTreeBuild(treeDictInfos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DictRequest dictRequest) {

        // 校验字典重复，同一个字典类型下不能有重复的字典编码或者字典名称
        this.validateRepeat(dictRequest, false);

        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictRequest, sysDict);

        // 填充字典的拼音
        sysDict.setDictNamePinyin(pinYinApi.parseEveryPinyinFirstLetter(sysDict.getDictName()));

        // 填充字典的pids
        String pids = this.createPids(sysDict.getDictParentId());
        sysDict.setDictPids(pids);

        this.save(sysDict);
    }

    @Override
    public void del(DictRequest dictRequest) {
        // 真实删除字典
        this.removeById(dictRequest.getDictId());

        // 删除缓存
        this.deleteDictIdCaches(dictRequest.getDictId());
    }

    @Override
    public void edit(DictRequest dictRequest) {

        // 校验字典重复
        this.validateRepeat(dictRequest, true);

        // 查询旧的字典
        SysDict sysDict = this.querySysDict(dictRequest);

        // 删除旧字典名称缓存
        this.deleteDictIdCaches(sysDict);

        // copy前端的属性
        BeanUtil.copyProperties(dictRequest, sysDict);

        // 不能修改字典类型、编码和字典的上下级关系（上下级关系和顺序，通过更新字典树接口更方便）
        sysDict.setDictTypeId(null);
        sysDict.setDictCode(null);
        sysDict.setDictParentId(null);

        // 填充拼音
        sysDict.setDictNamePinyin(pinYinApi.parseEveryPinyinFirstLetter(sysDict.getDictName()));

        this.updateById(sysDict);
    }

    @Override
    public SysDict detail(DictRequest dictRequest) {
        SysDict sysDict = this.querySysDict(dictRequest);
        if (ObjectUtil.isNotEmpty(sysDict.getDictTypeId())) {
            SysDictType sysDictType = dictTypeService.getById(sysDict.getDictTypeId());
            sysDict.setDictTypeName(sysDictType.getDictTypeName());
        }
        return sysDict;
    }

    @Override
    public List<SysDict> findList(DictRequest dictRequest) {

        LambdaQueryWrapper<SysDict> wrapper = this.createWrapper(dictRequest);

        // 只查询有用字段
        wrapper.select(SysDict::getDictId, SysDict::getDictTypeId, SysDict::getDictName, SysDict::getDictCode, SysDict::getDictSort, SysDict::getDictParentId);

        return this.list(wrapper);
    }

    @Override
    public void removeByDictTypeId(Long dictTypeId) {

        // 查询字典类型下的所有字典
        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictTypeId, dictTypeId);
        sysDictLambdaQueryWrapper.select(SysDict::getDictId, SysDict::getDictTypeId, SysDict::getDictCode);
        List<SysDict> list = this.list(sysDictLambdaQueryWrapper);
        if (ObjectUtil.isEmpty(list)) {
            return;
        }

        // 删除字典名称缓存
        for (SysDict sysDict : list) {
            this.deleteDictIdCaches(sysDict);
        }

        // 删除字典类型下的所有字典
        this.remove(sysDictLambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictTree(DictRequest dictRequest) {

        // 获取字典树的结构
        List<SysDict> totalDictStructure = dictRequest.getTotalDictStructure();

        // 调整字典的顺序
        DictFactory.updateSort(totalDictStructure, 1);

        // 填充树节点的parentId字段
        DictFactory.fillParentId(-1L, totalDictStructure);

        // 平行展开树形结构，准备从新整理pids
        List<SysDict> totalDictList = new ArrayList<>();
        DictFactory.collectTreeTasks(totalDictStructure, totalDictList);

        // 从新整理上下级结构，整理id和pid关系
        PidStructureBuildUtil.createPidStructure(totalDictList);

        // 更新菜单的sort字段、pid字段和pids字段这3个字段
        this.updateBatchById(totalDictList);
    }

    @Override
    public String getDictName(Long dictTypeId, String dictCode) {

        if (ObjectUtil.isEmpty(dictTypeId) || ObjectUtil.isEmpty(dictCode)) {
            return "";
        }

        // 拼接缓存key
        String cacheKey = dictTypeId + dictCode;

        // 获取缓存中是否有值
        String dictName = this.dictNameMixedCache.get(cacheKey);
        if (dictName != null) {
            return dictName;
        }

        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictTypeId, dictTypeId);
        sysDictLambdaQueryWrapper.eq(SysDict::getDictCode, dictCode);
        sysDictLambdaQueryWrapper.select(SysDict::getDictName);
        SysDict sysDict = this.getOne(sysDictLambdaQueryWrapper, false);

        // 添加结果到缓存，然后再返回
        if (ObjectUtil.isEmpty(sysDict)) {
            this.dictNameMixedCache.put(cacheKey, StrUtil.EMPTY, DictConstants.DICT_DEFAULT_CACHE_TIMEOUT_SECONDS);
            return "";
        } else {
            this.dictNameMixedCache.put(cacheKey, sysDict.getDictName(), DictConstants.DICT_DEFAULT_CACHE_TIMEOUT_SECONDS);
            return sysDict.getDictName();
        }
    }

    @Override
    public String getDictName(String dictTypeCode, String dictCode) {

        if (ObjectUtil.isEmpty(dictTypeCode) || ObjectUtil.isEmpty(dictCode)) {
            return StrUtil.EMPTY;
        }

        // 先获取缓存中是否有
        String cacheKey = dictTypeCode + dictCode;
        String dictName = this.dictNameMixedCache.get(cacheKey);
        if (dictName != null) {
            return dictName;
        }

        // 获取字典类型编码对应的字典类型id
        Long dictTypeId = dictTypeService.getDictTypeIdByDictTypeCode(dictTypeCode);

        // 查询字典类型下有哪些字典
        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictTypeId, dictTypeId);
        sysDictLambdaQueryWrapper.eq(SysDict::getDictCode, dictCode);
        sysDictLambdaQueryWrapper.select(SysDict::getDictName);
        List<SysDict> list = this.list(sysDictLambdaQueryWrapper);

        // 如果查询不到字典，则返回空串
        if (list.isEmpty()) {
            this.dictNameMixedCache.put(cacheKey, StrUtil.EMPTY, CacheConstants.DEFAULT_EXP_SECONDS);
            return StrUtil.EMPTY;
        }

        // 字典code存在多个重复的，返回空串并打印错误日志
        if (list.size() > 1) {
            log.error(DictExceptionEnum.DICT_CODE_REPEAT.getUserTip(), "", dictCode);
            this.dictNameMixedCache.put(cacheKey, StrUtil.EMPTY, CacheConstants.DEFAULT_EXP_SECONDS);
            return StrUtil.EMPTY;
        }

        String dictNameResult = list.get(0).getDictName();
        this.dictNameMixedCache.put(cacheKey, dictNameResult, CacheConstants.DEFAULT_EXP_SECONDS);
        return dictNameResult;
    }

    @Override
    public List<SimpleDict> getDictDetailsByDictTypeCode(String dictTypeCode, String searchText) {

        // 获取字典类型编码对应的字典类型id
        Long dictTypeId = dictTypeService.getDictTypeIdByDictTypeCode(dictTypeCode);

        if (dictTypeId == null) {
            return new ArrayList<>();
        }

        // 查询字典的列表
        DictRequest dictRequest = new DictRequest();
        dictRequest.setDictTypeId(dictTypeId);
        dictRequest.setSearchText(searchText);
        LambdaQueryWrapper<SysDict> wrapper = this.createWrapper(dictRequest);
        wrapper.select(SysDict::getDictId, SysDict::getDictName, SysDict::getDictCode);
        List<SysDict> dictList = this.list(wrapper);

        if (dictList.isEmpty()) {
            return new ArrayList<>();
        }

        // 转化成响应结果
        ArrayList<SimpleDict> simpleDictList = new ArrayList<>();
        for (SysDict sysDict : dictList) {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setCode(sysDict.getDictCode());
            simpleDict.setName(sysDict.getDictName());
            simpleDict.setId(sysDict.getDictId());
            simpleDictList.add(simpleDict);
        }

        return simpleDictList;
    }

    @Override
    public void deleteByDictId(Long dictId) {
        // 删除字典记录
        this.removeById(dictId);

        // 删除字典缓存
        this.deleteDictIdCaches(dictId);
    }

    @Override
    public DictDetail getDictByDictId(Long dictId) {
        if (dictId == null) {
            return new DictDetail();
        }

        // 先从缓存获取字典详情
        DictDetail dictDetail = dictInfoCache.get(dictId.toString());
        if (dictDetail != null) {
            return dictDetail;
        }

        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictId, dictId);
        sysDictLambdaQueryWrapper.select(SysDict::getDictName, SysDict::getDictCode, SysDict::getDictId, SysDict::getDictSort);
        SysDict sysDict = this.getOne(sysDictLambdaQueryWrapper, false);
        if (sysDict == null) {

            // 加入结果到缓存
            dictInfoCache.put(dictId.toString(), new DictDetail(), DictConstants.DICT_DEFAULT_CACHE_TIMEOUT_SECONDS);
            return new DictDetail();
        } else {

            // 加入结果到缓存中
            DictDetail dictDetailTemp = new DictDetail(sysDict.getDictId(), sysDict.getDictCode(), sysDict.getDictName(), sysDict.getDictSort());
            dictInfoCache.put(dictId.toString(), dictDetailTemp, DictConstants.DICT_DEFAULT_CACHE_TIMEOUT_SECONDS);

            return dictDetailTemp;
        }
    }

    @Override
    public String getDictNameByDictId(Long dictId) {
        if (dictId == null) {
            return "";
        }

        // 走统一方法，可以从缓存查询
        DictDetail dictDetail = this.getDictByDictId(dictId);
        if (dictDetail == null) {
            return "";
        }

        return dictDetail.getDictName();
    }

    @Override
    public void simpleAddDict(SimpleDictUpdateParam simpleDictUpdateParam) {

        // 组装添加字典的参数
        DictRequest dictRequest = new DictRequest();
        BeanUtil.copyProperties(simpleDictUpdateParam, dictRequest);

        // 设置为启用
        dictRequest.setStatusFlag(StatusEnum.ENABLE.getCode());

        // 添加字典
        this.add(dictRequest);
    }

    @Override
    public void simpleEditDict(SimpleDictUpdateParam simpleDictUpdateParam) {

        // 组装编辑字典的参数
        DictRequest dictRequest = new DictRequest();
        BeanUtil.copyProperties(simpleDictUpdateParam, dictRequest);

        // 更新字典
        this.edit(dictRequest);
    }

    @Override
    public List<DictTreeDto> buildDictTreeStructure() {

        // 获取所有字典类型
        LambdaQueryWrapper<SysDictType> dictTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictTypeLambdaQueryWrapper.select(SysDictType::getDictTypeId, SysDictType::getDictTypeCode, SysDictType::getDictTypeName);
        dictTypeLambdaQueryWrapper.orderByAsc(SysDictType::getDictTypeSort);
        List<SysDictType> dictTypeList = this.dictTypeService.list(dictTypeLambdaQueryWrapper);

        // 获取所有字典信息
        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.select(SysDict::getDictCode, SysDict::getDictName, SysDict::getDictTypeId);
        sysDictLambdaQueryWrapper.orderByAsc(SysDict::getDictSort);
        List<SysDict> dictList = this.list(sysDictLambdaQueryWrapper);

        // 构建字典类型基本信息
        Map<Long, DictTreeDto> dictTypeMap = new HashMap<>();
        for (SysDictType sysDictType : dictTypeList) {
            DictTreeDto dictTreeDto = new DictTreeDto();
            dictTreeDto.setDictLabel(sysDictType.getDictTypeName());
            dictTreeDto.setDictValue(sysDictType.getDictTypeCode());
            dictTypeMap.put(sysDictType.getDictTypeId(), dictTreeDto);
        }

        // 遍历所有字典，把字典信息装配到字典类型信息里
        for (SysDict sysDict : dictList) {
            Long dictTypeId = sysDict.getDictTypeId();
            if (ObjectUtil.isEmpty(dictTypeId)) {
                continue;
            }
            DictTreeDto dictTreeDto = dictTypeMap.get(dictTypeId);
            if (dictTreeDto == null) {
                continue;
            }

            List<DictTreeDto> children = dictTreeDto.getChildren();
            if (ObjectUtil.isEmpty(children)) {
                children = new ArrayList<>();
            }

            DictTreeDto dictValue = new DictTreeDto();
            dictValue.setDictLabel(sysDict.getDictName());
            dictValue.setDictValue(sysDict.getDictCode());

            children.add(dictValue);
            dictTreeDto.setChildren(children);
        }

        // map转化为树形结构
        return ListUtil.list(true, dictTypeMap.values());
    }

    @Override
    public String getPinyin(String name) {
        if (ObjectUtil.isNotEmpty(name)) {
            name = pinYinApi.parsePinyinString(name);
        }
        return name;
    }

    @Override
    public void batchDelete(DictRequest dictRequest) {

        // 获取这一批字典的详情
        List<SysDict> dictList = this.listByIds(dictRequest.getDictIdList());
        if (ObjectUtil.isEmpty(dictList)) {
            return;
        }

        // 删除这一批字典的缓存
        for (SysDict sysDict : dictList) {
            this.deleteDictIdCaches(sysDict);
        }

        // 删除字典
        this.removeBatchByIds(dictRequest.getDictIdList());
    }

    /**
     * 删除字典id对应的缓存，目前有3种缓存
     * <p>
     * 第一种：key是字典id
     * 第二种：key是字典类型id + 字典编码
     * 第三种：key是字典类型编码 + 字典编码
     *
     * @author fengshuonan
     * @since 2025/1/10 18:02
     */
    @Override
    public void deleteDictIdCaches(Long dictId) {
        if (dictId == null) {
            return;
        }

        // 获取字典id的详情
        SysDict sysDict = this.getById(dictId);
        if (sysDict == null) {
            return;
        }
        this.deleteDictIdCaches(sysDict);
    }

    /**
     * 删除字典id对应的缓存，目前有3种缓存
     * <p>
     * 第一种：key是字典id
     * 第二种：key是字典类型id + 字典编码
     * 第三种：key是字典类型编码 + 字典编码
     *
     * @author fengshuonan
     * @since 2025/1/10 18:02
     */
    @Override
    public void deleteDictIdCaches(SysDict sysDict) {
        if (sysDict == null) {
            return;
        }

        // 删除字典id为key的缓存
        this.dictInfoCache.remove(String.valueOf(sysDict.getDictId()));

        // 删除字典类型id+字典编码为缓存key的缓存
        this.dictNameMixedCache.remove(sysDict.getDictTypeId() + sysDict.getDictCode());

        // 获取字典类型对应的字典编码
        SysDictType sysDictType = dictTypeService.getById(sysDict.getDictTypeId());
        if (sysDictType != null) {
            this.dictNameMixedCache.remove(sysDictType.getDictTypeCode() + sysDict.getDictCode());
        }
    }

    /**
     * 获取详细信息
     *
     * @author chenjinlong
     * @since 2021/1/13 10:50
     */
    private SysDict querySysDict(DictRequest dictRequest) {
        SysDict sysDict = this.getById(dictRequest.getDictId());
        if (ObjectUtil.isNull(sysDict)) {
            throw new DictException(DictExceptionEnum.DICT_NOT_EXISTED, dictRequest.getDictId());
        }
        return sysDict;
    }

    /**
     * 构建wrapper
     *
     * @author chenjinlong
     * @since 2021/1/13 10:50
     */
    private LambdaQueryWrapper<SysDict> createWrapper(DictRequest dictRequest) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();

        // 根据名称或者编码进行查询
        String searchText = dictRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.and(wq -> {
                wq.like(SysDict::getDictName, searchText).or().like(SysDict::getDictCode, searchText).or()
                        .like(SysDict::getDictNamePinyin, searchText);
            });
        }

        // 根据字典类型id查询字典
        queryWrapper.eq(ObjectUtil.isNotEmpty(dictRequest.getDictTypeId()), SysDict::getDictTypeId, dictRequest.getDictTypeId());

        // 根据字典类型编码查询
        if (StrUtil.isNotBlank(dictRequest.getDictTypeCode())) {

            // 根据字典类型编码，获取字典类型的id
            Long dictTypeId = dictTypeService.getDictTypeIdByDictTypeCode(dictRequest.getDictTypeCode());
            if (dictTypeId != null) {
                queryWrapper.eq(SysDict::getDictTypeId, dictTypeId);
            } else {
                // 字典类型不存在，则查询一个不存在的字典类型id
                queryWrapper.eq(SysDict::getDictTypeId, -1L);
            }
        }

        // 排序
        queryWrapper.orderByAsc(SysDict::getDictSort);

        return queryWrapper;
    }

    /**
     * 检查添加和编辑字典是否有重复的编码和名称
     *
     * @author fengshuonan
     * @since 2021/5/12 16:58
     */
    private void validateRepeat(DictRequest dictRequest, boolean editFlag) {

        // 检验同字典类型下是否有一样的编码
        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictTypeId, dictRequest.getDictTypeId());
        sysDictLambdaQueryWrapper.eq(SysDict::getDictCode, dictRequest.getDictCode());
        if (editFlag) {
            sysDictLambdaQueryWrapper.ne(SysDict::getDictId, dictRequest.getDictId());
        }
        long count = this.count(sysDictLambdaQueryWrapper);
        if (count > 0) {
            throw new DictException(DictExceptionEnum.DICT_CODE_REPEAT, dictRequest.getDictTypeId(), dictRequest.getDictCode());
        }

        // 检验同字典类型下是否有一样的名称
        LambdaQueryWrapper<SysDict> dictNameWrapper = new LambdaQueryWrapper<>();
        dictNameWrapper.eq(SysDict::getDictTypeId, dictRequest.getDictTypeId());
        dictNameWrapper.eq(SysDict::getDictName, dictRequest.getDictName());
        if (editFlag) {
            dictNameWrapper.ne(SysDict::getDictId, dictRequest.getDictId());
        }
        long dictNameCount = this.count(dictNameWrapper);
        if (dictNameCount > 0) {
            throw new DictException(DictExceptionEnum.DICT_NAME_REPEAT, dictRequest.getDictTypeId(), dictRequest.getDictCode());
        }
    }

    /**
     * 创建字典的pids的值
     * <p>
     * 如果pid是顶级节点，pids = 【[-1],】
     * <p>
     * 如果pid不是顶级节点，pids = 【父菜单的pids,[pid],】
     *
     * @author fengshuonan
     * @since 2023/6/27 17:24
     */
    private String createPids(Long dictParentId) {
        if (dictParentId.equals(TreeConstants.DEFAULT_PARENT_ID)) {
            return SymbolConstant.LEFT_SQUARE_BRACKETS + TreeConstants.DEFAULT_PARENT_ID + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;
        } else {
            //获取父字典
            LambdaQueryWrapper<SysDict> dictWrapper = new LambdaQueryWrapper<>();
            dictWrapper.eq(SysDict::getDictId, dictParentId);
            dictWrapper.select(SysDict::getDictPids);
            SysDict parentDictInfo = this.getOne(dictWrapper, false);
            if (parentDictInfo == null) {
                return SymbolConstant.LEFT_SQUARE_BRACKETS + TreeConstants.DEFAULT_PARENT_ID + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;
            } else {
                // 组装pids
                return parentDictInfo.getDictPids() + SymbolConstant.LEFT_SQUARE_BRACKETS + dictParentId + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;
            }
        }
    }

}
