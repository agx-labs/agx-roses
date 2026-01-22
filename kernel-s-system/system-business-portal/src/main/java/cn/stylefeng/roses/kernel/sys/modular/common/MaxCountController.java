package cn.stylefeng.roses.kernel.sys.modular.common;

import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.db.api.maxsort.MaxCountConfig;
import cn.stylefeng.roses.kernel.db.api.maxsort.context.MaxSortConfigContext;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.api.exception.enums.MaxSortExceptionEnum;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 最大排序获取
 *
 * @author fengshuonan
 * @since 2023/10/29 17:08
 */
@RestController
@ApiResource(name = "最大排序获取")
public class MaxCountController {

    @Resource
    private DbOperatorApi dbOperatorApi;

    /**
     * 业务排序获取
     * <p>
     * 给前端使用
     *
     * @author fengshuonan
     * @since 2023/10/29 17:59
     */
    @GetResource(name = "业务排序获取", path = "/common/getBusinessMaxSort")
    public ResponseData<Long> getBusinessMaxSort(@RequestParam("code") String code) {
        MaxCountConfig config = MaxSortConfigContext.getConfig(code);
        if (config == null) {
            throw new ServiceException(MaxSortExceptionEnum.CANT_FIND_CODE);
        }
        Long maxSort = dbOperatorApi.getMaxSortByTableName(config.getTableName(), config.getSortFieldName());

        // 默认返回排序数加1
        maxSort++;

        return new SuccessResponseData<>(maxSort);
    }

}
