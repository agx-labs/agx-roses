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
package cn.stylefeng.roses.kernel.sys.modular.login.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.auth.api.PermissionServiceApi;
import cn.stylefeng.roses.kernel.auth.api.SessionManagerApi;
import cn.stylefeng.roses.kernel.auth.api.exception.AuthException;
import cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.ResourceDefinition;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.ResourceUrlParam;
import cn.stylefeng.roses.kernel.sys.api.ResourceServiceApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum.AUTH_EXPIRED_ERROR;
import static cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum.PERMISSION_RES_VALIDATE_ERROR;

/**
 * 权限相关的service
 *
 * @author fengshuonan
 * @since 2020/10/22 15:49
 */
@Service
public class PermissionCheckServiceImpl implements PermissionServiceApi {

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private ResourceServiceApi resourceServiceApi;

    @Resource
    private UserPermissionService userPermissionService;

    @Override
    public void checkPermission(String token, String requestUrl) {

        // 1. 校验token是否传参
        if (StrUtil.isEmpty(token)) {
            throw new AuthException(AuthExceptionEnum.TOKEN_GET_ERROR);
        }

        // 2. 获取token对应的用户信息
        LoginUser loginUser = sessionManagerApi.getSession(token);
        if (loginUser == null) {
            throw new AuthException(AUTH_EXPIRED_ERROR);
        }

        // 3. 获取url对应的资源信息
        ResourceDefinition resourceDefinition = resourceServiceApi.getResourceByUrl(new ResourceUrlParam(requestUrl));

        // 4. 如果资源找不到，则直接返回错误
        if (resourceDefinition == null) {
            throw new AuthException(AuthExceptionEnum.CANT_REQUEST_UN_OPEN_API, requestUrl);
        }

        // 5. 如果当前接口资源不需要权限校验，则直接返回成功
        if (!resourceDefinition.getRequiredPermissionFlag()) {
            return;
        }

        // 获取当前资源需要的权限编码
        String permissionCode = resourceDefinition.getPermissionCode();
        if (ObjectUtil.isEmpty(permissionCode)) {
            return;
        }

        // 判断当前用户是否有该权限编码，如果有该权限编码，则返回成功
        List<String> userPermissionCodeList = userPermissionService.getUserPermissionCodeList(loginUser);
        if (ObjectUtil.isNotEmpty(userPermissionCodeList) && userPermissionCodeList.contains(permissionCode)) {
            return;
        } else {
            throw new AuthException(PERMISSION_RES_VALIDATE_ERROR);
        }
    }

}
