package com.kevin.imageuploadclient.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
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
import com.kevin.imageuploadclient.util.UrlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.kevin.imageuploadclient.activity.UploadActivity.downLoad;
import static com.kevin.imageuploadclient.util.Constant.fileName;
import static com.kevin.imageuploadclient.util.Constant.remotePath;
import static com.kevin.imageuploadclient.util.Constant.urlRes;

public class ResultActivity extends AppCompatActivity {

    private ImageView mivPic;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private Spinner mSpinner;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Button mBtnGetResPic = findViewById(R.id.getResPicBtn);
        mivPic = findViewById(R.id.ivPic);

        final RadioButton mRBres1 = findViewById(R.id.resTxt1);
        final RadioButton mRBres2 = findViewById(R.id.resTxt2);
        final RadioButton mRBres3 = findViewById(R.id.resTxt3);

        mSpinner = findViewById(R.id.style_spinner);
        mEditText = findViewById(R.id.myNewRes);

        mRBres1.setText(Constant.res1);
        mRBres2.setText(Constant.res2);
        mRBres3.setText(Constant.res3);

        progressDialog = new ProgressDialog(this);//进度条
        progressDialog.setCancelable(false);

        mBtnGetResPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 下载结果图片
                progressDialog.setMessage("获取结果中...");

                // 上传选择的选项
                String resId = "";
                if (mRBres1.isSelected())
                    resId = Constant.res1;
                if (mRBres2.isSelected())
                    resId = Constant.res2;
                if (mRBres3.isSelected())
                    resId = Constant.res3;

                String style = mSpinner.getSelectedItem().toString();
                String input = mEditText.getText().toString();

                try {
                    new Thread(new UrlUtils("repair",style, resId, input, "12123")).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.e("!!!!!!!～～～",urlRes);
                showProcessDialog();
                Toast.makeText(getApplicationContext(), urlRes, Toast.LENGTH_SHORT).show();
                downLoad(remotePath,fileName+"01.jpg");
                loadImage(fileName+"01.jpg");


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
            mivPic.setImageBitmap(bmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
