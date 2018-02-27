package com.example.xy.dentist.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.widget.city.TimePicker;
import com.example.xy.dentist.widget.city.YearPicker;

import java.util.Calendar;

public class PopUtil {


    public  interface  onSelectFinishListener{
        public void onSelectFinish(String type);
    }
    public interface onSelectAreaFinishListener{
        public void onSelectAreaFinish(String areaid);
    }
    public static Dialog dialog;
    private static PopupWindow popup;

    public static PopupWindow  showView(Context context,View view, final Window window,View parent){
        popup=getPopup(context,view,window);
        popup.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //释放资源 因为应用的context对象  会导致内存泄露
                popup = null;
                backgroundAlpha(window, 1f);
            }
        });

        return popup;

    }

    //认证
    public static View getView(final Activity activity,String tile1,String subti,final Context context,final onSelectFinishListener listener) {
        View  views = LayoutInflater.from(activity).inflate(R.layout.publishauth_layout, null, false);
        TextView tv_yes = (TextView) views.findViewById(R.id.tv_yes);
        TextView tile = (TextView) views.findViewById(R.id.tile1);
        TextView subtile = (TextView) views.findViewById(R.id.tiole2);
        View vv = views.findViewById(R.id.vv);
        tile.setText(tile1);
        if(!TextUtils.isEmpty(subti)){

            subtile.setText(subti);
        }
        TextView tv_no = (TextView) views.findViewById(R.id.tv_no);
        if(tile1.equals("即将拨打电话")){
            subtile.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            subtile.setTextColor(Color.parseColor("#4B4B4B"));
        }

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                listener.onSelectFinish("yes");

            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                listener.onSelectFinish("no");


            }
        });



        return views;
    }

    // 第二次确认  就是拨打电话后再调接口
    public static View getViewSure(final Activity activity,String tile1,String subti,final Context context,final onSelectFinishListener listener) {
        View  views = LayoutInflater.from(activity).inflate(R.layout.publishauth_layout, null, false);
        TextView tv_yes = (TextView) views.findViewById(R.id.tv_yes);
        TextView tile = (TextView) views.findViewById(R.id.tile1);
        TextView subtile = (TextView) views.findViewById(R.id.tiole2);
        TextView tv_no = (TextView) views.findViewById(R.id.tv_no);

        View vv = views.findViewById(R.id.vv);
        tile.setText(tile1);
        if(!TextUtils.isEmpty(subti)){

            subtile.setText(subti);
        }
        /*if(!TextUtils.isEmpty(subti)){

            subtile.setText(subti);
        }
        if(tile1.equals("即将拨打电话")){
            subtile.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            subtile.setTextColor(Color.parseColor("#4B4B4B"));
        }*/

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    popup.dismiss();
                listener.onSelectFinish("yes");

            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    popup.dismiss();
                listener.onSelectFinish("no");


            }
        });



        return views;
    }


    public static void backgroundAlpha(Window window, float alpha) {
        LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.setAttributes(params);
    }


    private static PopupWindow getPopup(Context context, View contentView, Window window) {
        popup = new PopupWindow(context);
        popup.setAnimationStyle(R.style.PopupAnimation);
        popup.setHeight(LayoutParams.WRAP_CONTENT);
        popup.setWidth(LayoutParams.MATCH_PARENT);
        popup.setContentView(contentView);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setFocusable(true);
        backgroundAlpha(window, 0.5f);

        return popup;
    }




    public static void showPhoneView(final Context context, final Window window, View parent, final String phone) {
        View view = View.inflate(context, R.layout.pop_phone, null);
        final TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        final TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
        final TextView tv_call = (TextView) view.findViewById(R.id.tv_call);
        tv_phone.setText(phone);
        popup = getPopup(context, view, window);
        popup.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popup.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(window, 1f);
                closePopup();
            }
        });

        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (popup != null) {
                    popup.dismiss();
                    popup = null;
                }

                GlobalParams.  callPhone(context, phone);

            }
        });


        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popup != null) {
                    popup.dismiss();
                    popup = null;
                }
            }
        });
    }




    public static void showBirthdayPopwindow(Activity context, View view) {

        int mScreenWidth;
        int mScreenHeight;
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        dialog = new Dialog(context, R.style.about_dialog);
        dialog.setContentView(view);

        Window dialogWindow = dialog.getWindow();
        LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);

        lp.width = mScreenWidth;
        lp.height = (int) (mScreenHeight * 0.4);
        dialogWindow.setWindowAnimations(R.style.dialog_anim_bottom);
        dialogWindow.setAttributes(lp);

        dialog.setCanceledOnTouchOutside(true);

        dialog.show();


    }

    public static View getYearPick(final Context context, final onSelectFinishListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.year_picker, null, false);
        final YearPicker tpTime = (YearPicker) view.findViewById(R.id.tpTime);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tvDialog_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvDialog_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = Integer.valueOf(tpTime.getYear());

                if (dialog != null) {
                    dialog.dismiss();
                }
                if (listener != null) {
                    listener.onSelectFinish(year+"");
                }

            }

        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        return view;
    }
    //自定义一个回调的接口
    public static View getDataPick(final Context context, final onSelectFinishListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_picker, null, false);
        final TimePicker tpTime = (TimePicker) view.findViewById(R.id.tpTime);
        Calendar calendar = Calendar.getInstance();
        tpTime.setDefault(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        TextView tvConfirm = (TextView) view.findViewById(R.id.tvDialog_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvDialog_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = Integer.valueOf(tpTime.getMonth());
                String m;
                if (month < 10) {
                    m = "0" + month;
                } else {
                    m = String.valueOf(month);
                }
                int year = Integer.valueOf(tpTime.getYear());
                int d = getDay(year, month);
                int day = Integer.valueOf(tpTime.getDay());
                if (day > d) {
                    day = d;
                }
                String da;
                if (day < 10) {
                    da = "0" + day;
                } else {
                    da = String.valueOf(day);
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (listener != null) {
                    listener.onSelectFinish(year + "-" + m + "-" + da);
                }

            }

        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        return view;
    }


    //自定义一个回调的接口
    public static View getDataPick2(final Context context, final onSelectFinishListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_picker, null, false);
        final TimePicker tpTime = (TimePicker) view.findViewById(R.id.tpTime);
        Calendar calendar = Calendar.getInstance();
        tpTime.setDefault(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        TextView tvConfirm = (TextView) view.findViewById(R.id.tvDialog_confirm);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvDialog_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = Integer.valueOf(tpTime.getMonth());
                String m;
                if (month < 10) {
                    m = "0" + month;
                } else {
                    m = String.valueOf(month);
                }
                int year = Integer.valueOf(tpTime.getYear());
                int d = getDay(year, month);
                int day = Integer.valueOf(tpTime.getDay());
                if (day > d) {
                    day = d;
                }
                String da;
                if (day < 10) {
                    da = "0" + day;
                } else {
                    da = String.valueOf(day);
                }
               /* if (dialog != null) {
                    dialog.dismiss();
                }*/
                if (listener != null) {
                    listener.onSelectFinish(year + "-" + m + "-" + da);
                }

            }

        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        return view;
    }


    /**
     * @param year  年
     * @param month 月
     * @return 真实2月份天数
     */
    private static int getDay(int year, int month) {
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
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    public interface OnSureListener {
        void setOnClickListener(String content);
    }

    public static void closePopup() {
        if (popup != null) {
            popup = null;
        }
    }


}
