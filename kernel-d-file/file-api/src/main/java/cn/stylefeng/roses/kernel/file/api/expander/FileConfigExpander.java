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
package cn.stylefeng.roses.kernel.file.api.expander;

import cn.stylefeng.roses.kernel.config.api.context.ConfigContext;
import cn.stylefeng.roses.kernel.file.api.constants.FileConstants;
import cn.stylefeng.roses.kernel.file.api.pojo.props.LocalFileProperties;
import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;

/**
 * 文件相关的配置获取
 *
 * @author fengshuonan
 * @since 2020/11/29 14:47
 */
public class FileConfigExpander {

    /**
     * 默认存储的bucket名称
     *
     * @author fengshuonan
     * @since 2021/6/15 21:54
     */
    public static String getDefaultBucket() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_FILE_DEFAULT_BUCKET", String.class, FileConstants.DEFAULT_BUCKET_NAME);
    }

    /**
     * 获取服务部署的机器host，例如http://localhost
     * <p>
     * 这个配置为了用在文件url的拼接上
     *
     * @author fengshuonan
     * @since 2020/11/29 16:13
     */
    public static String getServerDeployHost() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_SERVER_DEPLOY_HOST", String.class, RuleConstants.DEFAULT_SERVER_DEPLOY_HOST);
    }

    /**
     * 获取文件生成auth url的失效时间
     *
     * @author fengshuonan
     * @since 2020/11/29 16:13
     */
    public static Long getDefaultFileTimeoutSeconds() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_DEFAULT_FILE_TIMEOUT_SECONDS", Long.class, FileConstants.DEFAULT_FILE_TIMEOUT_SECONDS);
    }

    /**
     * 本地文件存储位置（linux）
     *
     * @author fengshuonan
     * @since 2020/12/1 14:44
     */
    public static String getLocalFileSavePathLinux() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_LOCAL_FILE_SAVE_PATH_LINUX", String.class, new LocalFileProperties().getLocalFileSavePathLinux());
    }

    /**
     * 本地文件存储位置（windows）
     *
     * @author fengshuonan
     * @since 2020/12/1 14:44
     */
    public static String getLocalFileSavePathWindows() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_LOCAL_FILE_SAVE_PATH_WINDOWS", String.class, new LocalFileProperties().getLocalFileSavePathWin());
    }

}
