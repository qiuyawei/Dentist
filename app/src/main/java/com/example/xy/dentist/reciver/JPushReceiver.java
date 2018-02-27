package com.example.xy.dentist.reciver;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.xy.dentist.R;
import com.example.xy.dentist.ui.DilalogActivity;
import com.example.xy.dentist.ui.doctor.activity.MainActivity;
import com.example.xy.dentist.ui.patientside.activity.PatientActivity;
import com.example.xy.dentist.utils.DialogUtils;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.NoticationUtils;
import com.example.xy.dentist.utils.NotificationsUtils;
import com.example.xy.dentist.utils.ScreenUtils;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

import static cn.jpush.android.api.JPushInterface.ACTION_NOTIFICATION_RECEIVED;

/**
 * Created by Administrator on 2016/6/8 0008.
 */
public class JPushReceiver extends BroadcastReceiver {
    /**
     * 自定义接收器
     * <p>
     * 如果不定义这个 Receiver，则：
     * 1) 默认用户会打开主界面
     * 2) 接收不到自定义消息
     */
    private static final String TAG = "JPush";
    private String extra = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.i(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);

        } else if (ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            接收到推送通知 弹出通知
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

//            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//            int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtils.print("extras", extras);

            LogUtils.print("content", content);
//            LogUtils.print("message", message);

//            Log.i("TAG", "[MyReceiver] 接收到推送下来的通知" + "=" + bundle.toString());

//            extra=bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Log.i(TAG,extra);
//            收到推送  显示红点
            SharedPreferencesUtil.saveBooleanData(context, "SetIfHasRedDot", true);
            SharedPreferencesUtil.saveBooleanData(context, "MessageIfHasRedDot", true);

            Intent newIntent = new Intent("Has_New_Message");
            context.sendBroadcast(newIntent);
            newIntent.setClass(context, DilalogActivity.class);
            newIntent.putExtra("title", extras);
            newIntent.putExtra("content", content);
            newIntent.putExtra("message", message);

            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(newIntent);



        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            //点击通知取消红点
            SharedPreferencesUtil.saveBooleanData(context, "SetIfHasRedDot", false);
            SharedPreferencesUtil.saveBooleanData(context, "MessageIfHasRedDot", false);
            String t = "1";

            try {

                JSONObject jsonObject = new JSONObject(String.valueOf(bundle.getString(JPushInterface.EXTRA_EXTRA)));
                String[] ss = jsonObject.getString("type").split(",");
//                if(ss!=null&&ss.length>0)
//                Log.i(TAG,ss.length+"");

                if (ss != null && ss.length > 1) {
                    t = ss[1];
//                    Log.i(TAG,ss.length+"");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Log.i(TAG,t+"33333333333");
//            消息推送：
//            1、医生端-工作台-待确认-设置时间：设置好时间后推送消息给用户端（尊敬的用户您好，您的预约已生效，就诊时间为xx:xx-xx:xx）
//            2、医生端-工作台-进行中-完成-填写病历日志：填写并提交后推送消息给用户（尊敬的用户，您的就诊已完成）
//            3、用户端-即时预约：提交后推送消息给医生端（尊敬的用户，您有一条新的即时预约信息，请查看！）
//            4、在线预约推送
//            打开通知  跳转页面

            Intent mIntent = new Intent();
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            Log.i("TAG", "t"+t);

            if (t.contains("2")) {
                //在线预约
                mIntent.setClass(context, MainActivity.class);
                mIntent.putExtra("position", 0);
                mIntent.putExtra("childPos", 1);
                SharedPreferencesUtil.saveIntData(context, "JpushchildPos", 1);

            } else if (content.contains("已生效")) {
                mIntent.setClass(context, PatientActivity.class);
                mIntent.putExtra("position", 1);
                mIntent.putExtra("childPos", 0);
                SharedPreferencesUtil.saveIntData(context, "JpushchildPos", 0);

            } else if (content.contains("已完成")) {
                mIntent.setClass(context, PatientActivity.class);
                mIntent.putExtra("position", 1);
                mIntent.putExtra("childPos", 1);
                SharedPreferencesUtil.saveIntData(context, "JpushchildPos", 1);

            } else if (t.contains("1")) {
                //及时预约
                mIntent.setClass(context, MainActivity.class);
                mIntent.putExtra("position", 0);
                mIntent.putExtra("childPos", 0);
                SharedPreferencesUtil.saveIntData(context, "JpushchildPos", 0);

            }
            SharedPreferencesUtil.saveBooleanData(context, "Jpush", true);

            mIntent.putExtra("Jpush", "Jpush");
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.setAction("Open_Notification");
            context.sendBroadcast(mIntent);
            context.startActivity(mIntent);


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.i(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.i(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
//        Log.i(TAG, "sb.toString=" + sb.toString());
        return sb.toString();
    }

    //send msg to MainActivity
//        private void processCustomMessage(Context context, Bundle bundle) {
//            if (MainActivity.isForeground) {
//                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//                Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//                msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//                if (!ExampleUtil.isEmpty(extras)) {
//                    try {
//                        JSONObject extraJson = new JSONObject(extras);
//                        if (null != extraJson && extraJson.length() > 0) {
//                            msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                        }
//                    } catch (JSONException e) {
//
//                    }
//
//                }
//                context.sendBroadcast(msgIntent);
//            }
//        }


}
