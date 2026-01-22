package cn.stylefeng.roses.kernel.security.blackwhite.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.security.api.constants.SecurityPermissionConstants;
import cn.stylefeng.roses.kernel.security.blackwhite.BlackListService;
import cn.stylefeng.roses.kernel.security.blackwhite.WhiteListService;
import cn.stylefeng.roses.kernel.security.blackwhite.pojo.BlackWhiteList;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 黑白名单接口
 *
 * @author fengshuonan
 * @since 2024/7/10 18:52
 */
@RestController
@ApiResource(name = "黑白名单接口", requiredPermission = true, requirePermissionCode = SecurityPermissionConstants.BLACK_WHITE_LIST_UPDATE)
public class SecurityBlackWhiteController {

    @Resource
    private BlackListService blackListService;

    @Resource
    private WhiteListService whiteListService;

    /**
     * 获取所有的黑白名单列表
     *
     * @author fengshuonan
     * @since 2024/7/10 21:29
     */
    @GetResource(name = "获取黑白名单列表", path = "/blackWhite/list")
    public ResponseData<BlackWhiteList> blackWhiteList() {

        BlackWhiteList blackWhiteList = new BlackWhiteList();

        // 获取白名单
        Collection<String> blackList = blackListService.getBlackList();
        blackWhiteList.setBlackList(new ArrayList<>(blackList));

        // 获取黑名单
        Collection<String> whiteList = whiteListService.getWhiteList();
        blackWhiteList.setWhiteList(new ArrayList<>(whiteList));

        return new SuccessResponseData<>(blackWhiteList);
    }

    /**
     * 设置黑白名单列表
     *
     * @author fengshuonan
     * @since 2024/7/10 21:29
     */
    @PostResource(name = "设置黑白名单列表", path = "/blackWhite/updateBlackWhite")
    public ResponseData<?> updateBlackWhite(@RequestBody BlackWhiteList blackWhiteList) {

        // 清空黑名单列表
        Collection<String> blackList = blackListService.getBlackList();
        if (ObjectUtil.isNotEmpty(blackList)) {
            for (String key : blackList) {
                blackListService.removeBlackItem(key);
            }
        }

        // 更新黑名单列表
        if (ObjectUtil.isNotEmpty(blackWhiteList.getBlackList())) {
            for (String key : blackWhiteList.getBlackList()) {
                blackListService.addBlackItem(key);
            }
        }

        // 清空白名单列表
        Collection<String> whiteList = whiteListService.getWhiteList();
        if (ObjectUtil.isNotEmpty(whiteList)) {
            for (String key : whiteList) {
                whiteListService.removeWhiteItem(key);
            }
        }

        // 更新白名单列表
        if (ObjectUtil.isNotEmpty(blackWhiteList.getWhiteList())) {
            for (String key : blackWhiteList.getWhiteList()) {
                whiteListService.addWhiteItem(key);
            }
        }

        return new SuccessResponseData<>();
    }


}
