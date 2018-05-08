package com.kevin.imageuploadclient.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kevin.imageuploadclient.KevinApplication;
import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.helper.AppConfig;
import com.kevin.imageuploadclient.activity.helper.SQLiteManager;
import com.kevin.imageuploadclient.activity.helper.SessionManager;
import com.kevin.imageuploadclient.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登陆页面
 */
public class LoginActivity extends AppCompatActivity {

 //   private static final String TAG = Reg
    private Button loginButton;
    private Button linkToRegisterButton;
    private EditText emailInput;
    private EditText passwordInput;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLiteManager sqLiteManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);//email输入框
        passwordInput = findViewById(R.id.passwordInput);//密码输入框
        loginButton = findViewById(R.id.loginButton);//登陆按钮
        linkToRegisterButton = findViewById(R.id.linkToRegisterScreenButton);//跳转到注册页面按钮
        progressDialog = new ProgressDialog(this);//进度条
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(getApplicationContext());//登陆状态管理
        sqLiteManager = new SQLiteManager(getApplicationContext());//数据库
        //如果用户没有登陆,那么跳转到登陆页面
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, UserDetailActivity.class);
            startActivity(intent);
            finish();
        }
        loginButton.setOnClickListener(new loginOnClickListener());
        linkToRegisterButton.setOnClickListener(new linkToRegisterOnClickListener());

    }

    //登陆按钮点击操作
    private class loginOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            //判断输入是否为空
            if (email.trim().length() > 0 && password.trim().length() > 0) {
                checkLogin(email, password);
            }else {
                Toast.makeText(getApplicationContext(), " 请输入邮箱或密码", Toast.LENGTH_LONG).show();
            }
        }
    }

    //跳转到注册页面按钮点击操作
    private class linkToRegisterOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //跳转到注册页面
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //用户登陆操作
    private void checkLogin(final String email, final String password) {
        String tag_string_req = "req_login";
        progressDialog.setMessage("登陆中...");
        showDialog();

        //发起登陆请求
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN_Test, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                hideDialog();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(s);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        //设置登陆状态为true
                        sessionManager.setLogin(true);
                        JSONObject user = jsonObject.getJSONObject("user");
                        String uuid = user.getString("uuid");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        //在数据库中添加用户信息
                        sqLiteManager.addUser(name, email,uuid,created_at);
                        //跳转到用户信息页面
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Constant.IS_ROOT = false;
                        startActivity(intent);
                        finish();
                    } else {
                        //输出错误信息
                        String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
       //         System.out.println(volleyError.getMessage());
                Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            //提交的post参数
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };
        KevinApplication kevinApplication = KevinApplication.getInstance();
        if (kevinApplication != null)
            kevinApplication.addToRequestQueue(strReq, tag_string_req);
        else
            Toast.makeText(getApplicationContext(), "ohhh, 现在服务器出错了！暂时无法登陆！", Toast.LENGTH_SHORT).show();    }

    //显示进度条
    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    //隐藏进度条
    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private boolean isEmailValid(String email) {
        if(!email.contains("@")) {
            return false;
        }else {
            return true;
        }
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }else {
            return true;
        }
    }
}