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
package cn.stylefeng.roses.kernel.sys.api.pojo.user;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 较为全面的用户基本信息
 *
 * @author fengshuonan
 * @since 2023/7/19 11:06
 */
@Data
public class UserInfoDetailDTO {

    /**
     * 主键
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long userId;

    /**
     * 姓名
     */
    @TableField("real_name")
    @ChineseDescription("姓名")
    private String realName;

    /**
     * 昵称
     */
    @TableField("nick_name")
    @ChineseDescription("昵称")
    private String nickName;

    /**
     * 账号
     */
    @TableField("account")
    @ChineseDescription("账号")
    private String account;

    /**
     * 头像，存的为文件id
     */
    @ChineseDescription("头像，存的为文件id")
    private Long avatar;

    /**
     * 生日
     */
    @TableField("birthday")
    @ChineseDescription("生日")
    private Date birthday;

    /**
     * 性别：M-男，F-女
     */
    @ChineseDescription("性别：M-男，F-女")
    private String sex;

    /**
     * 邮箱
     */
    @ChineseDescription("邮箱")
    private String email;

    /**
     * 手机
     */
    @ChineseDescription("手机")
    private String phone;

    /**
     * 电话
     */
    @ChineseDescription("电话")
    private String tel;

    /**
     * 是否是超级管理员：Y-是，N-否
     */
    @ChineseDescription("是否是超级管理员：Y-是，N-否")
    private String superAdminFlag;

    /**
     * 状态：1-正常，2-冻结，3-临时冻结
     */
    @ChineseDescription("状态：1-正常，2-冻结，3-临时冻结")
    private Integer statusFlag;

    /**
     * 用户的排序
     */
    @ChineseDescription("用户的排序")
    private BigDecimal userSort;

    /**
     * 用户工号
     */
    @ChineseDescription("用户工号")
    private String employeeNumber;

    /**
     * 对接外部主数据的用户id
     */
    @ChineseDescription("对接外部主数据的用户id")
    private String masterUserId;

}
