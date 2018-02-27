package com.example.xy.dentist.ui.doctor.activity.recruit;

import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.TimeSetAdapter;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.DoctorInfoBean;
import com.example.xy.dentist.bean.TimeSetBean;
import com.example.xy.dentist.bean.eventbus.UpdateWorkState;
import com.example.xy.dentist.contract.TimeContract;
import com.example.xy.dentist.model.TimeModel;
import com.example.xy.dentist.presenter.TimePresenter;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.MyTimeUtils;
import com.example.xy.dentist.widget.TimeSelectorBirthday;
import com.example.xy.dentist.widget.TimeSelectorDayAndHour;
import com.google.gson.Gson;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class TimeSetActivity extends BaseActivity<TimePresenter, TimeModel> implements TimeContract.View {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.iv_pic)
    ImageView mIvPic;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_year)
    TextView mTvYear;
    @Bind(R.id.tv_phone)
    TextView mTvPhone;
    @Bind(R.id.tv_lab1)
    TextView mTvLab1;
    @Bind(R.id.tv_lab2)
    TextView mTvLab2;
    @Bind(R.id.tv_lab3)
    TextView mTvLab3;
    @Bind(R.id.tv_appo)
    TextView mTvAppo;
    @Bind(R.id.tv_neglect)
    TextView mTvNeglect;
    @Bind(R.id.gv)
    GridView mGv;
    @Bind(R.id.tv_exsice)
    TextView mTvExsice;
    @Bind(R.id.ll_root)
    LinearLayout mLlRoot;
    @Bind(R.id.sv)
    ScrollView sv;
    private List<TimeSetBean> data = new ArrayList<>();
    private TimeSetAdapter mAdapter;
    private DoctorInfoBean bean;
    private String doctor_token;
    private String date_id, id;

    private String date;
    @Bind(R.id.tv_selectTime)
    TextView tv_selectTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_time_set;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        LogUtils.print("date",date);
        mNtb.setTitleText("设定时间");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        mTvAppo.setVisibility(View.GONE);
        mTvNeglect.setVisibility(View.GONE);
        ImageLoaderUtils.displayRound(mActivity, mIvPic, R.mipmap.bna);
        sv.scrollTo(0, 0);

    }

    @Override
    protected void initData() {
        doctor_token = GlobalParams.getdoctor_token();
        bean = (DoctorInfoBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            ImageLoaderUtils.displayRound(mContext, mIvPic, ApiConstants.IMAGE_URL + bean.avatar);
            id = bean.id;//预约ID

            mTvTitle.setText(bean.name);
            mTvYear.setText(bean.age + "岁");
            mTvPhone.setText(bean.phone);
            Gson gson = new Gson();
            String obj2 = gson.toJson(bean);
            Log.i("TAG", "obj2=" + obj2);

            if (bean.tags != null) {
                String[] tag = bean.tags;
                if (tag.length == 1) {

                    mTvLab1.setText(tag[0]);
                    mTvLab1.setVisibility(View.VISIBLE);
                    mTvLab2.setVisibility(View.GONE);
                    mTvLab3.setVisibility(View.GONE);

                } else if (tag.length == 2) {
                    mTvLab1.setText(tag[0]);
                    mTvLab2.setText(tag[1]);
                    mTvLab1.setVisibility(View.VISIBLE);
                    mTvLab2.setVisibility(View.VISIBLE);
                    mTvLab3.setVisibility(View.GONE);
                } else if (tag.length == 3) {
                    mTvLab1.setText(tag[0]);
                    mTvLab2.setText(tag[1]);
                    mTvLab3.setText(tag[2]);
                    mTvLab1.setVisibility(View.VISIBLE);
                    mTvLab2.setVisibility(View.VISIBLE);
                    mTvLab3.setVisibility(View.VISIBLE);
                }
            }
        }
        mPresenter.doctor_getTimeListNew(doctor_token,date);


    }

   /* private void setTimeTestData() {
        TimeSetBean t0 = new TimeSetBean();
        t0.name = "08:10-08:30";
        t0.isCheck = false;
        TimeSetBean t = new TimeSetBean();
        t.name = "08:30-08:50";
        t.isCheck = false;
        TimeSetBean t1 = new TimeSetBean();
        t1.name = "08:50-09:10";
        t1.isCheck = false;
        TimeSetBean t2 = new TimeSetBean();
        t2.name = "09:10-09:30";
        t2.isCheck = false;
        TimeSetBean t3 = new TimeSetBean();
        t3.name = "09:30-09:50";
        t3.isCheck = false;
        TimeSetBean t4 = new TimeSetBean();
        t4.name = "09:50-10:10";
        t4.isCheck = false;
        TimeSetBean t5 = new TimeSetBean();
        t5.name = "10:10-10:30";
        t5.isCheck = false;
        TimeSetBean t6 = new TimeSetBean();
        t6.name = "10:30-10:50";
        t6.isCheck = false;
        TimeSetBean t7 = new TimeSetBean();
        t7.name = "10:50-11:10";
        t7.isCheck = false;
        TimeSetBean t8 = new TimeSetBean();
        t8.name = "11:10-11:30";
        t8.isCheck = false;
        TimeSetBean t9 = new TimeSetBean();
        t9.name = "11:30-11:50";
        t9.isCheck = false;
        TimeSetBean t10 = new TimeSetBean();
        t10.name = "11:50-12:10";
        t10.isCheck = false;
        TimeSetBean t11 = new TimeSetBean();
        t11.name = "12:10-12:30";
        t11.isCheck = false;
        TimeSetBean t12 = new TimeSetBean();
        t12.name = "12:30-12:50";
        t12.isCheck = false;
        TimeSetBean t13 = new TimeSetBean();
        t13.name = "12:50-13:10";
        t13.isCheck = false;
        TimeSetBean t14 = new TimeSetBean();
        t14.name = "13:10-13:30";
        t14.isCheck = false;
        TimeSetBean t15 = new TimeSetBean();
        t15.name = "13:30-13:50";
        t15.isCheck = false;
        TimeSetBean t16 = new TimeSetBean();
        t16.name = "13:50-14:10";
        t16.isCheck = false;
        TimeSetBean t17 = new TimeSetBean();
        t17.name = "14:10-14:30";
        t17.isCheck = false;
        TimeSetBean t18 = new TimeSetBean();
        t18.name = "14:30-14:50";
        t18.isCheck = false;
        TimeSetBean t19 = new TimeSetBean();
        t19.name = "14:50-15:10";
        t19.isCheck = false;
        TimeSetBean t20 = new TimeSetBean();
        t20.name = "15:10-15:30";
        t20.isCheck = false;
        TimeSetBean t21 = new TimeSetBean();
        t21.name = "15:30-15:50";
        t21.isCheck = false;
        TimeSetBean t22 = new TimeSetBean();
        t22.name = "15:50-16:10";
        t22.isCheck = false;
        TimeSetBean t23 = new TimeSetBean();
        t23.name = "16:10-16:30";
        t23.isCheck = false;
        TimeSetBean t24 = new TimeSetBean();
        t24.name = "16:30-16:50";
        t24.isCheck = false;
        TimeSetBean t25 = new TimeSetBean();
        t25.name = "16:50-17:10";
        t25.isCheck = false;
        TimeSetBean t26 = new TimeSetBean();
        t26.name = "17:10-17:30";
        t26.isCheck = false;


        TimeSetBean t27 = new TimeSetBean();
        t27.name = "17:30-17:50";
        t27.isCheck = false;
        TimeSetBean t28 = new TimeSetBean();
        t28.name = "17:50-18:10";
        t28.isCheck = false;
        TimeSetBean t29 = new TimeSetBean();
        t29.name = "18:10-18:30";
        t29.isCheck = false;
        TimeSetBean t30 = new TimeSetBean();
        t30.name = "18:30-18:50";
        t30.isCheck = false;
        TimeSetBean t31 = new TimeSetBean();
        t31.name = "18:50-19:10";
        t31.isCheck = false;
        TimeSetBean t32 = new TimeSetBean();
        t32.name = "19:10-19:30";
        t32.isCheck = false;
        TimeSetBean t33 = new TimeSetBean();
        t33.name = "19:30-19:50";
        t33.isCheck = false;
        TimeSetBean t34 = new TimeSetBean();
        t34.name = "19:50-20:10";
        t34.isCheck = false;
        TimeSetBean t35 = new TimeSetBean();
        t35.name = "20:10-20:30";
        t35.isCheck = false;
        TimeSetBean t36 = new TimeSetBean();
        t36.name = "20:30-20:50";
        t36.isCheck = false;
        TimeSetBean t37 = new TimeSetBean();
        t37.name = "20:50-21:10";
        t37.isCheck = false;

        data.add(t0);
        data.add(t);
        data.add(t1);
        data.add(t2);
        data.add(t3);
        data.add(t4);
        data.add(t5);
        data.add(t6);
        data.add(t7);
        data.add(t8);
        data.add(t9);
        data.add(t10);
        data.add(t11);
        data.add(t12);
        data.add(t13);
        data.add(t14);
        data.add(t15);
        data.add(t16);
        data.add(t17);
        data.add(t18);
        data.add(t19);
        data.add(t20);
        data.add(t21);
        data.add(t22);
        data.add(t23);
        data.add(t24);
        data.add(t25);
        data.add(t26);
        data.add(t27);
        data.add(t28);
        data.add(t29);
        data.add(t30);
        data.add(t31);
        data.add(t32);
        data.add(t33);
        data.add(t34);
        data.add(t35);
        data.add(t36);
        data.add(t37);
    }*/

    @Override
    protected void initListener() {
        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TimeSetBean item = (TimeSetBean) mGv.getItemAtPosition(position);
                LogUtils.print("disabled", item.disabled);

//                                           0  从选中到未选中 判断
                if (item.isCheck) {
                    //                                              先判断 他是否前后有字段被选中
                    boolean isUpIsSelect = false;
                    boolean isBackIsSelect = false;
                    if (position > 0) {
                        isBackIsSelect = ((TimeSetBean) mGv.getItemAtPosition(position - 1)).isCheck;
                    }
                    if (position < mAdapter.getCount() - 1) {
                        isUpIsSelect = ((TimeSetBean) mGv.getItemAtPosition(position + 1)).isCheck;
                    }
                    if (isBackIsSelect && isUpIsSelect) {
//                                                   item.isCheck = true;
//                                                   mAdapter.setData(position);
//                                                   清除所有被选中时间段
                        for (int i = 0; i < mAdapter.getCount(); i++) {
                            mAdapter.getItem(i).isCheck = false;
                            mAdapter.setData(i, false);

                        }
                    } else {
                        item.isCheck = false;
                        mAdapter.setData(position, false);
                    }

                } else {
//                                               未选中到选中  判断

                    //  1 此时间段改医生已经预约其它人  不可选择  disabled字段  判断
                    if (item.disabled.equals("1")) {
                        ToastUitl.showShort("此时间段已经其它用户占用");
                        return;
                    }
//                    date_id = item.id;//id:时间段ID
                   /* int t = MyTimeUtils.com(item.start_time, MyTimeUtils.getTime());
                    if (t < 0) {
                        ToastUitl.showShort("您选择的时间不合适，请选择其它时间段");
                        return;
                    }*/
//                  获取所有选中的下标集合
                    List<String> mPostion = new ArrayList<>();
                    mPostion.clear();
                    for (int i = 0; i < mAdapter.getCount(); i++) {
                        if (mAdapter.getItem(i).isCheck) {
                            mPostion.add(String.valueOf(i));
                        }
                    }

//                   一个选中的都没有
                    if (mPostion.size() == 0) {
                        item.isCheck = true;
                        mAdapter.setData(position, true);
                        return;
                    } else {
                        //                      判断当前点击位置和 所有被选中下标位置的关系
                        boolean flage = false;
                        for (int i = 0; i < mPostion.size(); i++) {
                            int temp = Integer.parseInt(mPostion.get(i));
                            //一共3中情况  1：前后有选中的   2：前后没有选中，前面是disable=1  3：前后没有选中 ，前面也不是disable
                            if (position == temp - 1 || position == temp + 1) {
                                //1：前后有选中的
                                flage = true;
                                item.isCheck = true;
                                mAdapter.setData(position, true);
                            } else {
//                                flage = false;
                            }

                        }
                        if (!flage) {
                            ToastUitl.showShort("时间不能跳跃选择");
                        }
                                                       /*if (position > 0) {
                                                           TimeSetBean currBean = (TimeSetBean) mGv.getItemAtPosition(position - 1);

                                                           if (currBean.disabled.equals("1")) {
                                                               if (position + 1 < mAdapter.getCount() - 1) {
                                                                   TimeSetBean currBeanNext = (TimeSetBean) mGv.getItemAtPosition(position + 1);
                                                                   if (currBeanNext.isCheck) {
                                                                       ToastUitl.showShort("时间不能跳跃选择");
                                                                   } else {

                                                                       item.isCheck = true;
                                                                       mAdapter.setData(position, true);

                                                                   }

                                                               } else {
                                                                   item.isCheck = true;
                                                                   mAdapter.setData(position, true);

                                                               }

                                                           }
                                                       }

                                                   }*/


                    }


                }


            }

        });
    }


    @OnClick({R.id.tv_exsice, R.id.tv_selectTime})
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_exsice:
                date_id = "";

//            取出时间段ID
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    if (mAdapter.getItem(i).isCheck) {
                        if (date_id.equals("")) {
                            date_id = mAdapter.getItem(i).id;
                        } else {
                            date_id = date_id + "," + mAdapter.getItem(i).id;
                        }
                    }
                }
                if (TextUtils.isEmpty(date_id)) {
                    ToastUitl.showShort("请选择时间段");
                } else {
//                    Log.i("TAG", "date_id=" + date_id);
                    mPresenter.setTime(doctor_token, id, date_id,date);
                }
                break;
            case R.id.tv_selectTime:

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long now=new Date().getTime();
                String startTime = sdf.format(new Date(now));
                String endTime=sdf.format(now+30L*24*3600*1000L);
                LogUtils.print("startTime",startTime);
                LogUtils.print("endTime",endTime);

                TimeSelectorBirthday timeSelector = new TimeSelectorBirthday(mContext, new TimeSelectorBirthday.ResultHandler() {
                    @Override
                    public void handle(String time) {
//                        int index = time.indexOf(" ");
//                        time = time.substring(0, index);
                        date=time.split(" ")[0];
                        LogUtils.print("time", date);

                        tv_selectTime.setText(date);
//                        调用接口查找时间段
                        mPresenter.doctor_getTimeListNew(doctor_token,date);

                    }
                }, startTime, endTime);
                timeSelector.show();
                break;
        }

    }

    @Override
    public void setTimeState(BaseResult bean, String message) {
        Intent intent=new Intent("NEED_CHANGE_PARENT_AND_CHILD_MAIN");
        intent.putExtra("parentIndex",0);
        intent.putExtra("childIndex",2);
        sendBroadcast(intent);
        ToastUitl.showShort("时间设定成功");
//        EventBus.getDefault().post(new UpdateWorkState(1));//预约单状态，0新预约1待确认2进行中3已完成-1已取消
        finish();

    }

//    private List<TimeSetBean> bean;

    @Override
    public void doctor_setTimeList(List<TimeSetBean> bean, String message) {
//        LogUtils.print("timeList", new Gson().toJson(bean));
//        Log.i("TAG",new Gson().toJson(bean));
        mAdapter = new TimeSetAdapter(mActivity, bean, 0);
        mGv.setAdapter(mAdapter);
    }
    @Override
    public void doctor_setTimeListNew(List<TimeSetBean> bean, String message) {
        LogUtils.print("timeListNew", new Gson().toJson(bean));
//        Log.i("TAG",new Gson().toJson(bean));
        mAdapter = new TimeSetAdapter(mActivity, bean, 0);
        mGv.setAdapter(mAdapter);
    }
    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }
}
