package com.kevin.imageuploadclient.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.ResultActivity;
import com.kevin.imageuploadclient.fragment.basic.PictureSelectFragment;
import com.kevin.imageuploadclient.util.Constant;
import com.youth.banner.loader.ImageLoader;

import java.io.File;

import butterknife.Bind;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.kevin.imageuploadclient.util.Constant.fileTxtName;
import static com.kevin.imageuploadclient.util.Constant.remoteTxtPath;

public class ManufactureFragment extends PictureSelectFragment {


    private ProgressDialog progressDialog;//进度条

    private Handler handler = new Handler();

    @Bind(R.id.ivManuPic)
    ImageView mIvManuPic;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.getManuResBtn1)
    Button mBtnSingleWordGet;

    @Bind(R.id.getManuResBtn2)
    Button mBtnTextGet;

    @Bind(R.id.manuRes)
    TextView mTvManuRes;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
        progressDialog = new ProgressDialog(mContext);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_manufacture;
    }

    @Override
    public void initViews(View view) {
        initToolbar(toolbar);
    }

    @Override
    public void initEvents() {


        mIvManuPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        mBtnSingleWordGet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("识别中...");
                progressDialog.setCancelable(false);
                showProcessDialog();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                    }
                }, 3000);
                mTvManuRes.setText("人 --- 入 --- 亻--- 八 --- 上");
            }
        });

        mBtnTextGet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("识别中...");
                progressDialog.setCancelable(false);
                showProcessDialog();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                    }
                }, 3000);
                mTvManuRes.setText("我 --- 的 --- 中 --- 国 --- 梦");
            }
        });

        // 设置裁剪图片结果监听
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(Uri fileUri, Bitmap bitmap) {
                mIvManuPic.setImageBitmap(bitmap);

                String filePath = fileUri.getEncodedPath();
                final String imagePath = Uri.decode(filePath);

                progressDialog.setMessage("上传中...");
                progressDialog.setCancelable(false);
                showProcessDialog();

                uploadImage(imagePath);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                    }
                }, 3000);

            }
        });


    }

    public static ManufactureFragment newInstance() {
        return new ManufactureFragment();
    }


    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    /**
     * 上传图片
     * @param imagePath
     */
    private void uploadImage(String imagePath) {
        new ManufactureFragment.NetworkTask().execute(imagePath);    }

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
                        .into(mIvManuPic);
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


    //显示进度条
    private void showProcessDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    //隐藏进度条
    private void hideProcessDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
}
