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
package cn.stylefeng.roses.kernel.rule.pojo.request;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.util.SqlInjectionDetector;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 请求基类，所有接口请求可继承此类
 *
 * @author fengshuonan
 * @since 2020/10/14 18:12
 */
@Data
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 查询开始时间
     */
    @ChineseDescription("查询开始时间")
    private String searchBeginTime;

    /**
     * 结束时间
     */
    @ChineseDescription("查询结束时间")
    private String searchEndTime;

    /**
     * 分页：每页大小（默认20）
     */
    @ChineseDescription("分页：每页大小（默认20）")
    private Integer pageSize;

    /**
     * 分页：第几页（从1开始）
     */
    @ChineseDescription("分页：第几页（从1开始）")
    private Integer pageNo;

    /**
     * 排序字段
     */
    @ChineseDescription("排序字段")
    private String orderBy;

    /**
     * 正序或者倒序排列（asc 或 desc）
     */
    @ChineseDescription("正序或者倒序排列（asc 或 desc）")
    private String sortBy;

    /**
     * 其他参数（如有需要）
     */
    @ChineseDescription("其他参数")
    private Map<String, Object> otherParams;

    /**
     * 唯一请求号
     */
    @ChineseDescription("唯一请求号")
    private String requestNo;

    /**
     * 业务节点id
     */
    @ChineseDescription("业务节点id")
    private String spanId;

    /**
     * 当前登录用户的token
     */
    @ChineseDescription("当前登录用户的token")
    private String token;

    /**
     * 分组查询条件，例如：所有分组 、 未分组、 我的分组等名称
     */
    @ChineseDescription("分组名称，例如：所有分组、未分组、我的分组等名称")
    private String conditionGroupName;
    /**
     * 查询分组时候in标识：固定传true或false，如果是true，则为in，false为为not in
     */
    @ChineseDescription("查询分组时候in标识：固定传true或false，如果是true，则为in，false为为not in")
    private Boolean conditionGroupInFlag;
    /**
     * 业务id集合，当查询未分组或者指定分组时候需要填充此字段，用来查到用户在这个组下有多少个业务id
     */
    @ChineseDescription("业务id集合，当查询未分组或者指定分组时候需要填充此字段，用来查到用户在这个组下有多少个业务id")
    private List<Long> conditionGroupUserBizIdList;

    /**
     * 搜索内容，通用查询条件的值
     */
    @ChineseDescription("通用搜索关键字")
    private String searchText;

    /**
     * 参数校验分组：分页
     */
    public @interface page {
    }

    /**
     * 参数校验分组：查询所有
     */
    public @interface list {
    }

    /**
     * 参数校验分组：增加
     */
    public @interface add {
    }

    /**
     * 参数校验分组：编辑
     */
    public @interface edit {
    }

    /**
     * 参数校验分组：删除
     */
    public @interface delete {
    }

    /**
     * 参数校验分组：详情
     */
    public @interface detail {
    }

    /**
     * 参数校验分组：导出
     */
    public @interface export {
    }

    /**
     * 参数校验分组：修改状态
     */
    public @interface updateStatus {
    }

    /**
     * 参数校验分组：批量删除
     */
    public @interface batchDelete {
    }

    /**
     * 获取排序的结尾拼接sql
     * <p>
     * 根据orderBy和sortBy参数，这俩参数均进行过sql注入过滤
     *
     * @author fengshuonan
     * @since 2023/5/30 16:29
     */
    public String getOrderByLastSql() {

        if (ObjectUtil.isEmpty(this.orderBy) || ObjectUtil.isEmpty(this.sortBy)) {
            return StrUtil.EMPTY;
        }

        // 检测这俩参数有没有注入风险
        if (SqlInjectionDetector.hasSqlInjection(this.orderBy) || SqlInjectionDetector.hasSqlInjection(this.sortBy)) {
            return StrUtil.EMPTY;
        }

        // 进行order by语句的拼接
        return " order by " + this.orderBy + " " + this.sortBy + " ";
    }

}
