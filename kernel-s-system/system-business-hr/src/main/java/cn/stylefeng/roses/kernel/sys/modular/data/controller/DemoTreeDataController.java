package cn.stylefeng.roses.kernel.sys.modular.data.controller;

import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleTreeDict;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.modular.data.factory.DemoDataCreateFactory;
import cn.stylefeng.roses.kernel.sys.modular.data.pojo.UserRequest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 这是一个Demo控制器，用在下拉列表填充一些随机数据
 *
 * @author fengshuonan
 * @since 2024/6/5 16:52
 */
@RestController
@ApiResource(name = "Demo控制器，填充测试数据")
public class DemoTreeDataController {

    /**
     * 生成一些随机的数据，组织机构信息
     *
     * @author fengshuonan
     * @since 2024/6/5 16:53
     */
    @GetResource(name = "生成一些随机的数据，组织机构信息", path = "/my/org/selectList")
    public ResponseData<List<SimpleTreeDict>> orgSelectList(BaseRequest baseRequest) {
        return new SuccessResponseData<>(DemoDataCreateFactory.createOrgData());
    }

    /**
     * 生成一些随机的数据，组织机构信息（树形下拉）
     *
     * @author fengshuonan
     * @since 2024/6/5 17:04
     */
    @GetResource(name = "生成一些随机的数据，组织机构信息（树形下拉）", path = "/my/org/treeSelectList")
    public ResponseData<List<SimpleTreeDict>> treeSelectList(BaseRequest baseRequest) {
        return new SuccessResponseData<>(DemoDataCreateFactory.createOrgData());
    }

    /**
     * 生成一些用户信息
     *
     * @author fengshuonan
     * @since 2024/6/5 17:04
     */
    @GetResource(name = "生成一些用户信息", path = "/my/user/selectList")
    public ResponseData<List<SimpleTreeDict>> userSelectList(UserRequest userRequest) {
        return new SuccessResponseData<>(DemoDataCreateFactory.createUserData());
    }

}
