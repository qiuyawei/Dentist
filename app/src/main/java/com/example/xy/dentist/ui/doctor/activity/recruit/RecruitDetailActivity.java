package com.example.xy.dentist.ui.doctor.activity.recruit;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.RecruitBean;
import com.example.xy.dentist.contract.EvaluateContract;
import com.example.xy.dentist.model.EvaluateModel;
import com.example.xy.dentist.presenter.EvaluatePresenter;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.MyTimeUtils;
import com.example.xy.dentist.utils.PopUtil;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class RecruitDetailActivity extends BaseActivity<EvaluatePresenter, EvaluateModel> implements EvaluateContract.View {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.tv_posi)
    TextView mTvPosi;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.tv_commp)
    TextView mTvCommp;
    @Bind(R.id.tv_money)
    TextView mTvMoney;
    @Bind(R.id.tv_phone)
    TextView mTvPhone;
    @Bind(R.id.tv_call)
    TextView mTvCall;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.sv)
    ScrollView mSv;
    @Bind(R.id.tv_exsice)
    TextView mTvExsice;
    @Bind(R.id.ll_root)
    LinearLayout mLlRoot;
    private String id;
    private String phone;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recruit_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mNtb.setTitleText("招聘详情");
        id= getIntent().getStringExtra("id");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        mSv.scrollTo(0, 0);
        initweb();
        mTvCall.setVisibility(View.GONE);
        mPresenter.recruit_info(GlobalParams.getdoctor_token(),id);
    }

    private void initweb() {
        //设置WebView属性，能够执行Javascript脚本
        WebSettings webSettings = mWebView.getSettings();



        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    protected void initData() {

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



    @OnClick(R.id.tv_exsice)
    public void onClick() {
        phone = mTvPhone.getText().toString().trim();
        PopUtil.showView(mActivity, PopUtil.getView(mActivity, "即将拨打电话", phone, mActivity, new PopUtil.onSelectFinishListener() {
            @Override
            public void onSelectFinish(String type) {
                if (type.equals("yes")) {

                    GlobalParams.callPhone(mActivity, phone);
                }

            }
        }), mActivity.getWindow(), mWebView);
    }


    @Override
    public void setRecruitListData(List<RecruitBean> bean, String message) {

    }

    @Override
    public void setRecruit_infoData(final RecruitBean bean, String message) {
        if(bean!=null){
            LogUtils.print("title",bean.name);
            mTvPosi.setText(bean.name);
            mTvCommp.setText(bean.company);
            mTvMoney.setText("薪资："+bean.money);
            mTvPhone.setText("联系电话："+bean.phone);
            mTvTime.setText("发布时间：" + MyTimeUtils.getnormalStrTime1(bean.create_time));
            //加载需要显示的网页
          //  mWebView.loadUrl("http://www.51cto.com/");
            mWebView.loadDataWithBaseURL("http://121.199.8.244:3501/",bean.description,"text/html", "UTF-8", null);
        }

    }

    private String getNewContent(String htmltext){

        Document doc=Jsoup.parse(htmltext);
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }

        return doc.toString();
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
