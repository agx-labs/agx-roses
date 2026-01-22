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

import cn.stylefeng.roses.kernel.dict.api.constants.DictConstants;
import cn.stylefeng.roses.kernel.dict.modular.entity.SysDict;
import cn.stylefeng.roses.kernel.dict.modular.pojo.TreeDictInfo;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.DictRequest;
import cn.stylefeng.roses.kernel.dict.modular.service.DictService;
import cn.stylefeng.roses.kernel.rule.annotation.ApiLog;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典详情管理，具体管理某个字典类型下的条目
 *
 * @author fengshuonan
 * @since 2020/10/29 14:45
 */
@RestController
@ApiResource(name = "字典详情管理")
public class DictController {

    @Resource
    private DictService dictService;

    /**
     * 获取树形字典列表
     *
     * @author fengshuonan
     * @since 2023/6/27 16:52
     */
    @GetResource(name = "获取树形字典列表", path = "/dict/getDictTreeList")
    public ResponseData<List<TreeDictInfo>> getDictTreeList(@Validated(DictRequest.treeList.class) DictRequest dictRequest) {
        List<TreeDictInfo> treeDictList = this.dictService.getTreeDictList(dictRequest);
        return new SuccessResponseData<>(treeDictList);
    }

    /**
     * 添加字典条目
     *
     * @author fengshuonan
     * @since 2020/10/29 16:35
     */
    @PostResource(name = "添加字典", path = "/dict/add", requiredPermission = true, requirePermissionCode = DictConstants.ADD_DICT)
    @ApiLog
    public ResponseData<?> add(@RequestBody @Validated(DictRequest.add.class) DictRequest dictRequest) {
        this.dictService.add(dictRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量删除字典条目
     *
     * @author liyanjun
     * @date 2023/07/04 10:29
     */
    @PostResource(name = "批量删除字典条目", path = "/dict/batchDelete", requiredPermission = true,
            requirePermissionCode = DictConstants.DELETE_DICT)
    public ResponseData<?> batchDelete(@RequestBody @Validated(DictRequest.batchDelete.class) DictRequest dictRequest) {
        dictService.batchDelete(dictRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除字典条目
     *
     * @author fengshuonan
     * @since 2020/10/29 16:35
     */
    @PostResource(name = "删除字典", path = "/dict/delete", requiredPermission = true, requirePermissionCode = DictConstants.DELETE_DICT)
    @ApiLog
    public ResponseData<?> delete(@RequestBody @Validated(DictRequest.delete.class) DictRequest dictRequest) {
        this.dictService.del(dictRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改字典条目
     *
     * @author fengshuonan
     * @since 2020/10/29 16:35
     */
    @PostResource(name = "修改字典", path = "/dict/edit", requiredPermission = true, requirePermissionCode = DictConstants.EDIT_DICT)
    @ApiLog
    public ResponseData<?> edit(@RequestBody @Validated(DictRequest.edit.class) DictRequest dictRequest) {
        this.dictService.edit(dictRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 获取字典详情
     *
     * @author fengshuonan
     * @since 2020/10/29 16:35
     */
    @GetResource(name = "获取字典详情", path = "/dict/detail")
    public ResponseData<SysDict> detail(@Validated(BaseRequest.detail.class) DictRequest dictRequest) {
        SysDict detail = this.dictService.detail(dictRequest);
        return new SuccessResponseData<>(detail);
    }

    /**
     * 获取平铺（展开）结构的字典列表
     * <p>
     * 本接口可能会传字典类型id，或者字典类型编码，或者searchText
     *
     * @author fengshuonan
     * @since 2023/6/27 17:36
     */
    @GetResource(name = "获取字典列表", path = "/dict/list")
    public ResponseData<List<SysDict>> list(DictRequest dictRequest) {
        return new SuccessResponseData<>(this.dictService.findList(dictRequest));
    }

    /**
     * 更新整个字典树结构，用来更新上下级结构和顺序
     *
     * @author fengshuonan
     * @since 2023/6/27 18:23
     */
    @PostResource(name = "更新整个字典树结构，用来更新上下级结构和顺序", path = "/dict/updateDictTree", requiredPermission = true,
            requirePermissionCode = DictConstants.EDIT_DICT)
    public ResponseData<List<SysDict>> updateDictTree(@RequestBody @Validated(DictRequest.updateTree.class) DictRequest dictRequest) {
        this.dictService.updateDictTree(dictRequest);
        return new SuccessResponseData<>();
    }

}
