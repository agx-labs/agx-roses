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
package cn.stylefeng.roses.kernel.log.loginlog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.LoginLogServiceApi;
import cn.stylefeng.roses.kernel.log.api.exception.LogException;
import cn.stylefeng.roses.kernel.log.api.exception.enums.LogExceptionEnum;
import cn.stylefeng.roses.kernel.log.api.pojo.loginlog.SysLoginLogRequest;
import cn.stylefeng.roses.kernel.log.loginlog.constants.LoginLogConstant;
import cn.stylefeng.roses.kernel.log.loginlog.entity.SysLoginLog;
import cn.stylefeng.roses.kernel.log.loginlog.mapper.SysLoginLogMapper;
import cn.stylefeng.roses.kernel.log.loginlog.service.SysLoginLogService;
import cn.stylefeng.roses.kernel.rule.util.HttpServletUtil;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 系统应用service接口实现类
 *
 * @author fengshuonan
 * @since 2020/3/13 16:15
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService, LoginLogServiceApi {

    @Resource
    private SysUserServiceApi sysUserServiceApi;

    @Override
    public void del(SysLoginLogRequest sysLoginLogRequest) {
        SysLoginLog sysLoginLog = this.querySysLoginLogById(sysLoginLogRequest);
        this.removeById(sysLoginLog.getLlgId());
    }

    @Override
    public SysLoginLog detail(SysLoginLogRequest sysLoginLogRequest) {
        LambdaQueryWrapper<SysLoginLog> queryWrapper = this.createWrapper(sysLoginLogRequest);
        return this.getOne(queryWrapper, false);
    }

    @Override
    public PageResult<SysLoginLog> findPage(SysLoginLogRequest sysLoginLogRequest) {

        LambdaQueryWrapper<SysLoginLog> wrapper = this.createWrapper(sysLoginLogRequest);

        Page<SysLoginLog> page = this.page(PageFactory.defaultPage(), wrapper);

        return PageResultFactory.createPageResult(page);
    }

    @Override
    public void add(SysLoginLogRequest sysLoginLogRequest) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        BeanUtil.copyProperties(sysLoginLogRequest, sysLoginLog);
        this.save(sysLoginLog);
    }

    @Override
    public void loginSuccess(Long userId, String account) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setLlgName(LoginLogConstant.LOGIN_IN_LOGINNAME);
        sysLoginLog.setUserId(userId);
        sysLoginLog.setAccount(account);
        sysLoginLog.setLlgIpAddress(HttpServletUtil.getRequestClientIp(HttpServletUtil.getRequest()));
        sysLoginLog.setLlgSucceed(LoginLogConstant.OPERATION_SUCCESS);
        sysLoginLog.setLlgMessage(LoginLogConstant.LOGIN_IN_SUCCESS_MESSAGE);
        this.save(sysLoginLog);
    }

    @Override
    public void loginFail(String account, String ip) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setLlgName(LoginLogConstant.LOGIN_IN_LOGINNAME);
        sysLoginLog.setLlgSucceed(LoginLogConstant.OPERATION_FAIL);
        sysLoginLog.setLlgMessage(LoginLogConstant.LOGIN_IN_SUCCESS_FAIL);
        sysLoginLog.setLlgIpAddress(ip);
        sysLoginLog.setAccount(account);
        this.save(sysLoginLog);
    }

    @Override
    public void loginOutSuccess(Long userId) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setLlgName(LoginLogConstant.LOGIN_OUT_LOGINNAME);
        sysLoginLog.setUserId(userId);
        sysLoginLog.setLlgIpAddress(HttpServletUtil.getRequestClientIp(HttpServletUtil.getRequest()));
        sysLoginLog.setLlgSucceed(LoginLogConstant.OPERATION_SUCCESS);
        sysLoginLog.setLlgMessage(LoginLogConstant.LOGIN_OUT_SUCCESS_MESSAGE);
        this.save(sysLoginLog);
    }

    @Override
    public void loginOutFail(Long userId) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setLlgName(LoginLogConstant.LOGIN_OUT_LOGINNAME);
        sysLoginLog.setUserId(userId);
        sysLoginLog.setLlgIpAddress(HttpServletUtil.getRequestClientIp(HttpServletUtil.getRequest()));
        sysLoginLog.setLlgSucceed(LoginLogConstant.OPERATION_FAIL);
        sysLoginLog.setLlgMessage(LoginLogConstant.LOGIN_OUT_SUCCESS_FAIL);
        this.save(sysLoginLog);
    }

    @Override
    public void delAll() {
        this.remove(null);
    }

    /**
     * 获取详细信息
     *
     * @author chenjinlong
     * @since 2021/1/13 10:50
     */
    private SysLoginLog querySysLoginLogById(SysLoginLogRequest sysLoginLogRequest) {
        SysLoginLog sysLoginLog = this.getById(sysLoginLogRequest.getLlgId());
        if (ObjectUtil.isNull(sysLoginLog)) {
            throw new LogException(LogExceptionEnum.LOG_NOT_EXISTED, sysLoginLogRequest.getLlgId());
        }
        return sysLoginLog;
    }

    /**
     * 构建wrapper
     *
     * @author chenjinlong
     * @since 2021/1/13 10:50
     */
    private LambdaQueryWrapper<SysLoginLog> createWrapper(SysLoginLogRequest sysLoginLogRequest) {
        LambdaQueryWrapper<SysLoginLog> queryWrapper = new LambdaQueryWrapper<>();

        if (ObjectUtil.isEmpty(sysLoginLogRequest)) {
            return queryWrapper;
        }

        // 根据【创建时间】筛选
        Date beginTime = sysLoginLogRequest.getBeginTime();
        Date endTime = sysLoginLogRequest.getEndTime();
        if (ObjectUtil.isNotEmpty(beginTime) && ObjectUtil.isNotEmpty(endTime)) {
            queryWrapper.between(SysLoginLog::getCreateTime, beginTime + " 00:00:00", endTime + " 23:59:59");
        }

        // 根据【日志名称】筛选，名称只有登录和退出日志两种
        if (StrUtil.isNotBlank(sysLoginLogRequest.getLlgName())) {
            queryWrapper.eq(SysLoginLog::getLlgName, sysLoginLogRequest.getLlgName());
        }

        // 根据【指定用户】筛选
        if (ObjectUtil.isNotEmpty(sysLoginLogRequest.getUserId())) {
            queryWrapper.eq(SysLoginLog::getUserId, sysLoginLogRequest.getUserId());
        }

        // 根据请求参数的顺序排列
        if (ObjectUtil.isNotEmpty(sysLoginLogRequest.getOrderBy()) && ObjectUtil.isNotEmpty(sysLoginLogRequest.getSortBy())) {
            queryWrapper.last(sysLoginLogRequest.getOrderByLastSql());
        } else {
            queryWrapper.orderByDesc(SysLoginLog::getCreateTime);
        }

        return queryWrapper;
    }

}
