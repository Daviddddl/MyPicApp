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
import android.os.Message;
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
import com.kevin.imageuploadclient.activity.MainActivity;
import com.kevin.imageuploadclient.activity.ResultActivity;
import com.kevin.imageuploadclient.fragment.basic.PictureSelectFragment;
import com.kevin.imageuploadclient.util.Constant;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Callback;
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

    private Handler msgHandler = new Handler(){
        public void handleMessage(Message msg)
        {
            if (msg.what == Constant.GETMSG)
                mTvManuRes.setText((String) msg.obj);
        }
    };

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

    @Bind(R.id.action_back)
    ImageView mIvBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
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

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

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
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("识别中...");
                progressDialog.setCancelable(false);
                showProcessDialog();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                    }
                }, 3000);

                //开始单字识别

                /**
                 * 获取请求
                 */
                //1.okhttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //2构造Request,
                //builder.get()代表的是get请求，url方法里面放的参数是一个网络地址
                Request.Builder builder = new Request.Builder();
                Request request = builder.get().url(Constant.BASE_URL+"/FunctionServlet?function=recoSingle&args1="+""+"&args2="+""+"&args3="+""+"&args4=" + "").build();

                //3将Request封装成call
                final Call call = okHttpClient.newCall(request);

                //4，执行call，这个方法是异步请求数据
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //失败调用
                        Message msg = handler.obtainMessage();
                        msg.what = Constant.GETMSG;
                        msg.obj = "抱歉！服务器出错！";
                        msgHandler.sendMessage(msg);
                        Log.e("ManufactureFragment", "onFailure: ");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        //成功调用
                        Log.e("ManufactureFragment", "onResponse: ");
                        //获取网络访问返回的字符串
                        assert response.body() != null;
                        final String resBody = response.body().string();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (!resBody.startsWith("error")) {
                                    Message msg = handler.obtainMessage();
                                    msg.what = Constant.GETMSG;
                                    msg.obj = resBody;
                                    msgHandler.sendMessage(msg);
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

        mBtnTextGet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("识别中...");
                progressDialog.setCancelable(false);
                showProcessDialog();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                    }
                }, 12000);

                // 开始文本识别
                /**
                 * 获取请求
                 */
                //1.okhttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //2构造Request,
                //builder.get()代表的是get请求，url方法里面放的参数是一个网络地址
                Request.Builder builder = new Request.Builder();
                Request request = builder.get().url(Constant.BASE_URL+"/FunctionServlet?function=recoMore&args1="+""+"&args2="+""+"&args3="+""+"&args4=" + "").build();

                //3将Request封装成call
                final Call call = okHttpClient.newCall(request);

                //4，执行call，这个方法是异步请求数据
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //失败调用
                        Message msg = handler.obtainMessage();
                        msg.what = Constant.GETMSG;
                        msg.obj = "抱歉！服务器出错！";
                        msgHandler.sendMessage(msg);
                        Log.e("ManufactureFragment", "onFailure: ");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        //成功调用
                        Log.e("ManufactureFragment", "onResponse: ");
                        //获取网络访问返回的字符串
                        assert response.body() != null;
                        final String resBody = response.body().string();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (!resBody.startsWith("error")) {
                                    Message msg = handler.obtainMessage();
                                    msg.what = Constant.GETMSG;
                                    msg.obj = resBody;
                                    msgHandler.sendMessage(msg);
                                }else {
                                    Message msg = handler.obtainMessage();
                                    msg.what = Constant.GETMSG;
                                    msg.obj = "识别不出结果了！";
                                    msgHandler.sendMessage(msg);
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
                mIvManuPic.setImageBitmap(bitmap);

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
                }, 12000);

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
        new ManufactureFragment.NetworkTask().execute(imagePath);
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
                        .into(mIvManuPic);
            }
        }
    }

    private String doPost(String imagePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath))).addFormDataPart("wantedFilename","manufacture.jpg");

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
        if (progressDialog.isShowing())
            return;
        progressDialog.show();
    }

    //隐藏进度条
    private void hideProcessDialog() {
        if (!progressDialog.isShowing())
            return;
        progressDialog.hide();
    }
}
