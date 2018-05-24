package com.kevin.imageuploadclient.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.jkb.slidemenu.SlideMenuAction;
import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.fragment.basic.PictureSelectFragment;

public class HelpActivity extends AppCompatActivity {

    private SlideMenuAction slideMenuAction;
    BottomNavigationView buttomNavigationView;
    //private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        slideMenuAction = findViewById(R.id.mainSlideMenu);
        slideMenuAction.setSlideMode(SlideMenuAction.SLIDE_MODE_LEFT_RIGHT);
        slideMenuAction.setContentToggle(true);

        buttomNavigationView = findViewById(R.id.bottom_navigation);

        /*mIvBack.findViewById(R.id.action_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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

        buttomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    protected void functionMain(){
        //Toast.makeText(this.getContext(), "点击了设置", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    protected void functionHelp(){
        //Toast.makeText(this.getContext(), "点击了帮助", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), HelpActivity.class));
    }

    protected void functionAccount(){
        //Toast.makeText(this.getContext(), "点击了我的", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), UserDetailActivity.class));
    }
}
