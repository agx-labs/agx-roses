package cn.stylefeng.roses.kernel.sys.api.pojo.menu;

import lombok.Data;

import java.util.List;

/**
 * 项目业务封装
 *
 * @author fengshuonan
 * @since 2024-01-10 17:16
 */
@Data
public class ProjectBusinessDTO {

    /**
     * 项目id或业务id
     */
    private Long id;

    /**
     * 项目名称或业务名称
     */
    private String name;

    /**
     * 项目下的业务信息
     */
    private List<ProjectBusinessDTO> businessList;

}
