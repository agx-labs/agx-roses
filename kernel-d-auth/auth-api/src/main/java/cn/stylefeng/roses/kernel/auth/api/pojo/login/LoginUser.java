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
package cn.stylefeng.roses.kernel.auth.api.pojo.login;

import cn.hutool.core.lang.Dict;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录用户信息
 *
 * @author fengshuonan
 * @since 2020/10/17 9:58
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键id
     */
    @ChineseDescription("用户主键id")
    private Long userId;

    /**
     * 账号
     */
    @ChineseDescription("账号")
    private String account;

    /**
     * 用户的token
     */
    @ChineseDescription("用户的token")
    private String token;

    /**
     * 当前登录租户id
     */
    @ChineseDescription("当前登录租户id")
    private Long tenantId;

    /**
     * 当前用户正在访问的appId
     */
    @ChineseDescription("当前用户正在访问的appId")
    private Long currentAppId;

    /**
     * 当前用户激活的组织机构id（正在以哪个身份访问系统）
     */
    @ChineseDescription("当前用户激活的组织机构id（正在以哪个身份访问系统）")
    private Long currentOrgId;

    /**
     * 当前用户激活的职务id（正在以哪个身份访问系统）
     */
    @ChineseDescription("当前用户激活的职务id（正在以哪个身份访问系统）")
    private Long currentPositionId;

    /**
     * 当前用户语种的标识，例如：chinese，english
     * <p>
     * 这个值是根据字典获取，字典类型编码 languages
     * <p>
     * 默认语种是中文
     */
    @ChineseDescription("当前用户语种的标识")
    private String tranLanguageCode = RuleConstants.CHINESE_TRAN_LANGUAGE_CODE;

    /**
     * 登录时候的IP
     */
    @ChineseDescription("登录时候的IP")
    private String loginIp;

    /**
     * 登录时间
     */
    @ChineseDescription("登录时间")
    private Date loginTime;

    /**
     * 登录用户的其他信息
     */
    @ChineseDescription("登录用户的其他信息")
    private Dict otherInfos;

    public LoginUser() {
    }

    public LoginUser(Long userId, String account, String token, Long tenantId) {
        this.userId = userId;
        this.account = account;
        this.token = token;
        this.tenantId = tenantId;
    }

}
