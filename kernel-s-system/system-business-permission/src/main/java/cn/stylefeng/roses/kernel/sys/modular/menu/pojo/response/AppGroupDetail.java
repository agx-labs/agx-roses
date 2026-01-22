package cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response;

import cn.stylefeng.roses.kernel.file.api.format.FileUrlFormatProcess;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import lombok.Data;

import java.util.List;

/**
 * 用在菜单界面
 * <p>
 * 响应应用组下的应用信息，应用信息包含：应用名称、logo、应用说明、菜单列表（树形）
 *
 * @author fengshuonan
 * @since 2023/6/14 21:34
 */
@Data
public class AppGroupDetail {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用图标的文件id
     */
    @SimpleFieldFormat(processClass = FileUrlFormatProcess.class)
    private Long appIcon;

    /**
     * 应用说明
     */
    private String remark;

    /**
     * 应用下的菜单列表
     */
    private List<MenuItemDetail> menuList;

    /**
     * 展开的菜单id列表
     */
    private List<Long> openMenuIdList;

    public AppGroupDetail() {
    }

    public AppGroupDetail(Long appId, String appName, Long appIcon, String remark) {
        this.appId = appId;
        this.appName = appName;
        this.appIcon = appIcon;
        this.remark = remark;
    }
}
