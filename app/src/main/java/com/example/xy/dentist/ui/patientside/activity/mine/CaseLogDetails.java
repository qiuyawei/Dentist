package com.example.xy.dentist.ui.patientside.activity.mine;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.CaseAdapter;
import com.example.xy.dentist.adapter.CaseAdapter2;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.CaseBean;
import com.example.xy.dentist.bean.CaseBean2;
import com.example.xy.dentist.bean.TimeSetBean;
import com.example.xy.dentist.tool.MyGridView;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/12/26.
 * 病例日志详情
 */

public class CaseLogDetails extends BaseActivity {
    private List<CaseBean2> data = new ArrayList<>();
    private CaseAdapter2 mAdapter;
    private List<TimeSetBean> dataeval = new ArrayList<>();
    private List<CaseBean2> datachild = new ArrayList<>();
    private CaseAdapter2 mAdapter1;
    private String logId;//日志
    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.gv)
    MyGridView mGv;
    @Bind(R.id.gv_child)
    MyGridView mGvChild;
    @Bind(R.id.tv_content)
    TextView tv_content;

    @Override
    public int getLayoutId() {
        return R.layout.activity_case_detail;

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mNtb.setTitleText("病例详情");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        logId = getIntent().getStringExtra("logId");
        Log.i("TAG", logId);
    }

    @Override
    protected void initData() {
        CaseBean2 c = new CaseBean2();

        c.name = "8";
        c.isCheck = false;
        c.pos = 0;
        CaseBean2 c1 = new CaseBean2();
        c1.name = "7";
        c1.isCheck = false;
        c1.pos = 0;
        CaseBean2 c2 = new CaseBean2();
        c2.name = "6";
        c2.isCheck = false;
        c2.pos = 0;
        CaseBean2 c3 = new CaseBean2();
        c3.name = "5";
        c3.isCheck = false;
        c3.pos = 0;
        CaseBean2 c4 = new CaseBean2();
        c4.name = "4";
        c4.isCheck = false;
        c4.pos = 0;
        CaseBean2 c5 = new CaseBean2();
        c5.name = "3";
        c5.isCheck = false;
        c5.pos = 0;
        CaseBean2 c6 = new CaseBean2();
        c6.name = "2";
        c6.isCheck = false;
        c6.pos = 0;
        CaseBean2 c7 = new CaseBean2();
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


        CaseBean2 c20 = new CaseBean2();
        c20.name = "1";
        c20.isCheck = false;
        c20.pos = 1;
        CaseBean2 c21 = new CaseBean2();
        c21.name = "2";
        c21.isCheck = false;
        c21.pos = 1;
        CaseBean2 c22 = new CaseBean2();
        c22.name = "3";
        c22.isCheck = false;
        c22.pos = 1;
        CaseBean2 c23 = new CaseBean2();
        c23.name = "4";
        c23.isCheck = false;
        c23.pos = 1;
        CaseBean2 c24 = new CaseBean2();
        c24.name = "5";
        c24.isCheck = false;
        c24.pos = 1;
        CaseBean2 c25 = new CaseBean2();
        c25.name = "6";
        c25.isCheck = false;
        c25.pos = 1;
        CaseBean2 c26 = new CaseBean2();
        c26.name = "7";
        c26.isCheck = false;
        c26.pos = 1;
        CaseBean2 c27 = new CaseBean2();
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
        CaseBean2 c30 = new CaseBean2();
        c30.name = "8";
        c30.isCheck = false;
        c30.pos = 2;
        CaseBean2 c31 = new CaseBean2();
        c31.name = "7";
        c31.isCheck = false;
        c31.pos = 2;
        CaseBean2 c32 = new CaseBean2();
        c32.name = "6";
        c32.isCheck = false;
        c32.pos = 2;
        CaseBean2 c33 = new CaseBean2();
        c33.name = "5";
        c33.isCheck = false;
        c33.pos = 2;
        CaseBean2 c34 = new CaseBean2();
        c34.name = "4";
        c34.isCheck = false;
        c34.pos = 2;
        CaseBean2 c35 = new CaseBean2();
        c35.name = "3";
        c35.isCheck = false;
        c35.pos = 2;
        CaseBean2 c36 = new CaseBean2();
        c36.name = "2";
        c36.isCheck = false;
        c36.pos = 2;
        CaseBean2 c37 = new CaseBean2();
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
        CaseBean2 c40 = new CaseBean2();
        c40.name = "1";
        c40.isCheck = false;
        c40.pos = 3;
        CaseBean2 c41 = new CaseBean2();
        c41.name = "2";
        c41.isCheck = false;
        c41.pos = 3;
        CaseBean2 c42 = new CaseBean2();
        c42.name = "3";
        c42.isCheck = false;
        c42.pos = 3;
        CaseBean2 c43 = new CaseBean2();
        c43.name = "4";
        c43.isCheck = false;
        c43.pos = 3;
        CaseBean2 c44 = new CaseBean2();
        c44.name = "5";
        c44.isCheck = false;
        c44.pos = 3;
        CaseBean2 c45 = new CaseBean2();
        c45.name = "6";
        c45.isCheck = false;
        c45.pos = 3;
        CaseBean2 c46 = new CaseBean2();
        c46.name = "7";
        c46.isCheck = false;
        c46.pos = 3;
        CaseBean2 c47 = new CaseBean2();
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

        mAdapter = new CaseAdapter2(mActivity, data, 0);
        mGv.setAdapter(mAdapter);
        CaseBean2 ca = new CaseBean2();
        ca.name = "Ⅴ";
        ca.isCheck = false;
        ca.pos = 4;
        CaseBean2 ca1 = new CaseBean2();
        ca1.name = "Ⅳ";
        ca1.isCheck = false;
        ca1.pos = 4;
        CaseBean2 ca2 = new CaseBean2();
        ca2.name = "Ⅲ";
        ca2.isCheck = false;
        ca2.pos = 4;
        CaseBean2 ca3 = new CaseBean2();
        ca3.name = "Ⅱ";
        ca3.isCheck = false;
        ca3.pos = 4;
        CaseBean2 ca4 = new CaseBean2();
        ca4.name = "Ⅰ";
        ca4.isCheck = false;
        ca4.pos = 4;
        datachild.add(ca);
        datachild.add(ca1);
        datachild.add(ca2);
        datachild.add(ca3);
        datachild.add(ca4);
        CaseBean2 c2a = new CaseBean2();
        c2a.name = "Ⅰ";
        c2a.isCheck = false;
        c2a.pos = 5;
        CaseBean2 c2a1 = new CaseBean2();
        c2a1.name = "Ⅱ";
        c2a1.isCheck = false;
        c2a1.pos = 5;
        CaseBean2 c2a2 = new CaseBean2();
        c2a2.name = "Ⅲ";
        c2a2.isCheck = false;
        c2a2.pos = 5;
        CaseBean2 c2a3 = new CaseBean2();
        c2a3.name = "Ⅳ";
        c2a3.isCheck = false;
        c2a3.pos = 5;
        CaseBean2 c2a4 = new CaseBean2();
        c2a4.name = "Ⅴ";
        c2a4.isCheck = false;
        c2a4.pos = 5;
        datachild.add(c2a);
        datachild.add(c2a1);
        datachild.add(c2a2);
        datachild.add(c2a3);
        datachild.add(c2a4);
        CaseBean2 c3a = new CaseBean2();
        c3a.name = "Ⅴ";
        c3a.isCheck = false;
        c3a.pos = 6;
        CaseBean2 c3a1 = new CaseBean2();
        c3a1.name = "Ⅳ";
        c3a1.isCheck = false;
        c3a1.pos = 6;
        CaseBean2 c3a2 = new CaseBean2();
        c3a2.name = "Ⅲ";
        c3a2.isCheck = false;
        c3a2.pos = 6;
        CaseBean2 c3a3 = new CaseBean2();
        c3a3.name = "Ⅱ";
        c3a3.isCheck = false;
        c3a3.pos = 6;
        CaseBean2 c3a4 = new CaseBean2();
        c3a4.name = "Ⅰ";
        c3a4.isCheck = false;
        c3a4.pos = 6;
        datachild.add(c3a);
        datachild.add(c3a1);
        datachild.add(c3a2);
        datachild.add(c3a3);
        datachild.add(c3a4);
        CaseBean2 c4a = new CaseBean2();
        c4a.name = "Ⅰ";
        c4a.isCheck = false;
        c4a.pos = 7;
        CaseBean2 c4a1 = new CaseBean2();
        c4a1.name = "Ⅱ";
        c4a1.isCheck = false;
        c4a1.pos = 7;
        CaseBean2 c4a2 = new CaseBean2();
        c4a2.name = "Ⅲ";
        c4a2.isCheck = false;
        c4a2.pos = 7;
        CaseBean2 c4a3 = new CaseBean2();
        c4a3.name = "Ⅳ";
        c4a3.isCheck = false;
        c4a3.pos = 7;
        CaseBean2 c4a4 = new CaseBean2();
        c4a4.name = "Ⅴ";
        c4a4.isCheck = false;
        c4a4.pos = 7;
        datachild.add(c4a);
        datachild.add(c4a1);
        datachild.add(c4a2);
        datachild.add(c4a3);
        datachild.add(c4a4);


        mAdapter1 = new CaseAdapter2(mActivity, datachild, 1);
        mGvChild.setAdapter(mAdapter1);
        getData();
    }

    @Override
    protected void initListener() {
        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        startProgressDialog();
        OkHttpClient client = new OkHttpClient();
//通过FormEncodingBuilder对象添加多个请求参数键值对
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user_token", GlobalParams.getuser_token());
        builder.add("id", logId);

//        builder.add("type", type);
//        builder.add("code", openId);

//通过请求地址和请求体构造Post请求对象Request
        Request request = new Request.Builder().url(ApiConstants.SERVER_URL + "user/getUserDateInfo").post(builder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "IOExceptionjson=" + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.i("TAG", "json=" + result);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("200")) {
                                JSONObject mjson = (jsonObject.getJSONObject("data")).getJSONObject("info");
                                Log.i("TAG", "mjson=" + mjson.toString());
                                String ss = mjson.getString("tooth").toString();
//                                ss=ss.replace("\\","");
//                                JSONObject newJson=new JSONObject(mjson.toString().replace("\\",""));
                                Log.i("TAG", "newJson=" + ss);
                                tv_content.setText(mjson.getString("content"));
//                                JSONArray jsonArray=jsonObjectsss.getJSONArray("tooth");
//                                List<CaseBean2> tempList= JSON.parseArray(arry,CaseBean2.class);
                                if(!TextUtils.isEmpty(ss)){
                                    List<CaseBean2> tempList = new Gson().fromJson(ss, new TypeToken<List<CaseBean2>>() {
                                    }.getType());

//                                Log.i("TAG","size="+tempList.size());
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
