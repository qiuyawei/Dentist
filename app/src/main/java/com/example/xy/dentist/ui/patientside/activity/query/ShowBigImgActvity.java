package com.example.xy.dentist.ui.patientside.activity.query;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.MyViewpagerAdatper;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.widget.photo.PhotoView;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShowBigImgActvity extends BaseActivity {


    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.vp)
    ViewPager mVp;
    private MyViewpagerAdatper adapter;
    private ArrayList<View> lists=new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();  //图片集合
    private boolean falg;
    private int mSize;
    @Override
    public int getLayoutId() {
        return R.layout.activity_show_big_img_actvity;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mNtb.setTitleText("图片");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
    }

    @Override
    protected void initData() {
        falg=   getIntent().getBooleanExtra("falg", false);
        imageUrls= (List<String>) getIntent().getSerializableExtra("imageUrls");
        mSize = imageUrls.size();
        for (int i = 0; i < mSize; i++) {

/* view = inflater.inflate(R.layout.image_view_layout, null);
            image = (ImageView) view.findViewById(R.id.imageview);*/

            PhotoView view = new PhotoView(getApplicationContext());

            view.enable();
            view.getInfo();
            //压缩图片
            //  view.setImageResource(R.mipmap.pic1111);
           // view.setImageResource(R.mipmap.bna);
//            ImageLoaderUtils.display(mContext, view, ApiConstants.IMAGE_URL + imageUrls.get(i));
            RequestOptions options=new RequestOptions().placeholder(com.jaydenxiao.common.R.drawable.ic_image_loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(com.jaydenxiao.common.R.drawable.ic_empty_picture);

            Glide.with(mContext).load(ApiConstants.IMAGE_URL + imageUrls.get(i))
                   .apply(options).into(view);
            if(!falg){

              //  ImageLoaderUtils.display(mActivity, view, R.mipmap.bna);

            }else{

              //  GlobalParams.loaddefPic(Integer.parseInt(imageUrls.get(i)), view);
            }




            lists.add(view);
        }
        adapter = new MyViewpagerAdatper(mActivity, lists);
        mVp.setAdapter(adapter);
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


}
