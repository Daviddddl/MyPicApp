package com.kevin.imageuploadclient.fragment;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.fragment.basic.PictureSelectFragment;
import com.kevin.imageuploadclient.util.Constant;
import com.kevin.imageuploadclient.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainFragment extends PictureSelectFragment implements OnBannerListener {

    private ArrayList<String> list_path;
    private ArrayList<String> list_title;

    /** Toolbar */
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.main_frag_func1)
    ImageView mPictureFunc1;

    @Bind(R.id.main_frag_func2)
    ImageView mPictureFunc2;

    @Bind(R.id.main_frag_func3)
    ImageView mPictureFunc3;

    @Bind(R.id.main_frag_func4)
    ImageView mPictureFunc4;

    @Bind(R.id.main_frag_func5)
    ImageView mPictureFunc5;

    @Bind(R.id.bottom_navigation)
    BottomNavigationView buttomNavigationView;

    @Bind(R.id.banner)
    Banner mBanner;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initViews(View view) {
        initToolbar(toolbar);
    }

    @Override
    public void initEvents() {

        mPictureFunc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function1();
            }
        });

        mPictureFunc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                function2(mPictureFunc2);
            }
        });

        mPictureFunc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectPicture();
                function3();
            }
        });

        mPictureFunc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function4();
                selectPicture();
            }
        });
        // 设置裁剪图片结果监听
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(Uri fileUri, Bitmap bitmap) {
                mPictureFunc4.setImageBitmap(bitmap);

                String filePath = fileUri.getEncodedPath();
                final String imagePath = Uri.decode(filePath);

                uploadImage(imagePath);

            }
        });

        buttomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        */

        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add(Constant.BASE_URL+"/files/banner/main1.png");
        list_path.add(Constant.BASE_URL+"/files/banner/main2.png");
        list_path.add(Constant.BASE_URL+"/files/banner/main3.png");
        list_path.add(Constant.BASE_URL+"/files/banner/main4.png");
        list_title.add("残损修复");
        list_title.add("笔迹鉴定");
        list_title.add("风格模仿");
        list_title.add("手写识别");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        mBanner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        mBanner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        mBanner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        mBanner.setBannerTitles(list_title);
        //设置轮播间隔时间
        mBanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        mBanner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第"+position+"张轮播图");
        Toast.makeText(this.getContext(),"你点了第"+position+"张轮播图",Toast.LENGTH_SHORT).show();
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_help: {
                    functionHelp();
                    return true;
                }
                case R.id.action_main: {
                    functionMain();
                    return true;
                }
                case R.id.action_mine: {
                    functionAccount();
                    return true;
                }
            }
            return false;
        }

    };

    /**
     * 上传图片
     * @param imagePath
     */
    private void uploadImage(String imagePath) {
        new NetworkTask().execute(imagePath);
    }

    /**
     * 访问网络AsyncTask,访问网络在子线程进行并返回主线程通知访问的结果
     */
    class NetworkTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return doPost(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(!"error".equals(result)) {
                Log.i(TAG, "图片地址 " + Constant.BASE_URL + result);
                Glide.with(mContext)
                        .load(Constant.BASE_URL + result)
                        .into(mPictureFunc5);
            }
        }
    }

    private String doPost(String imagePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 这里演示添加用户ID
//        builder.addFormDataPart("userId", "20160519142605");
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(Constant.BASE_URL + "/uploadimage")
                .post(requestBody)
                .build();

        Log.d(TAG, "请求地址 " + Constant.BASE_URL + "/uploadimage");
        try{
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d(TAG, "响应码 " + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d(TAG, "响应体 " + resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
