package com.example.xy.dentist.ui.doctor.activity.shop;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.ShopBean;
import com.example.xy.dentist.contract.ShopContract;
import com.example.xy.dentist.model.ShopModel;
import com.example.xy.dentist.presenter.ShopPresenter;
import com.example.xy.dentist.tool.CusConvenientBanner;
import com.example.xy.dentist.ui.login.WebViewActivity;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.PopUtil;
import com.example.xy.dentist.utils.transformers.ABaseTransformer;
import com.example.xy.dentist.utils.transformers.StackTransformer;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ShopDetailActivity extends BaseActivity<ShopPresenter, ShopModel> implements ShopContract.View , OnItemClickListener {
    private ShopBean shopBean;

    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.convenientBanner)
    CusConvenientBanner convenientBanner;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.tv_money)
    TextView mTvMoney;
    @Bind(R.id.tv_number)
    TextView mTvNumber;
    @Bind(R.id.tv_brands)
    TextView mTvBrands;
    @Bind(R.id.tv_inde)
    TextView mTvInde;
    @Bind(R.id.tv_specification)
    TextView mTvSpecification;
    @Bind(R.id.tv_unit)
    TextView mTvUnit;
    @Bind(R.id.tv_standard)
    TextView mTvStandard;
    @Bind(R.id.tv_class)
    TextView mTvClass;
    @Bind(R.id.tv_no)
    TextView mTvNo;
    @Bind(R.id.tv_Product)
    TextView mTvProduct;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.sv)
    ScrollView mSv;
    @Bind(R.id.tv_exsice)
    TextView mTvExsice;
    @Bind(R.id.ll_root)
    LinearLayout ll_root;
    private List<String> data=new ArrayList<>();
    private String id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        WindowManager windowManager=getWindowManager();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        Display display= windowManager.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        convenientBanner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)displayMetrics.widthPixels));

        id= getIntent().getStringExtra("id");
        mPresenter.shop_info(GlobalParams.getdoctor_token(), id);

        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        mSv.scrollTo(0, 0);

        //设置WebView属性，能够执行Javascript脚本
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);// 隐藏缩放按钮
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//排版适应屏幕
        webSettings.setUseWideViewPort(true);//可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);//保存表单数据
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);// 启用地理定位
        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        webSettings.setDomStorageEnabled(true);


        webSettings.setAllowFileAccess(true); // 允许访问文件

        webSettings.setPluginState(WebSettings.PluginState.ON);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        //加载需要显示的网页
        mWebView.loadUrl(ApiConstants.SERVER_URL+"shop/detail?id="+id);

    }

    @Override
    protected void initData() {




    }
    private void setBannerData() {
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
                .setPageIndicator(new int[]{R.mipmap.icon_white, R.mipmap.icon_gray})
                        //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)

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
    }

    @Override
    public void onItemClick(int position) {
//        WebViewActivity.launch(mActivity, "轮播图详情", "https://www.baidu.com/");
    }

    @Override
    public void setListData(List<ShopBean> bean, String message) {

    }

    @Override
    public void setInfo(ShopBean bean, String message) {
         this.shopBean=bean;
        if(bean!=null){
            Log.i("TAG","pic="+bean.pic);
        mNtb.setTitleText(bean.name);
            mTvContent.setText(bean.name);
            mTvMoney.setText("￥"+bean.price);
            if(!TextUtils.isEmpty(bean.pic)){
                String[] split = bean.pic.split(",");
                for (int i = 0; i <split.length; i++) {
                    data.add(split[i]);
                }
            }

            setBannerData();
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
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int) x));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
           // imageView.setBackgroundResource(R.mipmap.bna);
//            ImageLoader.getInstance().displayImage(data,imageView);
//            Log.d("Data", data);
            Log.i("TAG","data="+data+"=="+position);
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
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }





    @OnClick({R.id.tv_title, R.id.tv_exsice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                break;
            case R.id.tv_exsice:
                PopUtil.showView(mActivity, PopUtil.getView(mActivity, "即将拨打电话", shopBean.phone, mActivity, new PopUtil.onSelectFinishListener() {
                    @Override
                    public void onSelectFinish(String type) {
                        if (type.equals("yes")) {

                            GlobalParams. callPhone(mActivity, shopBean.phone);
                        }

                    }
                }), mActivity.getWindow(), ll_root);
                break;
        }
    }
}
