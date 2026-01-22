package cn.stylefeng.roses.kernel.sys.modular.data.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.modular.data.service.BizDataProviderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 表单组件数据提供
 *
 * @author fengshuonan
 * @since 2024-04-20 11:16
 */
@RestController
@ApiResource(name = "表单组件数据提供")
public class BizDataProviderController {

    @Resource
    private BizDataProviderService bizDataProviderService;

    /**
     * 通过id获取人员写姓名
     *
     * @author fengshuonan
     * @since 2024-04-20 11:19
     */
    @GetResource(name = "通过id获取人员写姓名", path = "/bizFormComponentProvider/getUserName")
    public ResponseData<String> getUserName(@RequestParam("userId") Long userId) {
        return new SuccessResponseData<>(bizDataProviderService.getUserName(userId));
    }

    /**
     * 通过id获取机构的名称
     *
     * @author fengshuonan
     * @since 2024-04-20 11:19
     */
    @GetResource(name = "通过id获取机构的名称", path = "/bizFormComponentProvider/getOrgName")
    public ResponseData<String> getOrgName(@RequestParam("orgId") Long orgId) {
        return new SuccessResponseData<>(bizDataProviderService.getOrgName(orgId));
    }

    /**
     * 通过id获取角色名称
     *
     * @author fengshuonan
     * @since 2024-04-20 11:24
     */
    @GetResource(name = "通过id获取角色名称", path = "/bizFormComponentProvider/getRoleName")
    public ResponseData<String> getRoleName(@RequestParam("roleId") Long roleId) {
        return new SuccessResponseData<>(bizDataProviderService.getRoleName(roleId));
    }

    /**
     * 通过id获取角色名称
     *
     * @author fengshuonan
     * @since 2024-04-20 11:24
     */
    @GetResource(name = "通过职位id获取职位名称", path = "/bizFormComponentProvider/getPositionName")
    public ResponseData<String> getPositionName(@RequestParam("positionId") Long positionId) {
        return new SuccessResponseData<>(bizDataProviderService.getPositionName(positionId));
    }

    /**
     * 通过字典id获取字典值名称
     *
     * @author fengshuonan
     * @since 2024-04-20 11:27
     */
    @GetResource(name = "通过字典id获取字典值名称", path = "/bizFormComponentProvider/getDictName")
    public ResponseData<String> getDictName(@RequestParam("dictId") Long dictId) {
        return new SuccessResponseData<>(bizDataProviderService.getDictName(dictId));
    }

}
