package com.example.xy.dentist.ui.doctor.activity.work;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.CaseAdapter;
import com.example.xy.dentist.adapter.TimeSetAdapter;
import com.example.xy.dentist.adapter.TimeSetAdapter2;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.AppointBean;
import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.CaseBean;
import com.example.xy.dentist.bean.DoctorInfoBean;
import com.example.xy.dentist.bean.TimeSetBean;
import com.example.xy.dentist.bean.eventbus.UpdateWorkState;
import com.example.xy.dentist.contract.LogContract;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.model.LogModel;
import com.example.xy.dentist.presenter.LogPresenter;
import com.example.xy.dentist.tool.MyGridView;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.widget.AutoLinefeedLayout;
import com.example.xy.dentist.widget.WrapLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import org.apmem.tools.layouts.FlowLayout;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LogEvaluationActivity extends BaseActivity<LogPresenter, LogModel> implements LogContract.View, ItemClickListener {



    @Bind(R.id.flowLayout)
    FlowLayout flowLayout;
    List<String> myData1 = new ArrayList<>();
    private List<TimeSetBean> beans = new ArrayList<>();
    //    用户标签
    private String tag1, tag2;

    private DoctorInfoBean doctorInfoBean;


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
    MyGridView mGv;
    @Bind(R.id.gv_child)
    MyGridView mGvChild;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.tv_exsice)
    TextView mTvExsice;
    @Bind(R.id.ll_root)
    LinearLayout mLlRoot;
    @Bind(R.id.sv)
    ScrollView sv;
    private List<CaseBean> data = new ArrayList<>();
    private CaseAdapter mAdapter;
    private List<TimeSetBean> dataeval = new ArrayList<>();
    private List<CaseBean> datachild = new ArrayList<>();
    private CaseAdapter mAdapter1;
    private TimeSetAdapter2 mAdapter2;
    private String title;
    private String id;
    private String doctor_token;
    String content, tooth, tags = "";
    LinkedHashMap<Integer, CaseBean> Linkmap = new LinkedHashMap<>();
    List<CaseBean> list = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_log_evaluation;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {


        tag1 = getIntent().getStringExtra("tag1");
        tag2 = getIntent().getStringExtra("tag2");

        doctor_token = GlobalParams.getdoctor_token();
        title = getIntent().getStringExtra("title");
        doctorInfoBean = (DoctorInfoBean) getIntent().getSerializableExtra("DoctorInfoBean");
        id = getIntent().getStringExtra("id");
        mNtb.setTitleText(title);
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        mTvAppo.setVisibility(View.VISIBLE);
        mTvNeglect.setVisibility(View.GONE);

        mTvAppo.setTextColor(Color.parseColor("#6F6F6F"));
        mTvAppo.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        sv.scrollTo(0, 0);
        if (title.equals("诊疗详情")) {
            mTvExsice.setVisibility(View.GONE);
            mEtContent.setEnabled(false);
            mEtContent.setHint("");
        } else {

            mTvExsice.setVisibility(View.VISIBLE);
            mEtContent.setEnabled(true);
            getTag();
//            getTag();
        }

    }


    @Override
    protected void initData() {

        if (doctorInfoBean != null) {
//            LogUtils.print("json",new Gson().toJson(doctorInfoBean));
            ImageLoaderUtils.displayRound(mContext, mIvPic, ApiConstants.IMAGE_URL + doctorInfoBean.avatar);

//            mTvAppo.setText(doctorInfoBean.start_time + "-" + doctorInfoBean.end_time);
            mTvAppo.setText(doctorInfoBean.year+"-"+doctorInfoBean.month+"-"+doctorInfoBean.day+" ("+doctorInfoBean.start_time + " - " + doctorInfoBean.end_time+")");
            LogUtils.print("doctorInfoBean",mTvAppo.getText().toString());
            mTvTitle.setText(doctorInfoBean.name);
            mTvYear.setText(doctorInfoBean.age + "岁");
            mTvPhone.setText(doctorInfoBean.phone);
        }

        if (!TextUtils.isEmpty(tag1)) {
            mTvLab1.setText(tag1);
            mTvLab1.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(tag2)) {
            mTvLab2.setText(tag2);
            mTvLab2.setVisibility(View.VISIBLE);

        }


        CaseBean c = new CaseBean();

        c.name = "8";
        c.isCheck = false;
        c.pos = 0;
        CaseBean c1 = new CaseBean();
        c1.name = "7";
        c1.isCheck = false;
        c1.pos = 0;
        CaseBean c2 = new CaseBean();
        c2.name = "6";
        c2.isCheck = false;
        c2.pos = 0;
        CaseBean c3 = new CaseBean();
        c3.name = "5";
        c3.isCheck = false;
        c3.pos = 0;
        CaseBean c4 = new CaseBean();
        c4.name = "4";
        c4.isCheck = false;
        c4.pos = 0;
        CaseBean c5 = new CaseBean();
        c5.name = "3";
        c5.isCheck = false;
        c5.pos = 0;
        CaseBean c6 = new CaseBean();
        c6.name = "2";
        c6.isCheck = false;
        c6.pos = 0;
        CaseBean c7 = new CaseBean();
        c7.name = "1";
        c7.isCheck = false;
        c7.pos = 0;
        data.add(c);
        data.add(c1);
        data.add(c2);
        data.add(c3);
        data.add(c4);
        data.add(c5);
        data.add(c6);
        data.add(c7);


        CaseBean c20 = new CaseBean();
        c20.name = "1";
        c20.isCheck = false;
        c20.pos = 1;
        CaseBean c21 = new CaseBean();
        c21.name = "2";
        c21.isCheck = false;
        c21.pos = 1;
        CaseBean c22 = new CaseBean();
        c22.name = "3";
        c22.isCheck = false;
        c22.pos = 1;
        CaseBean c23 = new CaseBean();
        c23.name = "4";
        c23.isCheck = false;
        c23.pos = 1;
        CaseBean c24 = new CaseBean();
        c24.name = "5";
        c24.isCheck = false;
        c24.pos = 1;
        CaseBean c25 = new CaseBean();
        c25.name = "6";
        c25.isCheck = false;
        c25.pos = 1;
        CaseBean c26 = new CaseBean();
        c26.name = "7";
        c26.isCheck = false;
        c26.pos = 1;
        CaseBean c27 = new CaseBean();
        c27.name = "8";
        c27.isCheck = false;
        c27.pos = 1;
        data.add(c20);
        data.add(c21);
        data.add(c22);
        data.add(c23);
        data.add(c24);
        data.add(c25);
        data.add(c26);
        data.add(c27);
        CaseBean c30 = new CaseBean();
        c30.name = "8";
        c30.isCheck = false;
        c30.pos = 2;
        CaseBean c31 = new CaseBean();
        c31.name = "7";
        c31.isCheck = false;
        c31.pos = 2;
        CaseBean c32 = new CaseBean();
        c32.name = "6";
        c32.isCheck = false;
        c32.pos = 2;
        CaseBean c33 = new CaseBean();
        c33.name = "5";
        c33.isCheck = false;
        c33.pos = 2;
        CaseBean c34 = new CaseBean();
        c34.name = "4";
        c34.isCheck = false;
        c34.pos = 2;
        CaseBean c35 = new CaseBean();
        c35.name = "3";
        c35.isCheck = false;
        c35.pos = 2;
        CaseBean c36 = new CaseBean();
        c36.name = "2";
        c36.isCheck = false;
        c36.pos = 2;
        CaseBean c37 = new CaseBean();
        c37.name = "1";
        c37.isCheck = false;
        c37.pos = 2;
        data.add(c30);
        data.add(c31);
        data.add(c32);
        data.add(c33);
        data.add(c34);
        data.add(c35);
        data.add(c36);
        data.add(c37);
        CaseBean c40 = new CaseBean();
        c40.name = "1";
        c40.isCheck = false;
        c40.pos = 3;
        CaseBean c41 = new CaseBean();
        c41.name = "2";
        c41.isCheck = false;
        c41.pos = 3;
        CaseBean c42 = new CaseBean();
        c42.name = "3";
        c42.isCheck = false;
        c42.pos = 3;
        CaseBean c43 = new CaseBean();
        c43.name = "4";
        c43.isCheck = false;
        c43.pos = 3;
        CaseBean c44 = new CaseBean();
        c44.name = "5";
        c44.isCheck = false;
        c44.pos = 3;
        CaseBean c45 = new CaseBean();
        c45.name = "6";
        c45.isCheck = false;
        c45.pos = 3;
        CaseBean c46 = new CaseBean();
        c46.name = "7";
        c46.isCheck = false;
        c46.pos = 3;
        CaseBean c47 = new CaseBean();
        c47.name = "8";
        c47.isCheck = false;
        c47.pos = 3;
        data.add(c40);
        data.add(c41);
        data.add(c42);
        data.add(c43);
        data.add(c44);
        data.add(c45);
        data.add(c46);
        data.add(c47);

        mAdapter = new CaseAdapter(mActivity, data, 0);
        mGv.setAdapter(mAdapter);
        CaseBean ca = new CaseBean();
        ca.name = "Ⅴ";
        ca.isCheck = false;
        ca.pos = 4;
        CaseBean ca1 = new CaseBean();
        ca1.name = "Ⅳ";
        ca1.isCheck = false;
        ca1.pos = 4;
        CaseBean ca2 = new CaseBean();
        ca2.name = "Ⅲ";
        ca2.isCheck = false;
        ca2.pos = 4;
        CaseBean ca3 = new CaseBean();
        ca3.name = "Ⅱ";
        ca3.isCheck = false;
        ca3.pos = 4;
        CaseBean ca4 = new CaseBean();
        ca4.name = "Ⅰ";
        ca4.isCheck = false;
        ca4.pos = 4;
        datachild.add(ca);
        datachild.add(ca1);
        datachild.add(ca2);
        datachild.add(ca3);
        datachild.add(ca4);
        CaseBean c2a = new CaseBean();
        c2a.name = "Ⅰ";
        c2a.isCheck = false;
        c2a.pos = 5;
        CaseBean c2a1 = new CaseBean();
        c2a1.name = "Ⅱ";
        c2a1.isCheck = false;
        c2a1.pos = 5;
        CaseBean c2a2 = new CaseBean();
        c2a2.name = "Ⅲ";
        c2a2.isCheck = false;
        c2a2.pos = 5;
        CaseBean c2a3 = new CaseBean();
        c2a3.name = "Ⅳ";
        c2a3.isCheck = false;
        c2a3.pos = 5;
        CaseBean c2a4 = new CaseBean();
        c2a4.name = "Ⅴ";
        c2a4.isCheck = false;
        c2a4.pos = 5;
        datachild.add(c2a);
        datachild.add(c2a1);
        datachild.add(c2a2);
        datachild.add(c2a3);
        datachild.add(c2a4);
        CaseBean c3a = new CaseBean();
        c3a.name = "Ⅴ";
        c3a.isCheck = false;
        c3a.pos = 6;
        CaseBean c3a1 = new CaseBean();
        c3a1.name = "Ⅳ";
        c3a1.isCheck = false;
        c3a1.pos = 6;
        CaseBean c3a2 = new CaseBean();
        c3a2.name = "Ⅲ";
        c3a2.isCheck = false;
        c3a2.pos = 6;
        CaseBean c3a3 = new CaseBean();
        c3a3.name = "Ⅱ";
        c3a3.isCheck = false;
        c3a3.pos = 6;
        CaseBean c3a4 = new CaseBean();
        c3a4.name = "Ⅰ";
        c3a4.isCheck = false;
        c3a4.pos = 6;
        datachild.add(c3a);
        datachild.add(c3a1);
        datachild.add(c3a2);
        datachild.add(c3a3);
        datachild.add(c3a4);
        CaseBean c4a = new CaseBean();
        c4a.name = "Ⅰ";
        c4a.isCheck = false;
        c4a.pos = 7;
        CaseBean c4a1 = new CaseBean();
        c4a1.name = "Ⅱ";
        c4a1.isCheck = false;
        c4a1.pos = 7;
        CaseBean c4a2 = new CaseBean();
        c4a2.name = "Ⅲ";
        c4a2.isCheck = false;
        c4a2.pos = 7;
        CaseBean c4a3 = new CaseBean();
        c4a3.name = "Ⅳ";
        c4a3.isCheck = false;
        c4a3.pos = 7;
        CaseBean c4a4 = new CaseBean();
        c4a4.name = "Ⅴ";
        c4a4.isCheck = false;
        c4a4.pos = 7;
        datachild.add(c4a);
        datachild.add(c4a1);
        datachild.add(c4a2);
        datachild.add(c4a3);
        datachild.add(c4a4);


        mAdapter1 = new CaseAdapter(mActivity, datachild, 1);
        mGvChild.setAdapter(mAdapter1);
//        获取预约详情
//        mPresenter.doctor_getUserInfo(doctor_token, id);
        getDocInfor();

    }


    @Override
    protected void initListener() {


        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!title.equals("诊疗详情")) {
            mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                           @Override
                                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                               CaseBean item = data.get(position);
                                               if (position <= 7) {
                                                   item.pos = 0;
                                               } else if (position <= 15) {
                                                   item.pos = 1;
                                               } else if (position <= 23) {
                                                   item.pos = 2;
                                               } else {
                                                   item.pos = 3;
                                               }

                                               if (item.isCheck) {
                                                   item.isCheck = false;
                                                   list.remove(item);
                                               } else {
                                                   list.add(item);
                                                   item.isCheck = true;
                                               }

                                               Linkmap.put(item.pos, item);
                                               mAdapter.updateItemData(mGv, position);


                                           }
                                       }

            );

            mGvChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CaseBean item = (CaseBean) mGvChild.getItemAtPosition(position);
                    if (position <= 4) {
                        item.pos = 4;
                    } else if (position <= 9) {
                        item.pos = 5;
                    } else if (position <= 14) {
                        item.pos = 6;
                    } else {
                        item.pos = 7;
                    }
                    if (item.isCheck) {
                        item.isCheck = false;
                        list.remove(item);
                    } else {
                        item.isCheck = true;
                        list.add(item);
                    }
                    Linkmap.put(item.pos, item);
                    // mAdapter1.notifyDataSetChanged();
                    mAdapter1.updateItemData(mGvChild, position);


                }
            });


        }
    }


    @OnClick(R.id.tv_exsice)
    public void onClick() {
        content = mEtContent.getText().toString().trim();
        tooth = new Gson().toJson(list);
        if (!tooth.contains("true")) {
            ToastUitl.showShort("请选择病例日志");
        } else if (TextUtils.isEmpty(content)) {
            ToastUitl.showShort("请输入诊断详情");
        } else {
            tags="";
//            LogUtils.print("tooth", tooth);
//            LogUtils.print("tags", tags);
//          可以不选评价标签  其它必选

                for(int i=0;i<beans.size();i++){
                    TimeSetBean item=beans.get(i);
                   if(item.isCheck){
                       if(TextUtils.isEmpty(tags)){
                           tags=item.id;
                       }else {
                           tags=tags+","+item.id;
                       }
                   }
                }


            LogUtils.print("tags",tags);
            mPresenter.doctor_judge(doctor_token, id, content, tooth, tags);
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void setData(AppointBean bean, String message) {
        Gson gson = new Gson();
        Log.i("TAG", "gson=" + gson.toJson(bean));
        if (bean != null) {
//            ImageLoaderUtils.displayRound(mContext, mIvPic, ApiConstants.IMAGE_URL + bean.avatar);

//            mTvAppo.setText(bean.start_time + "-" + bean.end_time);

//            mTvTitle.setText(bean.name);
//            mTvYear.setText(bean.age + "岁");
//            mTvPhone.setText(bean.phone);

            dataeval.clear();
           /* if (bean.tags!=null&&bean.tags.size() > 0) {
                for (int i = 0; i < bean.tags.size(); i++) {
                    TimeSetBean t = new TimeSetBean();
                    t.name = bean.tags.get(i);
                    t.isCheck = false;
                    dataeval.add(t);
                    LogUtils.print("i",bean.tags.get(i));

                }
            }*/
            if (title.equals("诊疗详情")) {
                flowLayout.removeAllViews();
//                mAdapter2 = new TimeSetAdapter2(mActivity, dataeval, 2);
//                mGvEvalu.setAdapter(mAdapter2);
                mEtContent.setText(bean.content);
                for (int i = 0; i < bean.tags.size(); i++) {
                    final View textView =  View.inflate(mContext, R.layout.essss, null);
                    CheckBox checkBox= (CheckBox) textView.findViewById(R.id.tv_exam);
                    textView.setEnabled(false);
                    checkBox.setEnabled(false);
                    checkBox.setBackgroundResource(R.mipmap.lable_yellow);
                    FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.

                    layoutParams.setMargins(10, 10, 0, 0);
                    layoutParams.setGravity(Gravity.LEFT);
                    textView.setLayoutParams(layoutParams);

                    final String nammei =  bean.tags.get(i);
                    checkBox.setText(nammei);
                    flowLayout.addView(textView);

                }
//                LogUtils.print("count",flowLayout.getChildCount());
//                LogUtils.print("count",((CheckBox)flowLayout.getChildAt(1)).getLeft());
//                LogUtils.print("count",((CheckBox)flowLayout.getChildAt(0)).getLeft());


            } else {

            }


            if (!TextUtils.isEmpty(bean.tooth)) {
                Log.i("TAG","tooth="+bean.tooth);
                List<CaseBean> tempList = new Gson().fromJson(bean.tooth, new TypeToken<List<CaseBean>>() {
                }.getType());
                for (int i = 0; i < tempList.size(); i++) {
                    for (int j = 0; j < data.size(); j++) {

                        if (data.get(j).name.equals(tempList.get(i).name) && data.get(j).pos == tempList.get(i).pos) {
                            data.get(j).isCheck = true;
                        } else {
                            //   data.get(j).isCheck = false;
                        }


                    }


                    for (int j = 0; j < datachild.size(); j++) {

                        if (datachild.get(j).name.equals(tempList.get(i).name) && datachild.get(j).pos == tempList.get(i).pos) {
                            datachild.get(j).isCheck = true;
                        } else {
                            // datachild.get(j).isCheck=false;
                        }


                    }
                }
                mAdapter.notifyDataSetChanged();

                mAdapter1.notifyDataSetChanged();


            }
        }

    }

    @Override
    public void setTag(List<TimeSetBean> bean) {
        Gson gson = new Gson();
        String tagsss = gson.toJson(bean);
        Log.i("TAG", "taggsss=" + tagsss);
//        setGridView(mGvEvalu);


    }

    @Override
    public void set_judge(BaseResult bean) {
        Intent intent=new Intent("NEED_CHANGE_PARENT_AND_CHILD_MAIN");
        intent.putExtra("parentIndex",0);
        intent.putExtra("childIndex",3);
        sendBroadcast(intent);
        ToastUitl.showShort("评价成功");
//        EventBus.getDefault().post(new UpdateWorkState(2));//预约单状态，0新预约1待确认2进行中3已完成-1已取消

        finish();
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

    @Override
    public void onItemClick(View view, int postion) {

    }


    private void getTag() {
        OkHttpClient client = new OkHttpClient();
//通过FormEncodingBuilder对象添加多个请求参数键值对
        FormBody.Builder builder = new FormBody.Builder();

//        builder.add("type", type);
//        builder.add("code", openId);

//通过请求地址和请求体构造Post请求对象Request
        Request request = new Request.Builder().url(ApiConstants.SERVER_URL + ApiConstants.tags).post(builder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onFailureIOException=" + e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resutl = response.body().string();
//                ToastUitl.showLong(resutl);
                Log.i("TAG", "result=" + resutl);
//                ToastUitl.showLong(resutl);
                try {
                    JSONObject json = new JSONObject(resutl);
                    if (json.getString("code").equals("200")) {
                        JSONArray jsonArray = json.getJSONObject("data").getJSONArray("list");
                        beans = JSON.parseArray(jsonArray.toString(), TimeSetBean.class);
                        mAdapter2 = new TimeSetAdapter2(mActivity, beans, 1);

                        runOnUiThread(new Runnable() {
                            @SuppressLint("NewApi")
                            @Override
                            public void run() {
                                for (int i = 0; i < beans.size(); i++) {

                                    final View textView =View.inflate(mContext, R.layout.essss, null);
                                    CheckBox checkBox= (CheckBox) textView.findViewById(R.id.tv_exam);
                                    FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams.setMargins(10, 10, 0, 0);
                                    layoutParams.setGravity(Gravity.LEFT);
                                    textView.setLayoutParams(layoutParams);

                                    final String nammei = beans.get(i).name;
                                    checkBox.setText(nammei);
                                    flowLayout.addView(textView);
                                    final int finalI = i;

                                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            beans.get(finalI).isCheck = isChecked;
                                        }
                                    });
                                }


                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }





    private void getDocInfor(){
        OkHttpClient client = new OkHttpClient();
//通过FormEncodingBuilder对象添加多个请求参数键值对
        FormBody.Builder builder = new FormBody.Builder();

        builder.add("doctor_token", doctor_token);
        builder.add("id", id);

//通过请求地址和请求体构造Post请求对象Request
        Request request = new Request.Builder().url(ApiConstants.SERVER_URL + ApiConstants.doctor_getUserInfo).post(builder.build()).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultJson=response.body().string();
                LogUtils.print("resultJson",resultJson);
                try {
                    JSONObject json=new JSONObject(resultJson);

                    if(json.getString("code").equals("200")){
                        JSONObject jsonObject=json.getJSONObject("data").getJSONObject("info");
                        final AppointBean appointuBean=JSON.parseObject(jsonObject.toString(),AppointBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData(appointuBean,"");
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
