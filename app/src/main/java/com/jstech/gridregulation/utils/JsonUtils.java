package com.jstech.gridregulation.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by hesm on 2018/10/14.
 */

public class JsonUtils {

    public static String getCityJson(String filename, Context context) {
        StringBuilder builder = new StringBuilder();
        try {

            AssetManager m = context.getAssets();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    m.open(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

}
