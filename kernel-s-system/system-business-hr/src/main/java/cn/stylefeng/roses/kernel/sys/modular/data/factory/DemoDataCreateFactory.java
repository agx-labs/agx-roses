package cn.stylefeng.roses.kernel.sys.modular.data.factory;

import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleTreeDict;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试数据的创建
 *
 * @author fengshuonan
 * @since 2024/6/5 17:00
 */
public class DemoDataCreateFactory {

    /**
     * 创建组织机构数据
     *
     * @author fengshuonan
     * @since 2024/6/5 17:01
     */
    public static List<SimpleTreeDict> createOrgData() {

        List<SimpleTreeDict> orgList = new ArrayList<>();

        // 创建几个顶级组织机构
        SimpleTreeDict bj = new SimpleTreeDict();
        bj.setId("1798280342488690689");
        bj.setName("北京总部");
        bj.setCode("bj");
        bj.setParentId("-1");
        orgList.add(bj);

        SimpleTreeDict sh = new SimpleTreeDict();
        sh.setId("1798280342488690696");
        sh.setName("上海分公司");
        sh.setCode("sh");
        sh.setParentId("-1");
        orgList.add(sh);

        SimpleTreeDict gz = new SimpleTreeDict();
        gz.setId("1798280342488690704");
        gz.setName("广州分公司");
        gz.setCode("gz");
        gz.setParentId("-1");
        orgList.add(gz);

        // 公司下建立几个部门
        ArrayList<SimpleTreeDict> bjChildren = new ArrayList<>();
        SimpleTreeDict bj1 = new SimpleTreeDict();
        bj1.setId("1798280342488690711");
        bj1.setName("北京总部1部门");
        bj1.setCode("bj1");
        bj1.setParentId(bj.getId());
        bjChildren.add(bj1);

        SimpleTreeDict bj2 = new SimpleTreeDict();
        bj2.setId("1798280342488690720");
        bj2.setName("北京总部2部门");
        bj2.setCode("bj2");
        bj2.setParentId(bj.getId());
        bjChildren.add(bj2);

        ArrayList<SimpleTreeDict> shChildren = new ArrayList<>();
        SimpleTreeDict sh1 = new SimpleTreeDict();
        sh1.setId("1798280342488690731");
        sh1.setName("上海分公司1部门");
        sh1.setCode("sh1");
        sh1.setParentId(sh.getId());
        shChildren.add(sh1);

        SimpleTreeDict sh2 = new SimpleTreeDict();
        sh2.setId("1798280342488690742");
        sh2.setName("上海分公司2部门");
        sh2.setCode("sh2");
        sh2.setParentId(sh.getId());
        shChildren.add(sh2);

        ArrayList<SimpleTreeDict> gzChildren = new ArrayList<>();
        SimpleTreeDict gz1 = new SimpleTreeDict();
        gz1.setId("1798280342488690756");
        gz1.setName("广州分公司1部门");
        gz1.setCode("gz1");
        gz1.setParentId(gz.getId());
        gzChildren.add(gz1);

        SimpleTreeDict gz2 = new SimpleTreeDict();
        gz2.setId("1798280342488690769");
        gz2.setName("广州分公司2部门");
        gz2.setCode("gz2");
        gz2.setParentId(gz.getId());
        gzChildren.add(gz2);

        bj.setChildren(bjChildren);
        sh.setChildren(shChildren);
        gz.setChildren(gzChildren);

        return orgList;
    }

    /**
     * 创建一些用户数据
     *
     * @author fengshuonan
     * @since 2024/6/5 17:05
     */
    public static List<SimpleTreeDict> createUserData() {
        List<SimpleTreeDict> userList = new ArrayList<>();

        // 创建几个用户
        SimpleTreeDict user1 = new SimpleTreeDict();
        user1.setId("1798280342488690773");
        user1.setName("张三");
        user1.setCode("zhangsan");
        userList.add(user1);

        SimpleTreeDict user2 = new SimpleTreeDict();
        user2.setId("1798280342488690776");
        user2.setName("李四");
        user2.setCode("lisi");
        userList.add(user2);

        SimpleTreeDict user3 = new SimpleTreeDict();
        user3.setId("1798280342488690781");
        user3.setName("王五");
        user3.setCode("wangwu");
        userList.add(user3);

        return userList;
    }

}
