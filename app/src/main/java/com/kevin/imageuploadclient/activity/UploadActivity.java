package com.kevin.imageuploadclient.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.util.Constant;
import com.kevin.imageuploadclient.util.FileUtils;
import com.kevin.imageuploadclient.util.LQRPhotoSelectUtils;
import com.kevin.imageuploadclient.util.UploadUtil;
import com.kevin.imageuploadclient.view.SelectPicturePopupWindow;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import butterknife.Bind;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class UploadActivity extends AppCompatActivity {

    // 根据部署环境设置文件路径
    //static String remotePath = Constant.BASE_URL + "/ImageUploadServer_war/files/images/caffeRes.png";
    static String remotePath = Constant.BASE_URL + "/files/images/caffeRes.jpg";  // 本地测试
    static String fileName = "caffeRes01.jpg";

    private Button mBtnTakePhoto;
    private Button mBtnSelectPhoto;
    private Button mBtnGetRes;
    private Button mBtnGetHistory;
    private TextView mTvPath;
    private TextView mTvUri;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private ImageView mIvPic;

    //private AVLoadingIndicatorView avi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mBtnTakePhoto = findViewById(R.id.btnTakePhoto);
        mBtnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        mBtnGetRes = findViewById(R.id.getResButton);
        mBtnGetHistory = findViewById(R.id.getHistoryButton);
        mTvPath = findViewById(R.id.tvPath);
        mTvUri = findViewById(R.id.tvUri);
        mIvPic = findViewById(R.id.ivPic);

        init();
        initListener();

    }

    private void init() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) throws SQLException {
                // 4、当拍照或从图库选取图片成功后回调
                String absolutePath = outputFile.getAbsolutePath();
                String tvUri = outputUri.toString();

                Log.e("filepath",absolutePath);
                Log.e("uri",tvUri);

                mTvPath.setText(absolutePath);
                mTvUri.setText(tvUri);
                Glide.with(UploadActivity.this).load(outputUri).into(mIvPic);

                String fileKey = "file";
                //String requestUrl = Constant.BASE_URL+"/ImageUploadServer_war/uploadimage";  // 测试war包部署
                String requestUrl = Constant.BASE_URL+"/uploadimage";  // 测试本地部署
                UploadUtil.getInstance().uploadFile(absolutePath,fileKey,requestUrl,null);

                Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_SHORT).show();

                //下面考虑再将上传的图片信息保存到远程数据库中，以便获取历史操作列表



            }
        }, false);
        //true裁剪，false不裁剪

        //        mLqrPhotoSelectUtils.setAuthorities("com.lqr.lqrnativepicselect.fileprovider");
        //        mLqrPhotoSelectUtils.setImgPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
    }

    private void initListener() {
        mBtnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3、调用拍照方法
                PermissionGen.with(UploadActivity.this)
                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        ).request();
            }
        });

        mBtnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3、调用从图库选取图片方法
                PermissionGen.needPermission(UploadActivity.this,
                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );
            }
        });

        mBtnGetRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3、下载结果
                // 需要配置路径

                //startAnim(avi);  // 开启加载动画

                downLoad(remotePath,fileName);
                loadImage(fileName);

                //stopAnim(avi);  // 关闭加载动画
            }
        });

        mBtnGetHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 4、历史记录
                startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
            }
        });
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        try {
            mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showDialog() {
        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-汉字修复大师-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                }
                assert intent != null;
                intent.setData(Uri.parse("package:" + UploadActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }


    //设备API大于6.0时，主动申请权限
    private void requestPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            }
        }
    }


    /**
     * 从服务器下载文件
     * @param path 下载文件的地址
     * @param FileName 文件名字
     */
    public static void downLoad(final String path, final String FileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
                            FileUtils fileUtils = new FileUtils();
                            fileOutputStream = new FileOutputStream(fileUtils.createFile(FileName));//指定文件保存路径，代码看下一步
                            byte[] buf = new byte[1024];
                            int ch;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void loadImage(String filename) {
        String path = Environment.getExternalStorageDirectory().toString() + "/caffeRes";
        try {
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(new File(path, filename)));
            mIvPic.setImageBitmap(bmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /*void startAnim(AVLoadingIndicatorView avi){
        //avi.show();
        avi.smoothToShow();
    }

    void stopAnim(AVLoadingIndicatorView avi){
        //avi.hide();
        avi.smoothToHide();
    }*/

}
