package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;

import java.util.List;
import java.util.Set;

/**
 * 单独编写用户和组织机构关系的Api
 *
 * @author fengshuonan
 * @since 2023/6/18 23:14
 */
public interface SysUserOrgServiceApi {

    /**
     * 获取用户的主要任职信息
     * <p>
     * 返回一条结果，只返回主部门的信息
     *
     * @author fengshuonan
     * @since 2023/6/11 21:07
     */
    UserOrgDTO getUserMainOrgInfo(Long userId);

    /**
     * 获取用户绑定的组织机构列表，主要任职部门和次要任职部门都返回
     *
     * @param userId      用户id
     * @param getTotalOrg 是否获取用户所属的所有组织机构，true-获取所有，false-只获取启用的部门
     * @author fengshuonan
     * @since 2023/6/11 21:08
     */
    List<UserOrgDTO> getUserOrgList(Long userId, boolean getTotalOrg);

    /**
     * 获取用户所有的所属组织机构的id信息
     *
     * @param userId      用户id
     * @param getTotalOrg 是否获取用户所属的所有组织机构，true-获取所有，false-只获取启用的部门
     * @author fengshuonan
     * @since 2024-01-18 17:48
     */
    List<Long> getUserOrgIdList(Long userId, boolean getTotalOrg);

    /**
     * 获取某个机构下的所有用户id集合
     *
     * @param orgId             组织机构id
     * @param containSubOrgFlag 是否包含指定机构的子集机构，true-包含子集，false-不包含
     * @author fengshuonan
     * @since 2023/6/11 21:46
     */
    List<Long> getOrgUserIdList(Long orgId, Boolean containSubOrgFlag);

    /**
     * 判断用户是否有指定组织机构的权限
     *
     * @param orgId  组织机构id
     * @param userId 用户id
     * @return true-用户有所属该机构的权限，false-用户不属于该机构
     * @author fengshuonan
     * @since 2023/6/21 16:11
     */
    boolean validateUserOrgAuth(Long orgId, Long userId);

    /**
     * 获取某个机构某个职务下的所有用户id列表
     *
     * @author fengshuonan
     * @since 2023/7/15 22:24
     */
    List<Long> getPositionUserList(Long orgId, Long positionId);

    /**
     * 获取用户在指定机构下的职务
     *
     * @author fengshuonan
     * @since 2023/7/15 23:04
     */
    Long getUserOrgPositionId(Long userId, Long orgId);

    /**
     * 查询某些组织机构下，有哪些用户
     *
     * @param orgIdList 组织机构id集合
     * @author fengshuonan
     * @since 2023/7/19 0:18
     */
    Set<Long> getOrgUserIdList(Set<Long> orgIdList);

    /**
     * 更新其他公司的启用禁用状态，排除掉某个公司
     *
     * @param userId       用户id
     * @param excludeOrgId 排除的公司id，传空则是更新所有
     * @param statusFlag   状态标识，1-启用，2-禁用
     * @author fengshuonan
     * @since 2024-01-18 17:20
     */
    void updateOtherOrgStatus(Long userId, Long excludeOrgId, Integer statusFlag);

}
