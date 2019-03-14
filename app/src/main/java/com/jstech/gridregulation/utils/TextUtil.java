package com.jstech.gridregulation.utils;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Base64;

import com.jstech.gridregulation.ConstantValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextUtil {
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[01256789]\\d{8}|17[0678]\\d{8}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }


    public static boolean isEmpty(Object o) {
        if (null == o) {
            return true;
        }
        String str = o.toString();
        if ("".equals(str) || str.length() <= 0) {
            return true;
        }
        return false;
    }

    public static String date() {
        Time time = new Time();
        time.setToNow();
        int mMonth = time.month + 1;
        return time.year + "年" + mMonth + "月" + time.monthDay + "日";
    }

    public static String date2() {
        Time time = new Time();
        time.setToNow();
        int mMonth = time.month + 1;
        return time.year + "-" + mMonth + "-" + time.monthDay;
    }

    public static Date getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new Date();
    }


    public static String getRegulateResult(Object code) {
        if (null == code) {
            return "";
        }
        if (code.toString().equals(ConstantValue.RESULT_BASIC_QUALIFIED)) {
            return "基本合格";
        } else if (code.toString().equals(ConstantValue.RESULT_QUALIFIED)) {
            return "合格";
        } else if (code.toString().equals(ConstantValue.RESULT_UNQUALIFIED)) {
            return "不合格";
        } else {
            return "未检查";
        }
    }


    public static String encrypt(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);//Android自身的BASE64方法
    }
}
