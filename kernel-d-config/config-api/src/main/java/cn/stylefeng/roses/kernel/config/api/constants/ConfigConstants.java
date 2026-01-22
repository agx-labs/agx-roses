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
package cn.stylefeng.roses.kernel.config.api.constants;

/**
 * 系统配置表的常量
 *
 * @author fengshuonan
 * @since 2020/10/16 11:05
 */
public interface ConfigConstants {

    /**
     * config模块的名称
     */
    String CONFIG_MODULE_NAME = "kernel-d-config";

    /**
     * 异常枚举的步进值
     */
    String CONFIG_EXCEPTION_STEP_CODE = "04";

    /**
     * 后端部署的地址，在sys_config中的编码
     */
    String SYS_SERVER_DEPLOY_HOST = "SYS_SERVER_DEPLOY_HOST";

    /**
     * 系统配置分组code，字典类型编码
     */
    String CONFIG_GROUP_DICT_TYPE_CODE = "config_group";

    /**
     * 文件存储类型的配置
     * <p>
     * 10-本地，存储到默认路径（jar所在目录）
     * 11-本地，存储到指定路径下（需要配置linux和windows的路径）
     * 20-存储到MinIO
     * 30-存储到阿里云
     * 40-存储到腾讯云
     * 50-存储到青云
     */
    String SYS_FILE_SAVE_TYPE_CONFIG_CODE = "SYS_FILE_SAVE_TYPE";

    /**
     * 配置编码，LINUX本地存储的目录
     */
    String SYS_LOCAL_FILE_SAVE_PATH_LINUX = "SYS_LOCAL_FILE_SAVE_PATH_LINUX";

    /**
     * 配置编码，windows本地文件存储的目录
     */
    String SYS_LOCAL_FILE_SAVE_PATH_WINDOWS = "SYS_LOCAL_FILE_SAVE_PATH_WINDOWS";

}
