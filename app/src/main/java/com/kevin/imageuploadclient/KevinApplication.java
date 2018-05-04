package com.kevin.imageuploadclient;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kevin.imageuploadclient.activity.basic.ActivityStack;


public class KevinApplication extends Application {

    public static final String TAG = KevinApplication.class.getSimpleName();
    private RequestQueue myRequestQueue;
    protected static KevinApplication kevinApplication = null;
    /** 上下文 */
    protected Context mContext = null;
    /** Activity 栈 */
    public ActivityStack mActivityStack = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // 由于Application类本身已经单例，所以直接按以下处理即可
        kevinApplication = this;
        mContext = getApplicationContext();     // 获取上下文
        mActivityStack = new ActivityStack();   // 初始化Activity 栈

        initConfiguration();
    }

    /**
     * 获取当前类实例对象
     * @return
     */
    public static synchronized KevinApplication getInstance(){
        return kevinApplication;
    }

    private void initConfiguration() {

    }


    //获取requestqueue
    public RequestQueue getMyRequestQueue() {
        if (myRequestQueue == null) {
            myRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return myRequestQueue;
    }

    //向requestqueue中添加自定义tag的request
    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getMyRequestQueue().add(request);
    }

    //向requestqueue中添加带默认tag的request
    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getMyRequestQueue().add(request);
    }

    //删除所有tag的request
    public void cancelPendingRequests(Object tag) {
        if (myRequestQueue != null) {
            myRequestQueue.cancelAll(tag);
        }
    }
}
