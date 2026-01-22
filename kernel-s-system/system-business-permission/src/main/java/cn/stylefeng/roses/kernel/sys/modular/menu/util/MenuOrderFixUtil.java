package cn.stylefeng.roses.kernel.sys.modular.menu.util;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * 菜单排序进行修复
 * <p>
 * 对菜单进行再次排序，因为有的菜单是101，有的菜单是10101，需要将位数小的补0，再次排序
 *
 * @author fengshuonan
 * @since 2023/9/15 17:14
 */
public class MenuOrderFixUtil {

    /**
     * 修复菜单排序
     *
     * @author fengshuonan
     * @since 2023/9/15 17:15
     */
    public static void fixOrder(List<SysMenu> sysMenus) {
        if (ObjectUtil.isEmpty(sysMenus)) {
            return;
        }

        // 找到最大的数字位数
        int maxDigitCount = 0;
        for (SysMenu sysMenu : sysMenus) {
            BigDecimal menuSort = sysMenu.getMenuSort();
            if (menuSort == null) {
                continue;
            }
            int digitCount = getPointLeftDigitCount(menuSort);
            if (digitCount > maxDigitCount) {
                maxDigitCount = digitCount;
            }
        }

        for (SysMenu sysMenu : sysMenus) {
            BigDecimal menuSort = sysMenu.getMenuSort();
            if (menuSort == null) {
                menuSort = new BigDecimal(0);
            }
            int digitCount = getPointLeftDigitCount(menuSort);
            if (digitCount < maxDigitCount) {
                menuSort = menuSort.multiply(BigDecimal.valueOf(Math.pow(10, maxDigitCount - digitCount)));
            }
            sysMenu.setMenuSort(menuSort);
        }

        sysMenus.sort(Comparator.comparing(SysMenu::getMenuSort));
    }

    /**
     * 获取一个数字的小数点左边的位数
     *
     * @author fengshuonan
     * @since 2023/9/15 17:19
     */
    public static int getPointLeftDigitCount(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        String bigDecimalStr = bigDecimal.toString();
        int decimalIndex = bigDecimalStr.indexOf('.');
        return decimalIndex >= 0 ? decimalIndex : bigDecimalStr.length();
    }

}
