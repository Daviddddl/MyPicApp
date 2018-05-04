package com.kevin.imageuploadclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.helper.SQLiteManager;
import com.kevin.imageuploadclient.activity.helper.SessionManager;
import com.kevin.imageuploadclient.util.Constant;

public class UserDetailActivity extends AppCompatActivity {
    private TextView nameView;
    private TextView emailView;
    private TextView upView;
    private Button logoutButton;
    private Button upButton;
    private Button mainButton;
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
        upView = findViewById(R.id.upView);
        logoutButton = findViewById(R.id.logoutButton);
        upButton = findViewById(R.id.upButton);
        mainButton = findViewById(R.id.mainButton);

        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }
        nameView.setText(sqLiteManager.getUserDetails().get("name"));
        emailView.setText(sqLiteManager.getUserDetails().get("email"));
        upView.setText(Constant.IS_ROOT ? "恭喜您有权限！" : "抱歉您没有权限！");
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upAuthority();
            }
        });
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }

    private void backToMain(){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    private void upAuthority(){
        Constant.IS_ROOT = true;
        Toast.makeText(getApplicationContext(), "提升权限成功！您现在可以使用全部功能了！", Toast.LENGTH_SHORT).show();
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
