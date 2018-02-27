package com.example.xy.dentist.ui.patientside.activity.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import butterknife.Bind;
import butterknife.OnClick;

public class UserDataActivity extends BaseActivity {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.et_content)
    EditText mEtContent;
    @Bind(R.id.tv_exsice)
    TextView mTvExsice;
    private String content;
    private String name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_data;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mNtb.setTitleText("姓名");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);

    }

    @Override
    protected void initData() {
        name= getIntent().getStringExtra("name");
        if(!TextUtils.isEmpty(name)){
            mEtContent.setText(name);
        }

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
       content= mEtContent.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            ToastUitl.showShort("请输入内容");
        }else{
            intent=new Intent();
            intent.putExtra("data",content);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
