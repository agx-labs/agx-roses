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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.stylefeng.roses.kernel.file.api.pojo.response.SysFileInfoResponse;
import cn.stylefeng.roses.kernel.file.modular.pojo.request.SysFileBusinessRequest;
import cn.stylefeng.roses.kernel.file.modular.pojo.vo.SysFileInfoRes;
import cn.stylefeng.roses.kernel.file.modular.service.SysFileBusinessService;
import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
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
 * 文件和业务绑定的接口
 *
 * @author fengshuonan
 * @since 2023/3/31 15:31
 */
@RestController
@ApiResource(name = "文件和业务绑定的接口", resBizType = ResBizTypeEnum.SYSTEM)
public class SysFileBusinessController {

    @Resource
    private SysFileBusinessService sysFileBusinessService;

    /**
     * 增加文件下载次数
     *
     * @author fengshuonan
     * @since 2023/3/31 15:31
     */
    @PostResource(name = "增加文件下载次数", path = "/sysFileInfo/addFileDownloadCount", requiredLogin = false)
    public ResponseData<?> upload(
            @RequestBody @Validated(SysFileBusinessRequest.addFileDownloadCount.class) SysFileBusinessRequest sysFileBusinessRequest) {
        sysFileBusinessService.addFileDownloadCount(sysFileBusinessRequest.getBusinessId(), sysFileBusinessRequest.getFileId());
        return new SuccessResponseData<>();
    }

    /**
     * 获取业务关联的文件信息列表
     *
     * @author fengshuonan
     * @since 2023/4/5 16:00
     */
    @GetResource(name = "获取业务关联的文件信息列表", path = "/sysFileInfo/getBusinessFileList")
    public ResponseData<List<SysFileInfoRes>> getBusinessFileList(
            @Validated(SysFileBusinessRequest.getBusinessFileList.class) SysFileBusinessRequest sysFileBusinessRequest) {
        List<SysFileInfoResponse> list = sysFileBusinessService.getBusinessFileInfoList(sysFileBusinessRequest.getBusinessId());
        List<SysFileInfoRes> sysFileInfoRes = BeanUtil.copyToList(list, SysFileInfoRes.class);
        return new SuccessResponseData<>(sysFileInfoRes);
    }

    /**
     * 新增绑定业务和文件
     *
     * @author fengshuonan
     * @since 2023/4/4 20:56
     */
    @PostResource(name = "新增绑定业务和文件", path = "/sysFileInfo/bindFile")
    public ResponseData<?> bindFile(
            @RequestBody @Validated(SysFileBusinessRequest.bindFile.class) SysFileBusinessRequest sysFileBusinessRequest) {
        sysFileBusinessService.addFileBusinessBind(sysFileBusinessRequest.getBusinessCode(), sysFileBusinessRequest.getBusinessId(),
                ListUtil.list(false, sysFileBusinessRequest.getFileId()));
        return new SuccessResponseData<>();
    }

}
