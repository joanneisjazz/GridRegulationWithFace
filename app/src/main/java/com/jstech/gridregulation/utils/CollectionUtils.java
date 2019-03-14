package com.jstech.gridregulation.utils;

import java.util.Collection;

/**
 * Created by Administrator on 2017/8/2.
 */

public class CollectionUtils {

    /**
     * 判断集合是否为null或者0个元素
     *
     * @param c
     * @return
     */
    public static boolean isNullOrEmpty(Collection c) {
        if (null == c || c.isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * 判断集合是否为null或者0个元素
     *
     * @param str
     * @return
     */
    public static boolean isStrNullOrEmpty(String str) {
        if (null == str || str.isEmpty()) {
            return true;
        }
        return false;
    }
}
