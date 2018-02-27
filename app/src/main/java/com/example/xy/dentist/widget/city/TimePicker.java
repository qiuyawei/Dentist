package com.example.xy.dentist.widget.city;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.xy.dentist.R;

import java.util.ArrayList;


/**
 * container 3 wheelView implement timePicker
 */
public class TimePicker extends LinearLayout {
    public WheelView mWheelYear;
    public WheelView mWheelMonth;
    public WheelView mWheelDay;

    private int year;
    private int month;

    private WheelView.OnSelectListener yearListener;
    private WheelView.OnSelectListener monthListener;
    private WheelView.OnSelectListener dayListener;

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.view_time_picker, this);
        mWheelYear = (WheelView) findViewById(R.id.year);
        mWheelMonth = (WheelView) findViewById(R.id.month);
        mWheelDay = (WheelView) findViewById(R.id.day);

        mWheelYear.setData(getYearData());
        mWheelMonth.setData(getMonthData());
        mWheelDay.setData(getDayData());


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


        mWheelMonth.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                Message message = new Message();
                message.obj = id;
                message.what = 2;
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
                    mWheelDay.resetData(getResetDay(id + 1, mWheelMonth.getSelected() + 1));
                    mWheelMonth.setDefault(0);
                    mWheelDay.setDefault(0);
                    break;
                case 2:
                    mWheelDay.resetData(getResetDay(mWheelYear.getSelected() + 1, id + 1));
                    mWheelDay.setDefault(0);
                    break;
            }
        }
    };



    public void setListener(WheelView.OnSelectListener yearListener,WheelView.OnSelectListener monthListener,WheelView.OnSelectListener dayListener){
        this.yearListener = yearListener;
        this.monthListener = monthListener;
        this.dayListener = dayListener;

        if (dayListener != null){
            mWheelDay.setOnSelectListener(dayListener);
            mWheelMonth.setOnSelectListener(monthListener);
            mWheelYear.setOnSelectListener(yearListener);
        }
    }

    public void setDefault(int year,int month,int day){
        mWheelYear.setDefault(year + "");
        mWheelMonth.setDefault(month + "");
        mWheelDay.setDefault(day + "");
    }

    public String getYear(){
        return mWheelYear.getSelectedText();
    }
    public String getMonth(){
        return mWheelMonth.getSelectedText();
    }
    public int getMonthId(){
        return mWheelMonth.getSelected();
    }
    public String getDay(){
        return mWheelDay.getSelectedText();
    }

    private ArrayList<String> getYearData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 2030; i > 1950; i--) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 01; i <= 12; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private ArrayList<String> getDayData() {
        //ignore condition


        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private ArrayList<String> getResetDay(int year,int month){
        int day = getDay(year,month);

        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= day; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }
    /**
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 28 : 29;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }
}
