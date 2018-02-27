package com.example.xy.dentist.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.WindowManager;


import com.example.xy.dentist.R;
import com.example.xy.dentist.ui.doctor.activity.MainActivity;
import com.example.xy.dentist.ui.patientside.activity.PatientActivity;
import com.example.xy.dentist.utils.DialogUtils;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.example.xy.dentist.widget.FloatView;
import com.jaydenxiao.common.commonutils.ToastUitl;

import org.json.JSONException;
import org.json.JSONObject;

import static android.media.AudioManager.FLAG_SHOW_UI;

/**
 * Created by lenovo on 2017/12/18.
 */

public class DilalogActivity extends Activity {

    private String title, content, message;

    private Vibrator mVibrator;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置点亮屏幕
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        );
        setContentView(R.layout.activity_dialog);
        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        message = getIntent().getStringExtra("message");
        insertDummyContactWrapper();
        /**
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         */
    }


    private void insertDummyContactWrapper() {
        AudioManager audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_RAISE, AudioManager.FLAG_ALLOW_RINGER_MODES);

        mVibrator.vibrate(new long[]{1000, 1000, 0, 0}, -1);
        DialogUtils.createDialog(DilalogActivity.this, title, content);
    }






    @Override
    protected void onStop() {
        super.onStop();
        mVibrator.cancel();
    }
}
