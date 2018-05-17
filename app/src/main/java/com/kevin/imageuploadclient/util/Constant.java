package com.kevin.imageuploadclient.util;

public class Constant {

    /** 服务器基地址 */
    public static final String BASE_URL = "http://192.168.20.133:8080";

    public static boolean IS_ROOT = false;

    public static String res1 = "你";
    public static String res2 = "好";
    public static String res3 = "啊";

    //static String remotePath = Constant.BASE_URL + "/ImageUploadServer_war/files/images/caffeRes.png";
    public static String remotePath = Constant.BASE_URL + "/files/images/";  // 本地测试
    public static String remoteTxtPath = Constant.BASE_URL + "/files/images/caffeTxtRes.txt";  // 本地测试
    public static String fileName = "caffeRes";
    public static String fileTxtName = "caffeTxtRes";
}
