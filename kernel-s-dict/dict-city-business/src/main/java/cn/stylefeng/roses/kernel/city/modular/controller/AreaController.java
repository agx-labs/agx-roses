package cn.stylefeng.roses.kernel.city.modular.controller;

import cn.stylefeng.roses.kernel.city.modular.entity.Area;
import cn.stylefeng.roses.kernel.city.modular.pojo.request.AreaRequest;
import cn.stylefeng.roses.kernel.city.modular.service.AreaService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行政区域控制器
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
@RestController
@ApiResource(name = "行政区域")
public class AreaController {

    @Resource
    private AreaService areaService;

    /**
     * 获取列表
     *
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    @GetResource(name = "获取行政区域列表", path = "/area/list")
    public ResponseData<List<Area>> list(AreaRequest areaRequest) {
        return new SuccessResponseData<>(areaService.findList(areaRequest));
    }

}
