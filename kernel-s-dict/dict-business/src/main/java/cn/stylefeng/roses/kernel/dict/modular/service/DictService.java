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
package cn.stylefeng.roses.kernel.dict.modular.service;

import cn.stylefeng.roses.kernel.dict.api.DictApi;
import cn.stylefeng.roses.kernel.dict.modular.entity.SysDict;
import cn.stylefeng.roses.kernel.dict.modular.pojo.TreeDictInfo;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.DictRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 字典详情管理
 *
 * @author fengshuonan
 * @since 2020/10/29 17:43
 */
public interface DictService extends IService<SysDict>, DictApi {

    /**
     * 获取树形字典列表
     *
     * @author fengshuonan
     * @since 2023/6/27 16:58
     */
    List<TreeDictInfo> getTreeDictList(DictRequest dictRequest);

    /**
     * 新增字典
     *
     * @param dictRequest 字典对象
     * @author fengshuonan
     * @since 2020/10/29 17:43
     */
    void add(DictRequest dictRequest);

    /**
     * 删除字典
     *
     * @param dictRequest 字典对象
     * @author fengshuonan
     * @since 2020/10/29 17:43
     */
    void del(DictRequest dictRequest);

    /**
     * 批量删除字典
     *
     * @author liyanjun
     * @date 2023/07/04 10:29
     */
    void batchDelete(DictRequest dictRequest);

    /**
     * 修改字典
     *
     * @param dictRequest 字典对象
     * @author fengshuonan
     * @since 2020/10/29 17:43
     */
    void edit(DictRequest dictRequest);

    /**
     * 查询字典详情
     *
     * @param dictRequest 字典id
     * @return 字典的详情
     * @author fengshuonan
     * @since 2020/10/30 16:15
     */
    SysDict detail(DictRequest dictRequest);

    /**
     * 获取平铺（展开）结构的字典列表
     * <p>
     * 本接口可能会传字典类型id，或者字典类型编码，或者searchText
     *
     * @author fengshuonan
     * @since 2023/6/27 17:43
     */
    List<SysDict> findList(DictRequest dictRequest);

    /**
     * 删除字典类型下的所有字典
     *
     * @author fengshuonan
     * @since 2023/6/27 16:24
     */
    void removeByDictTypeId(Long dictTypeId);

    /**
     * 更新整个字典树结构，用来更新上下级结构和顺序
     *
     * @author fengshuonan
     * @since 2023/6/27 18:24
     */
    void updateDictTree(DictRequest dictRequest);

    /**
     * 通用获取中文拼音
     *
     * @author liyanjun
     * @since 2023/7/01 10:31
     */
    String getPinyin(String name);

    /**
     * 清空字典id对应的所有缓存
     *
     * @author fengshuonan
     * @since 2025/1/10 18:16
     */
    void deleteDictIdCaches(Long dictId);

    /**
     * 清空字典对应的所有缓存
     *
     * @author fengshuonan
     * @since 2025/1/10 18:17
     */
    void deleteDictIdCaches(SysDict sysDict);

}
