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
package cn.stylefeng.roses.kernel.db.api.maxsort.listener;

import cn.stylefeng.roses.kernel.db.api.maxsort.MaxCountConfig;
import cn.stylefeng.roses.kernel.db.api.maxsort.MaxSortCollectorApi;
import cn.stylefeng.roses.kernel.db.api.maxsort.context.MaxSortConfigContext;
import cn.stylefeng.roses.kernel.rule.listener.ApplicationStartedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.core.Ordered;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目启动后，获取所有表的最大排序数的配置
 *
 * @author fengshuonan
 * @since 2023/10/29 17:17
 */
@Slf4j
public class TableMaxSortConfigListener extends ApplicationStartedListener implements Ordered {

    @Override
    public void eventCallback(ApplicationStartedEvent event) {

        // 获取所有的配置信息
        Map<String, MaxSortCollectorApi> maxSortCollectorApiMap = null;
        try {
            maxSortCollectorApiMap = event.getApplicationContext().getBeansOfType(MaxSortCollectorApi.class);
        } catch (BeansException e) {
            // ignore
            return;
        }

        // 添加到配置容器中
        Collection<MaxSortCollectorApi> values = maxSortCollectorApiMap.values();
        Map<String, MaxCountConfig> totalConfigs = new HashMap<>();
        for (MaxSortCollectorApi value : values) {
            Map<String, MaxCountConfig> maxSortConfigs = value.createMaxSortConfigs();
            totalConfigs.putAll(maxSortConfigs);
        }
        MaxSortConfigContext.initConfig(totalConfigs);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
