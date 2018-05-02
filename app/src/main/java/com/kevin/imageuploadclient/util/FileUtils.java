package com.kevin.imageuploadclient.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @param
 * @author Davidddl
 * @package com.example.didonglin.testapplication.util
 * @fileName FileUtils
 * @date 2018/4/18
 * @return
 * @description
 */
public class FileUtils {
    private String path = Environment.getExternalStorageDirectory().toString() + "/caffeRes";

    public FileUtils() {
        File file = new File(path);
        /**
         *如果文件夹不存在就创建
         */
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 创建一个文件
     * @param FileName 文件名
     * @return
     */
    public File createFile(String FileName) {
        return new File(path, FileName);
    }


    public byte[] img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}