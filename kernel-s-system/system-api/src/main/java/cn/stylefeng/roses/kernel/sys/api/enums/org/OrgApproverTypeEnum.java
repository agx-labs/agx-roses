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
package cn.stylefeng.roses.kernel.sys.api.enums.org;

import lombok.Getter;

/**
 * 组织审批类型：1-负责人，2-部长，3-体系负责人，4-部门助理，5-资产助理（专员），6-考勤专员，7-HRBP，8-门禁员，9-办公账号员，10-转岗须知员
 *
 * @author fengshuonan
 * @since 2024-02-25 15:04
 */
@Getter
public enum OrgApproverTypeEnum {

    /**
     * 负责人
     */
    FZR(1, "负责人"),

    /**
     * 部长
     */
    BZ(2, "部长"),

    /**
     * 体系负责人
     */
    TXFZR(3, "体系负责人"),

    /**
     * 部门助理
     */
    BMZL(4, "部门助理"),

    /**
     * 资产助理（专员）
     */
    ZCZL(5, "资产助理（专员）"),

    /**
     * 考勤专员
     */
    KQZY(6, "考勤专员"),

    /**
     * HRBP
     */
    HRBP(7, "HRBP"),

    /**
     * 门禁员
     */
    MJY(8, "门禁员"),

    /**
     * 办公账号员
     */
    BGZHY(9, "办公账号员"),

    /**
     * 转岗须知员
     */
    ZGXZY(10, "转岗须知员");

    private final Integer code;

    private final String message;

    OrgApproverTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * code转化为enum
     *
     * @author fengshuonan
     * @since 2024-02-18 16:11
     */
    public static OrgApproverTypeEnum toEnum(Integer code) {
        for (OrgApproverTypeEnum orgTypeEnum : OrgApproverTypeEnum.values()) {
            if (orgTypeEnum.getCode().equals(code)) {
                return orgTypeEnum;
            }
        }
        return null;
    }

    /**
     * 获取code对应的message
     *
     * @author fengshuonan
     * @since 2024-02-18 16:11
     */
    public static String getCodeMessage(Integer code) {
        OrgApproverTypeEnum orgTypeEnum = toEnum(code);
        if (orgTypeEnum != null) {
            return orgTypeEnum.getMessage();
        } else {
            return "";
        }
    }

}
