package com.example.xy.dentist.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.ui.doctor.activity.MainActivity;
import com.example.xy.dentist.ui.patientside.activity.PatientActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by lenovo on 2017/12/18.
 */

public class DialogUtils {
    public static Dialog dialog;

    public static void createDialog(final Context context, final String title,final String content) {
//        AlertDialog.Builder builder=new AlertDialog.Builder(context,R.style.ActionSheetDialogStyle);
        View view = View.inflate(context, R.layout.dialog_common, null);
//        builder.setView(view);
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);

        TextView tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        TextView tv_cancell = (TextView) view.findViewById(R.id.tv_dialog_cancell);
        TextView tv_sure = (TextView) view.findViewById(R.id.tv_dialog_sure);

        tv_dialog_title.setText(content);
        final Dialog finalDialog = dialog;
        tv_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.cancel();
                ( (Activity)context).finish();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.showShort(context,"兑换成功");
                dialog.cancel();
                //点击通知取消红点
                SharedPreferencesUtil.saveBooleanData(context, "SetIfHasRedDot", false);
                SharedPreferencesUtil.saveBooleanData(context, "MessageIfHasRedDot", false);
                String t = "1";

                try {

                    JSONObject jsonObject = new JSONObject(title);
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
            Log.i("TAG",t+"");

                Intent mIntent = new Intent();
//                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
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
                ( (Activity)context).finish();
            }
        });
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ( (Activity)context).finish();
            }
        });
        dialog.show();
    }
}
