package com.kevin.imageuploadclient.fragment;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.ButtomNaviActivity;
import com.kevin.imageuploadclient.activity.HelpActivity;
import com.kevin.imageuploadclient.activity.MainActivity;
import com.kevin.imageuploadclient.activity.ResultActivity;
import com.kevin.imageuploadclient.activity.UploadTestActivity;
import com.kevin.imageuploadclient.fragment.basic.PictureSelectFragment;
import com.kevin.imageuploadclient.util.Constant;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Result;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.kevin.imageuploadclient.util.Constant.fileName;
import static com.kevin.imageuploadclient.util.Constant.fileTxtName;
import static com.kevin.imageuploadclient.util.Constant.remotePath;
import static com.kevin.imageuploadclient.util.Constant.remoteTxtPath;

public class UploadFragment extends PictureSelectFragment {


    private ProgressDialog progressDialog;//进度条

    private Handler handler = new Handler();

    @Bind(R.id.uploadRepairPic)
    ImageView imageView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.uploadBtn)
    Button mBtnUpload;

    @Bind(R.id.getResBtn)
    Button mBtnGetRes;

    @Bind(R.id.action_back)
    ImageView mIvBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
        //progressDialog = new ProgressDialog(mContext);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_upload;
    }

    @Override
    public void initViews(View view) {
        initToolbar(toolbar);
    }

    @Override
    public void initEvents() {

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        mBtnGetRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //downLoad(remoteTxtPath,fileTxtName+".txt");

                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("获取结果中...");
                progressDialog.setCancelable(false);
                showProcessDialog();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                    }
                }, 8000);

                // 此处进行step1
                final String[] httpRes = {""};
                /**
                 * 获取请求
                 */
                //1.okhttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //2构造Request,
                //builder.get()代表的是get请求，url方法里面放的参数是一个网络地址
                Request.Builder builder = new Request.Builder();
                Request request = builder.get().url(Constant.BASE_URL+"/FunctionServlet?function=repair_step1&args1="+""+"&args2="+""+"&args3="+""+"&args4=" + "").build();

                //3将Request封装成call
                final Call call = okHttpClient.newCall(request);

                //4，执行call，这个方法是异步请求数据
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //失败调用
                        Log.e("UploadFragment", "onFailure: ");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        //成功调用
                        Log.e("UploadFragment", "onResponse: ");
                        //获取网络访问返回的字符串
                        assert response.body() != null;
                        final String resBody = response.body().string();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpRes[0] = resBody;

                                if (!httpRes[0].startsWith("error")) {
                                    String[] res = httpRes[0].split(";");
                                    if (res.length == 3) {
                                        Constant.res1 = res[0];
                                        Constant.res2 = res[1];
                                        Constant.res3 = res[2];
                                        startActivity(new Intent(getContext(), ResultActivity.class));
                                    }
                                }else {
                                    Log.e("服务器无法返回结果！", "服务器无法返回结果！");
                                }
                            }
                        }).start();
                    }
                });

                /**
                 * 请求结束
                 */

            }
        });

        // 设置裁剪图片结果监听
        setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(Uri fileUri, Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);

                String filePath = fileUri.getEncodedPath();
                final String imagePath = Uri.decode(filePath);

                progressDialog = new ProgressDialog(mContext);
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

    public static UploadFragment newInstance() {
        return new UploadFragment();
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
                Log.i(TAG, "图片地址 " + Constant.BASE_URL +result);
                Glide.with(mContext)
                        .load(Constant.BASE_URL + result)
                        .into(imageView);
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
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath))).addFormDataPart("wantedFilename","fix_origin.jpg");

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
        if (progressDialog.isShowing()) {
            return;
        }
        progressDialog.show();
    }

    //隐藏进度条
    private void hideProcessDialog() {
        if (!progressDialog.isShowing()) {
            return;
        }
        progressDialog.hide();
    }
}
