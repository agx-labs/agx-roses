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
package cn.stylefeng.roses.kernel.security.blackwhite;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.security.blackwhite.exception.BlackWhiteExceptionEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 根据ip和黑白名单，校验当前ip是否能通过
 *
 * @author fengshuonan
 * @since 2024/7/11 10:14
 */
@Service
public class BlackWhiteValidateService {

    @Resource
    private BlackListService blackListService;

    @Resource
    private WhiteListService whiteListService;

    /**
     * 黑白名单统一校验，如果校验不通过直接抛出异常
     *
     * @author fengshuonan
     * @since 2024/7/11 10:44
     */
    public void totalValidate(String ip) {

        // 校验黑名单是否通过
        if (!validateBlackList(ip)) {
            throw new ServiceException(BlackWhiteExceptionEnum.IP_INVALID);
        }

        // 校验白名单是否通过
        if (!validateWhiteList(ip)) {
            throw new ServiceException(BlackWhiteExceptionEnum.IP_INVALID);
        }

    }

    /**
     * 校验指定IP是否通过黑名单校验
     *
     * @return true-通过（指定IP没被限制），false-不通过（指定ip存在黑名单的范围中）
     * @author fengshuonan
     * @since 2024/7/11 10:29
     */
    public boolean validateBlackList(String ip) {
        if (ObjectUtil.isEmpty(blackListService.getBlackList())) {
            return true;
        }

        // 如果包含了指定ip，则校验不通过
        return !blackListService.contains(ip);
    }

    /**
     * 校验指定IP是否通过白名单校验
     *
     * @return true-通过（指定IP在白名单中），false-不通过（指定ip不在白名单中）
     * @author fengshuonan
     * @since 2024/7/11 10:37
     */
    public boolean validateWhiteList(String ip) {
        if (ObjectUtil.isEmpty(whiteListService.getWhiteList())) {
            return true;
        }

        // 如果不为空，则白名单里包含了本IP，才可以放行
        return whiteListService.contains(ip);
    }

}
