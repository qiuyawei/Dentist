package com.example.xy.dentist.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.AppointmentPresenter;
import com.example.xy.dentist.ui.patientside.activity.appoint.EvaluateActivity;
import com.example.xy.dentist.widget.StarBarView;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/11.
 */
public class AppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private static int type;
    private View itemView;
    private final ViewHolder mViewHolder;
    private AppointmentPresenter mPresenter;
    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;
    private Drawable mDrawable;

    public AppointmentViewHolder(View itemView, final Context context, int type, ItemClickListener listener, ItemLongClickListener longClickListener) {
        super(itemView);
        this.itemView = itemView;
        this.type = type;
        this.mContext = context;
        mViewHolder = new ViewHolder(itemView);
        this.mListener = listener;
        this.mLongClickListener = longClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, getPosition());
        }
    }

    /**
     * 长按监听
     */
    @Override
    public boolean onLongClick(View arg0) {
        if (mLongClickListener != null) {
            mLongClickListener.onItemLongClick(arg0, getPosition());
        }
        return true;
    }


    public static RecyclerView.ViewHolder create(Context context, int viewType, ItemClickListener listener, ItemLongClickListener longClickListener) {
        int resouce;

        resouce = R.layout.item_appo;

        AppointmentViewHolder imageViewHolder = new AppointmentViewHolder(LayoutInflater.from(context).inflate(resouce, null), context, viewType, listener, longClickListener);
        return imageViewHolder;
    }

    public <T> void setData(AppointmentPresenter presenter, final T t, final int position, int flag) {
        this.mPresenter = presenter;
        mViewHolder.mTvAppo.setVisibility(flag == 2 ? View.VISIBLE : View.GONE);


        if (t instanceof AppointuBean) {
            final AppointuBean bean = (AppointuBean) t;
            if (bean != null) {
//                Log.i("TAG", "headUrl=" + bean.avatar);
//                Log.i("TAG", "Name=" + bean.name);
//                Log.i("TAG", "Skill=" + bean.skill);

                ImageLoaderUtils.displayRound(mContext, mViewHolder.mIvPic, ApiConstants.IMAGE_URL + bean.avatar);

                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");


                long half = (long) (0.5 * 60 * 60 * 1000);

//                Log.i("TAG", "create_time1=" + df1.format(Long.parseLong(bean.create_time)*1000));
//                Log.i("TAG", "create_time2=" + df2.format(Long.parseLong(bean.create_time)*1000));

                if (TextUtils.isEmpty(bean.start_time)) {
                    mViewHolder.mTvTime.setText("预约时间：" +df3.format(Long.parseLong(bean.create_time) * 1000)+" (" +df1.format(Long.parseLong(bean.create_time) * 1000) + "~" + df1.format(Long.parseLong(bean.create_time) * 1000 + half)+")");
                } else
//                    mViewHolder.mTvTime.setText("预约时间：" + bean.start_time + "~" + bean.end_time);
                    mViewHolder.mTvTime.setText("预约时间：" + bean.year + "-" + bean.month + "-" + bean.day + " (" + bean.start_time + "~" + bean.end_time + ")");


                if (bean.doctor_status.equals("0")) {//状态1忙碌0空闲
                    mDrawable = mContext.getResources().getDrawable(R.mipmap.free);

                    //  mViewHolder.mTvAppo.setText("即时预约");
                    //  mViewHolder.mTvAppo.setBackgroundResource(R.mipmap.hur_appo);
                } else {
                    mDrawable = mContext.getResources().getDrawable(R.mipmap.busy);

                    //   mViewHolder.mTvAppo.setText("在线预约");
                    //  mViewHolder.mTvAppo.setBackgroundResource(R.mipmap.line_appo);
                }
                /**这一步必须要做,否则不会显示.*/
                mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());//对图片进行压缩
                /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
                mViewHolder.mTvTitle.setCompoundDrawables(null, null, mDrawable, null);
                mViewHolder.mTvTitle.setText(bean.name);
                mViewHolder.mTvAdd.setText(bean.experience);
                mViewHolder.mTvPhone.setText("擅长：" + bean.skill);

                if (flag == 2) {
                    mViewHolder.mTvAppo.setText("去评价");
                    mViewHolder.mTvAppo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, EvaluateActivity.class);
                            intent.putExtra("bean", bean);
                            mContext.startActivity(intent);
                        }
                    });

                }

            }
        } else {
//            Log.i("TAG","notAppbean");
        }


    }


    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView mTvTime;
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
