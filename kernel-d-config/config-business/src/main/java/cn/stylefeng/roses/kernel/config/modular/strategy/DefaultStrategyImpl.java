package cn.stylefeng.roses.kernel.config.modular.strategy;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import cn.stylefeng.roses.kernel.config.api.ConfigInitStrategyApi;
import cn.stylefeng.roses.kernel.config.api.constants.ConfigConstants;
import cn.stylefeng.roses.kernel.config.api.enums.FileStorageTypeEnum;
import cn.stylefeng.roses.kernel.config.api.pojo.ConfigInitItem;
import cn.stylefeng.roses.kernel.config.modular.service.SysConfigService;
import cn.stylefeng.roses.kernel.rule.util.JarPathUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 默认的初始化配置策略，初始化系统一些必要的参数
 *
 * @author fengshuonan
 * @since 2021/7/8 17:47
 */
@Component
public class DefaultStrategyImpl implements ConfigInitStrategyApi {

    @Resource
    private SysConfigService sysConfigService;

    @Override
    public String getTitle() {
        return "系统参数";
    }

    @Override
    public String getDescription() {
        return "系统相关的初始化配置";
    }

    @Override
    public List<ConfigInitItem> getInitConfigs() {
        ArrayList<ConfigInitItem> configInitItems = new ArrayList<>();
        configInitItems.add(new ConfigInitItem("服务部署的访问地址", "SYS_SERVER_DEPLOY_HOST", "http://localhost:8080", "一般用在拼接文件的访问地址，注意，一定要配置连到后台的地址"));
        configInitItems.add(new ConfigInitItem("websocket的ws-url", "WEB_SOCKET_WS_URL", "ws://localhost:8080/ws/message/{token}", "websocket模块的连接url，用在消息通知模块，注意，一定要配置连到后台的地址"));
        configInitItems.add(new ConfigInitItem("auth认证用的jwt秘钥", "SYS_AUTH_JWT_SECRET", RandomUtil.randomString(30), "用于校验登录token，已随机生成一个30位密钥，请放心使用"));
        configInitItems.add(new ConfigInitItem("Druid控制台账号", "SYS_DRUID_ACCOUNT", "admin", "Druid控制台账号"));
        configInitItems.add(new ConfigInitItem("Druid控制台账号密码", "SYS_DRUID_PASSWORD", RandomUtil.randomString(20), "Druid控制台账号密码，已随机生成一个20位密钥，请放心使用"));
        configInitItems.add(new ConfigInitItem("session过期时间", "SYS_SESSION_EXPIRED_SECONDS", "3600", "单位：秒，session的过期时间，这个时间段内不操作会自动踢下线"));
        configInitItems.add(new ConfigInitItem("账号单端登录限制", "SYS_SINGLE_ACCOUNT_LOGIN_FLAG", "false", "如果开启，则同一个账号只能一个地方登录"));
        configInitItems.add(new ConfigInitItem("系统默认密码", "SYS_DEFAULT_PASSWORD", "123456", "用在重置密码的默认密码"));
        configInitItems.add(new ConfigInitItem("系统发布版本", "SYS_RELEASE_VERSION", DateUtil.format(new Date(), "yyyyMMdd"), "系统发布的版本号"));
        configInitItems.add(new ConfigInitItem("数据库加密AES秘钥", "SYS_ENCRYPT_SECRET_KEY", RandomUtil.randomString(32), "对称加密秘钥，用在数据库数据加密"));
        configInitItems.add(new ConfigInitItem("SSO服务端加密Token信息秘钥", "SYS_AUTH_SSO_DECRYPT_DATA_SECRET", RandomUtil.randomString(32), "SSO服务端加密Token信息秘钥，用在单点登录认证时候加密生成Token"));

        // 【2024年8月29日新增】设置文件存储的类型，新项目采用10-本地存储（jar启动路径）
        configInitItems.add(new ConfigInitItem("文件存储类型", ConfigConstants.SYS_FILE_SAVE_TYPE_CONFIG_CODE, String.valueOf(FileStorageTypeEnum.LOCAL_DEFAULT.getCode()), "文件存储位置类型：10-本地存储（jar包所在位置），11-本地存储（指定位置）"));

        // 【2024年8月29日新增】获取初始化路径地址，默认采用当前目录，所以获取一下当前jar路径
        String localDefaultStoragePath = JarPathUtil.getJarPath();
        if(StrUtil.isBlank(localDefaultStoragePath)){
            configInitItems.add(new ConfigInitItem("Windows本地文件保存路径", "SYS_LOCAL_FILE_SAVE_PATH_WINDOWS", "D:\\tempFilePath", "本地文件存储的路径，如果没有用本地文件存储，可忽略此配置"));
            configInitItems.add(new ConfigInitItem("Linux本地文件保存路径", "SYS_LOCAL_FILE_SAVE_PATH_LINUX", "/opt/gunsFilePath", "本地文件存储的路径，上线请注意别使用/tmp目录，如果没有用本地文件存储，可忽略此配置"));
        }else{
            if(SystemUtil.getOsInfo().isWindows()){
                configInitItems.add(new ConfigInitItem("Windows本地文件保存路径", "SYS_LOCAL_FILE_SAVE_PATH_WINDOWS", localDefaultStoragePath, "本地文件存储的路径，如果没有用本地文件存储，可忽略此配置"));
            }else{
                configInitItems.add(new ConfigInitItem("Linux本地文件保存路径", "SYS_LOCAL_FILE_SAVE_PATH_LINUX", localDefaultStoragePath, "本地文件存储的路径，上线请注意别使用/tmp目录，如果没有用本地文件存储，可忽略此配置"));
            }
        }

        return configInitItems;
    }

}
