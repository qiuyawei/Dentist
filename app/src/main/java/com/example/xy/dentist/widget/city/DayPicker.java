package com.example.xy.dentist.widget.city;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.xy.dentist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by XY on 2017/10/13.
 */
public class DayPicker extends LinearLayout {
    public WheelView mWheelYear;

    private int year;
    private int month;

    private WheelView.OnSelectListener yearListener;

    public DayPicker(Context context) {
        this(context, null);
    }

    public DayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.view_day_picker, this);
        mWheelYear = (WheelView) findViewById(R.id.year);

        mWheelYear.setData(getYearData());


        mWheelYear.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                Message message = new Message();
                message.obj = id;
                message.what = 1;
                mHandler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });





    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int id = (int) msg.obj;
            switch (msg.what){
                case 1:

                    break;
                case 2:
                  /*  mWheelDay.resetData(getResetDay(mWheelYear.getSelected() + 1, id + 1));
                    mWheelDay.setDefault(0);*/
                    break;
            }
        }
    };



    public void setListener(WheelView.OnSelectListener yearListener,WheelView.OnSelectListener monthListener,WheelView.OnSelectListener dayListener){
        this.yearListener = yearListener;

        if (dayListener != null){
            mWheelYear.setOnSelectListener(yearListener);
        }
    }

    public void setDefault(int year,int month,int day){
        mWheelYear.setDefault(year + "");
    }

    public String getYear(){
        return mWheelYear.getSelectedText();
    }

    private ArrayList<String> getYearData() {
        ArrayList<String> list = new ArrayList<>();
        list.add(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return list;
    }

    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<>();
        for(int i=1;i<=12;i++){
            list.add(i+"");
        }
        return list;
    }
    private ArrayList<String> getDayData() {
        ArrayList<String> list = new ArrayList<>();
        list.add(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return list;
    }
   
}
