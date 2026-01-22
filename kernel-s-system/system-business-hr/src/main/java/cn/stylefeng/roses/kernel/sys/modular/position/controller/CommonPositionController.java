package cn.stylefeng.roses.kernel.sys.modular.position.controller;

import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.position.pojo.request.HrPositionRequest;
import cn.stylefeng.roses.kernel.sys.modular.position.service.HrPositionService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用获取职位相关业务
 *
 * @author fengshuonan
 * @since 2023/11/2 10:28
 */
@RestController
@ApiResource(name = "通用获取职位相关业务")
public class CommonPositionController {

    @Resource
    private HrPositionService hrPositionService;

    /**
     * 批量获取职位名称
     *
     * @author fengshuonan
     * @since 2023/11/2 10:24
     */
    @PostResource(name = "批量获取职位名称", path = "/common/position/batchGetName")
    public ResponseData<List<SimpleDict>> batchGetName(
            @RequestBody @Validated(BaseRequest.batchDelete.class) HrPositionRequest hrPositionRequest) {
        List<SimpleDict> result = hrPositionService.batchGetName(hrPositionRequest);
        return new SuccessResponseData<>(result);
    }

}
