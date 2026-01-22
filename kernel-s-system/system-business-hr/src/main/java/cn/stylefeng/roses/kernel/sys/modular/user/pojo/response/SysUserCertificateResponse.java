package cn.stylefeng.roses.kernel.sys.modular.user.pojo.response;

import cn.stylefeng.roses.kernel.dict.api.format.DictFormatProcess;
import cn.stylefeng.roses.kernel.file.api.format.FileInfoFormatProcess;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户证书返回值封装
 *
 * @author fengshuonan
 * @since 2023/11/09 22:44
 */
@Data
public class SysUserCertificateResponse {

    /**
     * 证书类型，取字典id
     */
    @ChineseDescription("证书类型，取字典id")
    @SimpleFieldFormat(processClass = DictFormatProcess.class)
    private Long certificateType;

    /**
     * 证书编号
     */
    @ChineseDescription("证书编号")
    private String certificateNo;

    /**
     * 发证机构名称
     */
    @ChineseDescription("发证机构名称")
    private String issuingAuthority;

    /**
     * 证书发证日期
     */
    @ChineseDescription("证书发证日期")
    private Date dateIssued;

    /**
     * 证书到期日期，如果为空，则为长期
     */
    @ChineseDescription("证书到期日期，如果为空，则为长期")
    private Date dateExpires;

    /**
     * 附件id
     */
    @ChineseDescription("附件id")
    @SimpleFieldFormat(processClass = FileInfoFormatProcess.class)
    private Long attachmentId;

}
