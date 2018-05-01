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

public class ImitateActivity extends AppCompatActivity {

    private TextView tvImitateInput;
    private Button mBtnImitate;
    private ImageView ivImitateResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imitate);

        tvImitateInput = findViewById(R.id.imitate_input_text);
        mBtnImitate = findViewById(R.id.imitate_button);
        ivImitateResult = findViewById(R.id.imitate_result);

        mBtnImitate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context imitateInput = tvImitateInput.getContext();   //获取输入内容

                Toast.makeText(getApplicationContext(), "开始获取风格模仿结果……", Toast.LENGTH_SHORT).show();
                //开始获取风格模仿的结果

                //ivImitateResult.setImageResource();
            }
        });

    }
}
