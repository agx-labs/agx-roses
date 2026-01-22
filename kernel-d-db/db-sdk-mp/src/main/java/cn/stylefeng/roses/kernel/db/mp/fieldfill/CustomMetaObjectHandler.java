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
package cn.stylefeng.roses.kernel.db.mp.fieldfill;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.db.api.constants.DbFieldConstants;
import cn.stylefeng.roses.kernel.db.api.util.EntityFieldUtil;
import cn.stylefeng.roses.kernel.db.mp.tenant.holder.TenantIdHolder;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

import java.lang.reflect.Field;
import java.util.Date;


/**
 * 字段自动填充工具，在mybatis-plus执行更新和新增操作时候，会对指定字段进行自动填充，例如 create_time 等字段
 *
 * @author fengshuonan
 * @since 2020/10/16 17:14
 */
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        try {
            // 设置createUser（BaseEntity)
            setValue(metaObject, DbFieldConstants.CREATE_USER, this.getUserUniqueId());

            // 设置createTime（BaseEntity)
            setValue(metaObject, DbFieldConstants.CREATE_TIME, new Date());

            // 设置删除标记 默认N-删除
            setDelFlagDefaultValue(metaObject);

            // 设置状态字段 默认1-启用
            setStatusDefaultValue(metaObject);

            // 设置乐观锁字段，从0开始
            setValue(metaObject, DbFieldConstants.VERSION_FLAG, 0L);

            // 设置组织id
            setValue(metaObject, DbFieldConstants.ORG_ID, this.getUserOrgId());

            // 设置租户id
            setValue(metaObject, DbFieldConstants.TENANT_ID, this.getTenantId());

        } catch (ReflectionException e) {
            log.warn("CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {

        try {
            // 设置updateUser（BaseEntity)
            if (metaObject.hasSetter(DbFieldConstants.UPDATE_USER)) {
                metaObject.setValue(DbFieldConstants.UPDATE_USER, this.getUserUniqueId());
            }

            // 设置updateTime（BaseEntity)
            if (metaObject.hasSetter(DbFieldConstants.UPDATE_TIME)) {
                metaObject.setValue(DbFieldConstants.UPDATE_TIME, new Date());
            }
        } catch (ReflectionException e) {
            log.warn("CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }

    }

    /**
     * 获取用户唯一id
     *
     * @author fengshuonan
     * @since 2021/10/29 10:01
     */
    protected Long getUserUniqueId() {

        try {
            return LoginContext.me().getLoginUser().getUserId();
        } catch (Exception e) {
            //如果获取不到就返回-1
            return -1L;
        }

    }

    /**
     * 设置属性
     *
     * @author fengshuonan
     * @since 2021/10/29 10:01
     */
    protected void setValue(MetaObject metaObject, String fieldName, Object value) {
        Object originalAttr = getFieldValByName(fieldName, metaObject);
        if (ObjectUtil.isEmpty(originalAttr)) {
            setFieldValByName(fieldName, value, metaObject);
        }
    }

    /**
     * 获取用户唯一id
     *
     * @author yxx
     * @since 2022/09/01 10:14
     */
    protected Long getUserOrgId() {

        try {
            return LoginContext.me().getLoginUser().getCurrentOrgId();
        } catch (Exception e) {
            //如果获取不到就返回-1
            return -1L;
        }

    }

    /**
     * 获取用户的租户id
     *
     * @author fengshuonan
     * @since 2023/9/5 16:19
     */
    protected Long getTenantId() {
        try {

            // 1. 优先从线程变量中获取，这个优先级最高
            Long tenantId = TenantIdHolder.get();
            if (ObjectUtil.isNotEmpty(tenantId)) {
                return tenantId;
            }

            // 2. 其次，从loginUser中获取
            return LoginContext.me().getLoginUser().getTenantId();
        } catch (Exception e) {
            //如果获取不到就返回-1
            return -1L;
        }
    }

    /**
     * 设置属性，针对逻辑删除字段
     *
     * @author fengshuonan
     * @since 2022/9/7 17:23
     */
    protected void setDelFlagDefaultValue(MetaObject metaObject) {
        Object originalAttr = getFieldValByName(DbFieldConstants.DEL_FLAG, metaObject);
        if (ObjectUtil.isNotEmpty(originalAttr)) {
            return;
        }
        Object originalObject = metaObject.getOriginalObject();
        try {
            // 获取delFlag字段的类型，如果是枚举类型，则设置枚举
            Field declaredField = EntityFieldUtil.getDeclaredField(originalObject, DbFieldConstants.DEL_FLAG);
            if (ClassUtil.isEnum(declaredField.getType())) {
                setFieldValByName(DbFieldConstants.DEL_FLAG, YesOrNotEnum.N, metaObject);
            } else {
                setFieldValByName(DbFieldConstants.DEL_FLAG, YesOrNotEnum.N.getCode(), metaObject);
            }
        } catch (NoSuchFieldException ignored) {
            // 没有字段，忽略
        }
    }

    /**
     * 设置属性，针对状态字段
     *
     * @author fengshuonan
     * @since 2022/9/7 17:23
     */
    protected void setStatusDefaultValue(MetaObject metaObject) {
        Object originalAttr = getFieldValByName(DbFieldConstants.STATUS_FLAG, metaObject);
        if (ObjectUtil.isNotEmpty(originalAttr)) {
            return;
        }
        Object originalObject = metaObject.getOriginalObject();
        try {
            // 获取statusFlag字段的类型，如果是枚举类型，则设置枚举
            Field declaredField = EntityFieldUtil.getDeclaredField(originalObject, DbFieldConstants.STATUS_FLAG);
            if (ClassUtil.isEnum(declaredField.getType())) {
                setFieldValByName(DbFieldConstants.STATUS_FLAG, StatusEnum.ENABLE, metaObject);
            } else {
                setFieldValByName(DbFieldConstants.STATUS_FLAG, StatusEnum.ENABLE.getCode(), metaObject);
            }
        } catch (NoSuchFieldException ignored) {
            // 没有字段，忽略
        }
    }


}
