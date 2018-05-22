package com.kevin.imageuploadclient.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.util.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.kevin.imageuploadclient.activity.UploadActivity.downLoad;
import static com.kevin.imageuploadclient.util.Constant.fileName;
import static com.kevin.imageuploadclient.util.Constant.remotePath;

public class ImitateActivity extends AppCompatActivity {

    private EditText tvImitateInput;
    private Button mBtnImitate;
    private ImageView ivImitateResult;
    private Spinner mStyle_spinner;
    private ProgressDialog progressDialog;//进度条
    private ImageView mIvBack;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imitate);
        progressDialog = new ProgressDialog(this);//进度条
        progressDialog.setCancelable(false);
        tvImitateInput = findViewById(R.id.imitate_input_text);
        mBtnImitate = findViewById(R.id.imitate_button);
        ivImitateResult = findViewById(R.id.imitate_result);
        mStyle_spinner = findViewById(R.id.style_spinner);
        mIvBack.findViewById(R.id.action_back);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImitateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mBtnImitate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 下载结果图片
                progressDialog.setMessage("获取结果中...");

                String imitateInput = tvImitateInput.getText().toString();   //获取输入内容
                String style = mStyle_spinner.getSelectedItem().toString();

                //Toast.makeText(getApplicationContext(), "开始获取风格模仿结果……", Toast.LENGTH_SHORT).show();
                // 开始获取风格模仿的结果

                /**
                 * 获取请求
                 */
                //1.okhttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //2构造Request,
                //builder.get()代表的是get请求，url方法里面放的参数是一个网络地址
                Request.Builder builder = new Request.Builder();
                Integer styleRes = style.equals("楷书")?1:2;
                Request request = builder.get().url(Constant.BASE_URL+"/FunctionServlet?function=imitate&args1="+styleRes+"&args2="+imitateInput+"&args3="+""+"&args4=useless").build();

                //3将Request封装成call
                Call call = okHttpClient.newCall(request);

                //4，执行call，这个方法是异步请求数据
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //失败调用
                        Log.e("ImitateActivity", "onFailure: ");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        //成功调用
                        Log.e("ImitateActivity", "onResponse: ");
                        //获取网络访问返回的字符串
                        assert response.body() != null;
                        final String resBody = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  此处进行下载
                                Log.e("下载图片！",resBody);
                                if (resBody.startsWith("success")) {
                                    downLoad(remotePath + fileName + "_imitate.png", fileName + "_imitate.png");
                                    loadImage(fileName + "_imitate.png");
                                }
                            }
                        });
                    }
                });

                /**
                 * 请求结束
                 */
                showProcessDialog();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                    }
                }, 3000);
            }
        });

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

    private void loadImage(String filename) {
        String path = Environment.getExternalStorageDirectory().toString() + "/caffeRes";
        try {
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(new File(path, filename)));
            ivImitateResult.setImageBitmap(bmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
