package com.tzj.webview.delegate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

import com.tzj.webview.utils.UtilWebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * 上传文件,不放在 BaseLibActivity 中将无效
 */
public class UploadDelegate extends DefWebChromeClient {
    private ValueCallback<Uri[]> fielCallback;
    private ValueCallback<Uri> oldFielCallback;

    private static final int CHOISE_PIC = 3541;
    private static final int CHOISE_PIC_OLD = 3542;

    private File file;

    @Override
    public boolean onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            Uri uri = null;
            @Override
            public void run() {
                if (file != null) {//相机
                    try {
                        //todo 这里图片可能会挺大的，而且会出现两张一样的
                        String temp = MediaStore.Images.Media.insertImage(mWebView.getContext().getContentResolver(), file.getPath(), file.getName(), "temp");
                        uri = Uri.parse(temp);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (Activity.RESULT_OK == resultCode && data != null) {//选择
                    uri = data.getData();
                }
                file = null;
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (CHOISE_PIC == requestCode) {
                            if (null != fielCallback) {
                                fielCallback.onReceiveValue(new Uri[]{uri});
                                fielCallback = null;
                            }
                        } else if (CHOISE_PIC_OLD == requestCode) {
                            if (null != oldFielCallback) {
                                oldFielCallback.onReceiveValue(uri);
                                oldFielCallback = null;
                            }
                        }
                    }
                });
            }
        });
        return CHOISE_PIC_OLD == requestCode || CHOISE_PIC == requestCode;
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> denides = new ArrayList<>();//拒绝  弹窗告知没权限
        List<String> rememberDenides = new ArrayList<>();//拒绝并且记住   跳到设置界面
        Context context = mWebView.getContext();
        if (!(context instanceof Activity)) {
            return false;
        }
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//拒绝了
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mWebView.getContext(), permissions[i])) {
                    denides.add(permissions[i]);
                } else {//记住了
                    rememberDenides.add(permissions[i]);
                }
            }
        }
        if (rememberDenides.size() > 0) {
            Toast.makeText(mWebView.getContext(), "无法获取到权限", Toast.LENGTH_LONG).show();
        } else if (denides.size() > 0) {
            Toast.makeText(mWebView.getContext(), "你取消了权限请求", Toast.LENGTH_LONG).show();
        } else {
            if (fielCallback != null) {
                onShowFileChooser(mWebView, fielCallback, null);
                return true;
            }
            if (oldFielCallback != null) {
                MyopenFileChooser(oldFielCallback, null, null);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, final FileChooserParams fileChooserParams) {
        fielCallback = filePathCallback;
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                Context context = mWebView.getContext();
                Activity activity = (Activity) context;
                if (context instanceof Activity) {
                    if (mWebView.requestPermissions(activity,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        openChoice(activity, CHOISE_PIC);
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "没有权限", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return true;
    }

    public boolean MyopenFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        oldFielCallback = uploadFile;
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                Context context = mWebView.getContext();
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (mWebView.requestPermissions(activity,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        openChoice(activity, CHOISE_PIC_OLD);
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "没有权限", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return true;
    }

    private void openChoice(Activity act, int code) {
        //创建ChooserIntent
        Intent intent = new Intent(Intent.ACTION_CHOOSER);
        //创建相机Intent
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        file = new File(file.getAbsolutePath(), System.currentTimeMillis() + ".jpg");
        Uri uri = UtilWebView.parUri(act, file);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //创建相册Intent
//        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
//        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        Intent albumIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        albumIntent.addCategory(Intent.CATEGORY_OPENABLE);
        albumIntent.setType("image/*");
        //将相机Intent以数组形式放入Intent.EXTRA_INITIAL_INTENTS
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});
        //将相册Intent放入Intent.EXTRA_INTENT
        intent.putExtra(Intent.EXTRA_INTENT, albumIntent);
//        intent.setType("image/*");

        act.startActivityForResult(intent, code);
    }


}
