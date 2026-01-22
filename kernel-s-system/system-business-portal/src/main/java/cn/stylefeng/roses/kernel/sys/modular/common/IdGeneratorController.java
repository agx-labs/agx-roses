package cn.stylefeng.roses.kernel.sys.modular.common;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用id生成器
 *
 * @author fengshuonan
 * @since 2023/4/11 16:54
 */
@RestController
@ApiResource(name = "通用id生成器")
public class IdGeneratorController {

    /**
     * 通用id生成器
     *
     * @author fengshuonan
     * @since 2023/4/11 16:54
     */
    @GetResource(name = "通用id生成", path = "/id/getId")
    public ResponseData<String> getId() {
        return new SuccessResponseData<>(IdWorker.getIdStr());
    }

}
