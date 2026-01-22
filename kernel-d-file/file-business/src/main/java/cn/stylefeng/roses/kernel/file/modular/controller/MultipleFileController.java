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
package cn.stylefeng.roses.kernel.file.modular.controller;

import cn.stylefeng.roses.kernel.file.api.pojo.request.SysFileInfoRequest;
import cn.stylefeng.roses.kernel.file.api.pojo.response.SysFileInfoResponse;
import cn.stylefeng.roses.kernel.file.modular.service.MultipleFileService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 多文件上传和下载的接口
 *
 * @author fengshuonan
 * @since 2024/8/20 9:00
 */
@RestController
@ApiResource(name = "多文件上传和下载的接口")
public class MultipleFileController {

    @Resource
    private MultipleFileService multipleFileService;

    /**
     * 多文件上传，并同时携带json的body体
     *
     * @author fengshuonan
     * @since 2024/8/20 9:06
     */
    @PostResource(name = "多文件上传-带body体", path = "/sysFileInfo/multipleUploadFileList")
    public ResponseData<List<SysFileInfoResponse>> upload(@RequestPart("fileList") MultipartFile[] fileList,
                                                          @RequestPart("bodyJson") SysFileInfoRequest sysFileInfoRequest) {
        List<SysFileInfoResponse> fileResult = this.multipleFileService.batchUploadFile(fileList, sysFileInfoRequest);
        return new SuccessResponseData<>(fileResult);
    }

}
