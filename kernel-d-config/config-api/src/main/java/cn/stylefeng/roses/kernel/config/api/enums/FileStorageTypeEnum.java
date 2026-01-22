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
package cn.stylefeng.roses.kernel.config.api.enums;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 文件存储配置类型
 * <p>
 * 10-本地，存储到默认路径（jar所在目录）
 * 11-本地，存储到指定路径下（需要配置linux和windows的路径）
 * 20-存储到MinIO
 * 30-存储到阿里云
 * 40-存储到腾讯云
 * 50-存储到青云
 *
 * @author fengshuonan
 * @since 2024/8/29 21:44
 */
@Getter
public enum FileStorageTypeEnum implements ReadableEnum<FileStorageTypeEnum> {

    /**
     * 本地，存储到默认路径（jar所在目录）
     */
    LOCAL_DEFAULT(10, "本地，存储到默认路径（jar所在目录）"),

    /**
     * 本地，存储到指定路径下（需要配置linux和windows的路径）
     */
    LOCAL(11, "本地，存储到指定路径下（需要配置linux和windows的路径）"),

    /**
     * 存储到MinIO
     */
    MINIO(20, "存储到MinIO"),

    /**
     * 存储到阿里云
     */
    ALIYUN(30, "存储到阿里云"),

    /**
     * 存储到腾讯云
     */
    TEN_CLOUD(40, "存储到腾讯云"),

    /**
     * 青云
     */
    QING_CLOUD(50, "青云");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String message;

    FileStorageTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取枚举
     *
     * @author fengshuonan
     * @since 2020/10/29 18:59
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FileStorageTypeEnum codeToEnum(Integer code) {
        if (null != code) {
            for (FileStorageTypeEnum item : FileStorageTypeEnum.values()) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public Object getKey() {
        return this.code;
    }

    @Override
    public Object getName() {
        return this.message;
    }

    @Override
    public FileStorageTypeEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (FileStorageTypeEnum value : FileStorageTypeEnum.values()) {
            if (value.code.equals(Convert.toInt(originValue))) {
                return value;
            }
        }
        return null;
    }
}
