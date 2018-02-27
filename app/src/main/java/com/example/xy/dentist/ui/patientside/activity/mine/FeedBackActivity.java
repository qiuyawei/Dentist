package com.example.xy.dentist.ui.patientside.activity.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.contract.FeedbackContract;
import com.example.xy.dentist.model.FeedbackModel;
import com.example.xy.dentist.presenter.FeedbackPresenter;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.SharedPreferencesUtil;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FeedBackActivity extends BaseActivity<FeedbackPresenter, FeedbackModel> implements FeedbackContract.View {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.tv_exsice)
    TextView mTvExsice;
    private String title;
    private String hint;
    private String content;
    private String mToken;
    private boolean mIsdoc;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        title=  getIntent().getStringExtra("title");
        hint= getIntent().getStringExtra("hint");
        content=  getIntent().getStringExtra("content");

        mNtb.setTitleText(title);
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        if(TextUtils.isEmpty(content)){

        mEtContent.setHint(hint);
        }else {
            mEtContent.setText(content);
        }
    }

    @Override
    protected void initData() {

        mIsdoc = SharedPreferencesUtil.getBooleanData(mActivity, "isdoc", false);
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

        content=   mEtContent.getText().toString().trim();
        if(title.equals("意见反馈")){
        if(TextUtils.isEmpty(content)){
            ToastUitl.showShort("意见反馈不能为空哦！");

        }else{
            if(!mIsdoc){
                mToken = GlobalParams.getuser_token();
//                thirdLogin(content,mToken);
            }else{
                mToken = GlobalParams.getdoctor_token();
            }

            mPresenter.commitfeedback(mIsdoc,mToken,content);
        }

        }else{
            if(TextUtils.isEmpty(content)){
                ToastUitl.showShort(title+"內容不能為空哦！");
            }else{
                intent=new Intent();
                intent.putExtra("data",content);
                setResult(RESULT_OK,intent);
                finish();
            }


        }
    }

    @Override
    public void commitState(BaseResult result) {
        ToastUitl.showShort("意见反馈提价成功");
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


 /*   private void thirdLogin(String type,String user_token) {
//        Log.i("TAG","openId="+openId);
        //创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
//通过FormEncodingBuilder对象添加多个请求参数键值对
        FormBody.Builder builder = new FormBody.Builder();

        builder.add("content",type);
        builder.add("user_token",user_token);

//通过请求地址和请求体构造Post请求对象Request
        Request request = new Request.Builder().url(ApiConstants.SERVER_URL+ApiConstants.feedback).post(builder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG","onFailureIOException="+e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resutl=response.body().string();
//                ToastUitl.showLong(resutl);
                Log.i("TAG","result="+resutl);
//                ToastUitl.showLong(resutl);
            }
        });
    }
*/


}
