package com.kevin.imageuploadclient.fragment.basic;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kevin.crop.UCrop;
import com.kevin.imageuploadclient.R;
import com.kevin.imageuploadclient.activity.CropActivity;
import com.kevin.imageuploadclient.activity.HelpActivity;
import com.kevin.imageuploadclient.activity.IdentifyActivity;
import com.kevin.imageuploadclient.activity.ImitateActivity;
import com.kevin.imageuploadclient.activity.MainActivity;
import com.kevin.imageuploadclient.activity.ManuFactureActivity;
import com.kevin.imageuploadclient.activity.SettingsActivity;
import com.kevin.imageuploadclient.activity.UploadActivity;
import com.kevin.imageuploadclient.activity.UploadTestActivity;
import com.kevin.imageuploadclient.activity.UserDetailActivity;
import com.kevin.imageuploadclient.util.Constant;
import com.kevin.imageuploadclient.util.FileUtils;
import com.kevin.imageuploadclient.view.SelectPicturePopupWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.kevin.imageuploadclient.util.Constant.fileTxtName;

public abstract class PictureSelectFragment extends BaseFragment implements SelectPicturePopupWindow.OnSelectedListener {

    private static final int GALLERY_REQUEST_CODE = 0;    // 相册选图标记
    private static final int CAMERA_REQUEST_CODE = 1;    // 相机拍照标记
    // 拍照临时图片
    private String mTempPhotoPath;
    // 剪切后图像文件
    private Uri mDestinationUri;

    /**
     * 选择提示 PopupWindow
     */
    private SelectPicturePopupWindow mSelectPicturePopupWindow;
    /**
     * 图片选择的监听回调
     */
    private OnPictureSelectedListener mOnPictureSelectedListener;


    protected void function1(){
        if (Constant.IS_ROOT)
            startActivity(new Intent(this.getContext(),UploadTestActivity.class));
        else
            Toast.makeText(this.getContext(), "抱歉，您没有权限！", Toast.LENGTH_SHORT).show();
    }

    protected void function2(ImageView imageView){
        /*Toast.makeText(this.getContext(), "开始下载服务器结果……", Toast.LENGTH_SHORT).show();

        downLoad(Constant.BASE_URL+"files/images/3/10/123.jpeg","caffeRes01.jpeg");
        loadImage("caffeRes01.jpeg",imageView);*/
        startActivity(new Intent(this.getContext(), IdentifyActivity.class));
    }

    protected void function3(){
        //Toast.makeText(this.getContext(), "点击了function3", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this.getContext(),ImitateActivity.class));

    }

    protected void function4(){
        //Toast.makeText(this.getContext(), "点击了function4", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this.getContext(),ManuFactureActivity.class));
    }

    protected void functionSetting(){
        //Toast.makeText(this.getContext(), "点击了设置", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this.getContext(), SettingsActivity.class));
    }

    protected void functionMain(){
        //Toast.makeText(this.getContext(), "点击了设置", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this.getContext(), MainActivity.class));
    }

    protected void functionHelp(){
        //Toast.makeText(this.getContext(), "点击了帮助", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this.getContext(), HelpActivity.class));
    }

    protected void functionAccount(){
        //Toast.makeText(this.getContext(), "点击了我的", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this.getContext(), UserDetailActivity.class));
    }
    /**
     * 剪切图片
     */
    protected void selectPicture() {
        mSelectPicturePopupWindow.showPopupWindow(mActivity);
        //Toast.makeText(this.getContext(), "将图片上传至服务器……", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDestinationUri = Uri.fromFile(new File(activity.getCacheDir(), "cropImage.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        mSelectPicturePopupWindow = new SelectPicturePopupWindow(mContext);
        mSelectPicturePopupWindow.setOnSelectedListener(this);
    }

    @Override
    public void OnSelected(View v, int position) {
        switch (position) {
            case 0:
                // "拍照"按钮被点击了
                takePhoto();
                break;
            case 1:
                // "从相册选择"按钮被点击了
                pickFromGallery();
                break;
            case 2:
                // "取消"按钮被点击了
                mSelectPicturePopupWindow.dismissPopupWindow();
                break;
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            mSelectPicturePopupWindow.dismissPopupWindow();
            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //下面这句指定调用相机拍照后的照片存储的路径
            /*takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
            startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);*/

            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, new File(mTempPhotoPath).getAbsolutePath());
            Uri uri = this.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
        }
    }

    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            mSelectPicturePopupWindow.dismissPopupWindow();
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mActivity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:   // 调用相机拍照
                    File temp = new File(mTempPhotoPath);
                    startCropActivity(Uri.fromFile(temp));
                    break;
                case GALLERY_REQUEST_CODE:  // 直接从相册获取
                    startCropActivity(data.getData());
                    break;
                case UCrop.REQUEST_CROP:    // 裁剪图片结果
                    handleCropResult(data);
                    break;
                case UCrop.RESULT_ERROR:    // 裁剪图片错误
                    handleCropError(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity(Uri uri) {
        UCrop.of(uri, mDestinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(512, 512)
                .withTargetActivity(CropActivity.class)
                .start(mActivity, this);
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri && null != mOnPictureSelectedListener) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), resultUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mOnPictureSelectedListener.onPictureSelected(resultUri, bitmap);
        } else {
            Toast.makeText(mContext, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(mContext, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "无法剪切选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除拍照临时文件
     */
    private void deleteTempPhotoFile() {
        File tempFile = new File(mTempPhotoPath);
        if (tempFile.exists() && tempFile.isFile()) {
            tempFile.delete();
        }
    }

    /**
     * 设置图片选择的回调监听
     *
     * @param l
     */
    public void setOnPictureSelectedListener(OnPictureSelectedListener l) {
        this.mOnPictureSelectedListener = l;
    }

    /**
     * 图片选择的回调接口
     */
    public interface OnPictureSelectedListener {
        /**
         * 图片选择的监听回调
         *
         * @param fileUri
         * @param bitmap
         */
        void onPictureSelected(Uri fileUri, Bitmap bitmap);
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
                    //Log.e("connCode",""+con.getResponseCode());
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        //Log.e("datatest",con.getInputStream().read(new byte[1024])+"");
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
                            FileUtils fileUtils = new FileUtils();
                            fileOutputStream = new FileOutputStream(fileUtils.createFile(FileName));//指定文件保存路径，代码看下一步
                            byte[] buf = new byte[1024];
                            int ch;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                                //Log.e("123123","456456");
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


    public void loadImage(String filename, ImageView imageView) {
        String path = Environment.getExternalStorageDirectory().toString() + "/caffeRes";
        try {
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(new File(path, filename)));
            imageView.setImageBitmap(bmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String[] loadTxt(){
        String path = Environment.getExternalStorageDirectory().toString() + "/caffeRes";
        try {
            String res = FileUtils.readFile(path+"/"+fileTxtName+".txt");
            return res.split(";");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}