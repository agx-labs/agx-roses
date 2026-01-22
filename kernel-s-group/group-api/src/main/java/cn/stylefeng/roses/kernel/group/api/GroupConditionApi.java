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
package cn.stylefeng.roses.kernel.group.api;

import cn.stylefeng.roses.kernel.group.api.callback.GroupNameCallbackApi;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;

import java.util.List;

/**
 * 用在查询条件拼接语句时候的分组Api
 *
 * @author fengshuonan
 * @since 2023/5/30 16:47
 */
public interface GroupConditionApi {

    /**
     * 获取请求是不是请求的未分组
     * <p>
     * 判断baseRequest中请求的conditionGroupName参数是否是“未分组”
     * <p>
     * 如果是未分组，则查询条件中，应当拼接类似如下语句：
     * <p>
     * queryWrapper.nested(ObjectUtil.isNotEmpty(userBizIds), i -> i.notIn(DevopsApp::getAppId, userBizIds));
     *
     * @author fengshuonan
     * @since 2023/5/30 16:49
     */
    Boolean getRequestUnGroupedFlag(BaseRequest baseRequest);

    /**
     * 获取用户对应的业务分组下的业务id集合
     *
     * @param groupBizCode 业务分组编码，应该是个常量，每个分组都应该具有的
     * @author fengshuonan
     * @since 2023/5/30 16:49
     */
    List<Long> getUserGroupBizIds(String groupBizCode, BaseRequest baseRequest);

    /**
     * 将业务的数据集合渲染分组名称
     *
     * @param businessList 业务的数据集合
     * @author fengshuonan
     * @since 2023/5/30 17:15
     */
    void renderBizListGroupName(String groupBizCode, List<? extends GroupNameCallbackApi> businessList);

}
