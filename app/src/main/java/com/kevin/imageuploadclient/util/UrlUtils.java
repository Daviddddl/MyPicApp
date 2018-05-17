package com.kevin.imageuploadclient.util;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.kevin.imageuploadclient.util.Constant.BASE_URL;
import static com.kevin.imageuploadclient.util.Constant.urlRes;

public class UrlUtils implements Runnable{

    private static String addr = BASE_URL + "/FunctionServlet";

    private String function;
    private String args1, args2, args3, args4;

    public UrlUtils(String function, String args1, String args2, String args3, String args4){
        this.function = function;
        this.args1 = args1;
        this.args2 = args2;
        this.args3 = args3;
        this.args4 = args4;
    }
    /* 在android中用get方式向服务器提交请求 */

    public String get() throws Exception {

        String params = "function=" + function + "&args1=" + args1+ "&args2=" + args2+ "&args3=" + args3+ "&args4=" + args4;
        //将参数拼接在URl地址后面
        URL url = new URL(addr + "?" + params);
        //通过url地址打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时时间
        conn.setConnectTimeout(3000);
        //设置请求方式
        conn.setRequestMethod("GET");
        urlRes = conn.getResponseMessage();
        Log.e("RES===========", conn.getResponseMessage());
        if (conn.getResponseCode() == 200)
            return conn.getResponseMessage();
        return "error!!";
    }

    @Override
    public void run() {
        try {
            get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
