package com.example.xy.dentist.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.example.xy.dentist.R;

/**
 * Created by lenovo on 2018/1/25.
 */

public class NoticationUtils {
   private static final int NO_1 = 0x1;

    public static void show1(Context context){
                 NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                 builder.setSmallIcon(R.mipmap.ic_launcher);
                 builder.setContentTitle("新消息");
                 builder.setContentText("你有一条新的消息");
                 //设置点击通知跳转页面后，通知消失
                 builder.setAutoCancel(true);
                 /*Intent intent = new Intent(this,Main2Activity.class);
                 PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);*/
//                 builder.setContentIntent(pi);
                 Notification notification = builder.build();
                 NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                 notificationManager.notify(NO_1,notification);
             }
}
