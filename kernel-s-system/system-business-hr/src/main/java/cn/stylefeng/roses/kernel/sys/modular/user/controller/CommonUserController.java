package cn.stylefeng.roses.kernel.sys.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用用户相关的业务
 *
 * @author fengshuonan
 * @since 2023/11/2 10:24
 */
@RestController
@ApiResource(name = "通用用户相关的业务")
public class CommonUserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 批量获取用户的名称列表
     *
     * @author fengshuonan
     * @since 2023/11/2 10:24
     */
    @PostResource(name = "批量获取用户的名称列表", path = "/common/sysUser/batchGetName")
    public ResponseData<List<SimpleDict>> batchGetName(
            @RequestBody @Validated(BaseRequest.batchDelete.class) SysUserRequest sysUserRequest) {
        List<SimpleDict> result = sysUserService.batchGetName(sysUserRequest);
        return new SuccessResponseData<>(result);
    }

}
