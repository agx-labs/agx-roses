package cn.stylefeng.roses.kernel.security.captcha;

import cn.hutool.core.util.IdUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.security.api.constants.CaptchaConstants;
import cn.stylefeng.roses.kernel.security.api.exception.enums.SecurityExceptionEnum;
import cn.stylefeng.roses.kernel.security.api.pojo.DragCaptchaImageDTO;
import cn.stylefeng.roses.kernel.security.captcha.util.DragCaptchaImageUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DragCaptchaServiceV2 extends DragCaptchaService {

    private final CacheOperatorApi<String> cacheOperatorApi;

    public DragCaptchaServiceV2(CacheOperatorApi<String> cacheOperatorApi) {
        super(cacheOperatorApi);
        this.cacheOperatorApi = cacheOperatorApi;
    }

    @Override
    public DragCaptchaImageDTO createCaptcha() {
        try {
            // 1. 生成遮罩层（透明 PNG）+ 随机坐标
            DragCaptchaImageDTO maskBackground = DragCaptchaImageUtil.getMaskBackground();

            // 2. 缓存校验值
            String verKey = IdUtil.simpleUUID();
            cacheOperatorApi.put(verKey, String.valueOf(maskBackground.getLocationX()), CaptchaConstants.DRAG_CAPTCHA_IMG_EXP_SECONDS);

            // 3. 返回随机校验值，还有底图
            maskBackground.setKey(verKey);
            return maskBackground;

        } catch (IOException e) {
            log.error("生成拖拽验证码失败", e);
            throw new ServiceException(SecurityExceptionEnum.CAPTCHA_ERROR);
        }
    }

}