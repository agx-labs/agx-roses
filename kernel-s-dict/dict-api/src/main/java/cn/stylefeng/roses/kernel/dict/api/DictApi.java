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
package cn.stylefeng.roses.kernel.dict.api;

import cn.stylefeng.roses.kernel.dict.api.pojo.DictDetail;
import cn.stylefeng.roses.kernel.dict.api.pojo.DictTreeDto;
import cn.stylefeng.roses.kernel.dict.api.pojo.SimpleDictUpdateParam;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;

import java.util.List;

/**
 * 字典模块对外提供的api，方便其他模块直接调用
 *
 * @author fengshuonan
 * @since 2020/10/29 14:45
 */
public interface DictApi {

    /**
     * 通过字典类型编码和字典编码获取名称
     *
     * @param dictTypeId 字典类型id
     * @param dictCode   字典编码
     * @return 字典名称
     * @author fengshuonan
     * @since 2024/5/9 0:12
     */
    String getDictName(Long dictTypeId, String dictCode);

    /**
     * 通过字典类型编码和字典编码获取名称
     *
     * @param dictTypeCode 字典类型编码
     * @param dictCode     字典编码
     * @return 字典名称
     * @author liuhanqing
     * @since 2021/1/16 23:18
     */
    String getDictName(String dictTypeCode, String dictCode);

    /**
     * 根据字典类型编码获取所有的字典
     *
     * @param dictTypeCode 字典类型编码
     * @param searchText   查询条件，筛选字典类型下指定字符串的字典，可为空
     * @return 字典的集合
     * @author fengshuonan
     * @since 2021/1/27 22:13
     */
    List<SimpleDict> getDictDetailsByDictTypeCode(String dictTypeCode, String searchText);

    /**
     * 删除字典，通过dictId
     *
     * @param dictId 字典id
     * @author fengshuonan
     * @since 2021/1/30 10:03
     */
    void deleteByDictId(Long dictId);

    /**
     * 通过字典id获取字典的名称和编码信息
     *
     * @author fengshuonan
     * @since 2023/5/4 21:25
     */
    DictDetail getDictByDictId(Long dictId);

    /**
     * 通过字典id获取字典的名称
     *
     * @author fengshuonan
     * @since 2023/5/4 21:25
     */
    String getDictNameByDictId(Long dictId);

    /**
     * 外部系统调用本模块进行新增字典
     *
     * @author fengshuonan
     * @since 2023/6/28 17:26
     */
    void simpleAddDict(SimpleDictUpdateParam simpleDictUpdateParam);

    /**
     * 外部系统调用本模块进行更新字典
     *
     * @author fengshuonan
     * @since 2023/6/28 17:26
     */
    void simpleEditDict(SimpleDictUpdateParam simpleDictUpdateParam);

    /**
     * 构建一个完整的字典树
     * <p>
     * 包含字典的类型和字典底下的详情信息
     *
     * @author fengshuonan
     * @since 2023/11/15 18:55
     */
    List<DictTreeDto> buildDictTreeStructure();

}
