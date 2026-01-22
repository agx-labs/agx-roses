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
package cn.stylefeng.roses.kernel.sys.modular.resource.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.modular.resource.entity.SysResource;
import cn.stylefeng.roses.kernel.sys.modular.resource.pojo.ResourceRequest;
import cn.stylefeng.roses.kernel.sys.modular.resource.service.SysResourceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源管理控制器
 *
 * @author fengshuonan
 * @since 2020/11/24 19:47
 */
@RestController
@ApiResource(name = "资源管理", requiredPermission = true, requirePermissionCode = PermissionCodeConstants.AUTH_RESOURCE)
public class ResourceController {

    @Resource
    private SysResourceService sysResourceService;

    /**
     * 获取资源列表
     *
     * @author fengshuonan
     * @since 2020/11/24 19:47
     */
    @GetResource(name = "获取资源列表", path = "/resource/pageList")
    public ResponseData<PageResult<SysResource>> pageList(ResourceRequest resourceRequest) {
        PageResult<SysResource> result = this.sysResourceService.findPage(resourceRequest);
        return new SuccessResponseData<>(result);
    }

}
