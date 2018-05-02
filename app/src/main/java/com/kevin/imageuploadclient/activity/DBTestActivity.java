package com.kevin.imageuploadclient.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kevin.imageuploadclient.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTestActivity extends AppCompatActivity {

    private static String url = "jdbc:mysql://123.206.70.190:3306/wechat?useUnicode=true&characterEncoding=utf8";
    private static String userName = "root";
    private static String password = "753421";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
        new Thread(runnable).start();
    }

    Handler myHandler=new Handler(){
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Bundle data=new Bundle();
            data=msg.getData();

            Log.e("TAG","id:"+data.get("id").toString());
            TextView tv= (TextView) findViewById(R.id.jdbc);
        }
    };



    Runnable runnable=new Runnable() {
        private Connection con = null;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //引用代码此处需要修改，address为数据IP，Port为端口号，DBName为数据名称，UserName为数据库登录账户，Password为数据库登录密码
                con = DriverManager.getConnection(url, userName, password);

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                testConnection(con);    //测试数据库连接
            } catch (java.sql.SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void testConnection(Connection con1) throws java.sql.SQLException {
            try {
                String sql = "select * from smart";        //查询表名为“smart”的所有内容
                Statement stmt = con1.createStatement();        //创建Statement
                ResultSet rs = stmt.executeQuery(sql);          //ResultSet类似Cursor

                //<code>ResultSet</code>最初指向第一行
                Bundle bundle=new Bundle();
                while (rs.next()) {
                    bundle.clear();
                    bundle.putString("id",rs.getString("id"));
                    bundle.putString("type",rs.getString("type"));
                    bundle.putString("command",rs.getString("command"));
                    bundle.putString("operate_time",rs.getString("operate_time"));
                    bundle.putString("opertor",rs.getString("opertor"));

                    Message msg=new Message();
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                }

                rs.close();
                stmt.close();
            } catch (SQLException e) {

            } finally {
                if (con1 != null)
                    try {
                        con1.close();
                    } catch (SQLException e) {}
            }
        }
    };

}
