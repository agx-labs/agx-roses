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
package cn.stylefeng.roses.kernel.config.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.system.SystemUtil;
import cn.stylefeng.roses.kernel.config.api.ConfigInitCallbackApi;
import cn.stylefeng.roses.kernel.config.api.ConfigInitStrategyApi;
import cn.stylefeng.roses.kernel.config.api.context.ConfigContext;
import cn.stylefeng.roses.kernel.config.api.enums.FileStorageTypeEnum;
import cn.stylefeng.roses.kernel.config.api.exception.ConfigException;
import cn.stylefeng.roses.kernel.config.api.exception.enums.ConfigExceptionEnum;
import cn.stylefeng.roses.kernel.config.api.pojo.ConfigInitItem;
import cn.stylefeng.roses.kernel.config.api.pojo.ConfigInitRequest;
import cn.stylefeng.roses.kernel.config.modular.entity.SysConfig;
import cn.stylefeng.roses.kernel.config.modular.mapper.SysConfigMapper;
import cn.stylefeng.roses.kernel.config.modular.pojo.InitConfigGroup;
import cn.stylefeng.roses.kernel.config.modular.pojo.InitConfigResponse;
import cn.stylefeng.roses.kernel.config.modular.pojo.param.SysConfigParam;
import cn.stylefeng.roses.kernel.config.modular.service.SysConfigService;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.callback.ConfigUpdateCallback;
import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.util.JarPathUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.stylefeng.roses.kernel.config.api.constants.ConfigConstants.*;

/**
 * 系统参数配置service接口实现类
 *
 * @author fengshuonan
 * @since 2020/4/14 11:16
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysConfigParam sysConfigParam) {

        // 1.构造实体
        SysConfig sysConfig = new SysConfig();
        BeanUtil.copyProperties(sysConfigParam, sysConfig);

        sysConfig.setStatusFlag(StatusEnum.ENABLE.getCode());

        // 2.保存到库中
        this.save(sysConfig);

        // 3.添加对应context
        ConfigContext.me().putConfig(sysConfigParam.getConfigCode(), sysConfigParam.getConfigValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysConfigParam sysConfigParam) {

        // 1.根据id获取常量
        SysConfig sysConfig = this.querySysConfig(sysConfigParam);
        if (sysConfig == null) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_NOT_EXIST, "id: " + sysConfigParam.getConfigId());
        }

        // 2.不能删除系统参数
        if (YesOrNotEnum.Y.getCode().equals(sysConfig.getSysFlag())) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_SYS_CAN_NOT_DELETE);
        }

        // 3.逻辑删除
        this.removeById(sysConfig.getConfigId());

        // 4.删除对应context
        ConfigContext.me().deleteConfig(sysConfigParam.getConfigCode());
    }

    @Override
    public void delByConfigCode(String configGroupCode) {

        if (ObjectUtil.isEmpty(configGroupCode)) {
            return;
        }

        LambdaUpdateWrapper<SysConfig> sysConfigLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysConfigLambdaUpdateWrapper.eq(SysConfig::getGroupCode, configGroupCode);
        this.remove(sysConfigLambdaUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysConfigParam sysConfigParam) {

        // 1.根据id获取常量信息
        SysConfig sysConfig = this.querySysConfig(sysConfigParam);

        // 2.请求参数转化为实体
        BeanUtil.copyProperties(sysConfigParam, sysConfig);

        // 不能修改状态，用修改状态接口修改状态
        // 不能修改配置的分组和配置的编码
        sysConfig.setStatusFlag(null);
        sysConfig.setGroupCode(null);
        sysConfig.setConfigCode(null);

        // 3.更新记录
        this.updateById(sysConfig);

        // 4.更新对应常量context
        ConfigContext.me().putConfig(sysConfigParam.getConfigCode(), sysConfigParam.getConfigValue());

        // 5.发布属性修改的事件
        try {
            Map<String, ConfigUpdateCallback> beansOfType = SpringUtil.getBeansOfType(ConfigUpdateCallback.class);
            for (ConfigUpdateCallback value : beansOfType.values()) {
                value.configUpdate(sysConfig.getConfigCode(), sysConfig.getConfigValue());
            }
        } catch (Exception e) {
            // 忽略找不到Bean的异常
        }
    }

    @Override
    public SysConfig detail(SysConfigParam sysConfigParam) {
        return this.querySysConfig(sysConfigParam);
    }

    @Override
    public PageResult<SysConfig> findPage(SysConfigParam sysConfigParam) {
        LambdaQueryWrapper<SysConfig> wrapper = this.createWrapper(sysConfigParam);

        // 只查询列表显示的数据
        wrapper.select(SysConfig::getConfigId, SysConfig::getConfigName, SysConfig::getConfigCode, SysConfig::getConfigValue,
                SysConfig::getSysFlag);

        Page<SysConfig> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initConfig(ConfigInitRequest configInitRequest) {

        if (configInitRequest == null || configInitRequest.getSysConfigs() == null) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_INIT_ERROR);
        }

        // 如果当前已经初始化过配置，则不能初始化
        Boolean alreadyInit = this.getInitConfigFlag();
        if (alreadyInit) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_INIT_ALREADY);
        }

        // 获取初始化回调接口的所有实现类
        Map<String, ConfigInitCallbackApi> beans = SpringUtil.getBeansOfType(ConfigInitCallbackApi.class);

        // 调用初始化之前回调
        if (ObjectUtil.isNotNull(beans)) {
            for (ConfigInitCallbackApi initCallbackApi : beans.values()) {
                initCallbackApi.initBefore();
            }
        }

        // 添加系统已经初始化的配置
        Map<String, String> sysConfigs = configInitRequest.getSysConfigs();
        sysConfigs.put(RuleConstants.SYSTEM_CONFIG_INIT_FLAG_NAME, "true");

        // 针对每个配置执行更新库和刷新缓存的操作
        for (Map.Entry<String, String> entry : sysConfigs.entrySet()) {
            String configCode = entry.getKey();
            String configValue = entry.getValue();

            // 更新数据库的这条配置记录
            LambdaUpdateWrapper<SysConfig> sysConfigLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            sysConfigLambdaUpdateWrapper.eq(SysConfig::getConfigCode, configCode);
            sysConfigLambdaUpdateWrapper.set(SysConfig::getConfigValue, configValue);
            this.update(sysConfigLambdaUpdateWrapper);

            // 更新缓存
            ConfigContext.me().putConfig(configCode, configValue);
        }

        // 调用初始化之后回调
        if (ObjectUtil.isNotNull(beans)) {
            for (ConfigInitCallbackApi initCallbackApi : beans.values()) {
                initCallbackApi.initAfter();
            }
        }
    }

    @Override
    public Boolean getInitConfigFlag() {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigCode, RuleConstants.SYSTEM_CONFIG_INIT_FLAG_NAME);
        wrapper.select(SysConfig::getConfigValue);
        SysConfig sysConfig = this.getOne(wrapper, false);

        // 配置为空，还没初始化
        if (sysConfig == null) {
            return true;
        } else {
            String configValue = sysConfig.getConfigValue();
            if (StrUtil.isEmpty(configValue)) {
                return true;
            } else {
                return Convert.toBool(sysConfig.getConfigValue());
            }
        }
    }

    @Override
    public InitConfigResponse getInitConfigs() {

        // 如果当前已经初始化过配置，则不能初始化
        Boolean alreadyInit = this.getInitConfigFlag();
        if (alreadyInit) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_INIT_ALREADY);
        }

        InitConfigResponse initConfigResponse = new InitConfigResponse();
        initConfigResponse.setTitle("首次运行参数生成");
        initConfigResponse.setDescription("第一次进入Guns系统会配置系统的一些秘钥和部署的url信息，这些秘钥均为随机生成，以确保系统的安全性");

        // 获取所有参数分组下的配置信息
        List<InitConfigGroup> configGroupList = new ArrayList<>();
        Map<String, ConfigInitStrategyApi> beans = SpringUtil.getBeansOfType(ConfigInitStrategyApi.class);
        for (ConfigInitStrategyApi value : beans.values()) {
            String title = value.getTitle();
            String description = value.getDescription();
            List<ConfigInitItem> initConfigs = value.getInitConfigs();
            configGroupList.add(new InitConfigGroup(title, description, initConfigs));
        }
        initConfigResponse.setInitConfigGroupList(configGroupList);

        return initConfigResponse;
    }

    @Override
    public void updateSysConfigTypeCode(String originTypeCode, String destTypeCode) {
        LambdaUpdateWrapper<SysConfig> sysConfigLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysConfigLambdaUpdateWrapper.eq(SysConfig::getGroupCode, originTypeCode);
        sysConfigLambdaUpdateWrapper.set(SysConfig::getGroupCode, destTypeCode);
        this.update(sysConfigLambdaUpdateWrapper);
    }

    @Override
    public String getConfigValueByCode(String configCode) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigCode, configCode);
        wrapper.select(SysConfig::getConfigId, SysConfig::getConfigValue);
        SysConfig sysConfig = this.getOne(wrapper, false);
        if (sysConfig == null) {
            return "";
        } else {
            return sysConfig.getConfigValue();
        }
    }

    @Override
    public String initLocalDefaultStoragePath() {
        // 1. 获取文件存储的配置类型，是否为默认，还是指定位置存储
        String configValueByCode = this.getConfigValueByCode(SYS_FILE_SAVE_TYPE_CONFIG_CODE);
        Integer configValue = Convert.toInt(configValueByCode);

        // 2. 如果不是存储到默认路径，直接跳过本类执行
        if (ObjectUtil.notEqual(configValue, FileStorageTypeEnum.LOCAL_DEFAULT.getCode())) {
            return null;
        }

        // 3. 获取当前jar启动的位置的目录
        String currentJarPath = JarPathUtil.getJarPath();
        if (StrUtil.isBlank(currentJarPath)) {
            return null;
        }

        // 4. 获取当前系统的类型，是windows还是linux
        if (SystemUtil.getOsInfo().isWindows()) {
            this.updateConfigByCode(SYS_LOCAL_FILE_SAVE_PATH_WINDOWS, currentJarPath);
        } else {
            this.updateConfigByCode(SYS_LOCAL_FILE_SAVE_PATH_LINUX, currentJarPath);
        }

        return currentJarPath;
    }

    /**
     * 获取系统参数配置
     *
     * @author fengshuonan
     * @since 2020/4/14 11:19
     */
    public SysConfig querySysConfig(SysConfigParam sysConfigParam) {
        SysConfig sysConfig = this.getById(sysConfigParam.getConfigId());
        if (ObjectUtil.isEmpty(sysConfig) || sysConfig.getDelFlag().equals(YesOrNotEnum.Y.getCode())) {
            throw new ConfigException(ConfigExceptionEnum.CONFIG_NOT_EXIST, "id: " + sysConfigParam.getConfigId());
        }
        return sysConfig;
    }

    /**
     * 创建wrapper
     *
     * @author fengshuonan
     * @since 2020/11/6 10:16
     */
    private LambdaQueryWrapper<SysConfig> createWrapper(SysConfigParam sysConfigParam) {
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();

        // 根据查询名称搜索
        String searchText = sysConfigParam.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.and(wq -> {
                wq.like(SysConfig::getConfigName, searchText).or().like(SysConfig::getConfigCode, searchText).or()
                        .like(SysConfig::getConfigValue, searchText).or().like(SysConfig::getRemark, searchText);
            });
        }

        // 如果分类编码不为空，则带上分类编码
        String groupCode = sysConfigParam.getGroupCode();
        queryWrapper.eq(ObjectUtil.isNotEmpty(groupCode), SysConfig::getGroupCode, groupCode);

        // 通过排序字段排序
        queryWrapper.orderByAsc(SysConfig::getFldSort, BaseEntity::getCreateTime);

        return queryWrapper;
    }

    @Override
    public void batchDelete(SysConfigParam sysConfigParam) {

        for (Long configId : sysConfigParam.getConfigIdList()) {
            // 1.根据id获取常量
            SysConfig sysConfig = this.getById(configId);
            if (ObjectUtil.isEmpty(sysConfig) || sysConfig.getDelFlag().equals(YesOrNotEnum.Y.getCode())) {
                throw new ConfigException(ConfigExceptionEnum.CONFIG_NOT_EXIST, "id: " + sysConfigParam.getConfigId());
            }

            // 2.不能删除系统参数
            if (YesOrNotEnum.Y.getCode().equals(sysConfig.getSysFlag())) {
                throw new ConfigException(ConfigExceptionEnum.CONFIG_SYS_CAN_NOT_DELETE);
            }

            // 3.逻辑删除
            this.removeById(configId);

            // 4.删除对应context
            ConfigContext.me().deleteConfig(sysConfig.getConfigCode());
        }
    }

    @Override
    public void updateConfigByCode(String code, String value) {

        // 更新系统配置的值
        LambdaUpdateWrapper<SysConfig> sysConfigLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysConfigLambdaUpdateWrapper.eq(SysConfig::getConfigCode, code);
        sysConfigLambdaUpdateWrapper.set(SysConfig::getConfigValue, value);
        this.update(sysConfigLambdaUpdateWrapper);

        // 更新缓存中的值
        ConfigContext.me().putConfig(code, value);
    }

}
