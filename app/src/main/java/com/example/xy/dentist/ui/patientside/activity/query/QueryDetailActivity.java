package com.example.xy.dentist.ui.patientside.activity.query;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.xy.dentist.AppApplication;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.PhysicianAdapter;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.bean.DoctorDetailbean;
import com.example.xy.dentist.bean.UserInfo;
import com.example.xy.dentist.bean.eventbus.Notify;
import com.example.xy.dentist.contract.QueryDetailContract;
import com.example.xy.dentist.model.QueryDetailModel;
import com.example.xy.dentist.presenter.QueryDetailPresenter;
import com.example.xy.dentist.tool.CusConvenientBanner;
import com.example.xy.dentist.ui.patientside.activity.PatientActivity;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.PopUtil;
import com.example.xy.dentist.utils.transformers.ABaseTransformer;
import com.example.xy.dentist.utils.transformers.StackTransformer;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class QueryDetailActivity extends BaseActivity<QueryDetailPresenter, QueryDetailModel> implements QueryDetailContract.View, OnItemClickListener {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.convenientBanner)
    CusConvenientBanner convenientBanner;
    @Bind(R.id.tv_number)
    TextView mTvNumber;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_add)
    TextView mTvAdd;
    @Bind(R.id.tv_phone)
    TextView mTvPhone;
    @Bind(R.id.tv_indro)
    TextView mTvIndro;
    @Bind(R.id.tv_prod)
    TextView mTvProd;
    @Bind(R.id.lv_doc)
    ListView mLvDoc;
    @Bind(R.id.sv)
    ScrollView sv;
    @Bind(R.id.tv_distance)
    TextView tv_distance;
    List<String> data = new ArrayList<>();
    private ClinicBean bean;
    private String id, latitude, longitude;

    @Override
    public int getLayoutId() {
        return R.layout.activity_query_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {

        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        sv.scrollTo(0, 0);
    }

    @Override
    protected void initData() {
        bean = (ClinicBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            id = bean.id;
            latitude = bean.latitude;
            longitude = bean.longitude;
            mPresenter.getInfoData(id, AppApplication.latitude, AppApplication.longitude);
            Log.i("TAG", "latitude=" + latitude);

        }


    }

    private void setBannerData(String[] pic) {
        for (int i = 0; i < pic.length; i++) {

            data.add(pic[i]);
        }
        mTvNumber.setText(pic.length + "張");
 /*       int widthPixels = ScreenUtils.getScreenWidth(mActivity);
        int heith = widthPixels * 400 / 750;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heith);
        convenientBanner.setLayoutParams(params);*/
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, data)
                //    .setPointViewVisible(false)    //设置指示器是否可见
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                // .setPageIndicator(new int[]{R.mipmap.icon_white, R.mipmap.icon_gray})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)

                .setOnItemClickListener(this);


    }

    @Override
    public void initListener() {
        //网络加载例子
        ABaseTransformer transformer = new StackTransformer();
        convenientBanner.getViewPager().setPageTransformer(true, transformer);
        convenientBanner.setScrollDuration(1200);
        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLvDoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo user = (UserInfo) mLvDoc.getItemAtPosition(position);
                if (user.status.equals("0")) {//状态1忙碌0空闲
                    intent = new Intent(mActivity, PhysicianActivity.class);
                    intent.putExtra("title", "即时预约");
                    intent.putExtra("id", user.id);
                    intent.putExtra("clinic_id", info.id);
                    intent.putExtra("phone", info.phone);

                    startActivityForResult(intent, 201);
                } else {
                    intent = new Intent(mActivity, PhysicianActivity.class);
                    intent.putExtra("title", "在线预约");
                    intent.putExtra("id", user.id);
                    intent.putExtra("clinic_id", info.id);
                    intent.putExtra("phone", info.phone);
                    startActivityForResult(intent, 200);
                }

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        turnAroundBigImage(data);

    }

    private void turnAroundBigImage(List<String> list) {
        intent = new Intent(mActivity, ShowBigImgActvity.class);
        intent.putExtra("imageUrls", (Serializable) list);
        startActivity(intent);
    }

    ClinicBean info;

    @Override
    public void setInfoData(ClinicBean info, List<UserInfo> bean, String message) {

        if (info != null) {
            this.info = info;
            mNtb.setTitleText(info.name);
            mTvTitle.setText(info.name);
            mTvAdd.setText(info.address);
            mTvPhone.setText(info.phone);
            if (!TextUtils.isEmpty(info.distance)) {
                double d = Double.parseDouble(info.distance);
                DecimalFormat df = new DecimalFormat("######0.0");
                if (d >= 1000) {
                    tv_distance.setText("距我" + df.format(d / 1000) + "km");

                } else {
                    tv_distance.setText("距我" + info.distance + "m");

                }

            } else
//                mViewHolder.mTvDistance.setText("距我"+0+"公里");
                tv_distance.setText("距我" + 0 + "km");
            mTvIndro.setText(info.introduce);
            mTvProd.setText(info.project);
            if (!TextUtils.isEmpty(info.pic)) {
                String[] pic = info.pic.split(",");
                setBannerData(pic);
            }
            for (int i = 0; i < bean.size(); i++) {
                bean.get(i).clinic_id = info.id;

            }


        }


        mLvDoc.setAdapter(new PhysicianAdapter(mActivity, bean, mPresenter, mNtb));
        sv.smoothScrollTo(0, 0);
    }

    @Override
    public void setDicData(DoctorDetailbean info, String message) {

    }

    @Override
    public void setAppoint(BaseResult result, String status, String message) {
        LogUtils.print("message", message);
        LogUtils.print("status", status);

        if (status.equals("0")) {////状态1忙碌0空闲
            PopUtil.showView(mActivity, PopUtil.getView(mActivity, "预约成功", "请在15分钟内到达该诊所", mActivity, new PopUtil.onSelectFinishListener() {
                @Override
                public void onSelectFinish(String type) {
                    if (type.equals("yes")) {
                        intent = new Intent(mActivity, PatientActivity.class);
                        intent.putExtra("position", 1);
                        startActivity(intent);


                    }

                }
            }), mActivity.getWindow(), mNtb);
        } else {

            if (result.code == 200)
                ToastUitl.showShort("预约成功");
            else
                ToastUitl.showShort(result.message);

        }
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

    private class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            //  imageView.setBackgroundResource(R.mipmap.bna);
//            ImageLoader.getInstance().displayImage(data,imageView);
//            Log.d("Data", data);


            ImageLoaderUtils.display(mContext, imageView, ApiConstants.IMAGE_URL + data);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(5000);


    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();


    }


    @Override
    public void onEvent(Object o) {
        super.onEvent(o);
        if (o instanceof Notify) {

            if (!((Notify) o).isSuccess) {
                PopUtil.showView(mActivity, PopUtil.getView(mActivity, "预约成功", "请在15分钟内到达该诊所", mActivity, new PopUtil.onSelectFinishListener() {
                    @Override
                    public void onSelectFinish(String type) {
                        if (type.equals("yes")) {
                            intent = new Intent(mActivity, PatientActivity.class);
                            intent.putExtra("position", 1);
                            startActivity(intent);

                        }

                    }
                }), mActivity.getWindow(), sv);
            } else {
                PopUtil.showView(mActivity, PopUtil.getView(mActivity, "即将拨打电话", "0519-83806983", mActivity, new PopUtil.onSelectFinishListener() {
                    @Override
                    public void onSelectFinish(String type) {
                        if (type.equals("yes")) {

                            GlobalParams.callPhone(mActivity, "0519-83806983");
                        }

                    }
                }), mActivity.getWindow(), sv);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {

                }
                break;
        }
    }


}
