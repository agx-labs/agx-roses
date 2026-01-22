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
package cn.stylefeng.roses.kernel.config.modular.service;

import cn.stylefeng.roses.kernel.config.api.ConfigServiceApi;
import cn.stylefeng.roses.kernel.config.api.InitConfigApi;
import cn.stylefeng.roses.kernel.config.api.pojo.ConfigInitRequest;
import cn.stylefeng.roses.kernel.config.modular.entity.SysConfig;
import cn.stylefeng.roses.kernel.config.modular.pojo.InitConfigResponse;
import cn.stylefeng.roses.kernel.config.modular.pojo.param.SysConfigParam;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统参数配置service接口
 *
 * @author fengshuonan
 * @since 2020/4/14 11:14
 */
public interface SysConfigService extends IService<SysConfig>, InitConfigApi, ConfigServiceApi {

    /**
     * 添加系统参数配置
     *
     * @param sysConfigParam 添加参数
     * @author fengshuonan
     * @since 2020/4/14 11:14
     */
    void add(SysConfigParam sysConfigParam);

    /**
     * 删除系统参数配置
     *
     * @param sysConfigParam 删除参数
     * @author fengshuonan
     * @since 2020/4/14 11:15
     */
    void del(SysConfigParam sysConfigParam);

    /**
     * 批量删除系统参数配置
     *
     * @author liyanjun
     * @date 2023/07/03 21:29
     */
    void batchDelete(SysConfigParam sysConfigParam);

    /**
     * 删除系统参数配置
     *
     * @param configGroupCode 字典分类编码
     * @author fengshuonan
     * @since 2023/6/28 18:21
     */
    void delByConfigCode(String configGroupCode);

    /**
     * 编辑系统参数配置
     *
     * @param sysConfigParam 编辑参数
     * @author fengshuonan
     * @since 2020/4/14 11:15
     */
    void edit(SysConfigParam sysConfigParam);

    /**
     * 查看系统参数配置
     *
     * @param sysConfigParam 查看参数
     * @return 系统参数配置
     * @author fengshuonan
     * @since 2020/4/14 11:15
     */
    SysConfig detail(SysConfigParam sysConfigParam);

    /**
     * 查询系统参数配置
     *
     * @param sysConfigParam 查询参数
     * @return 查询分页结果
     * @author fengshuonan
     * @since 2020/4/14 11:14
     */
    PageResult<SysConfig> findPage(SysConfigParam sysConfigParam);

    /**
     * 初始化配置参数
     *
     * @author fengshuonan
     * @since 2021/7/8 16:48
     */
    void initConfig(ConfigInitRequest configInitRequest);

    /**
     * 获取初始化的配置列表
     *
     * @author fengshuonan
     * @since 2021/7/8 17:49
     */
    InitConfigResponse getInitConfigs();

    /**
     * 更新所有config所属的字典类型编码
     *
     * @param originTypeCode 原有的typeCode
     * @param destTypeCode   更新后的typeCode
     * @author fengshuonan
     * @since 2023/6/28 18:02
     */
    void updateSysConfigTypeCode(String originTypeCode, String destTypeCode);

    /**
     * 根据配置编码获取配置值
     *
     * @author fengshuonan
     * @since 2024/8/29 21:35
     */
    String getConfigValueByCode(String configCode);

    /**
     * 初始化文件存储配置，针对本地存储的方式
     *
     * @return 初始化后的本地文件存储路径
     * @author fengshuonan
     * @since 2024/8/29 22:16
     */
    String initLocalDefaultStoragePath();

}
