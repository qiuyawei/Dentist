package com.example.xy.dentist.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.example.xy.dentist.BuildConfig;
import com.example.xy.dentist.R;

import java.io.File;

/**
 * Created by lenovo on 2017/12/25.
 */

public class DownLoadUtils {
    public static void installApk(Context appContext, File apkFile){
        Intent installIntent = new Intent();
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setAction(Intent.ACTION_VIEW);

        Uri apkFileUri;
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkFileUri = FileProvider.getUriForFile(appContext, "com.example.xy.dentist.fileprovider", apkFile);
        } else {
            apkFileUri = Uri.fromFile(apkFile);
        }
        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        installIntent.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
        try {
            appContext.startActivity(installIntent);
        } catch (ActivityNotFoundException e) {
        }
    }







}
