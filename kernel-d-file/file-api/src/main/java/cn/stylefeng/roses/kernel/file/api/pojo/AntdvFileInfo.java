package cn.stylefeng.roses.kernel.file.api.pojo;

import lombok.Data;

/**
 * 前端antdv组件需要的文件信息封装
 *
 * @author fengshuonan
 * @since 2022/1/1 22:14
 */
@Data
public class AntdvFileInfo {

    /**
     * 文件唯一标识
     */
    private String uid;

    /**
     * 文件名
     */
    private String name;

    /**
     * 状态有：uploading done error removed
     */
    private String status;

    /**
     * 服务端响应内容
     */
    private String response = "{\"status\": \"success\"}";

    /**
     * 下载链接额外的 HTML 属性
     */
    private String linkProps;

    /**
     * XMLHttpRequest Header
     */
    private String xhr;

    /**
     * 文件访问的url
     */
    private String thumbUrl;

    /**
     * 文件大小：xx kb, xx mb
     * <p>
     * 【2024年4月10日新增】
     */
    private String sizeInfo;

    /**
     * 文件后缀
     * <p>
     * 【2024年4月10日新增】
     */
    private String fileSuffix;

}
