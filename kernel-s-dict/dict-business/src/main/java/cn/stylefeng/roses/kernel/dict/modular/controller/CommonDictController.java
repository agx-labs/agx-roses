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
package cn.stylefeng.roses.kernel.dict.modular.controller;

import cn.stylefeng.roses.kernel.dict.api.pojo.DictTreeDto;
import cn.stylefeng.roses.kernel.dict.modular.service.DictService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典通用接口
 *
 * @author fengshuonan
 * @since 2023/7/1 21:43
 */
@RestController
@ApiResource(name = "字典通用接口")
public class CommonDictController {

    @Resource
    private DictService dictService;

    /**
     * 通用获取中文拼音
     *
     * @author liyanjun
     * @since 2023/7/01 10:31
     */
    @GetResource(name = "通用获取中文拼音", path = "/common/getPinyin")
    public ResponseData<String> getPinyin(String name) {
        return new SuccessResponseData<>(this.dictService.getPinyin(name));
    }

    /**
     * 获取所有的字典类型树
     *
     * @author fengshuonan
     * @since 2023/11/15 19:08
     */
    @GetResource(name = "获取所有的字典类型树", path = "/dictType/dictTreeBuild")
    public ResponseData<List<DictTreeDto>> dictTreeBuild() {
        return new SuccessResponseData<>(dictService.buildDictTreeStructure());
    }

}
