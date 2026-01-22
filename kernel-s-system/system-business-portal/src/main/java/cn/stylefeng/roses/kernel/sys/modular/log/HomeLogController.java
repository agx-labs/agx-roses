package cn.stylefeng.roses.kernel.sys.modular.log;

import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.ApiLogManagerApi;
import cn.stylefeng.roses.kernel.log.api.pojo.manage.LogManagerRequest;
import cn.stylefeng.roses.kernel.log.api.pojo.record.LogRecordDTO;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页日志详情的接口
 *
 * @author fengshuonan
 * @since 2023/6/26 23:45
 */
@RestController
@ApiResource(name = "首页日志详情的接口")
public class HomeLogController {

    @Resource
    private ApiLogManagerApi logManagerApi;

    /**
     * 查询最近操作日志列表
     *
     * @author fengshuonan
     * @since 2023/6/26 23:48
     */
    @GetResource(name = "查询最近操作日志列表", path = "/homePage/getRecentLogs")
    public ResponseData<List<LogRecordDTO>> getRecentLogs() {

        // 只查询当前用户的
        LogManagerRequest logManagerRequest = new LogManagerRequest();
        logManagerRequest.setUserId(LoginContext.me().getLoginUser().getUserId());

        // 默认查询20条记录
        PageResult<LogRecordDTO> page = logManagerApi.apiLogPageQuery(logManagerRequest);

        return new SuccessResponseData<>(page.getRows());
    }

}
