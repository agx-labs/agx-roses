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
package cn.stylefeng.roses.kernel.log.requestapi.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.pojo.manage.LogManagerRequest;
import cn.stylefeng.roses.kernel.log.api.pojo.record.LogRecordDTO;
import cn.stylefeng.roses.kernel.log.requestapi.entity.SysLog;
import cn.stylefeng.roses.kernel.log.requestapi.service.SysLogService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API日志管理
 *
 * @author fengshuonan
 * @since 2023/7/21 11:22
 */
@RestController
@ApiResource(name = "API日志管理控制器", requiredPermission = true, requirePermissionCode = LogManagerController.OPERATE_LOG)
public class LogManagerController {

    /**
     * 操作日志界面的权限标识
     */
    public static final String OPERATE_LOG = "OPERATE_LOG";

    @Resource
    private SysLogService sysLogService;

    /**
     * 分页查询API日志列表
     *
     * @author fengshuonan
     * @since 2023/7/21 11:34
     */
    @GetResource(name = "分页查询API日志列表", path = "/logManager/page")
    public ResponseData<PageResult<LogRecordDTO>> page(LogManagerRequest logManagerRequest) {
        return new SuccessResponseData<>(sysLogService.apiLogPageQuery(logManagerRequest));
    }

    /**
     * 删除日志
     *
     * @author luojie
     * @since 2020/11/3 13:47
     */
    @PostResource(name = "删除日志", path = "/logManager/delete")
    public ResponseData<?> delete(@RequestBody @Validated(LogManagerRequest.delete.class) LogManagerRequest logManagerRequest) {
        sysLogService.delAll(logManagerRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看日志详情
     *
     * @author TSQ
     * @since 2021/1/11 17:36
     */
    @GetResource(name = "查看日志详情", path = "/logManager/detail")
    public ResponseData<SysLog> detail(@Validated(LogManagerRequest.detail.class) LogManagerRequest logManagerRequest) {
        return new SuccessResponseData<>(sysLogService.detail(logManagerRequest));
    }

}
