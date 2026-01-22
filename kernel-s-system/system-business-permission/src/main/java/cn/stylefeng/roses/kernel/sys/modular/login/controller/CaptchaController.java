package cn.stylefeng.roses.kernel.sys.modular.login.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.security.api.DragCaptchaApi;
import cn.stylefeng.roses.kernel.security.api.ImageCaptchaApi;
import cn.stylefeng.roses.kernel.security.api.pojo.DragCaptchaImageDTO;
import cn.stylefeng.roses.kernel.security.api.pojo.ImageCaptcha;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图形验证码接口
 *
 * @author fengshuonan
 * @since 2023/11/24 23:46
 */
@RestController
@Slf4j
@ApiResource(name = "图形验证码接口")
public class CaptchaController {

    @Resource
    private DragCaptchaApi dragCaptchaApi;

    @Resource
    private ImageCaptchaApi imageCaptchaApi;

    /**
     * 获取拖拽验证码的图片
     *
     * @author fengshuonan
     * @since 2023/11/24 23:47
     */
    @GetResource(name = "获取拖拽验证码的图片", path = "/getDragCaptcha", requiredLogin = false)
    public ResponseData<DragCaptchaImageDTO> getDragCaptcha() {
        DragCaptchaImageDTO captcha = dragCaptchaApi.createCaptcha();
        log.info("拖拽验证码的图片信息：key={}，x={}，Y={}", captcha.getKey(), captcha.getLocationX(), captcha.getLocationY());
        captcha.setLocationX(null);
        return new SuccessResponseData<>(captcha);
    }

    /**
     * 获取图形验证码
     *
     * @author fengshuonan
     * @since 2025/10/30 10:23
     */
    @GetResource(name = "获取图形验证码", path = "/getImageCaptcha", requiredLogin = false)
    public ResponseData<ImageCaptcha> getImageCaptcha() {
        ImageCaptcha captcha = imageCaptchaApi.captcha();
        return new SuccessResponseData<>(captcha);
    }

}
