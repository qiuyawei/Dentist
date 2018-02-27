package com.example.xy.dentist.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.UserInfo;
import com.example.xy.dentist.presenter.QueryDetailPresenter;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.PopUtil;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/11.
 */
public class PhysicianAdapter extends BaseAdapter {

    private Context context;
    List<UserInfo> data;
    private Drawable mDrawable;
    QueryDetailPresenter mPresenter;
    View parent;

    public PhysicianAdapter(Context context, List<UserInfo> data, QueryDetailPresenter mPresenter, View view) {

        this.context = context;
        this.data = data;
        this.mPresenter = mPresenter;
        this.parent = view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public UserInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final UserInfo bean = getItem(position);
        final ViewHolder hodler;
        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_physician, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        if (bean != null) {

            ImageLoaderUtils.displayRound(context, hodler.mIvPic, ApiConstants.IMAGE_URL + bean.avatar);
            if (bean.status.equals("0")) {//状态1忙碌0空闲
                mDrawable = context.getResources().getDrawable(R.mipmap.free);

                hodler.mTvAppo.setText("即时预约");
                hodler.mTvAppo.setBackgroundResource(R.mipmap.hur_appo);
            } else {
                mDrawable = context.getResources().getDrawable(R.mipmap.busy);

                hodler.mTvAppo.setText("在线预约");
                hodler.mTvAppo.setBackgroundResource(R.mipmap.line_appo);
            }
            /**这一步必须要做,否则不会显示.*/
            mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());//对图片进行压缩
            /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
            hodler.mTvTitle.setCompoundDrawables(null, null, mDrawable, null);
            hodler.mTvTitle.setText(bean.name);
            hodler.mTvAdd.setText(bean.experience);
            hodler.mTvPhone.setText("擅长：" + bean.skill);


            final Activity activity = (Activity) context;
            hodler.mTvAppo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.status.equals("0")) {
                        mPresenter.appointment(bean.status, GlobalParams.getuser_token(), bean.clinic_id, bean.id,"1");
                    } else {
                        PopUtil.showView(context, PopUtil.getView(activity, "即将拨打电话", bean.phone, context, new PopUtil.onSelectFinishListener() {
                            @Override
                            public void onSelectFinish(String type) {
                                if (type.equals("yes")) {

                                    GlobalParams.callPhone(context, bean.phone);
//                                    mPresenter.appointment(bean.status, GlobalParams.getuser_token(), bean.clinic_id, bean.id);
//打完电话预约
                                    PopUtil.showView(context, PopUtil.getViewSure(activity, "是否确定预约？", "", context, new PopUtil.onSelectFinishListener() {
                                        @Override
                                        public void onSelectFinish(String type) {
                                            if (type.equals("yes")) {

                                                mPresenter.appointment(bean.status, GlobalParams.getuser_token(), bean.clinic_id, bean.id,"2");

                                            }

                                        }
                                    }), activity.getWindow(), parent);

                                }

                            }
                        }), activity.getWindow(), parent);
                    }


                }
            });
        }
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.iv_pic)
        ImageView mIvPic;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_add)
        TextView mTvAdd;
        @Bind(R.id.tv_phone)
        TextView mTvPhone;
        @Bind(R.id.tv_appo)
        TextView mTvAppo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
