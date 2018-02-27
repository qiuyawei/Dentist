package com.example.xy.dentist.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.QueryPresenter;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/9/11.
 */
public class EvaluateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private int type;
    private View itemView;
    private final ViewHolder mViewHolder;
    private QueryPresenter mPresenter;
    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;

    public EvaluateViewHolder(View itemView, final Context context, int type, ItemClickListener listener, ItemLongClickListener longClickListener) {
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
        EvaluateViewHolder imageViewHolder = new EvaluateViewHolder(LayoutInflater.from(context).inflate(R.layout.item_evaluate, null), context, viewType, listener, longClickListener);
        return imageViewHolder;
    }

    public <T> void setData(QueryPresenter presenter, final T t, final int position) {
        this.mPresenter = presenter;
        if (t instanceof ClinicBean) {
            ClinicBean bean = (ClinicBean) t;
            if (bean != null) {
                ImageLoaderUtils.display(mContext, mViewHolder.mIvPic, ApiConstants.IMAGE_URL + bean.thumb);
                mViewHolder.mTvTitle.setText(bean.name);
                mViewHolder.mTvAdd.setText(bean.address);
                mViewHolder.mTvPhone.setText(bean.phone);
                if (!TextUtils.isEmpty(bean.distance)) {
                    double d = Double.parseDouble(bean.distance);
                    DecimalFormat df= new DecimalFormat("######0.0");
                    if(d>=1000){
                        mViewHolder.mTvDistance.setText("距我" + df.format(d/1000) + "km");
                    }else {
                        mViewHolder.mTvDistance.setText("距我" + bean.distance + "m");
                    }

                } else
                    mViewHolder.mTvDistance.setText("距我" + 0 + "km");

            }


        }


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
        @Bind(R.id.tv_distance)
        TextView mTvDistance;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
