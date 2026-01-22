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
package cn.stylefeng.roses.kernel.config.modular.controller;

import cn.stylefeng.roses.kernel.config.modular.pojo.param.SysConfigTypeParam;
import cn.stylefeng.roses.kernel.config.modular.service.SysConfigTypeService;
import cn.stylefeng.roses.kernel.dict.api.pojo.DictDetail;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 配置分类的控制器
 * <p>
 * 配置分类其实是对应的字典，字典类型编码是config_group
 *
 * @author fengshuonan
 * @since 2023/6/28 16:46
 */
@RestController
@ApiResource(name = "配置分类的接口", requiredPermission = true, requirePermissionCode = SysConfigTypeController.SYS_CONFIG)
public class SysConfigTypeController {

    /**
     * 系统配置界面的权限标识
     */
    public static final String SYS_CONFIG = "SYS_CONFIG";

    @Resource
    private SysConfigTypeService sysConfigTypeService;

    /**
     * 查看系统配置分类列表
     *
     * @author fengshuonan
     * @since 2023/6/28 16:51
     */
    @GetResource(name = "查看系统配置分类列表", path = "/sysConfigType/list")
    public ResponseData<List<SimpleDict>> sysConfigTypeList(SysConfigTypeParam sysConfigTypeParam) {
        return new SuccessResponseData<>(sysConfigTypeService.getSysConfigTypeList(sysConfigTypeParam));
    }

    /**
     * 新增配置类型
     *
     * @author fengshuonan
     * @since 2023/6/28 17:00
     */
    @PostResource(name = "新增配置类型", path = "/sysConfigType/add")
    public ResponseData<?> add(@RequestBody @Validated(SysConfigTypeParam.add.class) SysConfigTypeParam sysConfigTypeParam) {
        sysConfigTypeService.add(sysConfigTypeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑配置类型
     *
     * @author fengshuonan
     * @since 2023/6/28 17:00
     */
    @PostResource(name = "编辑配置类型", path = "/sysConfigType/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysConfigTypeParam.edit.class) SysConfigTypeParam sysConfigTypeParam) {
        sysConfigTypeService.edit(sysConfigTypeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 删除配置类型
     *
     * @author fengshuonan
     * @since 2023/6/28 17:00
     */
    @PostResource(name = "删除配置类型", path = "/sysConfigType/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysConfigTypeParam.delete.class) SysConfigTypeParam sysConfigTypeParam) {
        sysConfigTypeService.delete(sysConfigTypeParam);
        return new SuccessResponseData<>();
    }

    /**
     * 获取配置类型的详情
     *
     * @author fengshuonan
     * @since 2023/6/28 17:00
     */
    @GetResource(name = "获取配置类型的详情", path = "/sysConfigType/detail")
    public ResponseData<DictDetail> detail(@Validated(SysConfigTypeParam.detail.class) SysConfigTypeParam sysConfigTypeParam) {
        DictDetail detail = sysConfigTypeService.detail(sysConfigTypeParam);
        return new SuccessResponseData<>(detail);
    }

}


