package com.kevin.imageuploadclient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kevin.imageuploadclient.R;

public class HistoryActivity extends AppCompatActivity {


    private Button mBtnClearHistory;
    private ImageView mhistory_item1;
    private ImageView mhistory_item1_res;
    private ImageView mhistory_item2;
    private ImageView mhistory_item2_res;
    private ImageView mhistory_item3;
    private ImageView mhistory_item3_res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mBtnClearHistory = findViewById(R.id.clear_history_button);

        mhistory_item1 = findViewById(R.id.history_item1);
        mhistory_item1_res = findViewById(R.id.history_item1_res);
        mhistory_item2 = findViewById(R.id.history_item2);
        mhistory_item2_res = findViewById(R.id.history_item2_res);
        mhistory_item3 = findViewById(R.id.history_item3);
        mhistory_item3_res = findViewById(R.id.history_item3_res);

        mBtnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清楚历史记录
                mhistory_item1.setVisibility(ImageView.INVISIBLE);
                mhistory_item1_res.setVisibility(ImageView.INVISIBLE);
                mhistory_item2.setVisibility(ImageView.INVISIBLE);
                mhistory_item2_res.setVisibility(ImageView.INVISIBLE);
                mhistory_item3.setVisibility(ImageView.INVISIBLE);
                mhistory_item3_res.setVisibility(ImageView.INVISIBLE);
            }
        });

    }




}
