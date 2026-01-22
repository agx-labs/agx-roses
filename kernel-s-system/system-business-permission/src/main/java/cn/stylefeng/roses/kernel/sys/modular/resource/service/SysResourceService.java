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
package cn.stylefeng.roses.kernel.sys.modular.resource.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.api.ResourceServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.resource.entity.SysResource;
import cn.stylefeng.roses.kernel.sys.modular.resource.pojo.ResourceRequest;
import cn.stylefeng.roses.kernel.sys.modular.resource.pojo.ResourceTreeNode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 资源服务类
 *
 * @author fengshuonan
 * @since 2020/11/24 19:56
 */
public interface SysResourceService extends IService<SysResource>, ResourceServiceApi {

    /**
     * 获取资源分页列表
     *
     * @param resourceRequest 请求参数
     * @return 返回结果
     * @author fengshuonan
     * @since 2020/11/24 20:45
     */
    PageResult<SysResource> findPage(ResourceRequest resourceRequest);

    /**
     * 删除某个项目的所有资源
     *
     * @param projectCode 项目编码，一般为spring application name
     * @author fengshuonan
     * @since 2020/11/24 20:46
     */
    void deleteResourceByProjectCode(String projectCode);

    /**
     * 批量保存系统资源
     *
     * @author fengshuonan
     * @since 2023/6/18 10:37
     */
    void batchSaveResourceList(List<SysResource> sysResourceList);

    /**
     * 获取资源绑定列表（业务通用）
     * <p>
     * 一般用在api认证模块，勾选客户端的api接口绑定
     *
     * @param resourceCodes   业务已经绑定的资源的编码集合
     * @param treeBuildFlag   是否要构建成树
     * @param resourceBizType 资源的类型，1-业务类，2-系统类
     * @author fengshuonan
     * @since 2021/8/8 22:24
     */
    List<ResourceTreeNode> getResourceList(List<String> resourceCodes, Boolean treeBuildFlag, Integer resourceBizType);


}
