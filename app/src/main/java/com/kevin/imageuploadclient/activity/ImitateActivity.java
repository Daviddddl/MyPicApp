package com.kevin.imageuploadclient.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.imageuploadclient.R;

public class ImitateActivity extends AppCompatActivity {

    private EditText tvImitateInput;
    private Button mBtnImitate;
    private ImageView ivImitateResult;
    private Spinner mStyle_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imitate);

        tvImitateInput = findViewById(R.id.imitate_input_text);
        mBtnImitate = findViewById(R.id.imitate_button);
        ivImitateResult = findViewById(R.id.imitate_result);
        mStyle_spinner = findViewById(R.id.style_spinner);

        mBtnImitate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String imitateRes = tvImitateInput.getText().toString();   //获取输入内容
                String style = mStyle_spinner.getSelectedItem().toString();

                //Toast.makeText(getApplicationContext(), "开始获取风格模仿结果……", Toast.LENGTH_SHORT).show();
                // 开始获取风格模仿的结果



            }
        });

    }
}
