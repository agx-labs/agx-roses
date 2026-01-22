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
package cn.stylefeng.roses.kernel.file.api;

import cn.stylefeng.roses.kernel.file.api.pojo.response.SysFileInfoResponse;

import java.util.List;

/**
 * 文件业务信息绑定的API
 *
 * @author fengshuonan
 * @since 2023/3/31 13:38
 */
public interface FileBusinessApi {

    /**
     * 添加业务和文件的绑定
     *
     * @param businessCode 业务编码
     * @param businessId   业务id
     * @param fileIdList   文件id集合
     * @author fengshuonan
     * @since 2023/3/31 13:43
     */
    void addFileBusinessBind(String businessCode, Long businessId, List<Long> fileIdList);

    /**
     * 获取业务绑定的文件列表信息
     *
     * @param businessId 业务id
     * @return 文件的信息，不包含文件本身的字节信息
     * @author fengshuonan
     * @since 2020/11/29 16:26
     */
    List<SysFileInfoResponse> getBusinessFileInfoList(Long businessId);

    /**
     * 新增文件下载次数
     *
     * @author fengshuonan
     * @since 2023/3/31 14:23
     */
    void addFileDownloadCount(Long businessId, Long fileId);

    /**
     * 删除业务id的文件关联信息
     *
     * @param businessCode 业务编码
     * @param businessId   业务id
     * @author fengshuonan
     * @since 2023/3/31 14:23
     */
    void removeBusinessIdFileList(String businessCode, Long businessId);

}
