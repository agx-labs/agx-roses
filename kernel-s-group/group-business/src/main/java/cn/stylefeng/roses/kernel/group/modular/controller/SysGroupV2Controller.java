package cn.stylefeng.roses.kernel.group.modular.controller;

import cn.stylefeng.roses.kernel.group.api.pojo.SysGroupDTO;
import cn.stylefeng.roses.kernel.group.modular.pojo.GroupAddRequest;
import cn.stylefeng.roses.kernel.group.modular.pojo.GroupDelRequest;
import cn.stylefeng.roses.kernel.group.modular.pojo.GroupSelectListRequest;
import cn.stylefeng.roses.kernel.group.modular.service.SysGroupV2Service;
import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
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
 * 业务分组，v2版本
 *
 * @author fengshuonan
 * @since 2025/10/27 10:52
 */
@RestController
@ApiResource(name = "业务分组v2", resBizType = ResBizTypeEnum.SYSTEM)
public class SysGroupV2Controller {

    @Resource
    private SysGroupV2Service sysGroupV2Service;

    /**
     * 添加分组时候的选择列表
     *
     * @author fengshuonan
     * @since 2025/10/27 11:00
     */
    @GetResource(name = "添加分组时候的选择列表", path = "/v2/sysGroup/addSelect")
    public ResponseData<List<SysGroupDTO>> addSelect(@Validated GroupSelectListRequest groupSelectListRequest) {
        return new SuccessResponseData<>(sysGroupV2Service.selectList(groupSelectListRequest));
    }

    /**
     * 将某个业务记录添加到分组
     *
     * @author fengshuonan
     * @since 2025/10/27 11:05
     */
    @PostResource(name = "添加", path = "/v2/sysGroup/add")
    public ResponseData<?> add(@RequestBody @Validated GroupAddRequest groupAddRequest) {
        sysGroupV2Service.addBindGroup(groupAddRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 新的删除分组
     *
     * @author fengshuonan
     * @since 2025/10/27 11:43
     */
    @PostResource(name = "删除", path = "/v2/sysGroup/delete")
    public ResponseData<?> delete(@RequestBody @Validated GroupDelRequest groupDelRequest) {
        sysGroupV2Service.del(groupDelRequest);
        return new SuccessResponseData<>();
    }

}
