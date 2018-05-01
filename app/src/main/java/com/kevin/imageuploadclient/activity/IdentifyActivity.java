package com.kevin.imageuploadclient.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.imageuploadclient.R;

public class IdentifyActivity extends AppCompatActivity {

    protected Button mBtnIdentify;
    protected Button mBtnPic1Camera;
    protected Button mBtnPic1Album;
    protected Button mBtnPic2Camera;
    protected Button mBtnPic2Album;
    protected ImageView ivPic1;
    protected ImageView ivPic2;
    protected TextView tvIdentifyRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);

        mBtnIdentify = findViewById(R.id.identify_button);
        mBtnPic1Album = findViewById(R.id.pic1_album);
        mBtnPic2Album = findViewById(R.id.pic2_album);
        mBtnPic1Camera = findViewById(R.id.pic1_camera);
        mBtnPic2Camera = findViewById(R.id.pic2_camera);
        ivPic1 = findViewById(R.id.pic1);
        ivPic2 = findViewById(R.id.pic2);
        tvIdentifyRes = findViewById(R.id.identify_result);


        //各个button的点击方法
        buttonActions();

    }

    private void buttonActions() {
        mBtnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "开始笔记鉴定……", Toast.LENGTH_SHORT).show();
                //开始获取笔迹鉴定的结果

            }
        });

        mBtnPic1Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "图1拍照……", Toast.LENGTH_SHORT).show();
                //开始获取笔迹鉴定的结果

            }
        });

        mBtnPic1Album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "图1相册……", Toast.LENGTH_SHORT).show();
                //开始获取笔迹鉴定的结果

            }
        });

        mBtnPic2Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "图2拍照……", Toast.LENGTH_SHORT).show();
                //开始获取笔迹鉴定的结果

            }
        });

        mBtnPic2Album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "图2相册……", Toast.LENGTH_SHORT).show();
                //开始获取笔迹鉴定的结果

            }
        });

    }
}
