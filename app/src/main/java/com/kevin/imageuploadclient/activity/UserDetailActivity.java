package com.kevin.imageuploadclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.helper.SQLiteManager;
import com.kevin.imageuploadclient.activity.helper.SessionManager;

public class UserDetailActivity extends AppCompatActivity {
    private TextView nameView;
    private TextView emailView;
    private Button logoutButton;
    private SQLiteManager sqLiteManager;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        sqLiteManager = new SQLiteManager(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        nameView = findViewById(R.id.nameView);
        emailView = findViewById(R.id.emailView);
        logoutButton = findViewById(R.id.logoutButton);

        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }
        nameView.setText(sqLiteManager.getUserDetails().get("name"));
        emailView.setText(sqLiteManager.getUserDetails().get("email"));
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    //用户登出操作
    private void logoutUser() {
        //设置登陆状态为false
        sessionManager.setLogin(false);
        //数据库中删除用户信息
        sqLiteManager.deleteUsers();
        //跳转到登陆页面
        Intent intent = new Intent(UserDetailActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
