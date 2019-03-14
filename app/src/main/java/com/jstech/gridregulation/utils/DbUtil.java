package com.jstech.gridregulation.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class DbUtil {

    /**
     * 拷贝数据库到sd卡	 * 	 * @deprecated <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     */
    public static void copyDataBaseToSD(Context context, String copyName) {
//找到文件的路径  /data/data/包名/databases/数据库名称
        File dbFile = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/" + context.getPackageName() + "/databases/grid_db");
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //文件复制到sd卡中
            fis = new FileInputStream(dbFile);
            fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + copyName);// + "/copy.db"
            int len = 0;
            byte[] buffer = new byte[2048];
            while (-1 != (len = fis.read(buffer))) {
                fos.write(buffer, 0, len);
            }
            fos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭数据流
            try {
                if (fos != null) fos.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


}
