package cn.stylefeng.roses.kernel.config.modular.service;

import cn.stylefeng.roses.kernel.config.api.constants.ConfigConstants;
import cn.stylefeng.roses.kernel.config.modular.pojo.param.SysConfigTypeParam;
import cn.stylefeng.roses.kernel.dict.api.DictApi;
import cn.stylefeng.roses.kernel.dict.api.DictTypeApi;
import cn.stylefeng.roses.kernel.dict.api.pojo.DictDetail;
import cn.stylefeng.roses.kernel.dict.api.pojo.SimpleDictUpdateParam;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 配置类型业务
 *
 * @author fengshuonan
 * @since 2023/6/28 16:52
 */
@Service
public class SysConfigTypeService {

    @Resource
    private DictApi dictApi;

    @Resource
    private DictTypeApi dictTypeApi;

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 查询系统配置类型列表
     * <p>
     * 查询字典类型为config_group的所有字典列表
     *
     * @author fengshuonan
     * @since 2023/6/28 16:56
     */
    public List<SimpleDict> getSysConfigTypeList(SysConfigTypeParam sysConfigTypeParam) {
        return dictApi.getDictDetailsByDictTypeCode(ConfigConstants.CONFIG_GROUP_DICT_TYPE_CODE, sysConfigTypeParam.getSearchText());
    }

    /**
     * 新增系统配置类型
     * <p>
     * 针对字典类型编码为config_group的字典增加一个条目
     *
     * @author fengshuonan
     * @since 2023/6/28 17:07
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(SysConfigTypeParam sysConfigTypeParam) {

        // 查询字典类型
        Long dictTypeId = dictTypeApi.getDictTypeIdByDictTypeCode(ConfigConstants.CONFIG_GROUP_DICT_TYPE_CODE);

        // 配置类型信息转化为新增字典的参数信息
        SimpleDictUpdateParam simpleDictAddParam = new SimpleDictUpdateParam();
        simpleDictAddParam.setDictTypeId(dictTypeId);
        simpleDictAddParam.setDictName(sysConfigTypeParam.getConfigTypeName());
        simpleDictAddParam.setDictCode(sysConfigTypeParam.getConfigTypeCode());
        simpleDictAddParam.setDictSort(sysConfigTypeParam.getConfigTypeSort());

        // 新增一个字典
        dictApi.simpleAddDict(simpleDictAddParam);
    }

    /**
     * 新增系统配置类型
     * <p>
     * 针对字典类型编码为config_group的字典修改一个条目
     *
     * @author fengshuonan
     * @since 2023/6/28 17:32
     */
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysConfigTypeParam sysConfigTypeParam) {

        // 查询旧的字典编码
        DictDetail originDictInfo = dictApi.getDictByDictId(sysConfigTypeParam.getConfigTypeId());

        // 查询字典类型
        Long dictTypeId = dictTypeApi.getDictTypeIdByDictTypeCode(ConfigConstants.CONFIG_GROUP_DICT_TYPE_CODE);

        // 配置类型信息转化为编辑字典的参数信息
        SimpleDictUpdateParam simpleDictAddParam = new SimpleDictUpdateParam();
        simpleDictAddParam.setDictId(sysConfigTypeParam.getConfigTypeId());
        simpleDictAddParam.setDictTypeId(dictTypeId);
        simpleDictAddParam.setDictName(sysConfigTypeParam.getConfigTypeName());
        simpleDictAddParam.setDictCode(sysConfigTypeParam.getConfigTypeCode());
        simpleDictAddParam.setDictSort(sysConfigTypeParam.getConfigTypeSort());

        // 编辑字典
        dictApi.simpleEditDict(simpleDictAddParam);

        // 如果更新了字典的编码，则属于该类型下的字典配置编码也都修改
        if (!originDictInfo.getDictCode().equals(sysConfigTypeParam.getConfigTypeCode())) {
            sysConfigService.updateSysConfigTypeCode(originDictInfo.getDictCode(), sysConfigTypeParam.getConfigTypeCode());
        }

    }

    /**
     * 删除配置类型
     * <p>
     * 针对字典类型编码为config_group的字典删除一个条目
     *
     * @author fengshuonan
     * @since 2023/6/28 18:13
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(SysConfigTypeParam sysConfigTypeParam) {

        // 获取字典的编码
        DictDetail originDictInfo = dictApi.getDictByDictId(sysConfigTypeParam.getConfigTypeId());

        // 删除字典
        this.dictApi.deleteByDictId(sysConfigTypeParam.getConfigTypeId());

        // 删除配置类型对应的所有配置
        this.sysConfigService.delByConfigCode(originDictInfo.getDictCode());
    }

    /**
     * 获取配置类型详情
     * <p>
     * 本质是直接获取字典的详情
     *
     * @author fengshuonan
     * @since 2023/6/28 18:45
     */
    public DictDetail detail(SysConfigTypeParam sysConfigTypeParam) {
        return this.dictApi.getDictByDictId(sysConfigTypeParam.getConfigTypeId());
    }

}
