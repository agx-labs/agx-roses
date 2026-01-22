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

import cn.stylefeng.roses.kernel.file.api.pojo.AntdvFileInfo;
import cn.stylefeng.roses.kernel.file.api.pojo.request.SysFileInfoRequest;
import cn.stylefeng.roses.kernel.file.api.pojo.response.SysFileInfoResponse;

import java.io.File;
import java.util.List;

/**
 * 获取文件信息的api
 *
 * @author fengshuonan
 * @since 2020/11/29 16:21
 */
public interface FileInfoApi {

    /**
     * 获取文件详情
     *
     * @param fileId 文件id，在文件信息表的id
     * @return 文件的信息，不包含文件本身的字节信息
     * @author fengshuonan
     * @since 2020/11/29 16:26
     */
    SysFileInfoResponse getFileInfoWithoutContent(Long fileId);

    /**
     * 获取文件的下载地址（带鉴权的），生成外网地址
     *
     * @param fileId 文件id
     * @return 外部系统可以直接访问的url
     * @author fengshuonan
     * @since 2020/10/26 10:40
     */
    String getFileAuthUrl(Long fileId);

    /**
     * 获取文件的下载地址（带鉴权的），生成外网地址
     *
     * @param fileId 文件id
     * @param token  用户的token
     * @return 外部系统可以直接访问的url
     * @author fengshuonan
     * @since 2020/10/26 10:40
     */
    String getFileAuthUrl(Long fileId, String token);

    /**
     * 获取文件的下载地址（不带鉴权的），生成外网地址
     *
     * @param fileId 文件id
     * @return 外部系统可以直接访问的url
     * @author fengshuonan
     * @since 2020/10/26 10:40
     */
    String getFileUnAuthUrl(Long fileId);

    /**
     * 获取文件的下载地址（不带鉴权的），生成外网地址
     *
     * @param fileIdList 文件id集合
     * @return 外部系统可以直接访问的url
     * @author fengshuonan
     * @since 2024/3/26 22:38
     */
    List<String> batchGetFileUnAuthUrl(List<Long> fileIdList);

    /**
     * 获取AntdV组件格式对应的文件信息封装
     *
     * @author fengshuonan
     * @since 2022/3/28 14:32
     */
    AntdvFileInfo buildAntdvFileInfo(Long fileId);

    /**
     * 通过文件id集合，批量获取文件信息详情
     *
     * @author fengshuonan
     * @since 2023/11/2 9:48
     */
    List<AntdvFileInfo> buildAntdvFileInfoBatch(List<Long> fileIdList);

    /**
     * 真实删除文件
     *
     * @author fengshuonan
     * @since 2022/7/22 23:19
     */
    void removeFile(Long fileId);

    /**
     * 上传到服务器文件，并保存文件信息到库中
     *
     * @param file               原始
     * @param sysFileInfoRequest 文件附属信息（需要2个参数：是否是机密文件、bucket信息）
     * @return 返回文件id等信息
     * @author fengshuonan
     * @since 2022/10/19 18:24
     */
    SysFileInfoResponse uploadFileAndSave(File file, SysFileInfoRequest sysFileInfoRequest);

    /**
     * 删除文件信息（真删除文件信息）
     *
     * @param sysFileInfoRequest 删除参数
     * @author fengshuonan
     * @since 2020/11/29 13:44
     */
    void deleteReally(SysFileInfoRequest sysFileInfoRequest);

}
