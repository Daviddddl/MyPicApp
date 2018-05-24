package com.kevin.imageuploadclient.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.util.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.transform.Result;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.kevin.imageuploadclient.activity.UploadActivity.downLoad;
import static com.kevin.imageuploadclient.util.Constant.fileName;
import static com.kevin.imageuploadclient.util.Constant.remotePath;

public class ResultActivity extends AppCompatActivity {

    private ImageView mivPic;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private Spinner mSpinner;
    private EditText mEditText;
    //private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Button mBtnGetResPic = findViewById(R.id.getResPicBtn);
        mivPic = findViewById(R.id.ivPic);

        final RadioButton mRBres1 = findViewById(R.id.resTxt1);
        final RadioButton mRBres2 = findViewById(R.id.resTxt2);
        final RadioButton mRBres3 = findViewById(R.id.resTxt3);
        final RadioButton mRBres4 = findViewById(R.id.resTxt4);

        mSpinner = findViewById(R.id.style_spinner);
        mEditText = findViewById(R.id.myNewRes);
        //mIvBack.findViewById(R.id.action_back);

        mRBres1.setText(Constant.res1);
        mRBres2.setText(Constant.res2);
        mRBres3.setText(Constant.res3);


        progressDialog = new ProgressDialog(this);//进度条
        progressDialog.setCancelable(false);

        /*mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/

        mBtnGetResPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 下载结果图片
                progressDialog.setMessage("获取结果中...");

                // 上传选择的选项
                String resId=null;
                if (mRBres1.isChecked())
                    resId = Constant.res1;
                if (mRBres2.isChecked())
                    resId = Constant.res2;
                if (mRBres3.isChecked())
                    resId = Constant.res3;
                if (mRBres4.isChecked())
                    resId = "4";

                String style = mSpinner.getSelectedItem().toString();
                String input = mEditText.getText().toString();
                resId = resId.equals("4") ? input : resId;

                /**
                 * 获取请求
                 */
                //1.okhttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //2构造Request,
                //builder.get()代表的是get请求，url方法里面放的参数是一个网络地址
                Request.Builder builder = new Request.Builder();
                Integer styleId = style.equals("行书") ? 2 : 1;
                Request request = builder.get().url(Constant.BASE_URL+"/FunctionServlet?function=repair_step2&args1="+styleId+"&args2="+resId+"&args3="+input+"&args4=useless").build();

                //3将Request封装成call
                Call call = okHttpClient.newCall(request);

                //4，执行call，这个方法是异步请求数据
                call.enqueue(new Callback() {
                                 @Override
                                 public void onFailure(Call call, IOException e) {
                                     //失败调用
                                     Log.e("ResultActivity", "onFailure: ");
                                 }

                                 @Override
                                 public void onResponse(Call call, final Response response) throws IOException {
                                     //成功调用
                                     Log.e("ResultActivity", "onResponse: ");
                                     //获取网络访问返回的字符串
                                     assert response.body() != null;
                                     final String resBody = response.body().string();
                                     runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             //  此处进行step2 下载
                                             if (resBody.startsWith("success")) {
                                                 downLoad(remotePath + fileName + "_repair.png", fileName + "_repair.png");
                                                 loadImage(fileName + "_repair.png");
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
                }, 5000);

            }
        });

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



    private void loadImage(String filename) {
        String path = Environment.getExternalStorageDirectory().toString() + "/caffeRes";
        try {
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(new File(path, filename)));
            mivPic.setImageBitmap(bmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
