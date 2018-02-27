package com.example.xy.dentist.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.AppointBean;
import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.AppointmentPresenter;
import com.example.xy.dentist.widget.StarBarView;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/11.
 */
public class AppointmentFinishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private int type;
    private View itemView;
    private final ViewHolder mViewHolder;
    private AppointmentPresenter mPresenter;
    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;
    private Drawable mDrawable;

    public AppointmentFinishViewHolder(View itemView, final Context context, int type, ItemClickListener listener, ItemLongClickListener longClickListener) {
        super(itemView);
        this.itemView = itemView;
        this.type = type;
        this.mContext = context;
        mViewHolder = new ViewHolder(itemView);
        this.mListener = listener;
        this.mLongClickListener = longClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        mViewHolder.mTvAppo.setVisibility(View.GONE);
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
        AppointmentFinishViewHolder imageViewHolder = new AppointmentFinishViewHolder(LayoutInflater.from(context).inflate(R.layout.item_evalua, null), context, viewType, listener, longClickListener);
        return imageViewHolder;
    }

    public <T> void setData(AppointmentPresenter presenter, final T t, final int position) {
        this.mPresenter = presenter;
        if(t instanceof AppointuBean){
            AppointuBean bean = (AppointuBean) t;
            if(bean!=null){

                ImageLoaderUtils.displayRound(mContext, mViewHolder.mIvPic, ApiConstants.IMAGE_URL+bean.avatar);
//                mViewHolder.mTvTime.setText("预约时间：" + (bean.year+"-"+bean.month+"-"+bean.day+" "+bean.start_time) + "~" + (bean.year+"-"+bean.month+"-"+bean.day+" "+bean.end_time));

                mViewHolder.mTvTime.setText("预约时间：" + bean.year + "-" + bean.month + "-" + bean.day + " (" + bean.start_time + "~" + bean.end_time + ")");

                if(bean.status.equals("0")){//状态1忙碌0空闲
                    mDrawable = mContext.getResources().getDrawable(R.mipmap.free);

                   // mViewHolder.mTvAppo.setText("即时预约");
                  //  mViewHolder.mTvAppo.setBackgroundResource(R.mipmap.hur_appo);
                }else{
                    mDrawable = mContext.getResources().getDrawable(R.mipmap.busy);

                  //  mViewHolder.mTvAppo.setText("在线预约");
                  //  mViewHolder.mTvAppo.setBackgroundResource(R.mipmap.line_appo);
                }
                /**这一步必须要做,否则不会显示.*/
                mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());//对图片进行压缩
                /**设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片*/
                mViewHolder.mTvTitle.setCompoundDrawables(null, null, mDrawable, null);
                mViewHolder.mTvTitle.setText(bean.name);
                mViewHolder.mTvAdd.setText(bean.experience);
                mViewHolder.mTvPhone.setText("擅长：" + bean.skill);
                mViewHolder.starBar_recoveryrate.setStarMark(Float.parseFloat(bean.star));
                mViewHolder.tv_comments.setText(bean.comment);
            }
        }else {
            Log.i("TAG","bean++NULL");
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

        @Bind(R.id.ll_rap)
        LinearLayout ll_rap;
        @Bind(R.id.starBar_recoveryratess)
        StarBarView starBar_recoveryrate;
        @Bind(R.id.tv_comments)
        TextView tv_comments;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
